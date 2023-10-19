package image.api

import zio.ZIO
import zio.http._
import zio.http.model.{Method, Status}
import zio.stream.ZSink

import java.io.File
import java.nio.file.{Files, Paths}

object HttpRoutes {
  val app: HttpApp[Any, Response] =
    Http.collectZIO[Request] {
      case req@Method.PUT -> !! / "upload" / id =>
        (for {
          path <- ZIO.attempt(
            Files.createFile(
              Paths.get("./images/" + id + ".jpeg")
            )
          )
          bytesCount <- req.body.asStream
            .run(ZSink.fromPath(path))
        } yield bytesCount).either.map {
          case Right(bytesCount) if bytesCount <= 10 * 1024 * 1024 => Response.ok
          case _ =>
            Files.deleteIfExists(Paths.get("images/" + id))
            Response.status(Status.UnprocessableEntity)
        }
      case req@Method.GET -> !! / "download" / id =>
        val imagePath = Paths.get("./images/" + id + ".jpeg")
        if (Files.exists(imagePath)) {
          ZIO.succeed(
            Response(
              status = Status.Ok,
              body = Body.fromFile(new File(imagePath.toAbsolutePath.toString))
            )
          )
        } else {
          ZIO.succeed(Response.status(Status.NotFound))
        }
    }
}
