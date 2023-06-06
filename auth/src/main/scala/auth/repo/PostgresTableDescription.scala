package auth.repo

import auth.model.User
import zio.schema.DeriveSchema
import zio.sql.postgresql.PostgresJdbcModule

trait PostgresTableDescription extends PostgresJdbcModule {

  implicit val userSchema = DeriveSchema.gen[User]

  val users = defineTable[User]

  val (login, password) = users.columns
}
