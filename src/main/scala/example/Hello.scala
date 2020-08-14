package example

import PrettyPrint._
import PrettyPrint.PrettyPrintOps

object Hello extends Greeting with App {
  println(Person("Shane", 23).printPlease)
}

trait Greeting {
  lazy val greeting: String = "hello"
}
