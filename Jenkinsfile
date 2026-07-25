pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK11'
    }

    environment {
        REQRES_API_KEY = credentials('REQRES_API_KEY')
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
                    def ready = bat(
                        script: "curl -s ${statusUrl} | grep -o '\"ready\":true' || true",
                        returnStdout: true
                    ).trim()
                    if (!ready) {
                        error "Selenium Grid at ${env.SELENIUM_GRID_URL} is not ready. Check the Grid hub/node status before retrying."
                    }
                    echo "Selenium Grid is ready at ${env.SELENIUM_GRID_URL}"
                }
            }
        }

        stage('Execute Full Suite') {
            steps {
                bat 'mvn -B test -Dgrid.url=${SELENIUM_GRID_URL}'   // let it fail the build for now - we WANT to see red/green clearly in phase 1
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'test-output/**', allowEmptyArchive: true
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
