#!/usr/bin/env groovy

pipeline {
    agent any

    tools {
        jdk 'JAVA8'
    }

    stages {

        stage("Build") {
            steps {
                script {
                    step([$class: 'StashNotifier'])
                }
                sh "./gradlew clean build publish useVersionTag --stacktrace"
            }
        }

    }

    post {
        always {
            script {
                currentBuild.result = currentBuild.result ?: 'SUCCESS'
                step([$class: 'StashNotifier'])
            }
            junit "**/build/test-results/**/*.xml"
            deleteDir()
        }
        success {
            script {
                if (currentBuild.getPreviousBuild()?.result == 'FAILURE') {
                    slackSend(color: 'good', teamDomain: "teamdomainvalue", token: "tokenvalue", channel: "#channel",
                            message: "SUCCESSFUL: Job ${env.JOB_NAME} - #${env.BUILD_NUMBER} - (<${env.JOB_URL}|job>) - (<${env.BUILD_URL}|build>)")
                }
            }
        }
        failure {
            slackSend(color: 'danger', teamDomain: "teamdomainvalue", token: "tokenvalue", channel: "#channel",
                    message: "FAILED: Job ${env.JOB_NAME} - #${env.BUILD_NUMBER} - (<${env.JOB_URL}|job>) - (<${env.BUILD_URL}|build>)")
        }
    }
}