package com.example.android.samples.build

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.GradleBuild
/**
 * Created by ilewis on 7/3/13.
 */
class SampleGenPlugin implements Plugin {

    /**
     * Creates a new sample generator task based on the supplied sources.
     *
     * @param name Name of the new task
     * @param sources Source tree that this task should process
     */
    void createTask(Project project, String name, SampleGenProperties props, def sources, def destination) {
        project.task ([type:ApplyTemplates], name,  {
            sources.each { tree ->
                source += tree
            }
            outputDir = destination
            include = props.templatesInclude()
            filenameTransform = {s -> props.getOutputForInput(s)}
            parameters = props.templateParams()
        })
    }

    @Override
    void apply(project) {
        project.extensions.create("samplegen", SampleGenProperties)
        project.samplegen.project = project
        project.afterEvaluate({
            SampleGenProperties samplegen = project.samplegen
            project.task('create') {
                if (project.gradle.startParameter.taskNames.contains('create')) {
                    samplegen.getCreationProperties()
                }

            }

            project.task('refresh') {
                samplegen.getRefreshProperties()
            }

            createTask(project, 'processTemplates', samplegen, samplegen.templates(), samplegen.targetProjectDir)
            createTask(project, 'processCommon', samplegen, samplegen.common(), samplegen.targetCommonSourceDir())


            project.task([type: GradleBuild], 'bootstrap', {
                buildFile = "${samplegen.targetProjectDir}/build.gradle"
                dir = samplegen.targetProjectDir
                tasks = ["refresh"]
            })
            project.bootstrap.dependsOn(project.processTemplates)
            project.bootstrap.dependsOn(project.processCommon)
            project.create.dependsOn(project.bootstrap)

            project.refresh.dependsOn(project.processTemplates)
            project.refresh.dependsOn(project.processCommon)

            // People get nervous when they see a task with no actions, so...
            project.create << {println "Project creation finished."}
            project.refresh << {println "Project refresh finished."}
        })
    }


}