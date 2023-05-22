package auth.repo

import auth.Tools.PasswordEncryptor
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
  override def retrieve(user: User): ZStream[UserRepository, Throwable, User] = {
    val selectAll =
      select(username, password)
        .from(users)
        .where(username === user.username && password === PasswordEncryptor.encrypt(user.password))

    ZStream.fromZIO(
      ZIO.logInfo(s"Query to execute retrieve is ${renderRead(selectAll)}")
    ) *>
      execute(selectAll.to((User.apply _).tupled))
        .provideSomeLayer(driverLayer)
  }

  override def retrieveByUsername(user: User): ZStream[UserRepository, Throwable, User] = {
    val selectAll = select(username, password).from(users).where(username === user.username)

    ZStream.fromZIO(
      ZIO.logInfo(s"Query to execute retrieve is ${renderRead(selectAll)}")
    ) *>
      execute(selectAll.to((User.apply _).tupled))
        .provideSomeLayer(driverLayer)
  }

  override def add(user: User): ZIO[Any, Throwable, Unit] = {
    val query =
      insertInto(users)(username, password)
        .values(
          (
            user.username,
            PasswordEncryptor.encrypt(user.password)
          )
        )

    ZIO.logInfo(s"Query to insert user is ${renderInsert(query)}") *>
      execute(query)
        .provideSomeLayer(driverLayer)
        .unit
  }

}

object UserRepositoryImpl {
  val live: ZLayer[ConnectionPool, Throwable, UserRepository] =
    ZLayer.fromFunction(new UserRepositoryImpl(_))
}
