pipeline {
    agent any
    environment {
        DOCKERHUB_USERNAME = "prvinsm21"
    }
    stages {
        stage ('Git Checkout') {
            steps {
                sh 'echo Passed'
            }
        }
        stage ('Code Compile') {
            steps {
                sh 'mvn clean compile'
            }
        }
        stage ('Unit Testing') {
            steps {
                sh 'mvn test'
            }
        }
        stage ('Integration Testing') {
            steps {
                sh 'mvn clean verify -DskipUnitTests'
            }
        }
        stage ('Static Code analysis') {
            steps {
                script {
                    withSonarQubeEnv(credentialsId: 'sonar-api') {
                        sh 'mvn clean package sonar:sonar'
                }
            }
        }
    }
}
}