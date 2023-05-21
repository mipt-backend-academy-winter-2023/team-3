package auth.repo

import auth.model.User
import zio.{ZIO, ZLayer}
import zio.sql.ConnectionPool
import zio.stream.ZStream

final class UserRepositoryImpl(
    pool: ConnectionPool
) extends UserRepository with PostgresTableDescription {

  val driverLayer: ZLayer[Any, Nothing, SqlDriver] =
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

  override def retrieve(user: User): ZStream[UserRepository, Throwable, User] = {
    val selectAll =
      select(userId, username, password).from(users).where(username === user.username && password === user.password)

    ZStream.fromZIO(
      ZIO.logInfo(s"Query to execute retrieve is ${renderRead(selectAll)}")
    ) *>
      execute(selectAll.to((User.apply _).tupled))
        .provideSomeLayer(driverLayer)
  }

  override def retrieveByUsername(user: User): ZStream[UserRepository, Throwable, User] = {
    val selectAll = select(user.id, user.username).from(users).where(username, user.username)

    ZStream.fromZIO(
      ZIO.logInfo(s"Query to execute retrieve is ${renderRead(selectAll)}")
    ) *>
      execute(selectAll.to((User.apply _).tupled))
        .provideSomeLayer(driverLayer)
  }

  override def add(user: User): ZIO[Any, Throwable, Unit] = {
    val query =
      insertInto(users)(userId, username, password)
        .values(
          (
            user.id,
            user.username,
            user.password
          )
        )

    ZIO.logInfo(s"Query to insert user is ${renderInsert(query)}") *>
      execute(query)
        .provideSomeLayer(driverLayer)
        .unit
  }

  override def updateUser(
      user: User
  ): ZIO[Any, Throwable, Unit] = {
    val query =
      update(users)
        .set(username, user.username)
        .set(password, user.password)
        .where(userId === user.id)
    //.where(Expr.Relational(customerId, customer.id, RelationalOp.Equals))

    ZIO.logInfo(s"Query to update user is ${renderUpdate(query)}") *>
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

object UserRepositoryImpl {
  val live: ZLayer[ConnectionPool, Throwable, UserRepository] =
    ZLayer.fromFunction(new UserRepositoryImpl(_))
}
