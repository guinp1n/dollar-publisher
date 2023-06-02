rootProject.name = "hivemq-dollar-publisher-extension"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven {
            name = "hivemqEnterprise"
            url = uri("s3://hivemq-enterprise-extension-gradle-plugin/release")
            credentials(AwsCredentials::class)
        }
    }

    plugins {
        id("com.hivemq.enterprise-extension") version "${extra["plugin.hivemq-enterprise-extension.version"]}"
        id("com.github.sgtsilvio.gradle.utf8") version "${extra["plugin.utf8.version"]}"
    }
}