
<#-- This build script is a bootstrapper for the "real" android build script that
is contained in templates/base. It includes only what's necessary for Android Studio
to recognize this as an Android project and start the template engine. -->

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath 'com.android.tools.build:gradle:0.4.2'
    }
}

apply plugin: 'android'


android {
     <#-- Note that target SDK is hardcoded in this template. We expect all samples
          to always use the most current SDK as their target. -->
    compileSdkVersion 17
    buildToolsVersion "17.0.0"

    defaultConfig {
        minSdkVersion ${sample.minSdk}
        targetSdkVersion 17
    }
}

task preflight (dependsOn: parent.preflight) {}

// Inject a preflight task into each variant so we have a place to hook tasks
// that need to run before any of the android build tasks.
<#noparse>
android.applicationVariants.each { variant ->
    tasks.getByPath("prepare${variant.name.capitalize()}Dependencies").dependsOn preflight
}
</#noparse>



