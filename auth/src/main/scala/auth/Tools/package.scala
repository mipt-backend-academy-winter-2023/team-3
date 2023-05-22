package auth

import auth.model.User
import pdi.jwt.{JwtAlgorithm, JwtCirce}
import io.circe.Json
import io.circe.parser._
import io.circe.syntax._

import scala.util.{Failure, Success}
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import org.apache.commons.codec.binary.Base64

import java.util.Base64

package object Tools {
  def generateJwtToken(user: User): String = {
    val payload: Json = parse(s"""{"username": "${user.username}", "password": "${user.password}"} """) match {
      case Left(_) => Json.Null
      case Right(json) => json
    }

    JwtCirce.encode(payload, user.password, JwtAlgorithm.HS256)
  }

  object PasswordEncryptor {
    def encrypt(password: String): String = {
      val key = "mySecretKey123456"

      val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
      val secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES")

      cipher.init(Cipher.ENCRYPT_MODE, secretKey)

      val encryptedBytes = cipher.doFinal(password.getBytes("UTF-8"))

      Base64.getEncoder.encodeToString(encryptedBytes)
    }
  }
}
