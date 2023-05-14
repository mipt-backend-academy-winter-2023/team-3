package auth

import auth.api.HttpRoutes
import auth.config.ServiceConfig
import flyway.FlywayAdapter
import repo.CustomerRepositoryImpl
import zio.http.Server
import zio.sql.ConnectionPool
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault}

object AuthMain extends ZIOAppDefault {
  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = {
    val server = for {
      flyway <- ZIO.service[FlywayAdapter.Service]
      _ <- flyway.migration
      server <- zio.http.Server.serve(HttpRoutes.app)
    } yield server

    server.provide(
      ServiceConfig.dbLive,
      FlywayAdapter.live,
      Server.live,
      ServiceConfig.live,
      CustomerRepositoryImpl.live,
      ConnectionPool.live,
      ServiceConfig.connectionPoolConfigLive
    )
  }
}