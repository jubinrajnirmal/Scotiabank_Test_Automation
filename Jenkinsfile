pipeline {
    agent any

    options {
        timestamps()																// Build with timestamps
        disableConcurrentBuilds()													// prevent more than one build 
        buildDiscarder(logRotator(numToKeepStr: '50'))								// Keep last 50 builds
    }

    triggers {
        cron('TZ=America/Toronto\nH 12 * * *')   // 12 PM daily
        githubPush()							 // Webhook 
    }

    environment {
        GIT_URL = 'https://github.com/jubinrajnirmal/Scotiabank_Test_Automation.git'
        REPO_DIR = 'workdir'
        EMAIL_TO = 'test.automation.server.2025@gmail.com, siddalwadi@outlook.com, jubinrajnirmal10@gmail.com, Manpreet.Panech@fdmgroup.com, Teyfik.Acikgoz@fdmgroup.com'

        // CSV ledger lives on the Jenkins VM; we copy a snapshot per build
        CSV_LEDGER = '/home/tmp_admin/testResults/test_results.csv'
        CSV_LOCAL = 'test_results.csv'
    }

    stages {
        stage('Checkout') {
            steps {
                sh '''
                    set -eux
                    rm -rf "${REPO_DIR}" || true
                    git clone "${GIT_URL}" "${REPO_DIR}"
                '''
            }
        }

        stage('Run tests') {
            steps {
                dir("${env.REPO_DIR}") {
                    sh '''
                        set -eux
                        mvn -B clean test 
                    '''
                }
            }
            post {
                always {
                    dir("${env.REPO_DIR}") {
                        archiveArtifacts artifacts: 'reports/cucumber.json', allowEmptyArchive: true
                        archiveArtifacts artifacts: 'reports/cucumber.html', allowEmptyArchive: true
                        archiveArtifacts artifacts: 'allure-results/**', allowEmptyArchive: true
                    }
                }
            }
        }

        stage('Publish Allure report') {
            when { 
                expression { 
                    fileExists("${env.REPO_DIR}/allure-results") 
                } 
            }
            steps {
                allure includeProperties: false, jdk: '', results: [[path: "${env.REPO_DIR}/allure-results"]]
            }
        }

        stage('Parse results from cucumber.json') {
            steps {
                script {
                    def jsonPath = "${env.REPO_DIR}/reports/cucumber.json"

                    env.TOTAL_SCENARIOS = env.TOTAL_SCENARIOS ?: '0'
                    env.PASSED_SCENARIOS = env.PASSED_SCENARIOS ?: '0'
                    env.FAILED_SCENARIOS = env.FAILED_SCENARIOS ?: '0'
                    env.SKIPPED_SCENARIOS = env.SKIPPED_SCENARIOS ?: '0'
                    env.TOTAL_STEPS = env.TOTAL_STEPS ?: '0'
                    env.PASSED_STEPS = env.PASSED_STEPS ?: '0'
                    env.FAILED_STEPS = env.FAILED_STEPS ?: '0'
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
                                def scFailed = stepStatuses.any { it == 'failed' }
                                def scSkipped = !scFailed && stepStatuses.any { it in ['skipped', 'pending', 'undefined'] }

                                scenariosTotal++
                                if (scFailed) {
                                    scenariosFailed++
                                } else if (scSkipped) {
                                    scenariosSkipped++
                                } else {
                                    scenariosPassed++
                                }

                                (sc.steps ?: []).each { st ->
                                    def s = st.result?.status ?: 'unknown'
                                    stepsTotal++
                                    if (s == 'passed') {
                                        stepsPassed++
                                    } else if (s == 'failed') {
                                        stepsFailed++
                                    } else if (s in ['skipped', 'undefined']) {
                                        stepsSkipped++
                                    } else if (s == 'pending') {
                                        stepsPending++
                                    }
                                }
                            }
                    }

                    env.TOTAL_SCENARIOS = scenariosTotal.toString()
                    env.PASSED_SCENARIOS = scenariosPassed.toString()
                    env.FAILED_SCENARIOS = scenariosFailed.toString()
                    env.SKIPPED_SCENARIOS = scenariosSkipped.toString()
                    env.TOTAL_STEPS = stepsTotal.toString()
                    env.PASSED_STEPS = stepsPassed.toString()
                    env.FAILED_STEPS = stepsFailed.toString()
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
                    if [ -f "${REPO_DIR}/reports/cucumber.html" ]; then 
                        zip -r reports-bundle.zip "${REPO_DIR}/reports/cucumber.html"
                    fi
                    if [ -f "${REPO_DIR}/reports/cucumber.json" ]; then 
                        zip -r reports-bundle.zip "${REPO_DIR}/reports/cucumber.json"
                    fi
                    if [ -d "${REPO_DIR}/allure-results" ]; then 
                        zip -r reports-bundle.zip "${REPO_DIR}/allure-results"
                    fi
                '''
                archiveArtifacts artifacts: 'reports-bundle.zip', allowEmptyArchive: true
            }
        }

        stage('Append to local CSV') {
            steps {
                script {
                    // 1. Make sure the ledger dir/file exists and is writable for jenkins
                    sh """
                        set -eux
                        install -d -m 2775 -o jenkins -g jenkins "\$(dirname '${CSV_LEDGER}')"
                        touch '${CSV_LEDGER}'
                        chown jenkins:jenkins '${CSV_LEDGER}'
                        chmod 664 '${CSV_LEDGER}'
                    """

                    // 2. Build the row
                    def ts = new Date().format("yyyy-MM-dd'T'HH:mm:ssXXX", TimeZone.getTimeZone('America/Toronto'))
                    def row = [
                        ts,
                        env.JOB_NAME ?: '',
                        env.BUILD_NUMBER ?: '',
                        env.GIT_URL ?: '',
                        (env.GIT_COMMIT_SHORT ?: sh(returnStdout: true, script: "cd ${env.REPO_DIR} && git rev-parse --short HEAD || true").trim()),
                        env.TOTAL_SCENARIOS ?: '0',
                        env.PASSED_SCENARIOS ?: '0',
                        env.FAILED_SCENARIOS ?: '0',
                        env.SKIPPED_SCENARIOS ?: '0',
                        env.TOTAL_STEPS ?: '0',
                        env.PASSED_STEPS ?: '0',
                        env.FAILED_STEPS ?: '0',
                        env.SKIPPED_STEPS ?: '0',
                        env.PENDING_STEPS ?: '0',
                        env.BUILD_URL ?: '',
                        "${env.BUILD_URL}artifact/reports-bundle.zip",
                        "${env.BUILD_URL}allure"
                    ]
                    
                    def q = { s -> 
                        '"' + (s?.toString()?.replaceAll('"', '""') ?: '') + '"' 
                    }
                    def csvLine = row.collect(q).join(', ')
                    writeFile file: 'row.tmp', text: csvLine + "\n"

                    // 3. Header-once + append + copy to workspace
                    def header = [
                        'timestamp', 'jobName', 'buildNumber', 'gitUrl', 'gitCommit',
                        'scenariosTotal', 'scenariosPassed', 'scenariosFailed', 'scenariosSkipped',
                        'stepsTotal', 'stepsPassed', 'stepsFailed', 'stepsSkipped', 'stepsPending',
                        'buildUrl', 'bundleUrl', 'allureUrl'
                    ].join(', ')

                    sh """
                        set -eux
                        if ! head -1 '${CSV_LEDGER}' | grep -q '^timestamp, jobName, buildNumber, '; then
                            echo '${header}' >> '${CSV_LEDGER}'
                        fi
                        cat row.tmp >> '${CSV_LEDGER}'
                        cp '${CSV_LEDGER}' '${CSV_LOCAL}'
                    """

                    archiveArtifacts artifacts: "${env.CSV_LOCAL}", allowEmptyArchive: false
                }
            }
        }

        stage('Publish CSV as HTML') {
            steps {
                script {
                    def csvText = readFile(env.CSV_LOCAL)
                    def safe = csvText.replace('&', '&amp;').replace('<', '&lt;').replace('>', '&gt;')
                    def html = """<html><head><meta charset="utf-8">
<style>
body{font-family: system-ui, Segoe UI, Arial, sans-serif;margin: 16px}
pre{white-space: pre; overflow: auto; background: #fafafa; border: 1px solid #e5e5e5; padding: 12px}
a.btn{display: inline-block;padding: 8px 12px;border: 1px solid #0a66c2;border-radius: 8px;text-decoration: none}
</style>
<title>Test Results (CSV)</title></head>
<body>
  <h2>Test Results (CSV)</h2>
  <p><a class="btn" href="../artifact/${env.CSV_LOCAL}">Download CSV</a></p>
  <pre>${safe}</pre>
</body></html>"""
                    writeFile file: 'reports/results.html', text: html
                }

                publishHTML(target: [
                    reportDir            : 'reports',
                    reportFiles          : 'results.html',
                    reportName           : 'Test Results (CSV)',
                    keepAll              : true,
                    allowMissing         : false,
                    alwaysLinkToLastBuild: true
                ])
            }
        }
    }

    post {
        always {
            script {
                def status = currentBuild.currentResult ?: 'SUCCESS'
                def csvArtifactUrl = "${env.BUILD_URL}artifact/${env.CSV_LOCAL}"

                def REPORT_NAME = 'Test Results (CSV)'
                def encoded = java.net.URLEncoder.encode(REPORT_NAME, 'UTF-8').replace('+', '%20')
                def csvHtmlUrl = "${env.BUILD_URL}${encoded}/"

                def subject = "[Test Automation Run][${status}] ${env.JOB_NAME} #${env.BUILD_NUMBER}: ${env.PASSED_SCENARIOS}/${env.TOTAL_SCENARIOS} scenarios passed"
                def body = """
<b>Build: </b> <a href="${env.BUILD_URL}">${env.JOB_NAME} #${env.BUILD_NUMBER}</a><br/>
<b>Status: </b> ${status}<br/>
<hr/>
<b>Scenario Results: </b><br/>
Total: ${env.TOTAL_SCENARIOS} &nbsp; | &nbsp; Passed: ${env.PASSED_SCENARIOS} &nbsp; | &nbsp; Failed: ${env.FAILED_SCENARIOS} &nbsp; | &nbsp; Skipped: ${env.SKIPPED_SCENARIOS}<br/>
<b>Step Results: </b><br/>
Total: ${env.TOTAL_STEPS} &nbsp; | &nbsp; Passed: ${env.PASSED_STEPS} &nbsp; | &nbsp; Failed: ${env.FAILED_STEPS} &nbsp; | &nbsp; Skipped: ${env.SKIPPED_STEPS} &nbsp; | &nbsp; Pending: ${env.PENDING_STEPS}<br/>
<hr/>
<b>Allure report: </b> <a href="${env.BUILD_URL}allure">Open</a><br/>
Please see attached zip reports bundle.
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
