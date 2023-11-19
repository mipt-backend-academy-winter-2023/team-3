package auth.model

import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator

object PasswordEncryptor {

  private val keyGenerator = KeyGenerator.getInstance("AES")
  keyGenerator.init(128)

  private val secretKey = keyGenerator.generateKey

  private val cipher = Cipher.getInstance("AES")

  def encode(password: String): String = {
    cipher.init(Cipher.ENCRYPT_MODE, secretKey)

    val plainTextByte         = password.getBytes
    val encryptedByte         = cipher.doFinal(plainTextByte)
    val encoder               = Base64.getEncoder
    val encryptedText: String = encoder.encodeToString(encryptedByte)

    encryptedText
  }

  def decode(encryptedText: String): String = {
    cipher.init(Cipher.DECRYPT_MODE, secretKey)

    val decoder           = Base64.getDecoder
    val encryptedTextByte = decoder.decode(encryptedText)
    val decryptedByte     = cipher.doFinal(encryptedTextByte)
    val decryptedText     = new String(decryptedByte)

    decryptedText
  }
}
