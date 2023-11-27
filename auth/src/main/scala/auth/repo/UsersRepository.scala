package auth.repo

import auth.model.User
import zio.ZIO
import zio.stream.ZStream

trait UsersRepository {

  def findUser(user: User): ZStream[UsersRepository, Throwable, User]

  def add(user: User): ZIO[UsersRepository, Throwable, Unit]
}

object UsersRepository {
  def findUser(user: User): ZStream[UsersRepository, Throwable, User] =
    ZStream.serviceWithStream[UsersRepository](_.findUser(user))

  def add(user: User): ZIO[UsersRepository, Throwable, Unit] =
    ZIO.serviceWithZIO[UsersRepository](_.add(user))
}
