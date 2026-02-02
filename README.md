# IAGEN – Sistema de Classificação Automática de Reclamações

Este repositório contém a implementação da solução desenvolvida no Trabalho de Conclusão de Curso (Pós-graduação em Desenvolvimento Full Stack – PUCRS), cujo objetivo é validar o uso de Inteligência Artificial Generativa na classificação automática de reclamações em um sistema web.

A aplicação foi construída no formato de MVP (Mínimo Produto Viável), com foco na automatização da triagem de textos livres submetidos por usuários, reduzindo o esforço manual e o tempo de direcionamento das ocorrências.

---

## Descrição geral da aplicação

A solução consiste em um sistema web que permite o registro de reclamações por meio de uma interface pública, sem necessidade de autenticação. Após a submissão, o backend persiste os dados informados e realiza a classificação semântica automática utilizando Inteligência Artificial Generativa, retornando ao usuário um protocolo para acompanhamento e a categoria identificada.

A aplicação é composta pelos seguintes elementos:

- Frontend web desenvolvido em Angular, responsável pela interação com o usuário;
- Backend desenvolvido em Java com o framework Quarkus, responsável pela orquestração do fluxo, regras de negócio, persistência dos dados e integração com a IA;
- Banco de dados PostgreSQL para armazenamento das reclamações e classificações;
- Orquestração dos componentes por meio de Docker Compose, permitindo execução padronizada em ambiente local.

---

## Tecnologias utilizadas

- Java 17
- Quarkus
- LangChain4j
- Google Gemini (IA Generativa)
- Angular
- TypeScript
- PostgreSQL
- Docker
- Docker Compose
- Nginx

---

## Estrutura do projeto

```
TCC-PUCRS-IAGEN-RECLAMACOES/
├── iagen-reclamacoes-api/           # Backend (Quarkus)
├── sistema-gestao-reclamacao-web/   # Frontend (Angular)
├── compose.yaml                     # Orquestração dos containers
├── .gitignore
└── README.md                        # Documentação do projeto
```

---

## Requisitos

Para execução da aplicação em ambiente local, são necessários os seguintes requisitos:

- Docker (versão atualizada)
- Docker Compose (plugin do Docker)

Recomendado:
- Git (para clonagem do repositório)
- Navegador web atualizado

---

## Configuração de ambiente

Para executar a aplicação, é necessário criar um arquivo `.env` na raiz do projeto do sistema backend Quarkus (Api), ***Exemplo: .\iagen-reclamacoes-api\\.env***

### Exemplo de arquivo `.env`

```
# =========================================================
# PostgreSQL
# =========================================================

# Usuário do banco de dados
POSTGRES_USER=admin

# Senha do banco de dados
POSTGRES_PASSWORD=admin

# Nome do banco de dados utilizado pela aplicação
POSTGRES_DB=iagen_db

# Porta exposta pelo container do PostgreSQL
POSTGRES_PORT=5432


# =========================================================
# Backend (Quarkus)
# =========================================================

# Porta externa utilizada pelo container do backend
BACK_PORT=8080

# Porta interna configurada no Quarkus
QUARKUS_HTTP_PORT=8080

# Credenciais utilizadas pelo backend para acesso ao banco
QUARKUS_DATASOURCE_USERNAME=admin
QUARKUS_DATASOURCE_PASSWORD=admin


# =========================================================
# Integração com IA Generativa
# =========================================================

# Chave de API para consumo do serviço de IA (Google Gemini)
# Deve ser obtida no Google AI Studio
QUARKUS_LANGCHAIN4J_GEMINI_API_KEY=PUT_YOUR_API_KEY_HERE
```

## Como executar a aplicação

A. Com git instalado, execute o comando:
```
git clone https://github.com/brhluz/TCC-PUCRS-IAGEN-RECLAMACOES.git
```
 ***Em casos de falha neste comando acima, baixar o projeto em zip pelo github clicando em "Download Zip".***
 ***Extraia e prossiga abaixo***

B. Com o Docker e o Docker Compose instalados, execute o comando abaixo na raiz do projeto:

```
docker compose up --build
```

O processo realizará a inicialização dos seguintes componentes:

- Banco de dados PostgreSQL
- Backend Quarkus
- Frontend Angular servido via Nginx

Após a conclusão, a aplicação logara no console a mensagem
```
iagen-startup-msg  | ==================================================
iagen-startup-msg  |  PROJETO CARREGADO COM SUCESSO!
iagen-startup-msg  | Acesse o sistema em: http://localhost
iagen-startup-msg  | ==================================================
iagen-startup-msg exited with code 0
``` 
Após isto, a aplicação está disponível em ambiente local (localhost) e acessivel na URL http://localhost.

---

## Validação da aplicação

A validação da solução ocorre por meio da execução do fluxo completo da aplicação, desde o envio da reclamação até a exibição da classificação identificada.

Caso a variável `QUARKUS_LANGCHAIN4J_GEMINI_API_KEY` não esteja configurada, esteja inválida ou expirada, o sistema continuará operacional, registrando as reclamações com status **“Aguardando triagem”**.

---

## Observações finais

- Este repositório integra o portfólio acadêmico do autor;
- O presente README.md constitui a documentação específica do projeto disponibilizada publicamente;
- Nenhuma credencial sensível é armazenada ou versionada neste repositório;
- A chave de API para integração com a IA pode ser obtida gratuitamente por meio do [Google AI Studio](https://aistudio.google.com/).


---
## Tutorial geração chave Google Ai Studio
- Data do tutorial: 01/02/2026
1. Entrar no site [Google AI Studio](https://aistudio.google.com/)
2. Realizar Login com conta google
3. Acessar o link https://aistudio.google.com/api-keys ou cliar em "Get Api Key"
4. Clicar em "Criar Chave de API" conforme imagem:
   ![Tela Google AI Studio – criação da API Key](tela%20google%20ai%20studio%2C%20api%20key.png)
6. Dar um nome para a chave e atrela a um projeto, conforme imagem:
   
   ![tela google ai studio, nome chave e escolher projeto](tela%20google%20ai%20studio%2C%20nome%20chave%20e%20escolher%20projeto.png)
      - ***OBS:*** Se não houver projetos, criar um, seguindo o formulário após clicar em "+ Criar projeto"
8. Após isto, copiar a chave e utilizar como valor no arquivo .env, no properties ***QUARKUS_LANGCHAIN4J_GEMINI_API_KEY***
---

## Autor
Nome: Bruno Henrique da Luz Lopes

Projeto desenvolvido como Trabalho de Conclusão de Curso  
Pós-graduação em Desenvolvimento Full Stack – PUCRS

