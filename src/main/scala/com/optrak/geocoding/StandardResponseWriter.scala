package com.optrak.geocoding


import codec.JsonWriter
import codec.JsonWritingSupport._


object StandardResponseWriter {
  implicit val succWriter = JsonWriter[SuccessResponse]
  implicit val errWriter = JsonWriter[ErrorResponse]
  implicit val stdRespWriter = JsonWriter[StandardResponse]
}


