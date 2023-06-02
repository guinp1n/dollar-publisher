plugins {
    id("com.hivemq.enterprise-extension")
    id("com.github.sgtsilvio.gradle.utf8")
}

group = "com.hivemq.extensions"
description = "HiveMQ 4 $-publisher Extension"

hivemqExtension {
    name.set("Dollar Publisher Extension")
    author.set("Dasha")
    priority.set(1000)
    startPriority.set(1000)
    mainClass.set("$group.dollarpublisher.DollarPublisherMain")
    sdkVersion.set("$version")
}

dependencies {
    implementation("org.apache.commons:commons-lang3:${property("commons-lang3.version")}")
}

/* ******************** test ******************** */

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:${property("junit-jupiter.version")}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.mockito:mockito-core:${property("mockito.version")}")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

/* ******************** integration test ******************** */

dependencies {
    integrationTestImplementation("com.hivemq:hivemq-mqtt-client:${property("hivemq-mqtt-client.version")}")
    integrationTestImplementation("org.testcontainers:junit-jupiter:${property("testcontainers.version")}")
    integrationTestImplementation("org.testcontainers:hivemq:${property("testcontainers.version")}")
    integrationTestRuntimeOnly("ch.qos.logback:logback-classic:${property("logback.version")}")
}

/* ******************** debugging ******************** */

tasks.prepareHivemqHome {
    hivemqHomeDirectory.set(file("~/hivemq/hivemq-4.15.0"))
}

tasks.runHivemqWithExtension {
    debugOptions {
        enabled.set(false)
    }
}