package photo.config

import pureconfig._
import pureconfig.generic.semiauto.deriveReader
import zio.http.ServerConfig
import zio.sql.ConnectionPoolConfig
import zio.{ULayer, ZIO, ZLayer, http}

import java.util.Properties

object Config {
  private val basePath = "app"
  private val source = ConfigSource.default.at(basePath)



  val serverLive: ZLayer[Any, Nothing, ServerConfig] =
    zio.http.ServerConfig.live(
      http.ServerConfig.default.port(
        source.loadOrThrow[ConfigImpl].httpServiceConfig.port
      )
    )

}

case class ConfigImpl(
                       httpServiceConfig: HttpServerConfig
                     )


case class HttpServerConfig(
                             host: String,
                             port: Int
                           )

object ConfigImpl {
  implicit val configReader: ConfigReader[ConfigImpl] = deriveReader[ConfigImpl]
  implicit val configReaderHttpServerConfig: ConfigReader[HttpServerConfig] =
    deriveReader[HttpServerConfig]
}