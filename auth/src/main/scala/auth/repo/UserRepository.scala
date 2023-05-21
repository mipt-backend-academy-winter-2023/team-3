package auth.repo

import auth.model.User
import zio.ZIO
import zio.stream.ZStream

trait UserRepository {

  def findAll(): ZStream[Any, Throwable, User]

  def add(user: User): ZIO[Any, Throwable, Unit]

  def updateUser(user: User): ZIO[Any, Throwable, Unit]

  def retrieve(user: User): ZStream[UserRepository, Throwable, User]


  def retrieveByUsername(user: User): ZStream[UserRepository, Throwable, User]

  def delete(id: Int): ZIO[Any, Throwable, Unit]
}

object UserRepository {
  def findAll(): ZStream[UserRepository, Throwable, User] =
    ZStream.serviceWithStream[UserRepository](_.findAll())

  def retrieve(user: User): ZStream[UserRepository, Throwable, User] =
    ZStream.serviceWithStream[UserRepository](_.retrieve(user))

  def retrieveByUsername(user: User): ZStream[UserRepository, Throwable, User] =
    ZStream.serviceWithStream[UserRepository](_.retrieveByUsername(user))

  def add(user: User): ZIO[UserRepository, Throwable, Unit] =
    ZIO.serviceWithZIO[UserRepository](_.add(user))

  def update(user: User): ZIO[UserRepository, Throwable, Unit] =
    ZIO.serviceWithZIO[UserRepository](_.updateUser(user))

  def delete(id: Int): ZIO[UserRepository, Throwable, Unit] =
    ZIO.serviceWithZIO[UserRepository](_.delete(id))
}