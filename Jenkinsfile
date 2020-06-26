pipeline {
  agent any

  options {
    skipStagesAfterUnstable()
  }

  stages {

  	stage('Configure Android'){
      steps {
        sh "sudo ${ANDROID_HOME}/tools/bin/sdkmanager build-tools;${ANDROID_BUILD_TOOLS_VERSION} platforms;android-${ANDROID_VERSION} platform-tools"
      }
  	}

    stage('Static analysis') {
      steps {
        sh './gradlew lint'
        androidLint pattern: '**/lint-results-*.xml'
      }
    }

    stage('Unit test') {
      steps {
        sh './gradlew test'
        junit '**/TEST-*.xml'
      }
    }
  }

  post {
    failure {
      mail to: 'doc@1904labs.com', subject: 'Oops!', body: "Build ${env.BUILD_NUMBER} failed; ${env.BUILD_URL}"
    }
  }
}
