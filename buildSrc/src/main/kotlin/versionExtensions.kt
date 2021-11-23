import org.gradle.api.Project

val Project.kotlinVersion: String
    get() {
        return property("sevens.kotlin.version")!!.toString()
    }
val Project.coroutinesVersion: String
    get() {
        return property("sevens.kotlin.coroutines.version")!!.toString()
    }

val Project.androidVersion: String
    get() {
        return property("sevens.android.gradle.version")!!.toString()
    }