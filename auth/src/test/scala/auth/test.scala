package auth

import auth.api.HttpRoutes
import auth.model.User
import io.circe.Json
import io.circe.Encoder
import zio.http.{!!, Body, Request, Response, URL}
import zio.ZLayer
import zio.http.model.Status
import zio.test.{ZIOSpecDefault, assertTrue, suite, test}

import scala.collection.mutable

trait TestData {
  val user1               = new User("a", "12345")
  val user1_fake_password = new User("a", "54321")

  def userBody(user: User) = {
    val json = Json.obj(
      "login"    -> Encoder[String].apply(user.login),
      "password" -> Encoder[String].apply(user.password)
    )
    Body.fromString(json.noSpaces)
  }

  def statusCreated(response: Response) =
    response.status == Status.Created

  def statusOk(response: Response) =
    response.status == Status.Ok

  def statusBadRequest(response: Response) =
    response.status == Status.BadRequest

  def statusUnauthorized(response: Response) =
    response.status == Status.Unauthorized

  def register(user: User) =
    HttpRoutes.app.runZIO(
      Request.post(
        userBody(user),
        URL(!! / "auth" / "v1" / "register")
      )
    )

  def login(user: User) =
    HttpRoutes.app.runZIO(
      Request.post(
        userBody(user),
        URL(!! / "auth" / "v1" / "login")
      )
    )
}
object RegisterTests extends ZIOSpecDefault with TestData {
  def spec = suite("Register:")(
    test("tests:") {
      (for {
        user1_register       <- register(user1)
        user1_register_again <- register(user1)
      } yield {
        assertTrue(
          statusCreated(user1_register)
            && statusBadRequest(user1_register_again)
        )
      }).provideLayer(
        ZLayer.succeed(new MockUserRepository(mutable.HashMap.empty))
      )
    }
  )
}

object LogInTests extends ZIOSpecDefault with TestData {

  def spec = suite("login:")(
    test("tests:") {
      (for {
        user1_login              <- login(user1)
        user1_register           <- register(user1)
        user1_login_again        <- login(user1)
        user1_login_bad_password <- login(user1_fake_password)
      } yield {
        assertTrue(
          statusUnauthorized(user1_login)
            && statusCreated(user1_register)
            && statusOk(user1_login_again)
            && statusUnauthorized(user1_login_bad_password)
        )
      }).provideLayer(
        ZLayer.succeed(new MockUserRepository(mutable.HashMap.empty))
      )
    }
  )
}
