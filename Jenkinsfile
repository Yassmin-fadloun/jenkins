@Library('my-shared-library') _
pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-creds')
        IMAGE_NAME = "my-dockerhub-user/myapp"
        IMAGE_TAG = "v1.0.${env.BUILD_NUMBER}"
        KUBE_MANIFESTS = "manifests"
    }

    stages {
        stage('Build Image') {
            steps {
                script {
                    buildAndPush.buildImage(IMAGE_NAME, IMAGE_TAG)
                }
            }
        }

        stage('Scan Image') {
            steps {
                sh "trivy image ${IMAGE_NAME}:${IMAGE_TAG} --exit-code 0"
            }
        }

        stage('Push Image') {
            steps {
                script {
                    buildAndPush.pushImage(IMAGE_NAME, IMAGE_TAG)
                }
            }
        }

        stage('Delete Local Image') {
            steps {
                sh "docker rmi ${IMAGE_NAME}:${IMAGE_TAG} || true"
            }
        }

        stage('Update Manifests') {
            steps {
                sh """
                sed -i 's|image: .*|image: ${IMAGE_NAME}:${IMAGE_TAG}|' ${KUBE_MANIFESTS}/deployment.yaml
                """
            }
        }

        stage('Push Manifests to GitHub') {
            steps {
                sh """
                git config --global user.email "ci@example.com"
                git config --global user.name "ci-bot"
                git add ${KUBE_MANIFESTS}/deployment.yaml
                git commit -m "Update image tag to ${IMAGE_TAG}"
                git push origin main
                """
            }
        }
    }
}
