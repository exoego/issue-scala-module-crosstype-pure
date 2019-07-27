import sbtcrossproject.CrossPlugin.autoImport.{ crossProject, CrossType }
import ScalaModulePlugin._

lazy val root = project.in(file("."))
  .aggregate(fooJS, fooJVM)
  .settings(disablePublishing)

lazy val foo  = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Pure)
  .in(file("."))
  .settings(scalaModuleSettings: _*)
  .jvmSettings(scalaModuleSettingsJVM)
  .settings(
    crossScalaVersions in ThisBuild := Seq("2.12.8"),
    testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-v", "-s", "-a"),
    parallelExecution in Test := false,  // why?
    libraryDependencies ++= Seq(
      "junit"            % "junit"           % "4.12"   % Test,
      "com.novocode"     % "junit-interface" % "0.11"   % Test,
      "org.openjdk.jol"  % "jol-core"        % "0.9"    % Test
    )
  )
  .jsConfigure(_.enablePlugins(ScalaJSJUnitPlugin))
  .jsSettings(
    fork in Test := false
  )
lazy val fooJVM = foo.jvm
lazy val fooJS = foo.js
