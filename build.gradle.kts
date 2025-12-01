import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm
import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java-library")
    kotlin("jvm")

    id("org.jetbrains.dokka") version "2.1.0"
    id("org.jetbrains.dokka-javadoc") version "2.1.0"
    id("com.vanniktech.maven.publish") version "0.30.0"
}

allprojects {
    group = "io.github.nillerr"
    version = "1.0.3"

    repositories {
        mavenCentral()

        maven {
            url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots")
        }
    }

    apply(plugin = "java-library")
    apply(plugin = "kotlin")
    apply(plugin = "org.jetbrains.dokka")
    apply(plugin = "org.jetbrains.dokka-javadoc")
    apply(plugin = "com.vanniktech.maven.publish")
}

subprojects {
    dependencies {
        testImplementation(kotlin("test"))
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    tasks {
        test {
            useJUnitPlatform()
        }

        withType<KotlinCompile> {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_17)
            }
        }
    }

    val dokkaHtmlJar by tasks.registering(Jar::class) {
        dependsOn(tasks.dokkaGeneratePublicationHtml)
        from(tasks.dokkaGeneratePublicationHtml.flatMap { it.outputDirectory })
        archiveClassifier.set("html-docs")
    }

    mavenPublishing {
        publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
        signAllPublications()

        pom {
            name.set("Test HTTP Server")
            description.set("Provides an HTTP server for testing HTTP client implementations.")
            url.set("https://github.com/Nillerr/test-http-server-kotlin")
            inceptionYear.set("2024")

            licenses {
                license {
                    name.set("MIT")
                    url.set("https://github.com/Nillerr/test-http-server-kotlin/blob/main/LICENSE")
                }
            }

            developers {
                developer {
                    id.set("Nillerr")
                    name.set("Nicklas Jensen")
                    url.set("https://github.com/Nillerr")
                }
            }

            scm {
                url.set("https://github.com/Nillerr/test-http-server-kotlin")
                connection.set("scm:git:git://github.com/Nillerr/test-http-server-kotlin.git")
                developerConnection.set("scm:git:ssh://git@github.com/Nillerr/test-http-server-kotlin.git")
            }
        }

        // Configure Dokka for javadoc
        configure(
            KotlinJvm(
                javadocJar = JavadocJar.Dokka("dokkaGeneratePublicationJavadoc")
            )
        )
    }

    // Add the HTML docs jar as an additional artifact
    afterEvaluate {
        publishing {
            publications {
                named<MavenPublication>("maven") {
                    artifact(dokkaHtmlJar)
                }
            }
        }
    }
}
