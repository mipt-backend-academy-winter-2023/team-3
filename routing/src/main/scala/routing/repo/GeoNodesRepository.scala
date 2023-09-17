package routing.repo

import routing.model.GeoNode
import zio.ZIO
import zio.stream.ZStream

trait GeoNodesRepository {

  def findGeoNode(geoNode: GeoNode): ZStream[Any, Throwable, GeoNode]

  def add(geoNode: GeoNode): ZIO[Any, Throwable, Unit]
}

object GeoNodesRepository {
  def findGeoNode(geoNode: GeoNode): ZStream[GeoNodesRepository, Throwable, GeoNode] =
    ZStream.serviceWithStream[GeoNodesRepository](_.findGeoNode(geoNode))

  def add(geoNode: GeoNode): ZIO[GeoNodesRepository, Throwable, Unit] =
    ZIO.serviceWithZIO[GeoNodesRepository](_.add(geoNode))
}
