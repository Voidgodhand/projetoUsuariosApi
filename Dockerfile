#Baixando e instalar no Docker o JDK 21
FROM openjdk:21

#Criando uma pasta dentro do docker para fazer a publicação do projeto
WORKDIR /app

#Copiando todos os arquivos do projeto para dentro da pasta do container
COPY . /app

#Comando para realizar o DEPLOY do projeto (publicação)
RUN ./mvnw -B clean package -DskipTests

#Configurando a porta em que o projeto irá rodar no container
EXPOSE 8082

#Comando para executar o projeto
CMD ["java", "-jar", "target/projetoUsuariosApi-0.0.1-SNAPSHOT.jar"]