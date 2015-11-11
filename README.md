# Shapeless 2.2.5 Based JSON Serializer Fails to Compile

This is a small project made to illustrate the problem.

`codec` package contains the JSON serializer code.

`com.optrak.geocoding` package holds standardResponse.scala file where the `StandardResponse` sealed trait being serialized lives.
StandardResponseWriter.scala file in the same package contains JSON writers for the aforementioned trait.
The name of the latter file matters. If it lexicographically precedes the name of the former file, as it does now, I get
    
    [error] /media/System/Users/Yar/Programming/Scala/tims-scala/geocoder-serialization-problem/src/main/scala/com/optrak/geocoding/StandardResponseWriter.scala:11: could not find implicit value for parameter w: codec.JsonWriter[com.optrak.geocoding.StandardResponse]
    [error]   implicit val stdRespWriter = JsonWriter[StandardResponse]
    [error]                                          ^
    
However, if the file name is changed to, say, uStandardResponseWriter.scala (follows standardResponse.scala), compilation succeeds. 
If StandardResponseWriter object is placed into the standardResponse.scala file, the project compiles as well.