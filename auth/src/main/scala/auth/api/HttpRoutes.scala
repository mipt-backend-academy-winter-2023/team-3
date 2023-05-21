package auth.api

import auth.Tools.generateJwtToken
import io.circe.generic.codec.DerivedAsObjectCodec.deriveCodec
import io.circe.jawn._
import io.circe.syntax.EncoderOps
import auth.model.User
import auth.repo.UserRepository
import zio.ZIO
import zio.http._
import zio.http.model.{Headers, Method, Status}

object HttpRoutes {
  val app: HttpApp[UserRepository, Response] =
    Http.collectZIO[Request] {
      case req@Method.POST -> !! / "auth" / "v1" / "register" =>
        (for {
          bodyStr <- req.body.asString
          user <- ZIO.fromEither(decode[User](bodyStr)).tapError(e => ZIO.logError(e.getMessage))
          selectAll <- UserRepository.retrieveByUsername(user).runCollect.map(_.toArray)
        } yield (user, selectAll)).either.map {
          case Right(users) =>
            users match {
              case (user, Array()) =>
                UserRepository.add(user)
                ZIO.logInfo (s"Registered new user $user")
                Response.ok
              case (_, _) =>
                Response(Status.Conflict, Headers.empty, Body.fromString(
                  s"""
                     |{
                     |\"code\": \"409\",
                     |\"message\": \"user exists\"
                     }
                     |""".stripMargin))
            }
          case Left(_) => Response.status(Status.BadRequest)
        }

      case req@Method.POST -> !! / "auth" / "v1" / "login" =>
        (for {
          bodyStr <- req.body.asString
          user <- ZIO.fromEither(decode[User](bodyStr)).tapError(e => ZIO.logError(e.getMessage))
          selectAll <- UserRepository.retrieve(user).runCollect.map(_.toArray)
        } yield selectAll).either.map {
          case Right(users) =>
            users match {
              case Array() =>
                Response(Status.Unauthorized, Headers.empty, Body.fromString(
                  s"""
                     |{
                     |\"code\": \"401\",
                     |\"message\": \"incorrect username or password\"
                      }
                     |""".stripMargin))
              case arr =>
                ZIO.logInfo(s"Log in user ${arr.head}")
                Response.json(s"{\"access_token\": \"${generateJwtToken(arr.head)}\"}")
            }
          case Left(_) => Response.status(Status.BadRequest)
        }
    }
}