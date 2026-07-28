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
        MAIL_TO       = 'testuser.selenium67@gmail.com'//mailbox to which mail needs to be triggered
        SMTP_HOST     = 'smtp.sendgrid.net'
        SMTP_PORT     = '587'

        // API key for the API automation suite (ReqRes) - stored as a Jenkins
        // "Secret text" credential (create under Manage Jenkins > Credentials).
        // Never printed to console log; credentials() masks it automatically.
        // Variable name must match exactly what the RestAssured code reads here
        REQRES_API_KEY = credentials('REQRES_API_KEY')

        // Selenium Grid hub URL.
        
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
                    // curl -f fails (non-zero exit) on any non-2xx response, so this just
                    // confirms the Grid is reachable and responding -
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
                
                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                    bat "mvn -B test -Dgrid.url=${env.SELENIUM_GRID_URL}"
                }
            }
        }

        stage('Email: Full Run Report') {
            steps {
                // Runs as a SEPARATE mvn/JVM invocation, after the test JVM (and
                // ExtentCucumberAdapter's shutdown-hook flush) has fully exited -
                
                bat 'mvn exec:java -Dexec.mainClass="utils.ReportEmailTrigger" -Dexec.classpathScope=test -Dexec.args="test-output/SparkReport/Spark.html target/surefire-reports/testng-results.xml [Automation Report - Full Run]"'
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
                bat 'mvn exec:java -Dexec.mainClass="utils.ReportEmailTrigger" -Dexec.classpathScope=test -Dexec.args="test-output/SparkReport/Spark.html target/surefire-reports/testng-results.xml [Automation Report - Rerun of Failures]"'
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'test-output/**, target/rerun-*.txt, target/surefire-reports/**', allowEmptyArchive: true
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
