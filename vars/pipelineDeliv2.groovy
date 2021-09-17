def call(Map param){

    pipeline {
        agent {
            label "container"
        }
        stages {
		    stage('HelloWorld') {
			    steps {
			    	helloworld("${param.text}")
			    }
		    }
	    }
    }

}

def helloworld(text){
	echo text
}