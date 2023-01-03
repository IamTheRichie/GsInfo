pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
  }
}
plugins {
  id("org.ajoberstar.reckon.settings") version "0.17.0-beta.4"
}
rootProject.name = "GsInfo"

var reckon = (extensions.getByName("reckon") as org.ajoberstar.reckon.gradle.ReckonExtension)
reckon.snapshots()
reckon.setScopeCalc(reckon.calcScopeFromProp().or(reckon.calcScopeFromCommitMessages()))
reckon.setStageCalc(reckon.calcStageFromProp())
reckon.setDefaultInferredScope("patch")
reckon.setTagWriter { version: org.ajoberstar.reckon.core.Version? -> "v$version" }