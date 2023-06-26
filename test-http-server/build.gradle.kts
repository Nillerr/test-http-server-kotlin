dependencies {
    // AutoCleanup
    testImplementation("io.github.nillerr:autocleanup-junit5:1.0.0-SNAPSHOT") {
        isChanging = true
    }

    // Ktor Client
    testImplementation("io.ktor:ktor-client-core:2.3.1")
    testImplementation("io.ktor:ktor-client-cio:2.3.1")

    // Ktor Server
    implementation("io.ktor:ktor-server-core:2.3.1")
    implementation("io.ktor:ktor-server-netty:2.3.1")

    // MockK
    testImplementation("io.mockk:mockk:1.13.3")
}
