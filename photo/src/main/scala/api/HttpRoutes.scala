package photo.api

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths, Path}

import zio._
import zio.http._
import zio.http.model.{Method, Status, Header, Headers}
import zio.stream.{ZSink, ZStream, ZPipeline}

object HttpRoutes {

  def getPath(x: String) = Paths.get(s"/${x}file")


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
          _ <- request.body.asStream
            .via(ZPipeline.deflate())
            .run(ZSink.fromPath(path))
        } yield (nodeId)).either.map {
          case Left(e) =>
            Response(
              status = Status.BadRequest,
              body = Body.fromString(e.toString)
            )
          case Right(nodeId) =>
            Response(
              status = Status.Created,
              body = Body.fromString(nodeId)
            )
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
                body = Body.fromStream(
                  ZStream
                    .fromPath(path)
                    .via(ZPipeline.inflate())
                )
              )
            } else {
              Response(status = Status.BadRequest)
            }
        }
    }
}