package image.api

import image.validation.JpegValidation

import zio.ZIO
import zio.http._
import zio.http.model.{Method, Status}
import zio.stream.{ZPipeline, ZSink}

import java.nio.file.{Files, Paths}

object HttpRoutes {
  val app: HttpApp[Any, Response] =
    Http.collectZIO[Request] {
      case req@Method.PUT -> !! / "upload" / id =>
        (for {
          path <- ZIO.attempt(
            Files.createFile(
              Paths.get("images/" + id)
            )
          )
          bytesCount <- req.body.asStream
            .via(JpegValidation.pipeline)
            .run(ZSink.fromPath(path))
        } yield bytesCount).either.map {
          case Right(bytesCount) if bytesCount <= 10 * 1024 * 1024 => Response.ok
          case _ =>
            Files.deleteIfExists(Paths.get("images/" + id))
            Response.status(Status.BadRequest)
        }
    }
}
