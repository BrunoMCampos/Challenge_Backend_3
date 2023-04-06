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


---

#### 📜 Tarefas da Semana 2
- [x] 

---

### 📆 Semanas 3 e 4


#### 🔨 Forma de elaboração


##### Documentação



#### 📜 Tarefas das Semanas 3 e 4
- [x] 
---
