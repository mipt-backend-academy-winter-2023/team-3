package routing

import routing.api.HttpRoutes
import routing.config.Config
import routing.flyway.FlywayAdapter
import routing.repo.{StreetsRepositoryImpl, GeoNodesRepositoryImpl}
import zio.http.Server
import zio.sql.ConnectionPool
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault}

object RoutingMain extends ZIOAppDefault {
  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = {
    val server =
      for {
        flyway <- ZIO.service[FlywayAdapter.Service]
        _ <- flyway.migration
        server <- zio.http.Server.serve(HttpRoutes.app)
      } yield server

    server.provide(
      Config.dbLive,
      FlywayAdapter.live,
      Server.live,
      Config.serverLive,
      GeoNodesRepositoryImpl.live,
      ConnectionPool.live,
      Config.connectionPoolConfigLive
    )
  }
}
