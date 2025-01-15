pipeline {
    agent any

    stages {
        stage('test') {
            steps {
                sh './gradlew test'
            }
        }
        stage('build jar') {
            steps {
                sh './gradlew build -x test'
            }
        }
        stage('load to remote server') {
            steps {
                sshagent(credentials : ['metech-ssh']) {
                    sh "scp -o StrictHostKeyChecking=no ./build/libs/link-shortener-${tag}.jar me_tech@89.169.34.97:./nexus/link-shortener-${tag}.jar"
                }
            }
        }
    }
}
