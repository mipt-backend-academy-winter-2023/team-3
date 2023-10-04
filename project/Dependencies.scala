import Libs.*
import sbt.*

trait Dependencies {
  def dependencies: Seq[ModuleID]
}

object Dependencies {

  object Auth extends Dependencies {
    override def dependencies: Seq[ModuleID] = Seq(zio, pureconfig, flyway, circe, jwtCore).flatten
  }

  object Routing extends Dependencies {
    override def dependencies: Seq[ModuleID] = Seq(zio, pureconfig).flatten
  }

  object Photo extends Dependencies {
    override def dependencies: Seq[ModuleID] = Seq(zio, pureconfig, circe).flatten
  }

  object Helper extends Dependencies {
    override def dependencies: Seq[ModuleID] = Seq(zio, pureconfig).flatten
  }
}