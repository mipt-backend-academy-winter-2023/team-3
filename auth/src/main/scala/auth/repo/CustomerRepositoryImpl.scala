package auth.repo

import auth.model.User
import zio.sql.ConnectionPool

import zio.stream.ZStream
import zio.{ZIO, ZLayer}

final class CustomerRepositoryImpl(
    pool: ConnectionPool
) extends CustomerRepository
    with PostgresTableDescription {

  implicit val driverLayer: ZLayer[Any, Nothing, SqlDriver] =
    ZLayer
      .make[SqlDriver](
        SqlDriver.live,
        ZLayer.succeed(pool)
      )

  override def findAll(): ZStream[Any, Throwable, User] = {
    val selectAll =
      select(userId, username, password).from(users)

    ZStream.fromZIO(
      ZIO.logInfo(s"Query to execute findAll is ${renderRead(selectAll)}")
    ) *>
      execute(selectAll.to((User.apply _).tupled))
        .provideSomeLayer(driverLayer)
  }

  override def add(customer: User): ZIO[Any, Throwable, Unit] = {
    val query =
      insertInto(users)(userId, username, password)
        .values(
          (
            customer.id,
            customer.username,
            customer.password
          )
        )

    ZIO.logInfo(s"Query to insert customer is ${renderInsert(query)}") *>
      execute(query)
        .provideSomeLayer(driverLayer)
        .unit
  }

  override def updateCustomer(
      customer: User
  ): ZIO[Any, Throwable, Unit] = {
    val query =
      update(users)
      .set(username, customer.username)
        .set(password, customer.password)
        .where(userId === customer.id)
        //.where(Expr.Relational(customerId, customer.id, RelationalOp.Equals))

    ZIO.logInfo(s"Query to update customer is ${renderUpdate(query)}") *>
      execute(query)
        .provideSomeLayer(driverLayer)
        .unit
  }

  override def delete(id: Int): ZIO[Any, Throwable, Unit] = {
    val delete = deleteFrom(users).where(userId === id)
    execute(delete)
      .provideSomeLayer(driverLayer)
      .unit
  }
}

object CustomerRepositoryImpl {
  val live: ZLayer[ConnectionPool, Throwable, CustomerRepository] =
    ZLayer.fromFunction(new CustomerRepositoryImpl(_))
}
