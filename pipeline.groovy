pipeline{
    agent {label 'node2'}
    
    environment {
        docker_img_name = 'adhmabdein/crud'
    }
    
    stages{
        stage('pull code from github'){
            steps{
                script{
                    git "https://github.com/AdhmAbdein/spring-crud.git"
                    
                }
            }
        }
        stage('log in to docker hub'){
            steps{
                script{
                    withCredentials ([usernamePassword(credentialsId:'docker_hub', usernameVariable:'d_hub_usr', passwordVariable:'d_hub_pass')]){
                        sh 'docker login -u ${d_hub_usr} -p ${d_hub_pass}'
                    }
                }
            }
        }
        stage('CI - Build docker image'){
            steps{
                script{
                    sh 'docker build -t ${docker_img_name} -f Dockerfile .'
                }
            }
        }
        stage('push image to docker hub'){
            steps{
                script{
                    sh 'docker push ${docker_img_name}'
                }
            }
        }
        stage('CD - Deploy in k8s'){
            steps{
                script{

                       sh 'kubectl apply -f k8s-mysql-pv.yml'
                       sh 'kubectl apply -f k8s-mysql-pvc.yml'
                       sh 'kubectl apply -f k8s-mysql-secret.yml'
                       sh 'kubectl apply -f k8s-mysql-deploy.yml'
                       sh 'kubectl apply -f k8s-mysql-svc.yml'

                       sh 'kubectl apply -f k8s-spring-deploy.yml'
                       sh 'kubectl apply -f k8s-spring-svc.yml'
                    
                }
            }
        }
       
    }
}