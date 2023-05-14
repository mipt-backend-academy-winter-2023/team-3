package auth.api

import zio.ZIO
import zio.http._
import zio.http.model.{Method, Status}

object HttpRoutes {
  val app: HttpApp[Any, Response] =
    Http.collectZIO[Request] {
      case Method.POST -> !! / "auth" / "v1" / "login" =>
        ZIO.succeed(Response.status(Status.NotImplemented))
      case Method.POST -> !! / "auth" / "v1" / "register" =>
        ZIO.succeed(Response.status(Status.NotImplemented))
    }
}
