package auth.repo

import auth.model.User
import zio.ZIO
import zio.stream.ZStream

trait UserRepository {

  def add(user: User): ZIO[Any, Throwable, Unit]

  def retrieve(user: User): ZStream[UserRepository, Throwable, User]


  def retrieveByUsername(user: User): ZStream[UserRepository, Throwable, User]
}

object UserRepository {

  def retrieve(user: User): ZStream[UserRepository, Throwable, User] =
    ZStream.serviceWithStream[UserRepository](_.retrieve(user))

  def retrieveByUsername(user: User): ZStream[UserRepository, Throwable, User] =
    ZStream.serviceWithStream[UserRepository](_.retrieveByUsername(user))

  def add(user: User): ZIO[UserRepository, Throwable, Unit] =
    ZIO.serviceWithZIO[UserRepository](_.add(user))

}