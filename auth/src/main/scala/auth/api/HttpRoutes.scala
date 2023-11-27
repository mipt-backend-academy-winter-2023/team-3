package auth.api

import auth.jwt.JwtService
import auth.model.User
import auth.repo.UsersRepository
import io.circe.generic.codec.DerivedAsObjectCodec.deriveCodec
import io.circe.jawn._
import zio.ZIO
import zio.http._
import zio.http.model.{Method, Status}

object HttpRoutes {
  val app: HttpApp[UsersRepository, Response] =
    Http.collectZIO[Request] {
      case req @ Method.POST -> !! / "auth" / "v1" / "login" =>
        (for {
          bodyStr <- req.body.asString
          user    <- ZIO.fromEither(decode[User](bodyStr)).tapError(e => ZIO.logError(e.getMessage))
          found   <- UsersRepository.findUser(user).runHead
        } yield (found, user)).either.map {
          case Right((found, user)) =>
            found match {
              case Some(real_user) => Response.json(s"{\"token\": \"${JwtService.encode(real_user.login)}\"}")
              case None =>
                ZIO.logInfo(s"User '${user.login}' not found")
                Response.status(Status.Unauthorized)
            }
          case Left(_) => Response.status(Status.BadRequest)
        }

      case req @ Method.POST -> !! / "auth" / "v1" / "register" =>
        (for {
          bodyStr <- req.body.asString
          user    <- ZIO.fromEither(decode[User](bodyStr)).tapError(e => ZIO.logError(e.getMessage))
          _ <- UsersRepository
            .add(user)
            .tapError(e => ZIO.logInfo(s"User '${user.login}' already exist(${e.getMessage})"))
          _ <- ZIO.logInfo(s"Created new user '${user.login}'")
        } yield ()).either.map {
          case Right(_) => Response.status(Status.Created)
          case Left(_)  => Response.status(Status.BadRequest)
        }
    }
}
