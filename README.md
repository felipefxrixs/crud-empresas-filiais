# CRUD Empresas e Filiais

> Sistema para gerenciamento de empresas e suas filiais.

## Visão Geral

Este projeto consiste em um sistema CRUD (Create, Read, Update, Delete) para gerenciamento de empresas e suas filiais. A aplicação permite o cadastro, consulta, atualização e exclusão de empresas, bem como o gerenciamento de filiais vinculadas a uma empresa sede.

O projeto foi desenvolvido com o objetivo de demonstrar conhecimentos em desenvolvimento full stack, incluindo backend, frontend e integração com banco de dados.

## Stack de Tecnologias

Lista resumida das principais tecnologias utilizadas.

- **Linguagem:** Java 17, Spring Boot 3.4.4, Spring Web, Spring Data JPA, Validation, Maven
- **Framework principal:** Vue 3.5, Quasar 2, Vue Router, Axios, Vite
- **Banco de dados:** MySQL 8+
- **Infraestrutura:** execução local em `localhost`

## Principais Funcionalidades

- Cadastro de empresas sede
- Atualização de informações das empresas sede
- Exclusão de empresas sede
- Cadastro de filiais vinculadas a uma empresa sede
- Listagem de empresas sede e filiais
- Atualização de dados das filiais
- Exclusão de filiais
- Realocação de empresa sede
- Busca de empresa por CNPJ e nome
- Busca de pessoa por CPF e nome
- Cadastro de pessoas
- Vínculo de pessoas a uma empresa (representantes)
- Listagem de pessoas cadastradas
- Atualização de dados das pessoas cadastradas
- Atualização do vínculo de representantes
- Exclusão de representantes
- Exclusão de pessoas cadastradas

## Guia de Onboarding

### Pré-requisitos

Liste tudo que o desenvolvedor precisa ter instalado antes de começar.

- Java 17
- Node.js 22.12 ou superior
- npm 10 ou superior
- MySQL 8 ou superior
- Git
- Docker Desktop

### Instalação e Configuração     

Passo a passo para obter o projeto localmente.

```bash
# 1. Clone o repositório
git clone https://github.com/felipefxrixs/crud-empresas-filiais.git
cd crud-empresas-filiais

# 2. Suba a aplicação
docker compose up --build

# 3. Os acessos do projeto
Frontend: http://localhost:9000
Backend: http://localhost:8080
```

### Como Executar os Testes

- Atualmente o projeto não possui testes automatizados implementados.

## Ondas de Desenvolvimento

Histórico de ondas de entrega do projeto:

| Onda | Nome                                             | Status       | Período           | Pasta                                                                 |
| ---- | ------------------------------------------------ | ------------ | ----------------- | --------------------------------------------------------------------- |
| 1    | Implementação do CRUD de Empresas e Filiais      | Entregue     | 2026-04 / 2026-04 | [_docs/1-Imp-CRUD-Empresas-Filiais/](escopo.md)   |

## Documentação Adicional

Para aprofundamento técnico, consulte os seguintes documentos:

- [Arquitetura do Sistema](\docs\ARCHITETURE.md) — Detalhes de arquitetura, stack e padrões de código.
- [Documentação do Projeto](\docs\) — Repositório centralizado de documentação técnica (arquitetura, requisitos, atas, testes e decisões por onda).
