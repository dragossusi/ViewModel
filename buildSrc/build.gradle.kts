plugins {
    `kotlin-dsl`
}
repositories {
    mavenCentral()
    google()
}
val androidVersion = project.property("sevens.android.gradle.version").toString()

dependencies {
    implementation("com.android.tools.build:gradle:$androidVersion")
}