package auth.repo

import auth.model.User
import zio.schema.{DeriveSchema, Schema}
import zio.sql.macros.TableSchema
import zio.sql.postgresql.PostgresJdbcModule

trait PostgresTableDescription extends PostgresJdbcModule {

  implicit val userSchema = DeriveSchema.gen[User]

  val users = defineTable[User]

  val (username, password) = users.columns
}