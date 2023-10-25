package photo.api

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths, Path}

import zio._
import zio.http._
import zio.http.model.{Method, Status, Header, Headers, HeaderValues}
import zio.stream.{ZSink, ZStream, ZPipeline}

import java.io.File
import java.nio.file.{Files, Paths}

object HttpRoutes {

  def getPath(x: String) = Paths.get(s"/${x}file")

  val MAX_FILE_SIZE_MB = 10;

  val app: HttpApp[Any, Nothing] =
    Http.collectZIO[Request] {
      case request @ Method.PUT -> !! / "upload" =>
        (for {
          nodeId <- ZIO
            .fromOption(
              request.url.queryParams
                .get("nodeId")
                .flatMap(_.headOption)
            )
            .tapError(_ => ZIO.logError("no nodeId parameter"))
          path = getPath(nodeId)
          _ <- ZIO
            .attempt(Files.createFile(path))
          fileSize <- request.body.asStream
            .run(ZSink.fromPath(path))
        } yield (nodeId, fileSize) ).either.map {
          case Left(e) =>
            Response(
              status = Status.BadRequest,
              body = Body.fromString(e.toString)
            )
          case Right( (nodeId, fileSize) ) =>
            if (fileSize > MAX_FILE_SIZE_MB * 1024 * 1024) {
              Response(
                status = Status.UnprocessableEntity,
                body = Body.fromString("file too big")
              )
            } else {
              Response(
                status = Status.Created,
                body = Body.fromString(nodeId)
              )
            }
        }

      case request @ Method.GET -> !! / "download" =>
        (for {
          nodeId <- ZIO
            .fromOption(
              request.url.queryParams
                .get("nodeId")
                .flatMap(_.headOption)
            )
            .tapError(_ => ZIO.logError("no nodeId parameter"))
        } yield (nodeId)).either.map {
          case Left(e) =>
            Response(
              status = Status.BadRequest,
              body = Body.fromString(e.toString)
            )
          case Right(nodeId) =>
            val path = getPath(nodeId)
            if (Files.exists(path)) {
              Response(
                body = Body.fromFile(new File(path.toAbsolutePath.toString)),
              ).addHeader("content-type", "image/jpeg")
            } else {
              Response(status = Status.BadRequest)
            }
        }
    }
}