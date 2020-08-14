package PrettyPrint

import shapeless._

object Random {
  val generic = Generic[Person].to(Person("shane", 23))
}

abstract class PrettyPrint[A] {
  def myPrettyPrint(a: A): String
}

object PrettyPrint {
  def apply[A](implicit pret:PrettyPrint[A]) =  pret

  implicit val StringPrettyPrint: PrettyPrint[String] = new PrettyPrint[String] {
      def myPrettyPrint(a: String): String = a
  }

  implicit val IntPrettyPrint: PrettyPrint[Int] = new PrettyPrint[Int] {
      def myPrettyPrint(a: Int): String = a.toString()
  }

  implicit val hnilPrettyPrint: PrettyPrint[shapeless.HNil] = new PrettyPrint[shapeless.HNil] {
      def myPrettyPrint(a: shapeless.HNil): String = "HNil"
  }

  implicit def genericPrettyPrint[A, R](
    implicit
      gen: Generic.Aux[A, R],
      env: PrettyPrint[R]
  ): PrettyPrint[A] = new PrettyPrint[A] {
    def myPrettyPrint(value: A): String = env.myPrettyPrint(gen.to(value))
  }

  implicit def hlistEncoder[H, T <: HList](
    implicit
     hPretty: PrettyPrint[H],
     tPretty: PrettyPrint[T]
  ): PrettyPrint[H :: T] = new PrettyPrint[H :: T] {
    def myPrettyPrint(a: H :: T): String = {
      val h = a.head
      val t = a.tail
      hPretty.myPrettyPrint(h) ++ "|" ++ tPretty.myPrettyPrint(t)
    }
  }

  implicit class PrettyPrintOps[A](underlying: A) {
    def printPlease(implicit pr: PrettyPrint[A]) = {
      pr.myPrettyPrint(underlying)
    }
  }
}
