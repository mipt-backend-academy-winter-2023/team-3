package auth

import auth.model.User
import pdi.jwt.{JwtAlgorithm, JwtCirce}
import io.circe.Json
import io.circe.parser._
import io.circe.syntax._

import scala.util.{Failure, Success}

package object Tools {
  def generateJwtToken(user: User): String = {
    val payload: Json = parse(s"""{"userId": "${user.id}", "username": "${user.username}", "password": "${user.password}"} """) match {
      case Left(_) => Json.Null
      case Right(json) => json
    }

    JwtCirce.encode(payload, user.password, JwtAlgorithm.HS256)
  }
}
