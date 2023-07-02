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
                    docker.withRegistry('https://index.docker.io/v1/', "dockerhub") {
                    dockerImage.push()
            }
                }
            }
        }
        stage ('Update Deployment File') {
            environment {
                GIT_REPO_NAME = "E2E-CICD-Proj-2"
                GIT_USER_NAME = "prvinsm21"
            }
            steps {
                withCredentials([string(credentialsId: 'git-push', variable: 'GITHUB_TOKEN')]) {
                sh '''
                    git config user.email "prvinsm21@gmail.com"
                    git config user.name "Macko"
                    BUILD_NUMBER=${BUILD_NUMBER}
                    sed -i "s/replaceImageTag/${BUILD_NUMBER}/g" Manifest-files/Deployment.yml
                    git add Manifest-files/Deployment.yml
                    git commit -m "Update deployment image to version ${BUILD_NUMBER}"
                    git push https://${GITHUB_TOKEN}@github.com/${GIT_USER_NAME}/${GIT_REPO_NAME} HEAD:master
                '''
                }
            }
        }
    }  
}