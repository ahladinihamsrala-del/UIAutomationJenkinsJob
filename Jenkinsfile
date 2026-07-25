pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK11'
    }

    triggers {
        // Polls the repo every 5 minutes for new commits (e.g. a merge to main).
        // Only actually starts a build if new commits are found since the last poll.
        pollSCM('H/5 * * * *')
    }

    environment {
        // Read by EmailUtility.java via System.getenv(...)
        MAIL_FROM     = credentials('automation-mail-from')
        MAIL_PASSWORD = credentials('automation-mail-password')
        MAIL_TO       = 'ahthati@deloitte.com'
        SMTP_HOST     = 'smtp.gmail.com'
        SMTP_PORT     = '587'

        // API key for the API automation suite (ReqRes) - stored as a Jenkins
        // "Secret text" credential (create under Manage Jenkins > Credentials).
        // Never printed to console log; credentials() masks it automatically.
        // Variable name must match exactly what the RestAssured code reads.
        REQRES_API_KEY = credentials('REQRES_API_KEY')

        // Selenium Grid hub URL. Your DriverFactory.java reads this via:
        //   System.getProperty("grid.url", "http://192.168.4.190:4444/")
        // That's a JVM system property, not an env var - passed explicitly as
        // -Dgrid.url=... on the mvn command line in the stages below.
        SELENIUM_GRID_URL = 'http://192.168.4.190:4444/'
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/ahladinihamsrala-del/WebAPIAutomation.git',
                    credentialsId: 'github-credentials'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn -B clean compile'
            }
        }

        stage('Verify Selenium Grid is Up') {
            steps {
                script {
                    def base = env.SELENIUM_GRID_URL.endsWith('/') ? env.SELENIUM_GRID_URL : env.SELENIUM_GRID_URL + '/'
                    def statusUrl = "${base}status"
                    def result = bat(
                        script: "curl -sf \"${statusUrl}\" >nul",
                        returnStatus: true
                    )
                    if (result != 0) {
                        error "Selenium Grid at ${env.SELENIUM_GRID_URL} is not reachable (curl exit code ${result}). Check the Grid hub/node status before retrying."
                    }
                    echo "Selenium Grid responded successfully at ${env.SELENIUM_GRID_URL}"
                }
            }
        }

        stage('Execute Full Suite') {
            steps {
                // catchError lets the pipeline continue past test failures (so email/rerun
                // stages still run) while still marking this stage's result as FAILURE for
                // visibility - equivalent to the old `|| true` shell trick, but portable.
                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                    bat "mvn -B test -Dgrid.url=${env.SELENIUM_GRID_URL}"
                }
            }
        }

        stage('Email: Full Run Report') {
            steps {
                // Runs as a SEPARATE mvn/JVM invocation, after the test JVM (and
                // ExtentCucumberAdapter's shutdown-hook flush) has fully exited -
                // guarantees the attached Spark.html is complete, not half-written.
                bat 'mvn -q exec:java -Dexec.mainClass="utils.ReportEmailTrigger" -Dexec.classpathScope=test -Dexec.args="test-output/SparkReport/Spark.html test-output/testng-results.xml [Automation Report - Full Run]"'
            }
        }

        stage('Rerun Failed Scenarios') {
            when {
                anyOf {
                    expression { fileExists('target/rerun-api.txt') && readFile('target/rerun-api.txt').trim() != '' }
                    expression { fileExists('target/rerun-ui.txt') && readFile('target/rerun-ui.txt').trim() != '' }
                }
            }
            steps {
                echo 'Failures found - re-running only failed scenarios via rerun-testng.xml'
                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                    bat "mvn -B test -DsuiteXmlFile=src/test/resources/rerun-testng.xml -Dgrid.url=${env.SELENIUM_GRID_URL}"
                }
            }
        }

        stage('Email: Rerun Report') {
            when {
                anyOf {
                    expression { fileExists('target/rerun-api.txt') && readFile('target/rerun-api.txt').trim() != '' }
                    expression { fileExists('target/rerun-ui.txt') && readFile('target/rerun-ui.txt').trim() != '' }
                }
            }
            steps {
                // NOTE: ExtentCucumberAdapter starts a fresh Spark report per JVM run,
                // so this rerun's report overwrote the full-run's Spark.html at the same
                // path. This email covers the rerun-only results (see README).
                bat 'mvn -q exec:java -Dexec.mainClass="utils.ReportEmailTrigger" -Dexec.classpathScope=test -Dexec.args="test-output/SparkReport/Spark.html test-output/testng-results.xml [Automation Report - Rerun of Failures]"'
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'test-output/**, target/rerun-*.txt', allowEmptyArchive: true
            publishHTML(target: [
                allowMissing: true,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'test-output/SparkReport',
                reportFiles: 'Spark.html',
                reportName: 'Extent Spark Report'
            ])
        }
    }
}
