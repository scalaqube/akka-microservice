import com.typesafe.sbt.packager.docker._

enablePlugins(DockerPlugin)
enablePlugins(JavaAppPackaging)

name := "miep-service"
organization:= "com.cffex"
version := "1.1"
scalaVersion := "2.11.8"

dockerBaseImage := "openjdk"
Keys.mainClass in (Compile) := Some("com.cffex.ms.RootApp")
dockerExposedPorts := Seq(9001,2552)

//mainClass in assembly := Some("com.cffex.devtool.RootApp")

libraryDependencies ++= {

    val AkkaVersion             = "2.4.11"
    val Json4sVersion           = "3.4.1"
    val ScalaTestVersion        = "3.0.0"
    var reactivemongoVersion    = "0.11.14"
    var logbackVersion          = "1.1.7"
    Seq(
        "com.typesafe.akka" %% "akka-actor"              % AkkaVersion,
        "com.typesafe.akka" %% "akka-http-core"         % AkkaVersion,
        "com.typesafe.akka" %% "akka-http-experimental" % AkkaVersion,
        "com.typesafe.akka" %% "akka-http-spray-json-experimental" % AkkaVersion,
        "com.typesafe.akka" %% "akka-slf4j"             % AkkaVersion,

        "com.typesafe.akka" %% "akka-remote" % AkkaVersion,
        "com.typesafe.akka" %% "akka-cluster" % AkkaVersion,
        "com.typesafe.akka" %% "akka-cluster-tools" % AkkaVersion,
        "com.typesafe.akka" %% "akka-cluster-sharding" % AkkaVersion,

        "org.reactivemongo" %% "reactivemongo"          % reactivemongoVersion,
        "ch.qos.logback"    %  "logback-classic"        % logbackVersion,

        "org.json4s"        %% "json4s-core"            % Json4sVersion,
        "org.json4s"        %% "json4s-native"          % Json4sVersion,
        "org.json4s"        %% "json4s-jackson"         % Json4sVersion,
        "org.json4s"        %% "json4s-ext"             % Json4sVersion,
        "de.heikoseeberger" %% "akka-http-json4s"       % "1.10.1",

        "com.typesafe.akka" %% "akka-persistence"       % AkkaVersion,
        "com.typesafe.akka" %% "akka-persistence-tck"   % AkkaVersion,
        "com.typesafe.akka" %% "akka-persistence-query-experimental" % AkkaVersion,
        "com.geteventstore" % "eventstore-client_2.11"              % "2.3.0",
        "com.geteventstore" % "akka-persistence-eventstore_2.11"    % "2.3.0",
        "com.sclasen"       % "akka-zk-cluster-seed_2.11"           % "0.1.6",

        "me.maciejb.etcd-client" %% "etcd-client" % "0.1.1",

        "org.scalatest"     %% "scalatest"              % ScalaTestVersion  % "test",
        "com.typesafe.akka" %% "akka-http-testkit"      % AkkaVersion       % "test"
    )
}

