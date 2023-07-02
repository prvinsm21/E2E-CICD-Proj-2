pipeline {
    agent any
    environment {
        DOCKERHUB_USERNAME = "prvinsm21"
        DOCKERHUB_CREDENTIALS = credentials('dockerhub')
        IMAGE_NAME = "prvinsm21/resturant-website:${BUILD_NUMBER}"
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
        stage ('Quality Gate Status')  {
            steps {
                script {
                    waitForQualityGate abortPipeline: false, credentialsId: 'sonar-api'
                }
            }
        }
        stage ('Build and Push Docker Image') {
            steps {
                script {
                    sh 'docker build -t $IMAGE_NAME .'
                    def dockerImage = docker.image("${IMAGE_NAME}")
                    withDockerRegistry(credentialsId: 'dockerhub', toolName: 'docker', url: 'https://hub.docker.com/') {
                        dockerImage.push()
                    }
                }
            }
        }  
    }
}