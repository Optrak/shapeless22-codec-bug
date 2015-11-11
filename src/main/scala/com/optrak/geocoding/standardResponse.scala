package com.optrak.geocoding

/**
 * Created by yar on 08.09.15.
 * this file name has to lexicographically precede GlobalService, otherwise JsonWriter[StandardResponse] in
 * geocoderca.GeocodingRoute will not compile.
 * don't put things that need to be serialized/deserialized by scalautil into a package object
 * but, once project has been compiled successfully, they can be put into a package object until sbt clean
 */

case class Address(address: Option[String],
                   city : Option[String],
                   postalCode : Option[String],
                   country : Option[String])
sealed trait StandardResponse
case class SuccessResponse(standardAddress: Option[Address],
                           latitude: Double,
                           longitude: Double,
                           confidence: Option[Double]) extends StandardResponse
case class ErrorResponse(code: Option[String], description: String) extends StandardResponse

/*object StandardResponseWriter {
  // this compiles OK
  import codec.JsonWriter
  import codec.JsonWritingSupport._

  implicit val succWriter = JsonWriter[SuccessResponse]
  implicit val errWriter = JsonWriter[ErrorResponse]
  implicit val stdRespWriter = JsonWriter[StandardResponse]
}*/
