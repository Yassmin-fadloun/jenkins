def call(Map args = [:]) {
    def imageName = args.imageName
    def imageTag = args.imageTag

    echo "Building Docker image ${imageName}:${imageTag}"
    sh "docker build -t ${imageName}:${imageTag} ."
    
    withCredentials([usernamePassword(
        credentialsId: 'dockerhub-creds',
        usernameVariable: 'DOCKER_USER',
        passwordVariable: 'DOCKER_PWD'
    )]) {
        sh "echo \$DOCKER_PWD | docker login -u \$DOCKER_USER --password-stdin"
        sh "docker push ${imageName}:${imageTag}"
    }
}
