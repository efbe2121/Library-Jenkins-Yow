def call(){

    pipeline {
        agent {
            label "container"
        }
        stages {
		    stage('HelloWorld') {
			    steps {
			    	helloworld("${text}")
			    }
		    }
	    }
    }

}

def helloworld(Map param){
	echo text
}