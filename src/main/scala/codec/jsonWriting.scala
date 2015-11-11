package codec

import org.json4s.JsonAST._
import org.json4s.JsonDSL._
import shapeless._
import org.json4s.native.JsonMethods.{compact, render}
import shapeless.labelled._

object JsonBits {
  type JResult = List[JField]
}

import JsonBits._

trait JsonWriter[A] {
  def write(name: Option[String], a: A): JResult
}

object JsonWriter extends LabelledTypeClassCompanion[JsonWriter] {
  def apply[T](implicit w: JsonWriter[T]) = w

  def toJResult(nameOpt: Option[String], ns: JValue): JResult =
    nameOpt.map(name => List(JField(name, ns))).getOrElse(List.empty)

  def toJResult(nameOpt: Option[String], s: String): JResult =
    nameOpt.map(name => List(JField(name, s))).getOrElse(List.empty)

  def toJResult(nameOpt: Option[String], content: Seq[JValue]): JResult =
    nameOpt.map(name => List(JField(name, content))).getOrElse(List.empty)

  implicit val typeClass = new LabelledTypeClass[JsonWriter] {

    def emptyProduct = new JsonWriter[HNil] {
      def write(name: Option[String], hn: HNil): JResult = {
        assert(name == None)
        List.empty
      }
    }

    def product[F, T <: HList](fieldName: String, FHead: JsonWriter[F], FTail: JsonWriter[T]) = new JsonWriter[F :: T] {
      def write(name: Option[String], ft: F :: T): JResult = {
        val hCons = FHead.write(Some(fieldName), ft.head) ::: FTail.write(None, ft.tail)
        name.map { name =>
          List(JField(name, hCons))
        }.getOrElse(hCons)
      }
    }

    def emptyCoproduct = new JsonWriter[CNil] {
      def write(name: Option[String], t: CNil) = {
        assert(name == None)
        List.empty
      }
    }

    def coproduct[L, R <: Coproduct] (fieldName: String, CL: => JsonWriter[L], CR: => JsonWriter[R]) = new JsonWriter[L :+: R] {
      override def write(name: Option[String], lr: L :+: R): JResult = {
        lr match {
          case Inl(l) => CL.write(Some(fieldName), l) // or List(JField(fieldName, CL.write(None, l)))
          case Inr(r) => CR.write(None, r)
        }
      }
    }

    def project[F, G](instance : => JsonWriter[G], to: F => G, from: G => F) = new JsonWriter[F] {
      def write(name: Option[String], t: F): JResult = {
        val tot = to(t)
        instance.write(name, tot)
      }
    }
  }

}


object JsonWritingSupport {

  import JsonWriter._

  implicit val IntWriter = new JsonWriter[Int] {
    def write(nameOpt: Option[String], i: Int) = toJResult(nameOpt, i.toString)
  }
  implicit val DoubleWriter = new JsonWriter[Double] {
    def write(nameOpt: Option[String], d: Double) = toJResult(nameOpt, d.toString)
  }
  implicit val StringWriter = new JsonWriter[String] {
    def write(nameOpt: Option[String], s: String) = toJResult(nameOpt, s)
  }

  class OptWriter[A](implicit aWriter: JsonWriter[A]) extends JsonWriter[Option[A]] {
    def write(nameOpt: Option[String], aOpt: Option[A]) = (for {
      name <- nameOpt
      a <- aOpt
    } yield aWriter.write(nameOpt, a)).getOrElse(List.empty)
  }

  class ListWriter[A](implicit aWriter: JsonWriter[A]) extends JsonWriter[List[A]] {
    def write(name: Option[String], aList: List[A]): JResult = {
      val items = aList.map(a => aWriter.write(None, a))
      println(s"list writer writes $items")
      toJResult(name, items)
    }
  }

  implicit def mkListWriter[A](implicit aWriter: JsonWriter[A]) = new ListWriter[A]
  implicit def mkOptWriter[A](implicit aWriter: JsonWriter[A]) = new OptWriter[A]
}