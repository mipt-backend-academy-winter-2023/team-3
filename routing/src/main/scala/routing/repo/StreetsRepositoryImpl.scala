package routing.repo

import routing.model.Street
import zio.sql.ConnectionPool
import zio.stream.ZStream
import zio.{ZIO, ZLayer}

final class StreetsRepositoryImpl(
                                 pool: ConnectionPool
                               ) extends StreetsRepository
  with PostgresTableDescription {

  val driverLayer: ZLayer[Any, Nothing, SqlDriver] =
    ZLayer
      .make[SqlDriver](
        SqlDriver.live,
        ZLayer.succeed(pool)
      )

  override def findStreet(street: Street): ZStream[Any, Throwable, Street] = {
    val selectStreet = select(from_latitude, from_longitude, to_latitude, to_longitude)
      .from(streets)
      .where(from_latitude == geoNode.from_latitude &&
             from_longitude == geoNode.from_longitude &&
             to_latitude == geoNode.to_latitude &&
             to_longitude == geoNode.to_longitude)

    ZStream.fromZIO(
      ZIO.logInfo(s"Query to execute findStreet is ${renderRead(selectStreet)}")
    ) *>
      execute(selectStreet.to((Street.apply _).tupled))
        .provideSomeLayer(driverLayer)
  }

  override def add(street: Street): ZIO[StreetsRepository, Throwable, Unit] = {
    val query =
      insertInto(streets)(from_latitude, from_longitude, to_latitude, to_longitude)
        .values(
          (
            streets.from_latitude,
            streets.from_longitude,
            streets.to_latitude,
            streets.to_longitude
          )
        )


    ZIO.logInfo(s"Query to insert Street is ${renderInsert(query)}") *>
      execute(query)
        .provideSomeLayer(driverLayer)
        .unit
  }
}

object StreetsRepositoryImpl {
  val live: ZLayer[ConnectionPool, Throwable, StreetsRepository] =
    ZLayer.fromFunction(new StreetsRepositoryImpl(_))
}
