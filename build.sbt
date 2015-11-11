organization := "com.optrak"

name := "shapeless22-codec-bug"

version := "1.2.1-SNAPSHOT"

resolvers += Resolver.sonatypeRepo("releases")

scalaVersion := "2.11.7"

addCompilerPlugin("com.artima.supersafe" %% "supersafe" % "1.0.0")

libraryDependencies ++= {
  val integration = version.value
    Seq(
      "com.chuusai" %% "shapeless" % "2.2.5",
      "org.json4s" %% "json4s-native" % "3.2.11"
    )
}

scalacOptions ++= Seq("-deprecation")


