ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

ThisBuild / assemblyMergeStrategy := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x =>
    val oldStrategy = (ThisBuild / assemblyMergeStrategy).value
    oldStrategy(x)
}

lazy val helper = (project in file("."))
  .settings(
    name := "project-helper",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "2.0.13",
      "dev.zio" %% "zio-http" % "0.0.5",
      "com.github.pureconfig" %% "pureconfig" % "0.17.3"
    ),
    assembly / assemblyJarName := "helper.jar",
    assembly / assemblyOutputPath := baseDirectory.value / "target" / (assembly / assemblyJarName).value
  )
