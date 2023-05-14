package auth.repo

import auth.model.User
import zio.ZIO
import zio.stream.ZStream

trait CustomerRepository {

  def findAll(): ZStream[Any, Throwable, User]

  def add(customer: User): ZIO[Any, Throwable, Unit]

  def updateCustomer(customer: User): ZIO[Any, Throwable, Unit]

  def delete(id: Int): ZIO[Any, Throwable, Unit]
}

object CustomerRepository {
  def findAll(): ZStream[CustomerRepository, Throwable, User] =
    ZStream.serviceWithStream[CustomerRepository](_.findAll())

  def add(customer: User): ZIO[CustomerRepository, Throwable, Unit] =
    ZIO.serviceWithZIO[CustomerRepository](_.add(customer))

  def update(customer: User): ZIO[CustomerRepository, Throwable, Unit] =
    ZIO.serviceWithZIO[CustomerRepository](_.updateCustomer(customer))

  def delete(id: Int): ZIO[CustomerRepository, Throwable, Unit] =
    ZIO.serviceWithZIO[CustomerRepository](_.delete(id))
}
