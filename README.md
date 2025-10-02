# 🏍️ MotoMap - Sistema de Gestão de Pátios de Motos

O MotoMap é um sistema desenvolvido para auxiliar na organização e gestão dos pátios de motos da empresa Mottu. A ideia surgiu a partir de uma dor real da empresa: a dificuldade em manter o controle físico e visual de centenas de motos distribuídas entre diferentes filiais.

Para resolver isso, o MotoMap oferece um sistema integrado com:

- Organização visual dos pátios
- Controle de movimentações
- Triagem de veículos com auxílio de visão computacional

## 📦 Módulo Java

Este módulo é responsável pelo gerenciamento das entidades principais do sistema:

- Filiais
- Motos
- Posições no Pátio
- Problemas
- Usuários

Desenvolvido com Spring Boot e Spring Data JPA, este módulo fornece uma API RESTful robusta que:

- Suporta a aplicação mobile
- Permite cadastro, consulta e atualização de dados essenciais
- Conexão ao módulo em C# com ASP.NET Core, que gerencia o histórico de posições das motos e as movimentações internas (Não implementado)

## 👨‍💻 Equipe

- Caike Dametto – RM: 558614
- Guilherme Janunzzi – RM: 558461

## 🚀 Como Executar o Projeto

### ✅ Pré-requisitos

- Java 17 (JDK)
- Maven

### 🔧 Passo a Passo

1.  **Clone o repositório**
    ```bash
    git clone https://github.com/GuiJanunzzi/MottoMap-JavaBackend.git
    cd motomap-java
    ```

2.  **Execute o projeto com Maven**
    ```bash
    mvn spring-boot:run
    ```

3.  **Acesse a documentação**
    - Swagger UI: `http://localhost:8080/swagger-ui.html`
    - H2 Console: `http://localhost:8080/h2-console`

## 🔗 Endpoints

### 📍 Filial

| Método | Endpoint        | Descrição              |
| :----- | :-------------- | :--------------------- |
| GET    | `/filial`       | Lista todas as filiais |
| POST   | `/filial`       | Cadastra nova filial   |
| GET    | `/filial/{id}`  | Busca filial por ID    |
| PUT    | `/filial/{id}`  | Atualiza uma filial    |
| DELETE | `/filial/{id}`  | Remove uma filial      |

### 🛵 Moto

| Método | Endpoint          | Descrição           |
| :----- | :---------------- | :------------------ |
| GET    | `/api/motos`      | Lista todas as motos|
| POST   | `/api/motos`      | Cadastra nova moto  |
| GET    | `/api/motos/{id}` | Busca moto por ID   |
| PUT    | `/api/motos/{id}` | Atualiza uma moto   |
| DELETE | `/api/motos/{id}` | Remove uma moto     |

### 📌 Posição no Pátio

| Método | Endpoint               | Descrição                |
| :----- | :--------------------- | :----------------------- |
| GET    | `/posicao-patio`       | Lista todas as posições  |
| POST   | `/posicao-patio`       | Cadastra nova posição    |
| GET    | `/posicao-patio/{id}`  | Busca posição por ID     |
| PUT    | `/posicao-patio/{id}`  | Atualiza uma posição     |
| DELETE | `/posicao-patio/{id}`  | Remove uma posição       |

### ⚠️ Problemas

| Método | Endpoint           | Descrição                  |
| :----- | :----------------- | :------------------------- |
| GET    | `/problema`        | Lista todos os problemas   |
| POST   | `/problema`        | Cadastra um novo problema  |
| GET    | `/problema/{id}`   | Busca problema por ID      |
| PUT    | `/problema/{id}`   | Atualiza um problema       |
| DELETE | `/problema/{id}`   | Remove um problema         |

### 👤 Usuários

| Método | Endpoint          | Descrição               |
| :----- | :---------------- | :---------------------- |
| GET    | `/usuario`        | Lista todos os usuários |
| POST   | `/usuario`        | Cadastra um novo usuário|
| GET    | `/usuario/{id}`   | Busca usuário por ID    |
| PUT    | `/usuario/{id}`   | Atualiza um usuário     |
| DELETE | `/usuario/{id}`   | Remove um usuário       |

## 🗂️ Estrutura do Projeto

```text
MOTTOMAP-JAVABACKEND
├── src/main/java/br/com/fiap/mottomap
│   ├── config          # Configurações do projeto
│   ├── controller      # Controladores REST
│   ├── dto             # Objetos de transferência de dados
│   ├── exception       # Tratamento de exceções
│   ├── filter          # Filtros e interceptadores
│   ├── model           # Entidades JPA
│   ├── repository      # Interfaces Spring Data JPA
│   └── specification   # Consultas dinâmicas
├── src/main/resources
│   └── application.properties  # Arquivo de configuração
└── README.md           # Este arquivo
````

## 📅 Licença
**MotoMap © 2025 - FIAP**\
Todos os direitos reservados.