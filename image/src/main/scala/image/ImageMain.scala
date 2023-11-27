package image

import image.api.HttpRoutes
import image.config.ServiceConfig
import zio.http.Server
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault}

object ImageMain extends ZIOAppDefault {
  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = {
    for {
      _ <- ZIO.logInfo("Start ImageMain")
      _ <- zio.http.Server
        .serve(HttpRoutes.app)
        .provide(
          Server.live,
          ServiceConfig.live
        )
    } yield ()
  }
}
