dependencies {
    // AutoCleanup
    testImplementation("io.github.nillerr:autocleanup-junit5:1.0.0")

    // Ktor Client
    testImplementation("io.ktor:ktor-client-core:3.3.2")
    testImplementation("io.ktor:ktor-client-cio:3.3.2")

    // Ktor Server
    implementation("io.ktor:ktor-server-core:3.3.2")
    implementation("io.ktor:ktor-server-netty:3.3.2")

    // MockK
    testImplementation("io.mockk:mockk:1.13.3")
}
