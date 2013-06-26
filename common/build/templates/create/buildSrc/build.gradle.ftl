<#ftl>
repositories {
    mavenCentral()
}
dependencies {
    compile 'org.freemarker:freemarker:2.3.20'
}

sourceSets {
    main {
        groovy {
            srcDir new File(rootDir, "../${meta.common}/build/buildSrc/src/main/groovy")
        }
    }
}

