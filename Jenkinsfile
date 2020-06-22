pipeline {
  agent {
    label 'default'
  }

  options {
    skipStagesAfterUnstable()
  }

  stages {

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