import org.jetbrains.kotlin.fir.types.CompilerConeAttributes.EnhancedNullability.key
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.3"
	id("io.spring.dependency-management") version "1.0.13.RELEASE"
	id ("java")
	id ("idea")
	id ("eclipse")
	id ("org.sonarqube") version "3.3"
	war
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	kotlin("plugin.jpa") version "1.6.21"

}




sonarqube {
	properties {
		property ("sonar.projectKey", "gl")
		property ("sonar.organization", "dapps")
		property ("sonar.host.url", "https://sonarcloud.io")
			}
}
/*
compileJava.options.encoding = 'utf-8'
compileTestJava.options.encoding = 'utf-8'
*/

group = "ar.edu.unq.desapp.grupoL"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17
//java.sourceCompatibility = JavaVersion.VERSION_1_8

/*
configurations {
	developmentOnly
	runtimeClasspath {
		extendsFrom developmentOnly
	}
}
*/
repositories {
	mavenCentral()
	mavenLocal()

}

dependencies {

	//implementation ("io.springfox:springfox-swagger2:2.9.2")
	implementation ("io.springfox:springfox-boot-starter:3.0.0")
	implementation ("io.springfox:springfox-swagger-ui:3.0.0")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	//implementation ("org.postgresql:postgresql:42.2.12")

	implementation("mysql:mysql-connector-java:8.0.30")
	//runtimeOnly("com.mysql.cj.jdbc.Driver")
	//runtimeOnly("org.postgresql")
	runtimeOnly("com.h2database:h2")
	providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
}



tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
		//jvmTarget = "1.8"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
