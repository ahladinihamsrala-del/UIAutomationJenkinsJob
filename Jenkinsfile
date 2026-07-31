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
            allowMissing: true,
            alwaysLinkToLastBuild: true,
            keepAll: true,
            reportDir: 'target',
            reportFiles: 'cucumber-report.html',
            reportName: 'Cucumber Report'
        ])

        publishHTML(target: [
            allowMissing: true,
            alwaysLinkToLastBuild: true,
            keepAll: true,
            reportDir: 'test-output/SparkReport',
            reportFiles: 'Spark.html',
            reportName: 'Extent Report'
        ])
    }
    ...
}
}