package auth.repo

import auth.model.{PasswordEncryptor, User}
import zio.sql.ConnectionPool
import zio.stream.ZStream
import zio.{ZIO, ZLayer}

final class UsersRepositoryImpl(
                                 pool: ConnectionPool
                               ) extends UsersRepository
  with PostgresTableDescription {

  val driverLayer: ZLayer[Any, Nothing, SqlDriver] =
    ZLayer
      .make[SqlDriver](
        SqlDriver.live,
        ZLayer.succeed(pool)
      )

  override def findUser(user: User): ZStream[Any, Throwable, User] = {
    val selectUser = select(login, password)
      .from(users)
      .where(login === user.login && password === PasswordEncryptor.encode(user.password))

    ZStream.fromZIO(
      ZIO.logInfo(s"Query to execute findUser is ${renderRead(selectUser)}")
    ) *>
      execute(selectUser.to((User.apply _).tupled))
        .provideSomeLayer(driverLayer)
  }

  override def add(user: User): ZIO[Any, Throwable, Unit] = {
    val query =
      insertInto(users)(login, password)
        .values(
          (
            user.login,
            PasswordEncryptor.encode(user.password)
          )
        )


    ZIO.logInfo(s"Query to insert user is ${renderInsert(query)}") *>
      execute(query)
        .provideSomeLayer(driverLayer)
        .unit
  }
}

object UsersRepositoryImpl {
  val live: ZLayer[ConnectionPool, Throwable, UsersRepository] =
    ZLayer.fromFunction(new UsersRepositoryImpl(_))
}
