# Saldo Vivo — Backend

API REST de controle de finanças pessoais, construída com **Spring Boot 3.3** e **Java 21**.

## Funcionalidades

- Autenticação com JWT (registro, login, atualização de perfil e senha)
- Controle de gastos por mês/categoria com status (realizado / previsto)
- Registro de renda mensal
- Despesas recorrentes com geração automática de gastos
- Cálculo de saldo atual e projetado
- Chatbot financeiro integrado à API Gemini
- Importação de vídeos do YouTube com extração de transcrição para base de conhecimento da IA

## Tecnologias

| Tecnologia | Versão |
|---|---|
| Java | 21 |
| Spring Boot | 3.3.7 |
| Spring Security | JWT stateless |
| Spring Data JPA | Hibernate |
| Banco de dados | H2 (dev) / PostgreSQL (prod) |
| Lombok | — |
| jjwt | 0.12.6 |

## Pré-requisitos

- Java 21+
- Maven 3.9+ (ou use o `mvnw.cmd` incluído)

## Configuração

Crie o arquivo `src/main/resources/application-local.yml` com suas credenciais (já está no `.gitignore`):

```yaml
gemini:
  api-key: SUA_CHAVE_GEMINI_AQUI

app:
  jwt:
    secret: SEU_SEGREDO_JWT_AQUI
```

> Veja `.env.example` para referência das variáveis disponíveis.

## Como rodar

```bash
./mvnw.cmd spring-boot:run
```

A API sobe em `http://localhost:8080`.  
O console H2 fica disponível em `http://localhost:8080/h2-console`.

## Endpoints principais

| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/auth/register` | Criar conta |
| POST | `/api/auth/login` | Login |
| PUT | `/api/auth/perfil` | Atualizar nome |
| PUT | `/api/auth/senha` | Alterar senha |
| GET/POST | `/api/gastos/{ano}/{mes}` | Listar / criar gastos |
| DELETE | `/api/gastos/{id}` | Deletar gasto |
| GET/POST | `/api/renda/{ano}/{mes}` | Consultar / salvar renda |
| GET | `/api/saldo/{ano}/{mes}` | Calcular saldo do mês |
| GET/POST | `/api/recorrentes` | Listar / criar recorrentes |
| DELETE | `/api/recorrentes/{id}` | Desativar recorrente |
| POST | `/api/recorrentes/gerar/{ano}/{mes}` | Gerar gastos do mês |
| POST | `/api/chat` | Chatbot financeiro |
| GET/POST | `/api/videos` | Listar / adicionar vídeo |
| POST | `/api/videos/canal` | Importar canal do YouTube |
| DELETE | `/api/videos/{id}` | Remover vídeo |

> Todos os endpoints (exceto `/api/auth/**`) exigem o header `Authorization: Bearer <token>`.

## Estrutura do projeto

```
src/main/java/com/financeapp/
├── config/        # Security, JWT, exception handler
├── controller/    # REST controllers
├── dto/           # Request / response records
├── model/         # Entidades JPA
├── repository/    # Spring Data repositories
└── service/       # Lógica de negócio
```
