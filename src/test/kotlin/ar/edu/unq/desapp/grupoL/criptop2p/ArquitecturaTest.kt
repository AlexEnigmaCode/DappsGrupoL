package ar.edu.unq.desapp.grupoL.criptop2p

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.junit.ArchUnitRunner
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
class ArquitecturaTest {

   /*
    @Test
    fun Services_should_only_be_accessed_by_Controllers() {
        val importedClasses = ClassFileImporter().importPackages("ar.edu.unq.desapp.grupoL.criptop2p")
        val myRule: ArchRule = classes()
            .that().resideInAPackage("ar.edu.unq.desapp.grupoL.criptop2p.service")
            .should().onlyBeAccessed().byAnyPackage("ar.edu.unq.desapp.grupoL.criptop2p.webservice", "ar.edu.unq.desapp.grupoL.criptop2p.service")
        myRule.check(importedClasses)
    }
*/


    @Test
    fun Services_should_only_be_accessed_by_Controllers() {
        val importedClasses = ClassFileImporter().importPackages("ar.edu.unq.desapp.grupoL.criptop2p")
        val myRule: ArchRule = classes()
            .that().resideInAPackage("...repository ...")
            .should().onlyBeAccessed().byAnyPackage("...service...")

            .andShould().resideInAPackage("...service ...")
            .andShould().onlyBeAccessed().byAnyPackage("...webservice...", "...service...")
        myRule.check(importedClasses)
    }

}