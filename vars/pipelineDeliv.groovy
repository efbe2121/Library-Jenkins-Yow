#!/usr/bin/env groovy
//test

def call(Map param){
	pipeline {
		agent {
			label "${param.agents}"
		}
		stages {
			stage ("Notification Building"){
				steps{
					echo "${getMessage()} ${param.text}"
				}
			}
			stage('Build') {
				steps {
					sh 'mvn -B -DskipTests clean package'
				}
			}
			stage('Test') {
				steps {
					sh 'mvn test'
				}
				post {
					always {
						junit 'target/surefire-reports/*.xml'
					}
				}
			}
			stage('Verify') {
				when {
					expressions {param.agents == "container"}
				}
				steps{
					echo "This is ${param.text}"
				}
			}
			stage('Verify2') {
				when {
					expressions {param.agents == "vmmm"}
				}
				steps {
					echo "${param.text}"
				}
			}
		}
    }
}

def getMessage (){
	def commiter = sh(script: "git show -s --pretty=%cn",returnStdout: true).trim()
	def message = "$commiter deploying app"
	return message
}