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

dependencies {
    implementation(project(":application"))
    implementation(project(":db"))
    implementation(project(":security"))
    implementation(project(":ui"))
    implementation(project(":api"))


    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-mustache")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-websocket")

    testImplementation(platform(libs.org.junit.junit.bom))
    testImplementation("org.junit.jupiter:junit-jupiter")
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