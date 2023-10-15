package routing.repo

import routing.model.Street
import zio.ZIO
import zio.stream.ZStream

trait StreetsRepository {

  def findStreet(street: Street): ZStream[Any, Throwable, Street]

  def add(street: Street): ZIO[Any, Throwable, Unit]
}

object StreetsRepository {
  def findStreet(street: Street): ZStream[StreetsRepository, Throwable, Street] =
    ZStream.serviceWithStream[StreetsRepository](_.findStreet(street))

  def add(street: Street): ZIO[StreetsRepository, Throwable, Unit] =
    ZIO.serviceWithZIO[StreetsRepository](_.add(street))
}
