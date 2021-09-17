#!/usr/bin/env groovy
//test

def call(Map param){
	pipeline {
		agent none

		stages {
			stage ("Notification Building"){
				agent {
					label 'vmmm'
					label 'container'
				}
				steps{
					echo "${getMessage()} ${param.text}"
				}
			}
			stage('Build') {
				agent {
					label 'vmmm'
					label 'container'
				}
				steps {
					sh 'mvn -B -DskipTests clean package'
				}
			}
			stage('Test') {
				agent {
					label 'vmmm'
					label 'container'
				}
				steps {
					sh 'mvn test'
				}
				post {
					always {
						junit 'target/surefire-reports/*.xml'
					}
				}
			}
        	stage('Build image') {
				agent {
					label 'container'
				}
            	steps {
                	sh 'docker build -t my-app .'
            	}
        	}
        	stage('Run app') {
				agent {
					label 'container'
				}
            	steps {
                	sh 'docker run -p 8000:8181 my-app'
            	}
       	 	}
    	}
    	post {
        	always {
            	deleteDir()
        	}
    	}
    }
}

def getMessage (){
	def commiter = sh(script: "git show -s --pretty=%cn",returnStdout: true).trim()
	def message = "$commiter deploying app"
	return message
}