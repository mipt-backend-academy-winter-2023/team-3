package image.api

import zio.ZIO
import zio.http._
import zio.http.model.{Method, Status}
import zio.stream.ZSink

import java.nio.file.{FileAlreadyExistsException, Files, Path, Paths}

case object FileAlreadyExistError

object HttpRoutes {
  private val MaxFileSize = 10 * 1024 * 1024

  val app: HttpApp[Any, Response] =
    Http.collectZIO[Request] {
      case req @ Method.PUT -> !! / "upload" / id =>
        val path = getPath(id)
        ensureDirectoryExists(path.getParent)
        (for {
          _ <- ZIO.attempt(Files.createFile(path)).mapError(_ => FileAlreadyExistError)
          bytesCount <- req.body.asStream
            .run(ZSink.fromPath(path))
        } yield bytesCount).either.map {
          case Right(bytesCount) if bytesCount <= MaxFileSize => Response.ok
          case Left(FileAlreadyExistError) =>
            Response.apply(status = Status.BadRequest, body = Body.fromString("File is already exist"))
          case Left(err) =>
            Response.apply(status = Status.BadRequest, body = Body.fromString(err.toString))
          case _ =>
            Files.deleteIfExists(path)
            Response.apply(status = Status.UnprocessableEntity, body = Body.fromString("The file size exceeds 10 MB"))
        }
      case _ @Method.GET -> !! / "download" / id =>
        val path = getPath(id)
        if (Files.exists(path)) {
          ZIO.succeed(Response(status = Status.Ok, body = Body.fromFile(path.toFile)))
        } else {
          ZIO.succeed(Response.status(Status.NotFound))
        }
    }

  private def getPath(id: String): Path = {
    Paths.get(s"./images/$id.jpeg")
  }

  private def ensureDirectoryExists(path: Path): Unit = {
    if (!Files.exists(path)) {
      Files.createDirectories(path)
    }
  }
}
