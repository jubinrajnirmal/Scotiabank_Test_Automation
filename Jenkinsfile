pipeline {
  agent any

  options {
    timestamps()                                        // Build log with timestamps
    disableConcurrentBuilds()                           // To prevent overlapping runs
    buildDiscarder(logRotator(numToKeepStr: '50'))      // Only keep last 50 builds, delete the rest
  }

  triggers {
    cron('TZ=America/Toronto\nH 12 * * *')              // 12 PM daily
    githubPush()                                        // GitHub webhook
  }

  environment {
    GIT_URL  = 'https://github.com/jubinrajnirmal/Scotiabank_Test_Automation.git'
    REPO_DIR = 'workdir'
    EMAIL_TO = 'test.automation.server.2025@gmail.com'
  }

  stages {

    stage('Checkout') {
      steps {
        sh '''
          set -eux
          rm -rf "${REPO_DIR}" || true
          echo "Cloning the Github repo..."
          git clone "${GIT_URL}" "${REPO_DIR}"
        '''
      }
    }

    stage('Run tests using tee cmd') {
      steps {
        dir("${env.REPO_DIR}") {
          sh '''
            set -eux
            rm -f build_log.txt || true
            mvn -B clean test | tee build_log.txt
          '''
        }
      }
      post {
        always {
          dir("${env.REPO_DIR}") {
            archiveArtifacts artifacts: 'build_log.txt',         allowEmptyArchive: true
            archiveArtifacts artifacts: 'reports/cucumber.json', allowEmptyArchive: true
            archiveArtifacts artifacts: 'reports/cucumber.html', allowEmptyArchive: true
            archiveArtifacts artifacts: 'allure-results/**',     allowEmptyArchive: true
          }
        }
      }
    }

    stage('Publish Allure report') {
      when { expression { fileExists("${env.REPO_DIR}/allure-results") } }
      steps {
        allure includeProperties: false, jdk: '', results: [[path: "${env.REPO_DIR}/allure-results"]]
      }
    }

    stage('Parse results from cucumber.json') {
      steps {
        script {
          def jsonPath = "${env.REPO_DIR}/reports/cucumber.json"
          
          env.TOTAL_SCENARIOS   = env.TOTAL_SCENARIOS   ?: '0'
          env.PASSED_SCENARIOS  = env.PASSED_SCENARIOS  ?: '0'
          env.FAILED_SCENARIOS  = env.FAILED_SCENARIOS  ?: '0'
          env.SKIPPED_SCENARIOS = env.SKIPPED_SCENARIOS ?: '0'
          env.TOTAL_STEPS   = env.TOTAL_STEPS   ?: '0'
          env.PASSED_STEPS  = env.PASSED_STEPS  ?: '0'
          env.FAILED_STEPS  = env.FAILED_STEPS  ?: '0'
          env.SKIPPED_STEPS = env.SKIPPED_STEPS ?: '0'
          env.PENDING_STEPS = env.PENDING_STEPS ?: '0'

          if (!fileExists(jsonPath)) {
            echo "cucumber.json not found at ${jsonPath} — skipping parse."
            return
          }

          def text = readFile(jsonPath)
          def features = new groovy.json.JsonSlurper().parseText(text)

          int scenariosTotal = 0, scenariosPassed = 0, scenariosFailed = 0, scenariosSkipped = 0
          int stepsTotal = 0, stepsPassed = 0, stepsFailed = 0, stepsSkipped = 0, stepsPending = 0

          features.each { f ->
			  (f.elements ?: [])
			    .findAll { (it.type ?: '').toLowerCase() != 'background' }   
			    .each { sc ->
			      def stepStatuses = (sc.steps ?: []).collect { it.result?.status ?: 'unknown' }
			      def scFailed  = stepStatuses.any { it == 'failed' }
			      def scSkipped = !scFailed && stepStatuses.any { it in ['skipped','pending','undefined'] }
			
			      scenariosTotal++
			      if (scFailed)       scenariosFailed++
			      else if (scSkipped) scenariosSkipped++
			      else                scenariosPassed++
			
			      (sc.steps ?: []).each { st ->
			        def s = st.result?.status ?: 'unknown'
			        stepsTotal++
			        if (s == 'passed') stepsPassed++
			        else if (s == 'failed') stepsFailed++
			        else if (s in ['skipped','undefined']) stepsSkipped++
			        else if (s == 'pending') stepsPending++
			      }
			    }
			}

          env.TOTAL_SCENARIOS   = scenariosTotal.toString()
          env.PASSED_SCENARIOS  = scenariosPassed.toString()
          env.FAILED_SCENARIOS  = scenariosFailed.toString()
          env.SKIPPED_SCENARIOS = scenariosSkipped.toString()

          env.TOTAL_STEPS   = stepsTotal.toString()
          env.PASSED_STEPS  = stepsPassed.toString()
          env.FAILED_STEPS  = stepsFailed.toString()
          env.SKIPPED_STEPS = stepsSkipped.toString()
          env.PENDING_STEPS = stepsPending.toString()

          echo "Scenarios => total=${env.TOTAL_SCENARIOS}, passed=${env.PASSED_SCENARIOS}, failed=${env.FAILED_SCENARIOS}, skipped=${env.SKIPPED_SCENARIOS}"
          echo "Steps => total=${env.TOTAL_STEPS}, passed=${env.PASSED_STEPS}, failed=${env.FAILED_STEPS}, skipped=${env.SKIPPED_STEPS}, pending=${env.PENDING_STEPS}"
        }
      }
    }

    stage('Bundle reports') {
      steps {
        sh '''
          set -eux
          rm -f reports-bundle.zip || true
          if [ -f "${REPO_DIR}/reports/cucumber.html" ]; then zip -r reports-bundle.zip "${REPO_DIR}/reports/cucumber.html"; fi
          if [ -f "${REPO_DIR}/reports/cucumber.json" ]; then zip -r reports-bundle.zip "${REPO_DIR}/reports/cucumber.json"; fi
          if [ -d "${REPO_DIR}/allure-results" ]; then zip -r reports-bundle.zip "${REPO_DIR}/allure-results"; fi
        '''
        archiveArtifacts artifacts: 'reports-bundle.zip', allowEmptyArchive: true
      }
    }

    stage('Append to Google Sheets') {
      environment {
        GS_WEBAPP_URL = credentials('GOOGLE_SHEET_WEBAPP_URL')
      }
      steps {
        script {
          def commit = sh(returnStdout: true, script: "cd ${env.REPO_DIR} && git rev-parse --short HEAD").trim()
          env.GIT_COMMIT_SHORT = commit ?: ''

          def payload = [
            timestamp        : new Date().format("yyyy-MM-dd'T'HH:mm:ssXXX", TimeZone.getTimeZone('America/Toronto')),
            jobName          : env.JOB_NAME,
            buildNumber      : env.BUILD_NUMBER,
            gitUrl           : env.GIT_URL,
            gitCommit        : env.GIT_COMMIT_SHORT,
            scenariosTotal   : env.TOTAL_SCENARIOS,
            scenariosPassed  : env.PASSED_SCENARIOS,
            scenariosFailed  : env.FAILED_SCENARIOS,
            scenariosSkipped : env.SKIPPED_SCENARIOS,
            stepsTotal       : env.TOTAL_STEPS,
            stepsPassed      : env.PASSED_STEPS,
            stepsFailed      : env.FAILED_STEPS,
            stepsSkipped     : env.SKIPPED_STEPS,
            stepsPending     : env.PENDING_STEPS,
            buildUrl         : env.BUILD_URL,
            bundleUrl        : "${env.BUILD_URL}artifact/reports-bundle.zip",
            allureUrl        : "${env.BUILD_URL}allure"
          ]
          writeFile file: 'payload.json', text: groovy.json.JsonOutput.prettyPrint(groovy.json.JsonOutput.toJson(payload))
          sh 'cat payload.json'
          sh '''
            set -eux
            curl -sS -X POST -H "Content-Type: application/json" --data @payload.json "$GS_WEBAPP_URL" -o gs_response.txt -w "\\nHTTP %{http_code}\\n"
          '''
          archiveArtifacts artifacts: 'payload.json, gs_response.txt', allowEmptyArchive: true
        }
      }
    }
  }

  post {
    always {
      script {
        def status = currentBuild.currentResult ?: 'SUCCESS'
        def sheetLink = (fileExists('gs_response.txt') ? readFile('gs_response.txt').readLines().find { it ==~ /https?:.*/ } : null) ?: 'Google Sheet updated (see Apps Script).'
        def subject = "[Test Automation Run][${status}] ${env.JOB_NAME} #${env.BUILD_NUMBER}: ${env.PASSED_SCENARIOS}/${env.TOTAL_SCENARIOS} scenarios passed"

        def body = """
        <b>Build:</b> <a href="${env.BUILD_URL}">${env.JOB_NAME} #${env.BUILD_NUMBER}</a><br/>
        <b>Status:</b> ${status}<br/>
        <b>Commit:</b> ${env.GIT_COMMIT_SHORT ?: ''}<br/>
        <hr/>
        <b>Scenario Results:</b><br/>
        Total: ${env.TOTAL_SCENARIOS} &nbsp; | &nbsp; Passed: ${env.PASSED_SCENARIOS} &nbsp; | &nbsp; Failed: ${env.FAILED_SCENARIOS} &nbsp; | &nbsp; Skipped: ${env.SKIPPED_SCENARIOS}<br/>
        <b>Step Results:</b><br/>
        Total: ${env.TOTAL_STEPS} &nbsp; | &nbsp; Passed: ${env.PASSED_STEPS} &nbsp; | &nbsp; Failed: ${env.FAILED_STEPS} &nbsp; | &nbsp; Skipped: ${env.SKIPPED_STEPS} &nbsp; | &nbsp; Pending: ${env.PENDING_STEPS}<br/>
        <hr/>
        <b>Google Sheet:</b> ${sheetLink}<br/>
        <b>Report bundle (zip):</b> <a href="${env.BUILD_URL}artifact/reports-bundle.zip">Download</a><br/>
        <b>Allure report:</b> <a href="${env.BUILD_URL}allure">Open</a><br/>
        """

        emailext(
          subject: subject,
          body: body,
          mimeType: 'text/html',
          to: env.EMAIL_TO,
          attachLog: (status != 'SUCCESS'),
          attachmentsPattern: 'reports-bundle.zip'
        )
      }
    }
  }
}
