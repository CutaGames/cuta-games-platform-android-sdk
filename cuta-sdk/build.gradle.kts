plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
   // alias(libs.plugins.kotlin.serialization)
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
    buildFeatures {
        aidl = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    publishing {
        // 配置发布变体（Release 或 Debug）
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    //implementation(libs.androidx.core.ktx)
    //implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                groupId = "com.github.CutaGames"
                artifactId = "cuta-games-platform-android-sdk"
                version = "0.0.1"

                //artifact("$buildDir/outputs/aar/${project.name}-release.aar")
                artifact(tasks.named("bundleReleaseAar"))
                /*afterEvaluate {
                    from(components["release"])
                }*/

                // 自动生成 POM 依赖（可选）
                pom {
                    name.set("cuta platform sdk")
                    description.set("A library for cuta platform")
                    url.set("https://github.com/CutaGames/cuta-games-platform-android-sdk")
                    licenses {
                        license {
                            name.set("MIT")
                            url.set("https://opensource.org/licenses/MIT")
                        }
                    }
                    developers {
                        developer {
                            id.set("cuta developer")
                            name.set("swepthong")
                        }
                    }
                    scm {
                        connection.set("scm:git:github.com/CutaGames/cuta-games-platform-android-sdk.git")
                        url.set("https://github.com/CutaGames/cuta-games-platform-android-sdk")
                    }
                }
            }
        }
    }
}