### Requisitos
___

* Java 18
* Docker/Composer
* Maven 3.5+
* JUnit 5

### Executando com docker
___

Executar o projeto localmente via docker, irá subir 2 containers contendo MySQL executando na porta 3336 e outro com a 
aplicação executando na porta 8080

> docker-compose up

### Executando manualmente
___
Executar o docker-composer para subir o banco de dados, necessário para a aplicação

> docker-compose up mysqldb

Compilar o projeto utilizando maven local ou o wrapper

> mvn clean install

ou

> ./mvnw clean install

Executar o servidor

> mvn spring-boot:run

### Testes
___

> mvn clean test

ou

> ./mvnw clean test

### DB
___
O projeto contém 2 bancos de dados, MySQL para execução da aplicação e H2 para execução efêmera
dos testes unitários

#### Acessar Mysql

> URL=jdbc:mysql://localhost:3336/pismo?allowPublicKeyRetrieval=true&useSSL=false \
> USERNAME=pismo \
> PASSWORD=q1w2e3

### API
___

Todos os endpoints podem ser verificados, após execução da aplicação, pelo swagger

> http://localhost:8080/swagger-ui/index.html#/

#### POST - criar conta
> curl --location 'http://localhost:8080/accounts' \
--header 'Content-Type: application/json' \
--data '{
"document_number": 12345678906
}'

#### GET - consultar conta por ID
> curl --location 'http://localhost:8080/accounts/1'

#### POST - criar transação
> curl --location 'http://localhost:8080/transactions' \
--header 'Content-Type: application/json' \
--data '{
"account_id":4,
"operation_type_id": 1,
"amount": 1
}'


#### GET - consultar transação por ID
> curl --location 'http://localhost:8080/transactions/2'