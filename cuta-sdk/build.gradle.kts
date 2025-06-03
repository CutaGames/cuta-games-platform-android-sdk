plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.maven.publish)
}

android {
    namespace = "com.cuta.games.platform.sdk"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        version = "0.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.web3j.core)
    implementation(libs.tink.android)
    implementation(libs.material)
    implementation(libs.androidx.security.crypto.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

afterEvaluate {
    val isAndroidLibraryProject = plugins.hasPlugin("com.android.library")
    if (isAndroidLibraryProject) {
        tasks.register<Copy>("copyDeps") {
            from(configurations["includeJars"].filter { it.name.endsWith(".jar") })
            into("./build/intermediates/packaged-classes/release/libs")
        }
        tasks.named("mergeReleaseJniLibFolders") {
            dependsOn("copyDeps")
        }

        tasks.register<Copy>("copyDebugDeps") {
            from(configurations["includeJars"].filter { it.name.endsWith(".jar") })
            into("./build/intermediates/packaged-classes/debug/libs")
        }
        tasks.named("mergeDebugJniLibFolders") {
            dependsOn("copyDebugDeps")
        }
    }

    publishing {
        publications {
            create<MavenPublication>("library") {
                groupId = "com.cuta.games.platform"
                artifactId = "cutasdk"
                version = "0.0.1"
                artifact(tasks.named("bundleReleaseAar"))

                pom.withXml {
                    val dependenciesNode = asNode().appendNode("dependencies")
                    configurations["includeJars"].allDependencies.forEach { dep ->
                        val dependencyNode = dependenciesNode.appendNode("dependency")
                        dependencyNode.appendNode("groupId", dep.group)
                        dependencyNode.appendNode("artifactId", dep.name)
                        dependencyNode.appendNode("version", dep.version)
                        dependencyNode.appendNode("scope", "runtime")
                    }
                }
            }
        }
        repositories {
            mavenLocal()
        }
    }
}