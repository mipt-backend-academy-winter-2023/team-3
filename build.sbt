import Dependencies.{Auth, Helper, Routing, Image}
ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"

ThisBuild / assemblyMergeStrategy := {
  case PathList("META-INF", xs @ _*) =>
    xs.map(_.toLowerCase) match {
      case "manifest.mf" :: Nil | "index.list" :: Nil | "dependencies" :: Nil =>
        MergeStrategy.discard
      case _ =>
        MergeStrategy.first
    }
  case x => (ThisBuild / assemblyMergeStrategy).value(x)
}

ThisBuild / javacOptions ++= Seq("-source", "17", "-target", "17")

lazy val root = (project in file("."))
  .settings(
    name := "project-mipt"
  )
  .aggregate(
    auth,
    routing,
    image,
    helper
  )
  .dependsOn(
    auth,
    routing,
    image,
    helper
  )

lazy val auth = (project in file("auth"))
  .settings(
    name := "project-auth",
    libraryDependencies ++= Auth.dependencies,
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
    assembly / assemblyJarName := "auth.jar",
    assembly / mainClass       := Some("auth.AuthMain")
  )

lazy val routing = (project in file("routing"))
  .settings(
    name := "project-routing",
    libraryDependencies ++= Routing.dependencies,
    assembly / assemblyJarName := "routing.jar",
    assembly / mainClass       := Some("routing.RoutingMain")
  )

lazy val image = (project in file("image"))
  .settings(
    name := "project-image",
    libraryDependencies ++= Image.dependencies,
    assembly / assemblyJarName := "image.jar",
    assembly / mainClass       := Some("image.ImageMain")
  )

lazy val helper = (project in file("helper"))
  .settings(
    name := "project-helper",
    libraryDependencies ++= Helper.dependencies
  )
