package routing.repo

import routing.model.GeoNode
import zio.sql.ConnectionPool
import zio.stream.ZStream
import zio.{ZIO, ZLayer}

final class GeoNodesRepositoryImpl(
    pool: ConnectionPool
) extends GeoNodesRepository
    with PostgresTableDescription {

  val driverLayer: ZLayer[Any, Nothing, SqlDriver] =
    ZLayer
      .make[SqlDriver](
        SqlDriver.live,
        ZLayer.succeed(pool)
      )

  override def findGeoNode(geoNode: GeoNode): ZStream[Any, Throwable, GeoNode] = {
    val selectGeoNode = select(node_id, node_type, name, latitude, longitude)
      .from(geoNodes)
      .where(node_type === geoNode.node_type && latitude === geoNode.latitude && longitude === geoNode.longitude)

    ZStream.fromZIO(
      ZIO.logInfo(s"Query to execute findGeoNode is ${renderRead(selectGeoNode)}")
    ) *>
      execute(selectGeoNode.to((GeoNode.apply _).tupled))
        .provideSomeLayer(driverLayer)
  }

  override def add(geoNode: GeoNode): ZIO[Any, Throwable, Unit] = {
    val query =
      insertInto(geoNodes)(node_id, node_type, name, latitude, longitude)
        .values(
          (
            geoNode.node_id,
            geoNode.node_type,
            geoNode.name,
            geoNode.latitude,
            geoNode.longitude
          )
        )

    ZIO.logInfo(s"Query to insert geoNode is ${renderInsert(query)}") *>
      execute(query)
        .provideSomeLayer(driverLayer)
        .unit
  }
}

object GeoNodesRepositoryImpl {
  val live: ZLayer[ConnectionPool, Throwable, GeoNodesRepository] =
    ZLayer.fromFunction(new GeoNodesRepositoryImpl(_))
}
