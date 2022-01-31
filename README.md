# Orientações

<p>Esse microsserviço é responsável por receber dados através um arquivo chamado `fileIn.txt`, que encontra-se na parte de recursos deste projeto.<br>
Path: `src/main/resources/fileIn.txt`
</p><br><br>

<p>A saída do processamento será através de um arquivo chamado `fileOut.txt`, que encontra-se na parte de recursos deste projeto.<br>
Path: `src/main/resources/fileOut.txt`
</p><br><br>

# Design arquitetural
<p>Esse microsserviço foi implementado utilizando a *Arquitetura Hexagonal*, com a objetivo de ganhar em extensabilidade, manutenabilidade e centralização do domínio do software.</p>

# Plataforma
- Java

# Linguagem
- Java 11

# Frameworks
- Spring Boot 2
- JUnit 5

# Execução da aplicação
## Build
> mvn clean install

## Testes
> mvn test

# Executar
> java -jar .\target\capital-gains-ms-0.0.1-SNAPSHOT.jar

# Construir imagem Docker
> docker run -it --rm --name capital-gains capital-gains:v1

# Executar imagem Docker
> docker run -it --rm --name capital-gains capital-gains:v1