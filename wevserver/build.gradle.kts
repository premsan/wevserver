plugins {
    id("java")
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
    requiresUnpack("**/asciidoctorj-*.jar")
}

dependencies {
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Required dependencies
    implementation(project(":application"))
    implementation(project(":db"))
    implementation(project(":lib"))
    implementation(project(":scheduled"))
    implementation(project(":security"))
    implementation(project(":ui"))

    // Optional dependencies
    implementation(project(":barcode"))
    implementation(project(":blog"))
    implementation(project(":broadcast"))
    implementation(project(":conversation"))
    implementation(project(":email"))
    implementation(project(":grep"))
    implementation(project(":json"))
    implementation(project(":mock"))
    implementation(project(":payment"))
    implementation(project(":reservation"))
    implementation(project(":proxyagent"))
    implementation(project(":proxyserver"))

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    runtimeOnly("com.h2database:h2")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
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