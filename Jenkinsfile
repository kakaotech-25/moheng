pipeline {
    agent any

    tools {
        nodejs 'MyNode'
        gradle 'MyGradle'
        jdk 'JDK_22'
    }

    environment {
        JAVA_HOME = "${tool 'JDK_22'}"
        PATH = "${env.PATH}:${env.JAVA_HOME}/bin"
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
                                sh 'docker build -t leovim5072/moheng-nginx:latest -f Dockerfile.prod ../'
                                sh 'docker push leovim5072/moheng-nginx:latest'
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
                                sh 'docker build -t leovim5072/moheng-backend:latest -f Dockerfile.prod ../'
                                sh 'docker push leovim5072/moheng-backend:latest'
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
