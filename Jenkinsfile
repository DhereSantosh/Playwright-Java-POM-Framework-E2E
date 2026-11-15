pipeline {
    agent any

    tools {
        // Ensure these names match what is configured in Jenkins -> Manage Jenkins -> Tools
        maven 'Maven-3.9'
        jdk 'JDK-21'
    }

    parameters {
        choice(name: 'BROWSER', choices: ['chromium', 'firefox', 'webkit'], description: 'Select target browser')
        choice(name: 'GROUP', choices: ['all','regression', 'smoke', 'functional'], description: 'Select TestNG group tag to execute')
        string(name: 'THREADS', defaultValue: '3', description: 'Number of parallel browser threads')
    }

    stages {
        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

        stage('Clean Old Results') {
            steps {
                // For Windows Jenkins agents:
                bat 'if exist allure-results rmdir /s /q allure-results'
                // Note: If your Jenkins runs on Linux/Mac, comment the line above and uncomment below:
                // sh 'rm -rf allure-results'
            }
        }

        stage('Run Playwright Tests') {
            steps {
            // Pass all three tags separated by commas if 'all' is selected
            def targetGroups = (params.GROUP == 'all') ? 'regression,smoke,functional' : params.GROUP
                // For Windows Jenkins agents:
                bat """
                    mvn clean test ^
                    -Dbrowser=${params.BROWSER} ^
                    -Dheadless=true ^
                    -Dgroups=${targetGroups} ^
                    -DthreadCount=${params.THREADS}
                """
                // Note: For Linux/Mac Jenkins agents, use 'sh' and '\' instead of 'bat' and '^'
            }
        }
    }

    post {
        always {
            // Publishes Allure HTML report to the Jenkins build dashboard
            allure([
                includeProperties: false,
                jdk: '',
                properties: [],
                reportBuildPolicy: 'ALWAYS',
                results: [[path: 'allure-results']]
            ])
        }
    }
}
