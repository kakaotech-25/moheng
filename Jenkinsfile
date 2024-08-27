pipeline {
    agent {
        docker {
            image 'docker:latest'
            args '-v /var/run/docker.sock:/var/run/docker.sock'
        }
    }

    tools {
        nodejs 'MyNode'
        gradle 'MyGradle'
        jdk 'JDK_22'
    }

    environment {
        JAVA_HOME = "${tool 'JDK_22'}"
        PATH = "${env.PATH}:${env.JAVA_HOME}/bin"
        IMAGE_STORAGE_CREDENTIAL = 'dockerhub-access-token'
        IMAGE_STORAGE = 'docker.io'
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    echo 'Checking out code...'
                }
            }
            post {
                failure {
                    sendErrorNotification('Checkout')
                }
            }
        }

        stage('Parallel Build'){
            parallel{
                stage('Frontend Docker Build') {
                    steps {
                        script {
                            echo 'Starting Frontend Build...'

                            dir('nginx') {
                                docker.withRegistry("https://${IMAGE_STORAGE}", IMAGE_STORAGE_CREDENTIAL) {
                                    def nginxImage = docker.build("leovim5072/moheng-nginx:latest", "-f Dockerfile.prod ../")
                                    nginxImage.push("latest")
                                }
                            }

                            echo 'Frontend Build Completed!'
                        }
                    }
                    post {
                        failure {
                            sendErrorNotification('Frontend Build')
                        }
                    }
                }

                stage('Backend Docker Build') {
                    steps {
                        script {
                            echo 'Starting Backend Build...'
                            
                            dir('backend') {
                                docker.withRegistry("https://${IMAGE_STORAGE}", IMAGE_STORAGE_CREDENTIAL) {
                                    def backendImage = docker.build("leovim5072/moheng-backend:latest", "-f Dockerfile.prod .")
                                    backendImage.push("latest")
                                }
                            }

                            echo 'Backend Build Completed!'
                        }
                    }
                    post {
                        failure {
                            sendErrorNotification('Backend Build')
                        }
                    }
                }
            }
        }
    }

    post {
        success {
            withCredentials([string(credentialsId: 'discord-webhook', variable: 'DISCORD')]) {
                discordSend description: """
                제목 : ${currentBuild.displayName}
                결과 : ${currentBuild.result}
                실행 시간 : ${currentBuild.duration / 1000}s
                """,
                link: env.BUILD_URL, result: currentBuild.currentResult, 
                title: "${env.JOB_NAME} : ${currentBuild.displayName} 성공", 
                webhookURL: "$DISCORD"
            }
        }
    }
}

def sendErrorNotification(stageName) {
    script {
        def errorMessage = ''
        try {
            errorMessage = currentBuild.rawBuild.getLog(50).join("\n")
        } catch (Exception e) {
            errorMessage = 'Failed to retrieve error message.'
        }
        
        errorMessage = errorMessage.take(2000)  // 메시지 길이를 제한

        withCredentials([string(credentialsId: 'discord-webhook', variable: 'DISCORD')]) {
            discordSend description: """
            제목 : ${currentBuild.displayName}
            결과 : ${currentBuild.result}
            실패 단계: ${stageName}
            실행 시간 : ${currentBuild.duration / 1000}s
            오류 메시지: ${errorMessage}
            """,
            link: env.BUILD_URL, result: currentBuild.currentResult, 
            title: "${env.JOB_NAME} : ${currentBuild.displayName} 실패", 
            webhookURL: "$DISCORD"
        }
    }
}
