plugins {
    java
    jacoco

    id("org.unbroken-dome.test-sets") version "4.0.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("com.diffplug.spotless") version "6.1.2"
    id("com.github.spotbugs") version "5.0.4"
    id("com.avast.gradle.docker-compose") version "0.14.11"
    id("com.github.ben-manes.versions") version "0.41.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

repositories { mavenCentral() }

testSets { create("integrationTest") }

dependencies {
    val flinkVersion = "1.14.2"
    compileOnly("org.apache.flink:flink-java:$flinkVersion")
    compileOnly("org.apache.flink:flink-streaming-java_2.11:$flinkVersion")
    implementation("org.apache.flink:flink-metrics-dropwizard:$flinkVersion")

    val scalaBinaryVersion = "2.12"
    runtimeOnly("org.apache.flink:flink-statebackend-rocksdb_${scalaBinaryVersion}:${flinkVersion}")

    val slf4jVersion = "1.7.7"
    implementation("org.slf4j:slf4j-log4j12:$slf4jVersion")

    val jacksonVersion = "2.13.0"
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")

    val junitVersion = "5.8.2"
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.apache.flink:flink-test-utils_2.11:$flinkVersion")
    testImplementation("org.assertj:assertj-core:3.22.0")
    testImplementation("org.mockito:mockito-junit-jupiter:4.2.0")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testRuntimeOnly("org.slf4j:slf4j-simple:1.7.32")

    val integrationTestImplementation by configurations
    integrationTestImplementation("com.mashape.unirest:unirest-java:1.4.9")
    integrationTestImplementation("org.awaitility:awaitility:4.1.1")
    integrationTestImplementation("com.github.docker-java:docker-java:3.2.12")
}

tasks {
    withType(Test::class).configureEach { useJUnitPlatform() }
    "jacocoTestReport"(JacocoReport::class) {
        reports {
            xml.required.set(true)
            html.required.set(true)
        }
    }
    "check" { dependsOn("jacocoTestReport") }
}

dockerCompose {
    isRequiredBy(tasks["integrationTest"])
    projectName = null
}

spotless {
    java { googleJavaFormat() }
    kotlinGradle { ktlint() }
}
