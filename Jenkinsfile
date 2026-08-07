/*
 * Jenkins Declarative Pipeline — UIAutomationJenkinsJob
 * -----------------------------------------------------------------
 * Combines the existing project pipeline (session cookie injection,
 * Chrome/Firefox TestNG run, Extent/Spark + Cucumber reporting) with:
 *   1. Capturing % of passing test cases from TestNG's native report
 *   2. Comparing pass % against a configurable threshold
 *   3. Reverting the last commit on the target branch if threshold is not met
 *   4. Passing pass %/counts/revert status to the build tool via a
 *      properties file (and optionally a downstream job)
 *
 * PREREQUISITE — configured once under Manage Jenkins -> Tools:
 *   - Maven installation named exactly:  Maven3
 *       ("Install automatically" checked, installing 3.9.16 from Apache)
 *   - Git installation named exactly:    Default
 *       Path to Git executable -> full path to git.exe (not a bare
 *       filename — that would still depend on the agent's system PATH)
 * PREREQUISITE — GitHub PAT credential named "github-credentials"
 *   (Username with password: username = repo owner, password = PAT)
 *   used both for checkout and for the auto-revert push.
 * -----------------------------------------------------------------
 */

pipeline {
    agent any

    tools {
        // Must match the Maven installation name in Manage Jenkins -> Tools
        maven 'Maven3'
    }

    parameters {
        string(name: 'PASS_THRESHOLD', defaultValue: '80', description: 'Minimum pass % required to avoid a revert')
        string(name: 'REVERT_BRANCH',  defaultValue: 'main', description: 'Branch to revert if threshold is not met')
        booleanParam(name: 'AUTO_PUSH_REVERT', defaultValue: false, description: 'If true, pushes the revert commit automatically using the GitHub PAT credential. If false, revert is created locally only (safer default).')
    }

    environment {
        // Maven + TestNG default (Surefire delegates to TestNG and writes
        // the native TestNG report here).
        TESTNG_RESULTS_FILE = 'target/surefire-reports/testng-results.xml'
        // Name of the Git installation configured under Manage Jenkins -> Tools
        GIT_TOOL_NAME = 'Default'
        // GitHub PAT credential ID (Manage Jenkins -> Credentials)
        GITHUB_CRED_ID = 'github-credentials'
        // Repo path only (no protocol/credentials) — used to build an authenticated push URL
        GITHUB_REPO_PATH = 'github.com/ahladinihamsrala-del/UIAutomationJenkinsJob.git'
    }

    stages {

        stage('Resolve Git Tool') {
            steps {
                script {
                    // Declarative `tools {}` only supports maven/jdk/gradle out of the box,
                    // so Git's path is resolved manually here and reused later. This is the
                    // exact path configured in Jenkins Tools, independent of agent PATH.
                    env.GIT_EXE = tool(name: env.GIT_TOOL_NAME, type: 'git')
                    echo "Using Git executable: ${env.GIT_EXE}"
                    bat 'where mvn'
                }
            }
        }

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Inject Session Cookies') {
            steps {
                withCredentials([file(credentialsId: 'ixigo-session-cookies', variable: 'COOKIE_FILE')]) {
                    bat 'copy "%COOKIE_FILE%" "%WORKSPACE%\\session-cookies.ser"'
                }
            }
        }

        stage('Run Tests - Chrome and Firefox') {
            steps {
                bat 'mvn -B clean test -Dheadless=true'
            }
            post {
                always {
                    // Publishes TestNG results to the Jenkins UI (trend graphs, history).
                    // Requires the "TestNG Results" plugin. Wrapped in try/catch so a
                    // missing plugin doesn't fail the whole pipeline — pass % calculation
                    // below reads the XML directly and doesn't depend on this step.
                    script {
                        if (fileExists(env.TESTNG_RESULTS_FILE)) {
                            try {
                                step([$class: 'TestNGPublisher', reportFilenamePattern: env.TESTNG_RESULTS_FILE])
                            } catch (Exception e) {
                                echo "TestNG Results plugin not available or failed to publish (non-fatal): ${e.message}"
                            }
                        } else {
                            echo "No TestNG results file found at ${env.TESTNG_RESULTS_FILE} to publish."
                        }
                    }
                }
            }
        }

        stage('Calculate Pass %') {
            steps {
                script {
                    if (!fileExists(env.TESTNG_RESULTS_FILE)) {
                        error("TestNG results file not found at ${env.TESTNG_RESULTS_FILE} — cannot calculate pass percentage")
                    }

                    def xmlContent = readFile(env.TESTNG_RESULTS_FILE)
                    def stats = parseTestNGResults(xmlContent)

                    int total   = stats.total
                    int passed  = stats.passed
                    int failed  = stats.failed
                    int skipped = stats.skipped

                    double passPercent = total > 0 ? (passed / (double) total) * 100 : 0
                    passPercent = Math.round(passPercent * 100) / 100.0d

                    env.PASS_PERCENT  = passPercent.toString()
                    env.TOTAL_TESTS   = total.toString()
                    env.PASSED_TESTS  = passed.toString()
                    env.FAILED_TESTS  = failed.toString()
                    env.SKIPPED_TESTS = skipped.toString()

                    echo "TestNG Results: ${passed}/${total} passed, ${failed} failed, ${skipped} skipped -> ${passPercent}%"
                }
            }
        }

        stage('Evaluate Threshold & Revert if Needed') {
            steps {
                script {
                    double threshold   = params.PASS_THRESHOLD as Double
                    double passPercent = env.PASS_PERCENT as Double

                    if (passPercent < threshold) {
                        echo "Pass % (${passPercent}) is BELOW threshold (${threshold}). Reverting last commit on ${params.REVERT_BRANCH}."

                        bat """
                            "${env.GIT_EXE}" config user.email "jenkins-ci@yourcompany.com"
                            "${env.GIT_EXE}" config user.name "Jenkins CI"
                            "${env.GIT_EXE}" checkout ${params.REVERT_BRANCH}
                            "${env.GIT_EXE}" revert --no-edit HEAD
                        """

                        if (params.AUTO_PUSH_REVERT) {
                            // Injects the GitHub PAT credential so the raw `git push` below can
                            // authenticate — checkout scm handles auth automatically via the
                            // Jenkins Git plugin, but a plain `git push` in a bat step does not.
                            withCredentials([usernamePassword(
                                credentialsId: env.GITHUB_CRED_ID,
                                usernameVariable: 'GIT_USER',
                                passwordVariable: 'GIT_TOKEN'
                            )]) {
                                bat """
                                    "${env.GIT_EXE}" push https://%GIT_USER%:%GIT_TOKEN%@${env.GITHUB_REPO_PATH} ${params.REVERT_BRANCH}
                                """
                            }
                            echo "Revert commit pushed to ${params.REVERT_BRANCH}."
                        } else {
                            echo "AUTO_PUSH_REVERT is false: revert commit created locally only. Push manually or open a PR — this avoids surprise force-changes on a shared branch."
                        }

                        env.BUILD_REVERTED = 'true'
                        currentBuild.result = 'UNSTABLE'
                    } else {
                        echo "Pass % (${passPercent}) meets threshold (${threshold}). No revert needed."
                        env.BUILD_REVERTED = 'false'
                    }
                }
            }
        }

        stage('Pass Info to Build Tool') {
            steps {
                script {
                    // Write a properties file the build tool / downstream steps can consume
                    writeFile file: 'test-summary.properties', text: """PASS_PERCENT=${env.PASS_PERCENT}
TOTAL_TESTS=${env.TOTAL_TESTS}
PASSED_TESTS=${env.PASSED_TESTS}
FAILED_TESTS=${env.FAILED_TESTS}
SKIPPED_TESTS=${env.SKIPPED_TESTS}
BUILD_REVERTED=${env.BUILD_REVERTED}
"""
                    // OPTION A: feed values into Maven as -D system properties, e.g.:
                    // bat "mvn -B deploy -DtestPassPercent=${env.PASS_PERCENT} -DbuildReverted=${env.BUILD_REVERTED}"

                    // OPTION B: pass to a downstream Jenkins job
                    // build job: 'downstream-build-job',
                    //     parameters: [
                    //         string(name: 'TEST_PASS_PERCENT', value: env.PASS_PERCENT),
                    //         booleanParam(name: 'BUILD_WAS_REVERTED', value: env.BUILD_REVERTED.toBoolean())
                    //     ],
                    //     wait: false
                }
            }
        }
    }

    post {
        always {
            junit '**/target/surefire-reports/*.xml'
            archiveArtifacts artifacts: 'test-output/SparkReport/**, target/cucumber-report.html, test-summary.properties', allowEmptyArchive: true

            publishHTML(target: [
                allowMissing: true,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'test-output/SparkReport',
                reportFiles: 'Spark.html',
                reportName: 'Extent Report'
            ])

            echo "Final result -> Pass %: ${env.PASS_PERCENT}, Reverted: ${env.BUILD_REVERTED}"
        }
    }
}

/*
 * NonCPS helper: parses the <testng-results total="" passed="" failed="" skipped="">
 * root attributes of TestNG's native XML report. Must be @NonCPS because XmlSlurper
 * is not serializable and can't run inside the pipeline's CPS-transformed steps.
 */
@NonCPS
def parseTestNGResults(String xmlContent) {
    def parsed = new XmlSlurper().parseText(xmlContent)
    return [
        total  : parsed.@total.text() as Integer,
        passed : parsed.@passed.text() as Integer,
        failed : parsed.@failed.text() as Integer,
        skipped: parsed.@skipped.text() as Integer
    ]
}
