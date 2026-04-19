# Escopo — Onda 1: Implementação do CRUD de Empresas e Filiais

- **Número da Onda:** 1
- **Nome:** Implementação do CRUD de Empresas e Filiais
- **Status:** Entregue
- **Período:** 2026-04-15 a 2026-04-19
- **Responsável técnico:** Felipe Farias
- **Repositório / Branch:** `feature/onda-1-Imp-CRUD-Empresas-Filiais`

## Objetivo

Desenvolver um sistema CRUD para gerenciamento de empresas e filiais, permitindo operações de cadastro, consulta, atualização e exclusão, além do relacionamento entre empresa sede e suas filiais.

## Funcionalidades Planejadas

- [ ] Cadastro de empresa sede — `RF-001`
- [ ] Atualização de empresa sede— `RF-002`
- [ ] Exclusão de empresa sede — `RF-003`
- [ ] Cadastro de filial vinculada a uma empresa sede — `RF-004`
- [ ] Listagem de empresas sede e filiais — `RF-005`
- [ ] Atualização de filial — `RF-006`
- [ ] Exclusão de filial — `RF-007`
- [ ] Realocação de empresa sede — `RF-008`
- [ ] Cadastro de pessoas — `RF-009`
- [ ] Vínculo de pessoas a uma empresa (representantes) — `RF-010`
- [ ] Listagem de pessoas cadastradas — `RF-011`
- [ ] Atualização de dados das pessoas cadastradas — `RF-012`
- [ ] Atualização do vínculo de representantes — `RF-013`
- [ ] Exclusão de representantes — `RF-014`
- [ ] Exclusão de pessoas cadastradas — `RF-015`

## Critérios de Aceite da Onda

Condições que devem ser atendidas para que esta onda seja considerada concluída.

1. Os dados estiverem sendo persistidos corretamente no banco de dados.
2. O frontend estiver integrado com o backend e exibindo os dados corretamente.
3. O relacionamento entre empresa sede e filial estiver funcionando corretamente.
4. O vínculo de pessoas como representantes das empresas estiver funcionando corretamente.

## Dependências

- Não há dependências externas nesta onda.
- Dependência interna: MySQL, Java 17 e Node.js 22.12+

## Documentos Relacionados

- [Arquitetura do Projeto](..\ARCHITETURE.md)
- [Atas de Reunião](\ata.md)
