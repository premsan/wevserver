plugins {
    `java-library`
    alias(libs.plugins.org.springframework.boot)
    alias(libs.plugins.io.spring.dependency.management)
    alias(libs.plugins.com.diffplug.spotless)
}

group = "com.wevserver"

val artifactVersion: String by rootProject.extra
version = artifactVersion

repositories {
    mavenCentral()
}

tasks.bootJar {
    enabled = false
}

dependencies {
    implementation(project(":application"))
    implementation(project(":db"))
    implementation(project(":scheduled"))
    implementation(project(":ui"))
    implementation(project(":api"))
    implementation(project(":lib"))

    api("org.springframework.boot:spring-boot-starter-security")
    api("org.springframework.boot:spring-boot-starter-oauth2-client")
    api("org.springframework.session:spring-session-core")
    api("org.springframework.session:spring-session-jdbc")

    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.liquibase:liquibase-core")
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")

    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")

    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

spotless {
    format("html") {
        val htmlTabWidth: Int by rootProject.extra
        prettier().config(mapOf("tabWidth" to htmlTabWidth, "parser" to "html"))

        target("src/**/templates/**/*.html", "src/**/templates/**/*.mustache")
    }
    java {
        val googleJavaFormatVersion: String by rootProject.extra

        googleJavaFormat(googleJavaFormatVersion).aosp().reflowLongStrings().skipJavadocFormatting()
        formatAnnotations()
    }
}