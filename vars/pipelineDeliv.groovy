#!/usr/bin/env groovy
//test

def call(Map param){
	pipeline {
		agent none
		stages {
			stage ("Notification Building"){
				agent {
					label 'vmmm'
				}
				steps{
					echo "${getMessage()} ${param.text}"
				}
			}
			stage('Build') {
				agent {
					label 'vmmm'
				}
				steps {
					sh 'mvn -B -DskipTests clean package'
				}
			}
			stage('Test') {
				agent {
					label 'vmmm'
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
        	stage('') {
				agent {
					docker { image 'node:14-alpine' }
				}
            	steps {
                	echo "${getMessage()} ${param.text}"
            	}
        	}
        	stage('Run app') {
				agent {
					docker { image 'node:14-alpine' }
				}
            	steps {
                	sh 'node --version'
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