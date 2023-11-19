package auth

import auth.model.User
import auth.repo.UsersRepository
import zio.ZIO
import zio.stream.ZStream

import scala.collection.mutable

final class MockUserRepository(users: mutable.Map[String, User])
    extends UsersRepository {
  override def add(user: User): ZIO[UsersRepository, Throwable, Unit] = {
    if (!users.contains(user.login)) {
      users += user.login -> user
      ZIO.succeed()
    } else {
      ZIO.fail(new Exception("Provided login is taken"))
    }
  }

  override def findUser(user: User): ZStream[UsersRepository, Throwable, User] = {
    try {
      if (users(user.login).password != user.password) {
        throw new Exception("Wrong password")
      }
      ZStream.fromZIO(ZIO.succeed(user))
    } catch {
      case _ => ZStream.empty
    }
  }
}
