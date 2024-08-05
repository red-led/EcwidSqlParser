import org.gradle.kotlin.dsl.support.isGradleKotlinDslJar

plugins {
    id("java")
}

group = "ru.redled"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "com.testAssignment.sqlParser.Main"
    }
}
