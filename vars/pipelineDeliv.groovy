#!/usr/bin/env groovy
//test

def call(Map param){
	pipeline {
		agent {
			label "${param.agents}"
		}
		stages {
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
        	stage('Build image') {
				when {
					expression { param.agents == "container" }
				}
            	steps {
                	sh 'docker build -t my-app .'
            	}
        	}
        	stage('Run app') {
				when {
					expression { param.agents == "container" }
				}
            	steps {
                	sh 'docker run -p 8000:8181 my-app'
            	}
        	}
			stage('Run app2') {
				when {
					expression { param.agents == "vmmm" }
				}
				steps{
					sh 'java -jar target/*.jar'
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