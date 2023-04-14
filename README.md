# 🎯 Challenge Backend 3ª Edição - Alura

![Badge laranja do Java 17](https://img.shields.io/badge/Java-17-orange)
![Badge verde do MySQL](https://img.shields.io/badge/MySQL-green)
![Badge verde do Spring Boot 3.0.5](https://img.shields.io/badge/Spring%20Boot-3.0.5-green)
![Badge verde de Gestão de Dependências Maven](https://img.shields.io/badge/Gestão%20De%20Dependências-Maven-green)
![Badge amarela esverdeada de Quantidade de Dependências 13](https://img.shields.io/badge/Depend%C3%AAncias-13-yellowgreen)
![Badge de linguagem utiliaza Java](https://img.shields.io/badge/Linguagem-JAVA-yellow)
![Badge de Status do projeto como em desenvolvimento](https://img.shields.io/badge/Status-Em%20Desenvolvimento-yellowgreen)
![Badge de Desenvolvedor com o nome Bruno](https://img.shields.io/badge/Desenvolvedor-Bruno-green)
![Badge de Token JWT verde com Auth0](https://img.shields.io/badge/TokenJWT-Auth0-green)
![Badge de Google Cloud branca com Compute Engine](https://img.shields.io/badge/Google%20Cloud-Compute%20Engine-white)

---

## 📋 Descrição do Projeto
Projeto criado durante um evento da escola online de tecnologia [Alura](https://www.alura.com.br/) onde todos os alunos receberam um conjunto de informações via [Trello](https://trello.com) e instruções via lives e videos gravados para criarem uma aplicação web tradicional, server-side, com o objetivo de realizar a analize de arquivos CSV de transações financeiras entre bancos e a inserção das informações presentes nele dentro do banco de dados, respeitando algumas regras de negócio e necessidades do cliente final.

---

### 📆 Semana 1
Nesta primeira semana as tarefas foram referentes a criação do projeto, do banco de dados e do formulário para realização do upload de arquivos CSV. Como em todos os demais desafios a Alura disponibiliza diversos cursos para nos aperfeiçoarmos em diferentes linguagens, tudo dentro da própria plataforma, e também sanarmos algumas duvidas que possamos ter, e nos da total liberdade de escolhermos como iremos trabalhar durante esse projeto, escolhendo linguagens, plataformas, etc.

![image](https://user-images.githubusercontent.com/100006703/230392796-a71b4d7b-53eb-4cee-afbc-a7325aaeeaf6.png)
![image](https://user-images.githubusercontent.com/100006703/230392852-a4a7acb6-90c7-4541-861d-c6af8513c8d1.png)

Abaixo detalhei o que elaborei nesta primeira etapa do projeto:

#### Banco de Dados
Para a elaboração do projeto utilizei MySQL com Flyway, permitindo versionamento do banco de dados e controle das querys lanças ao banco de dados dentro da pasta resources/db/migration.

#### Linguagem e Framework
Por questão de prática e até mesmo para treino e aperfeiçoamento escolhi a linguagem JAVA, que é a que tenho maior interesse, assim como o Framework Spring.
Junto destes utilizei Thymeleaf e Bootstrap para trabalhar no html e css.

---

#### 🔨 Forma de elaboração
Para realizar o upload dos arquivos a primeira etapa foi a criação do formulário html onde, como descrevi acima, utilizei Bootstrap para a estilização e Thymeleaf para alguns controles de validação e respostas enviadas para o html.
![image](https://user-images.githubusercontent.com/100006703/230394905-47dfcd91-17dd-43c3-9a91-14d77668335d.png)
![image](https://user-images.githubusercontent.com/100006703/230394968-2c1e84c3-8edc-4c15-82e2-0355d178f500.png)
![image](https://user-images.githubusercontent.com/100006703/230395315-1897c140-039d-4e5b-839c-a2b46686207e.png)

Após a elaboração desta tela, nesta mesma semana, foi solicitada a alteração para exibir também uma listagem das datas das transações presentes nos arquivos csv (cada arquivo deveria conter apenas as tranções de um único dia) que tiveram seus uploads realizados, assim como a data que foram realizados esses uploads.

![image](https://user-images.githubusercontent.com/100006703/230396126-ffdb353d-0b8d-4476-a936-e1244affcf33.png)

Por fim foi gerada a tela abaixo:
![image](https://user-images.githubusercontent.com/100006703/230396213-3e88f0b0-90b1-4908-9af2-19362a4a7cb7.png)

Nesta etapa do projeto utilizei a criação de apenas um controller e apenas uma url, porém com a disponibilidade de dois métodos, POST e GET, para realizar o upload e a recuperação das informações.
![image](https://user-images.githubusercontent.com/100006703/230396748-4daefbd1-1ba5-4070-a269-dfcd2723bb45.png)

Para o controle das transações criei uma entidade - ```Transacao``` - e para o controle das datas de upload criei outra - ```Importacao``` - , ficando com duas classes e duas tabelas dentro do banco de dados, e para o controle das regras de negócio e tratamento das informações, convertendo de fato o arquivo CSV para uma instância de Transação, criei uma classe chamada de ```TransacaoService```.

---

#### 📜 Tarefas da Semana 1
- [x] Criar o banco de dados;
- [x] Criar endpoints para o formulário.
- [x] Realizar validação dos arquivos CSV antes do upload e, se estiverem okay, realizar cadastro no banco de dados; 
- [x] Realizar a criação das telas para o upload e verificação dos arquivos que já foram carregados.

---

### 📆 Semana 2
Nesta segunda semana as tarefas foram focadas em pontos que são muito exigidos, sejam em aplicações MVC ou APIs REST, que são os de segurança e autenticação. Fomos orientados a criar o CRUD de usuários, assim como as telas de login e de cadastro e também tivemos que alterar nosso banco de dados para adicionar um campo para registrar os usuários responsáveis pelo upload de cada uma das importações realizadas na aplicação. 
Uma série de regras de negócio foi exigida para a criação do CRUD de usuários:

```
  Regras de negócio:
  
    Apenas 2 informações serão necessárias no cadastro: Nome e Email, sendo ambas obrigatórias
    A aplicação deve gerar uma senha aleatória para o usuário, composta de 6 números;
    A senha deverá ser enviada para o email do usuário sendo cadastrado;
    A senha não deve ser armazenada no banco de dados em texto aberto, devendo ser salvo um hash dela, gerado pelo algoritmo BCrypt;
    A aplicação não deve permitir o cadastro de um usuário com o email de outro usuário já cadastrado, devendo exibir uma mensagem de erro caso essa situação ocorra;
    A aplicação deve ter um usuário padrão previamente cadastrado, com nome: Admin, email: admin@email.com.br e senha: 123999;
    O usuário padrão não pode ser editado e nem excluído da aplicação, tampouco deve ser exibido na lista de usuários cadastrados;
    Qualquer usuário tem permissão para listar, cadastrar, alterar e excluir outros usuários;
    Um usuário não pode excluir ele próprio da aplicação.
```

Com usuários criados também tivemos que realizar a alteração da exclusão padrão para uma exclusão lógica de usuários do banco de dados e também criar uma tela para exibir detalhes das importações, mostrando todas as transações que foram registradas dentro daquele arquivo

#### 🔨 Forma de elaboração
Para realizar o login utilizei a seguinte tela, com auxilio do ```Spring Security``` para implementação de seguranã e também de ```Thymeleaf``` para integração do HTML com os controllers como já vinha fazendo:
![image](https://user-images.githubusercontent.com/100006703/231603353-5a44d981-7487-4420-8a78-fbed3738f171.png)

Caso algum erro seja encontrado com as informações de login a seguinte tela é mostrada:
![image](https://user-images.githubusercontent.com/100006703/231603414-65b03882-9195-4fff-a569-59fa85016c8c.png)

A tela utilizada para importação de transações ganhou duas novas colunas dentro da tabela de exibição de importações realizadas, uma referente ao usuário que realizou o upload do arquivo e outra para permitir o detalhamento das informações:
![image](https://user-images.githubusercontent.com/100006703/231603668-1bd099f1-dde9-4fbb-9361-5d24294789dc.png)

Todas as telas ganharam também um navbar para permitir que sejam acessados todos os endpoints da aplicação sem maiores problemas:
![image](https://user-images.githubusercontent.com/100006703/231603734-10a7cd2f-d149-4282-9e96-eee045b08c59.png)

Ao se clicar no botão de ```Detalhar``` em um dos itens da tabela se é exibido todas as transações do banco de dados:
![image](https://user-images.githubusercontent.com/100006703/231603856-4a9a2346-2718-4889-ad7e-5ca8d6649af3.png)

Para a funcionalidade da imagem acima utilizei a data das transações como ```PathVariable``` e a utilizei em meu ```Repository``` para pesquisar todas as transações referentes, considerando que um arquivo só poderá realizar uploads de transações de uma mesma data e que, uma vez que uma data tenha o seu arquivo CSV cadastrado ela não pode mais ser utilizada.

Para os usuários utilizei a tela abaixo para a listagem, sendo acessada via navbar:
![image](https://user-images.githubusercontent.com/100006703/231604254-97220232-cec9-4b70-9179-3e3a7a968d76.png)

Permitindo o cadastro de novos usuarios pelo botão ```Novo```
![image](https://user-images.githubusercontent.com/100006703/231604307-b9f4e2f7-4df6-48ab-b704-992932271697.png)
![image](https://user-images.githubusercontent.com/100006703/231604327-222e7e8b-ac54-4ef9-9ed3-c8e07d7c56a0.png)
![image](https://user-images.githubusercontent.com/100006703/231604557-a49185c8-ef6c-49fa-8879-9ef43ced5f89.png)

Uma tela semelhante é exibida ao se selecionar a edição de um dos usuários:
![image](https://user-images.githubusercontent.com/100006703/231604697-9ca9be55-11aa-44db-9ac4-a32cd7f8aa43.png)

E por fim utilizei um modal junto ao botão de excluir para permitir uma confirmação do usuário para exclusão lógica do usuário em questão:
![image](https://user-images.githubusercontent.com/100006703/231604795-142b2665-7865-4798-a2b6-1e1bffcb23e3.png)

Todas as validações foram feitas com ```BeanValidation``` e o usuário ```admin``` foi inserido no banco de dados previamente, com senha criptografada e deixado de lado nas listagens de usuário por meio de edição dos repositorys com a anotação ```@Query```.
![image](https://user-images.githubusercontent.com/100006703/231605032-fb3e8940-6c47-4fa2-9fb2-ed537fe2b99f.png)
![image](https://user-images.githubusercontent.com/100006703/231605072-72873220-65b7-4e61-b384-892f2bf56e2b.png)

As alterações no banco de dados foram feitas por meio do Flyway:
![image](https://user-images.githubusercontent.com/100006703/231605147-515d264a-3b6a-4ee4-8b06-82f06c45f1ba.png)
![image](https://user-images.githubusercontent.com/100006703/231605173-56759243-4202-4c92-bae7-fe3b98ad3299.png)

Como meus dados de login seriam diferentes do padrão do Spring Security utilizei meu próprio esquema de tabelas e classes necessárias.

---

#### 📜 Tarefas da Semana 2
- [x] Criar o CRUD de usuários;
- [x] Implementar a autenticação;
- [x] Criar uma tela para permitir o detalhamento de uma importação;
- [x] Registrar quem realizou a importação, alterando o banco de dados para guardar esta informação.
---

### 📆 Semanas 3 e 4


#### 🔨 Forma de elaboração


##### Documentação



#### 📜 Tarefas das Semanas 3 e 4
- [x] 
---
