package routing.repo

import routing.model.{GeoNode, Street}
import zio.schema.DeriveSchema
import zio.sql.postgresql.PostgresJdbcModule

trait PostgresTableDescription extends PostgresJdbcModule {

  implicit val geoNodeSchema = DeriveSchema.gen[GeoNode]

  val geoNodes = defineTable[GeoNode]

  val (node_id, node_type, name, latitude, longitude) = geoNodes.columns

  implicit val streetsSchema = DeriveSchema.gen[Street]

  val streets = defineTable[Street]

  val (from_latitude, from_longitude, to_latitude, to_longitude) = streets.columns
}
