package auth

import api.HttpRoutes
import config.Config
import flyway.FlywayAdapter
import repo.UserRepositoryImpl
import zio.http.Server
import zio.sql.ConnectionPool
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault}

import scala.language.postfixOps

object AuthMain extends ZIOAppDefault {
  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = {
    val server = for {
      flyway <- ZIO.service[FlywayAdapter.Service]
      _ <- flyway.migration
      server <- zio.http.Server.serve(HttpRoutes.app)
    } yield server

    server.provide(
      Config.dbLive,
      FlywayAdapter.live,
      Server.live,
      Config.serverLive,
      UserRepositoryImpl.live,
      ConnectionPool.live,
      Config.connectionPoolConfigLive
    )
  }
}