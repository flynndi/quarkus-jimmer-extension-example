import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin ("jvm") version "1.9.22"
    id ("io.quarkus")
    id ("com.google.devtools.ksp") version "1.9.21-1.0.15"
    kotlin ("plugin.allopen") version "1.9.23"
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation (enforcedPlatform("${project.property("quarkusPluginId")}:${project.property("quarkusPlatformArtifactId")}:${project.property("quarkusPluginVersion")}"))
    implementation ("io.quarkus:quarkus-config-yaml")
    implementation ("io.quarkus:quarkus-jackson")
    implementation ("io.quarkus:quarkus-kotlin")
    implementation (kotlin("stdlib"))
    implementation ("org.jetbrains.kotlin:kotlin-stdlib")
    runtimeOnly ("io.quarkus:quarkus-jdbc-h2")
//    implementation ("io.quarkus:quarkus-jdbc-postgresql")
    implementation ("io.quarkus:quarkus-arc")
    implementation ("io.quarkus:quarkus-resteasy-reactive-jackson")
    implementation ("io.quarkus:quarkus-rest-client-reactive-jackson")
    implementation ("io.github.flynndi:quarkus-jimmer:999-SNAPSHOT")
    implementation ("io.github.flynndi:quarkus-jimmer-deployment:999-SNAPSHOT")
    implementation ("io.quarkus:quarkus-bootstrap-gradle-resolver")
    annotationProcessor ("org.babyfish.jimmer:jimmer-apt:0.8.118")
    ksp ("org.babyfish.jimmer:jimmer-ksp:0.8.118")
    testImplementation ("io.quarkus:quarkus-junit5")
    testImplementation ("io.rest-assured:rest-assured")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

project.afterEvaluate {
    getTasksByName("quarkusGenerateCode", true).forEach { task ->
        task.setDependsOn(task.dependsOn.filterIsInstance<Provider<Task>>().filter { it.get().name != "processResources" })
    }
    getTasksByName("quarkusGenerateCodeDev", true).forEach { task ->
        task.setDependsOn(task.dependsOn.filterIsInstance<Provider<Task>>().filter { it.get().name != "processResources" })
    }
}

kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
}


allOpen {
    annotation("jakarta.ws.rs.Path")
    annotation("jakarta.enterprise.context.ApplicationScoped")
    annotation("jakarta.persistence.Entity")
    annotation("io.quarkus.test.junit.QuarkusTest")
}


tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
    kotlinOptions.javaParameters = true
}
