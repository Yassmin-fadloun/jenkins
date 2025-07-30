def buildImage(imageName, imageTag) {
    sh "docker build -t ${imageName}:${imageTag} ."
}

def pushImage(imageName, imageTag) {
    withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
        sh """
        echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
        docker push ${imageName}:${imageTag}
        """
    }
}
