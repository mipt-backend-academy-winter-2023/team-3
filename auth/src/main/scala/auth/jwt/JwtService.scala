package auth.jwt

import java.time.Clock

import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim}

object JwtService {

  private val SECRET_KEY = "secretKey"

  implicit val clock: Clock = Clock.systemUTC()

  def encode(login: String): String = {
    val claim = JwtClaim(login).issuedNow.expiresIn(300)
    Jwt.encode(claim, SECRET_KEY, JwtAlgorithm.HS512)
  }

  def decode(token: String): Option[JwtClaim] = {
    Jwt.decode(token, SECRET_KEY, Seq(JwtAlgorithm.HS512)).toOption
  }
}
