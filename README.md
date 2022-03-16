# Desafio de Backend Donus - API REST

## Instruções de execução

O projeto foi construído usando o gerenciador da pacotes maven, basta importar o projeto como um 'maven project'.

As configurações de conexão com o Banco de Dados estão no arquivo: /resources/application.properties

O Banco de Dados utilizado foi o PostgreSQL com o nome "donus". Favor iniciar o banco com o script abaixo.

```
CREATE DATABASE donus
```

Para conexão com o banco local favor trocar as credenciais no aplication.properties
```
spring.datasource.username=postgres
spring.datasource.password=admin
```

Assim que a aplicação já estriver rodando e o spring já setou os campos do banco, favor executar o script abaixo para gerar dois usuários iniciais e bem como suas permissões.
O script abaixo irá preencher a tabela de permissões além criar dois usuários, um Administrador e um usuário simples. Ambas senhas dos usuários é 123.
```
insert into tb_costumer(id, cpf, name, password) values (1, '000.000.000-00', 'Administrador', '$2a$10$ByoourbQM2eHUQHSGc3bMeG/fEAkTbG6B7Cz9FXaYi7q8wPXmD/.C')
insert into tb_costumer(id, cpf, name, password) values (1, '111.111.111-11', 'Usuario', '$2a$10$ByoourbQM2eHUQHSGc3bMeG/fEAkTbG6B7Cz9FXaYi7q8wPXmD/.C')

insert into profile (id, name) values (1,'ROLE_ADMIN')
insert into profile (id, name) values (2,'ROLE_USER')

insert into tb_costumer_profiles (costumer_id, profiles_id) values (1,1)
insert into tb_costumer_profiles (costumer_id, profiles_id) values (2,2)
```

## Documentação
A documentação do projeto pode ser acessada tanto pelo /swagger-ui.html , quanto pela [coleção disponibilizada do postman](postman%20collection).

## Monitoramento
Algumas informações de monitoramento estão disponíveis uma sub coleção postman do Spring Actuator.
