import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML

fun properties(key: String) = providers.gradleProperty(key)
fun environment(key: String) = providers.environmentVariable(key)

buildscript {
    dependencies {
        classpath("com.guardsquare:proguard-gradle:7.4.2")
    }
}

plugins {
    id("java") // Java support
    // Kotlin support
    id("org.jetbrains.kotlin.jvm") version "1.8.10"
    // Gradle IntelliJ Plugin
    id("org.jetbrains.intellij") version "1.13.2"
    // Gradle Changelog Plugin
    id("org.jetbrains.changelog") version "2.0.0"
    // Gradle Qodana Plugin
    id("org.jetbrains.qodana") version "0.1.13"
    // Gradle Kover Plugin
    id("org.jetbrains.kotlinx.kover") version "0.6.1"
}

group = properties("pluginGroup").get()
version = properties("pluginVersion").get()

// Configure project's dependencies
repositories {
    mavenCentral()
}


configurations {
    register("myImplementation") {
        extendsFrom(implementation.get())
        isCanBeResolved = true
    }
}

// Dependencies are managed with Gradle version catalog - read more: https://docs.gradle.org/current/userguide/platforms.html#sub:version-catalog
dependencies {
//    implementation(libs.annotations)
    implementation("cn.hutool:hutool-all:5.8.12")
    implementation("org.jsoup:jsoup:1.18.1")
}

// Set the JVM language level used to build the project. Use Java 11 for 2020.3+, and Java 17 for 2022.2+.
kotlin {
    jvmToolchain(17)
}

java {
//    sourceCompatibility = JavaVersion.VERSION_17
//    targetCompatibility = JavaVersion.VERSION_17
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
// Configure Gradle IntelliJ Plugin - read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    pluginName.set(properties("pluginName"))
    version.set(properties("platformVersion"))
    type.set(properties("platformType"))

    // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file.
    plugins.set(properties("platformPlugins").map { it.split(',').map(String::trim).filter(String::isNotEmpty) })
}

// Configure Gradle Changelog Plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
changelog {
    groups.empty()
    repositoryUrl.set(properties("pluginRepositoryUrl"))
}

// Configure Gradle Qodana Plugin - read more: https://github.com/JetBrains/gradle-qodana-plugin
qodana {
    cachePath.set(provider { file(".qodana").canonicalPath })
    reportPath.set(provider { file("build/reports/inspections").canonicalPath })
    saveReport.set(true)
    showReport.set(environment("QODANA_SHOW_REPORT").map { it.toBoolean() }.getOrElse(false))
}

// Configure Gradle Kover Plugin - read more: https://github.com/Kotlin/kotlinx-kover#configuration

tasks {
    //去除警告
    buildSearchableOptions {
        isEnabled = false
    }
    instrumentedJar {
        isEnabled = true
        dependsOn(jar)
    }


    

    wrapper {
        gradleVersion = properties("gradleVersion").get()
    }

    runIde {
        systemProperty("idea.is.internal", true)

        // Enable hotswap, requires JBR 17+ or JBR 11 with DCEVM, and run in debug mode.
//        jvmArgs = listOf("-XX:+AllowEnhancedClassRedefinition")

        // Path to IDE distribution that will be used to run the IDE with the plugin.
        // ideDir.set(File("path to IDE-dependency"))
    }

    patchPluginXml {
        version.set(properties("pluginVersion"))
        sinceBuild.set(properties("pluginSinceBuild"))
        untilBuild.set(properties("pluginUntilBuild"))

        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
        pluginDescription.set(providers.fileContents(layout.projectDirectory.file("README.md")).asText.map {
            val start = "<!-- Plugin description -->"
            val end = "<!-- Plugin description end -->"

            with(it.lines()) {
                if (!containsAll(listOf(start, end))) {
                    throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
                }
                subList(indexOf(start) + 1, indexOf(end)).joinToString("\n").let(::markdownToHTML)
            }
        })

        val changelog = project.changelog // local variable for configuration cache compatibility
        // Get the latest available change notes from the changelog file
        changeNotes.set(properties("pluginVersion").map { pluginVersion ->
            with(changelog) {
                renderItem(
                    (getOrNull(pluginVersion) ?: getUnreleased())
                        .withHeader(false)
                        .withEmptySections(false),
                    Changelog.OutputType.HTML,
                )
            }
        })
    }

    // Configure UI tests plugin
    // Read more: https://github.com/JetBrains/intellij-ui-test-robot
//    runIdeForUiTests {
//        systemProperty("robot-server.port", "8082")
//        systemProperty("ide.mac.message.dialogs.as.sheets", "false")
//        systemProperty("jb.privacy.policy.text", "<!--999.999-->")
//        systemProperty("jb.consents.confirmation.enabled", "false")
//    }

    signPlugin {
        certificateChain.set(environment("CERTIFICATE_CHAIN"))
        privateKey.set(environment("PRIVATE_KEY"))
        password.set(environment("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
//        dependsOn("patchChangelog")
        token.set(environment("PUBLISH_TOKEN"))
        // The pluginVersion is based on the SemVer (https://semver.org) and supports pre-release labels, like 2.1.7-alpha.3
        // Specify pre-release label to publish the plugin in a custom Release Channel automatically. Read more:
        // https://plugins.jetbrains.com/docs/intellij/deployment.html#specifying-a-release-channel
        channels.set(properties("pluginVersion").map {
            listOf(
                it.split('-').getOrElse(1) { "default" }.split('.').first()
            )
        })
    }
}


fun getIDEAPath(): String {
//    return "/Users/wilson/.gradle/caches/modules-2/files-2.1/com.jetbrains.intellij.idea/ideaIC/2021.3/75777e10a0e2880bc02945066dda2480a696c3d9/ideaIC-2021.3"
    return "/Users/kings/.gradle/caches/modules-2/files-2.1/com.jetbrains.intellij.idea/ideaIC/2021.3/75777e10a0e2880bc02945066dda2480a696c3d9/ideaIC-2021.3"
}
