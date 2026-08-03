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
                    
                }
            }
        }

        stage('Run Tests - Chrome and Firefox') {
    steps {
        bat 'mvn clean test -Dheadless=true'
    }
}
    }

 post {
    always {
        junit '**/target/surefire-reports/*.xml'
        archiveArtifacts artifacts: 'test-output/SparkReport/**, target/cucumber-report.html', allowEmptyArchive: true

        publishHTML(target: [
            allowMissing: true,
            alwaysLinkToLastBuild: true,
            keepAll: true,
            reportDir: 'test-output/SparkReport',
            reportFiles: 'Spark.html',
            reportName: 'Extent Report'
        ])
    }
}
}