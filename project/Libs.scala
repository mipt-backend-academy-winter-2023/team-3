import sbt.*

object V {
  val zio         = "2.0.13"
  val zioHttp     = "0.0.5"
  val flyway      = "9.16.0"
  val zioSql      = "0.1.2"
  val pureconfig  = "0.17.3"
  val circe       = "0.14.1"
  val jwtCore     = "9.2.0"
  val test        = "3.2.15"
  val zioTest     = "2.0.15"
  val zioTestMock = "1.0.0-RC9"
}

object Libs {

  val zio: List[ModuleID] = List(
    "dev.zio" %% "zio"              % V.zio,
    "dev.zio" %% "zio-http"         % V.zioHttp,
    "dev.zio" %% "zio-sql-postgres" % V.zioSql
  )

  val flyway: List[ModuleID] = List(
    "org.flywaydb" % "flyway-core" % V.flyway
  )

  val pureconfig: List[ModuleID] = List(
    "com.github.pureconfig" %% "pureconfig" % V.pureconfig
  )

  val jwtCore: List[ModuleID] = List(
    "com.github.jwt-scala" %% "jwt-core" % V.jwtCore
  )

  val circe: List[ModuleID] = List(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser"
  ).map(_ % V.circe)

  val test: List[ModuleID] = List(
    // "org.scalatest" %% "scalatest" % V.test % Test,
    "dev.zio" %% "zio-test"          % V.zioTest % Test,
    "dev.zio" %% "zio-test-sbt"      % V.zioTest % Test,
    "dev.zio" %% "zio-test-magnolia" % V.zioTest % Test
    // "dev.zio" %% "zio-mock" % V.zioTestMock % Test
  )
}
