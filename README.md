# 🏭 ForgePlan API – Controle de Produção Industrial

> API REST desenvolvida em Java com Quarkus para controle de estoque e planejamento de produção industrial. O sistema gerencia o relacionamento entre produtos e suas matérias-primas, calculando a viabilidade de fabricação com base nas matérias primas disponíveis.

![Status](https://img.shields.io/badge/status-estável-2ECC71?style=flat-square)

---

O que ele faz:

- CRUD completo de produtos, matérias-primas e a associação entre eles.
- Endpoint que calcula quanto de cada produto pode ser fabricado com o estoque atual, priorizando os itens de maior valor unitário
- Impede a exclusão de matérias-primas que estejam vinculadas a algum produto, evitando erros de chave estrangeira
- Utiliza Hibernate com o padrão Panache (Active Record) para manipulação de dados
- Cobertura de testes unitários com JUnit, Mockito e Panache Mock focados nas regras de negócio e validações de dados

---

## 🏗️ Escolha do Framework Quarkus (Java)

Embora meu ponto forte seja no ecossistema **JavaScript** e no back-end eu seja focado em **Node.js** utilizando **NestJS**, como o teste sugeria utilizar **Java** com **Quarkus** mesmo sem nunca programar em Java resolvi me desafiar e aprender seus principais conceitos para aplicar a resolução do teste imposto. Confesso que com Java tive dificuldades por conta de sua sintaxe, mas gosto de aprender, **conheço bem POO e tipagem por conta do TypeScript que também aplico fortemente**, mas não tive dificuldades com Quarkus por suas **"annotations"** serem semelhantes aos **decorators** utilizados no NestJS, os testes serem semelhantes ao **Vitest** e seus conceitos que estou acostumado a usar para **testes unitários e de integração** e finalmente sua **arquitetura modular e conceitos de resourcers (controllers), services e repositories (Active Record)**.

---

## 🛞 Instruções para rodar o projeto

#### Pré-requisitos

- JDK 17 instalado
- Framework: Quarkus **3.31.2**
- Build Tool: Maven **3.9.12**
- Docker & Docker Compose instalados e em execução

1. Clone o repositório e acesse o diretório do projeto:

```bash
git clone https://github.com/oliveiradniel/forgeplan-server.git

cd forgeplan-server
```

2. Copiar as variáveis:

Antes de rodar a aplicação certifique-se de copiar o arquivo `.env.example`para `.env` pois o Docker Compose irá procurar especificamente neste arquivo.

Linux/macOS

```bash
cp .env.example .env
```

Se estiver no Windows (PowerShell)

```bash
copy .env.example .env
```

3. Iniciar o banco de dados. Aqui especifique onde o arquivo .env que contém as variáveis de ambiente e o docker-compose se localizam. No root da aplicação execute:

```bash
docker compose --env-file .env -f src/main/docker/docker-compose.yml up -d
```

Isso irá iniciar o banco de dados com PostgreSQL dentro de um container no Docker.

⚠️ Se estiver com uma versão antiga do Docker precisará executar `docker-compose --env-file .env -f src/main/docker/docker-compose.yml up -d`.

3.1. Baixar dependências (Opcional):

Caso queira garantir que todas as bibliotecas do Maven foram baixadas antes de executar.

```bash
./mvnw dependency:go-offline
```

4. Executando em modo de desenvolvimento:

```bash
./mvnw compile quarkus:dev

# Se tiver a linha de comando do Quarkus instalado na sua máquina basta executar:

quarkus dev
```

---

## ❤️ RF004 - O coração do Sistema

Seguindo o requisito funcional RF004, adicionei uma rota e funcionalidade para cálculo dos produtos que podem ser produzidos com as matérias primas disponíveis em estoque. Abaixo explico um pouco melhor.

- Priorização: Assim como no requisito é feito primeiramente a priorização de produtos que tenham o maior valor unitário.
- Limitador de gargalo: Para calcular a quantidade de produções viáveis é feito a divisão e comparação para pegar o menor valor produzível, é definido então um valor de referência inicial para que qualquer quantidade real de material que tenha seja menor que ele na primeira comparação.
- Precisão: É utilizado o tipo `BigDecimal`para um cálculo preciso e sem erros de produtos que não podem ser produzidos acima da capacidade física real do estoque.

---

## 🎲 Seeds (Dados Prontos)

Para uma melhor visualização no front-end quando a aplicação é iniciada coloquei alguns dados prontos na tabela de produtos, matérias-primas e associei algumas matérias-primas aos produtos. Caso queira uma aplicação limpa e sem dados basta ir até: `src/main/resources/application.properties` e mudar a propriedade:

```bash
quarkus.hibernate-orm.sql-load-script=import.sql

# para

quarkus.hibernate-orm.sql-load-script=no-file
```

---

## 🧩 Modelagem de Dados

O sistema utiliza um relacionamento N:N entre Product e RawMaterial, gerenciado pela entidade ProductMaterial. Esta tabela associativa não apenas vincula os registros, mas também armazena a quantityNeeded, permitindo a definição precisa da ficha técnica de cada item produzido.

![Modelagem de Dados](https://raw.githubusercontent.com/oliveiradniel/forgeplan-server/refs/heads/main/src/main/assets/screenshot_of_data_modeling.png)

---

## 🧪 Testes

Fiz testes unitários dos serviços da aplicação cobrindo os principais fluxos de sucessos e erros. Foi feito uma separação com `@Nested` entre as operações de leitura e operações que mexem no banco de dados.

Foram feitos **37 testes unitários** e para executá-los basta executar:

```bash
./mvnw test
```

Se optar por executar com Quarkus deverá executar:

```bash
quarkus test
```

O Quarkus em específico trava o terminal e entra no modo **Continuous Testing Mode**, ele irá ficar ouvindo seus arquivo e roda os testes sempre que você salva.

---

## 🚀 Tecnologias utilizadas

| Tecnologia      | Finalidade                                                                  |
| --------------- | --------------------------------------------------------------------------- |
| Quarkus REST (Jackson)            | Implementação de APIs REST com serialização JSON de alta performance. |
| Quarkus Hibernate Panache           | Abstração do Hibernate (Active Record) para operações de banco de dados simplificadas.            |
| Quarkus JDBC PostgreSQL      | Driver de conexão otimizado para persistência em ambiente de produção.           |
| Quarkus JDBC H2     | Banco de dados em memória utilizado para agilizar a execução dos testes automatizados.                  |
| Quarkus Hibernate Validator         | Validação de beans (Bean Validation) baseada em anotações para garantir dados íntegros.            |
| Quarkus JUnit 5             | Framework para execução de testes unitários e de integração no ecossistema Quarkus.                           |
| Mockito Core | Criação de objetos simulados (mocks) para isolamento de lógica nos testes.              |
| Quarkus Panache Mock | Ferramenta específica para mockar entidades e repositórios Panache em testes.                        |
| Docker  | Orquestração do ambiente de banco de dados (PostgreSQL) via containers para paridade entre ambientes.

---

## 📄 Variáveis de Ambiente (Docker Compose)

O projeto utiliza um arquivo `.env` com as seguintes variáveis:

| Nome | Descrição | Exemplo |
|------|------------|----------|
| `POSTGRES_DB` | Nome do banco de dados que será criado e usado pela aplicação | `forgeplan` |
| `POSTGRES_USER` | Nome do usuário do banco de dados PostgreSQL | `forgeplan_user` |
| `POSTGRES_PASSWORD` | Senha do usuário do banco de dados PostgreSQL | `forgeplan_pass` |
| `POSTGRES_PORT` | Porta de conexão com o PostgreSQL | `5432` |
| `QUARKUS_HTTP_CORS_ORIGIN` | URL base do frontend usada no CORS | `http://localhost:5173` |

---

## 🔗 Tudo pronto?

### Ferramenta de Desenvolvimento

Este projeto utiliza as facilidades do **Quarkus Dev Mode**. Ao rodar a aplicação em modo de desenvolvimento, você pode acessar a **Dev UI** em: `http://localhost:8080/q/dev`.

Lá é possível visualizar a saúde da aplicação, documentação de endpoints, queries executadas para o banco de dados e gerenciar beans de forma interativa.

Clique no badge abaixo e veja como iniciar a aplicação web.

[![Repositório front-end](https://img.shields.io/badge/Repositório_Front_End-00A6F4?style=for-the-badge&logoColor=white)](https://github.com/oliveiradniel/forgeplan-web)