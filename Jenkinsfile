pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Inject Session Cookies') {
            steps {
                withCredentials([file(credentialsId: 'ixigo-session-cookies', variable: 'COOKIE_FILE')]) {
                    bat 'copy "%COOKIE_FILE%" "%WORKSPACE%\\session-cookies.ser"'
                    // Linux agent: sh 'cp "$COOKIE_FILE" "$WORKSPACE/session-cookies.ser"'
                }
            }
        }

        stage('Run Tests - Headless') {
            steps {
                bat 'mvn clean test -Dbrowser=chrome -Dheadless=true'
                // Linux agent: sh 'mvn clean test -Dbrowser=chrome -Dheadless=true'
            }
        }
    }

    post {
        always {
            junit '**/target/surefire-reports/*.xml'
            publishHTML(target: [
                reportDir: 'target',
                reportFiles: 'cucumber-report.html',
                reportName: 'Cucumber Report'
            ])
        }
        unstable {
            echo 'Some tests failed - if failures mention session/cookie loading, the saved session may have expired. Re-run SaveSessionCookies locally and re-upload the Jenkins credential.'
        }
    }
}