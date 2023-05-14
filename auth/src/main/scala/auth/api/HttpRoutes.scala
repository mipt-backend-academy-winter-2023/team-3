package auth.api

import io.circe.generic.codec.DerivedAsObjectCodec.deriveCodec
import io.circe.jawn._
import io.circe.syntax.EncoderOps
import auth.model.User
import auth.repo.CustomerRepository
import zio.ZIO
import zio.http._
import zio.http.model.{Method, Status}

object HttpRoutes {
  val app: HttpApp[CustomerRepository, Response] =
    Http.collectZIO[Request] {
      case Method.POST -> !! / "auth" / "v1" / "login" =>
        CustomerRepository
          .findAll()
          .runCollect
          .map(chunk => chunk.toArray)
          .tapError(e => ZIO.logError(e.getMessage))
          .either
          .map {
            case Right(users) => Response.json(users.asJson.spaces2)
            case Left(e)      => Response.status(Status.InternalServerError)
          }

      case Method.POST -> !! / "auth" / "v1" / "register" =>
        CustomerRepository
          .findAll()
          .runCollect
          .map(chunk => chunk.toArray)
          .tapError(e => ZIO.logError(e.getMessage))
          .either
          .map {
            case Right(users) => Response.json(users.asJson.spaces2)
            case Left(e)      => Response.status(Status.InternalServerError)
          }
    }
}
