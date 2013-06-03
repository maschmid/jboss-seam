package org.jboss.seam.example.seamspace.test;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

import java.io.File;
import org.jboss.shrinkwrap.api.asset.StringAsset;

public class Deployments {
   public static WebArchive seamSpaceDeployment() {
      File[] libs = Maven.resolver().loadPomFromFile("pom.xml")
                .importCompileAndRuntimeDependencies()
                // force resolve jboss-seam, because it is provided-scoped in the pom, but we need it bundled in the WAR
                .resolve("org.jboss.seam:jboss-seam")
                .withTransitivity().asFile();

      WebArchive war = ShrinkWrap.create(WebArchive.class, "seam-seamspace.war")
                .addPackages(true, "org.jboss.seam.example.seamspace")

                // already in EJB module
                .addAsResource("import.sql", "import.sql")
                .addAsResource("seam.properties", "seam.properties")
                .addAsWebInfResource("META-INF/ejb-jar.xml", "ejb-jar.xml")
                .addAsResource("META-INF/orm.xml", "META-INF/orm.xml")
                .addAsResource("META-INF/security-rules.drl", "META-INF/security-rules.drl")

                // test-specific persistence.xml
                .addAsResource("persistence.xml", "META-INF/persistence.xml")

                // manually copied from Web module
                .addAsWebInfResource("pages.xml", "pages.xml")

                // manually copied from Web module, modified
                .addAsWebInfResource("web.xml", "web.xml") // only contains MockSeamListener definition
                .addAsWebInfResource("components.xml", "components.xml") // corrected ejb component jndi-name references from java:app/jboss-seam to java:app/seam-seampay

                // manually copied from EAR module
                .addAsWebInfResource("jboss-deployment-structure.xml", "jboss-deployment-structure.xml")
                .addAsLibraries(libs);

      return war;
   }
}
