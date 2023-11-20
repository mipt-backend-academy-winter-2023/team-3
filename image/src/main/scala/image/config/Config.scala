package image.config

import pureconfig.ConfigSource
import pureconfig.generic.auto.exportReader
import zio.http.ServerConfig
import zio.{ZLayer, http}

case class Config(host: String, port: Int)

object ServiceConfig {
  private val source                = ConfigSource.default.at("app").at("http-service")
  private val serviceConfig: Config = source.loadOrThrow[Config]

  val live: ZLayer[Any, Nothing, ServerConfig] = zio.http.ServerConfig.live {
    http.ServerConfig.default
      .binding(serviceConfig.host, serviceConfig.port)
  }
}
