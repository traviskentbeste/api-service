pipeline {
    agent any

    stages {
        stage('Checkout From SCM') {
            steps {
                cleanWs()
                git branch: 'main', credentialsId: 'github-ssh-ui-service', url: 'git@github.com:traviskentbeste/ui-service.git'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        stage('Build Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'registry-credentials', passwordVariable: 'password', usernameVariable: 'username')]) {
                    sh "docker login -u $username -p $password registry.tencorners.com"
                    sh "docker build -t ui-service:0.0.1 ."
                }
            }
        }
        stage('Push To Repository') {
            steps {
                
                withCredentials([usernamePassword(credentialsId: 'registry-credentials', passwordVariable: 'password', usernameVariable: 'username')]) {
                    sh "docker tag ui-service:0.0.1 registry.tencorners.com/ui-service:0.0.1"
                    sh "docker push registry.tencorners.com/ui-service:0.0.1"
                    sh "docker image rm registry.tencorners.com/ui-service:0.0.1"
                }
        
            }
        }
        stage('Deploy To Kubernetes') {
            steps {
                

                withCredentials([file(credentialsId: 'kubernetes-config', variable: 'config')]) {
                    sh '''
                        cat $config > ./kubernetes.config
                    '''
                    sh "kubectl delete -f ./k8s/deployment.yaml --kubeconfig=./kubernetes-config"
                    sh "kubectl apply  -f ./k8s/deployment.yaml --kubeconfig=./kubernetes-config"
                    
                }
    
                
            }
        }
    }
}
