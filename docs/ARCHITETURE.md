# Arquitetura do CRUD Empresas e Filiais

> **Localização obrigatória deste documento:** `_docs/blueprint-doc-ARCHITETURE-model.md`

## 1. Visão Arquitetural

O sistema **CRUD Empresas e Filiais** adota uma arquitetura do tipo **monólito em camadas**, com separação clara entre frontend, backend e banco de dados.

No backend, a aplicação segue o padrão em camadas com Spring Boot, organizado em:

- **Controller**
- **Service**
- **Repository**
- **Entity**
- **DTO**

No frontend, a aplicação foi desenvolvida utilizando Vue.js com o framework Quasar, seguindo uma arquitetura baseada em componentes e páginas.

A arquitetura do frontend é estruturada da seguinte forma:

- **Pages**
- **Layouts**
- **Router**
- **Services**
- **Css**

A escolha dessa arquitetura foi feita por ser a mais adequada para um sistema CRUD mais simples, oferecendo:

- simplicidade de implementação;
- facilidade de manutenção;
- organização clara do código;

## 2. Stack de Tecnologias Detalhado

| Camada              | Tecnologia          | Versão | Justificativa                                                 |
| ------------------- | ------------------  | ------ | ------------------------------------------------------------- |
| Frontend            | Vue.js + Quasar     | 3.x    | Ferramenta solicitada no escopo das ferramentas do projeto.   |
| Backend             | Java + Spring Boot  | 17/3.x | Ferramenta solicitada no escopo das ferramentas do projeto.   |
| Banco de dados      | MySQL               | 8.x    | Ferramenta solicitada no escopo das ferramentas do projeto.   |
| Infraestrutura      | Ambiente local      | N/A    | O projeto foi desenvolvido para execução local.               |

## 3. Diagrama de Arquitetura (ASCII ou Mermaid)

Como o sistema possui uma arquitetura simples, foi adotado um diagrama em **ASCII** para representar os principais componentes da aplicação e sua comunicação.

+---------+
| Usuário |
+---------+
     |
     v
+---------------------------+
| Frontend                  |
| Vue.js + Quasar           |
+---------------------------+
     |
     v
+---------------------------+
| Backend                   |
| Java + Spring Boot        |
| Controller / Service /    |
| Repository / Entity       |
+---------------------------+
     |
     v
+---------------------------+
| Banco de Dados            |
| MySQL                     |
+---------------------------+

## 4. Estrutura de Pastas         

Descrição de cada diretório relevante do projeto e sua responsabilidade.

```filesystem
src/
├── domain/       # Regras de negócio puras (sem dependências externas)
├── application/  # Casos de uso e orquestração
├── infra/        # Integrações com banco de dados, APIs externas, etc.
└── presentation/ # Controllers, rotas e contratos de API
```

## 5. Componentes e Módulos   

### 5.1 Módulo Empresa

- **Responsabilidade:** gerenciar sedes, filiais, promoção de filial para sede e vínculos entre empresas.
- **Localização:** `empresas-filiais/src/main/java/br/com/crud/empresas_filiais/{controller,service,repository}/Empresa*`
- **Dependências:** `EmpresaRepository`, `TipoEmpresa`, entidade `Empresa`
- **Destaque:** concentra a regra de negócio mais rica do projeto.

### 5.2 Módulo Pessoa

- **Responsabilidade:** gerenciar cadastro e manutenção das pessoas físicas vinculáveis ao QSA.
- **Localização:** `empresas-filiais/src/main/java/br/com/crud/empresas_filiais/{controller,service,repository}/Pessoa*`
- **Dependências:** `PessoaRepository`, entidade `Pessoa`
- **Destaque:** implementa busca por CPF, nome e prevenção básica de duplicidade.

### 5.3 Módulo QSA

- **Responsabilidade:** vincular pessoas a empresas com um papel societário/administrativo.
- **Localização:** `empresas-filiais/src/main/java/br/com/crud/empresas_filiais/{controller,service,repository}/Qsa*`
- **Dependências:** `QsaRepository`, `EmpresaRepository`, `PessoaRepository`, `QsaRequestDTO`, `QsaResponseDTO`
- **Destaque:** é o único módulo já parcialmente desacoplado via DTOs.

### 5.4 Frontend de Cadastro

- **Responsabilidade:** oferecer a interface de busca, cadastro, edição e visualização das entidades do sistema.
- **Localização:** `empresas-front/src/pages/BuscaCadastroPage.vue`
- **Dependências:** `src/services/api.js`, Vue reactivity APIs, componentes do Quasar
- **Destaque:** concentra o fluxo principal da aplicação, mas hoje possui alto acoplamento e volume de código.

## 6. Padrões Adotados

- **Padrão de nomenclatura:** camelCase para variáveis, PascalCase para classes.
- **Padrão de commit:** Conventional Commits (`feat:`, `fix:`, `docs:`, `refactor:`)
- **Gerenciamento de estado:** estado local dos componentes (`ref`, `reactive`), sem store global
- **Tratamento de erros:** `RuntimeException` no backend e `Notify`/`console.error` no frontend

## 7. Arquivos Importantes  

| Arquivo                                                                                       | Descrição                                             |
| ----------------------------------------------------------------------------------------------| ----------------------------------------------------- |
| `empresas-filiais/pom.xml`                                                                    | Declara dependências, Java 17 e plugin do Spring Boot |
| `empresas-filiais/src/main/resources/application.properties`                                  | Configuração do datasource, Hibernate e porta da API  |
| `empresas-filiais/src/main/java/br/com/crud/empresas_filiais/EmpresasFiliaisApplication.java` | Ponto de entrada do backend                           |
| `empresas-front/package.json`                                                                 | Scripts e dependências do frontend                    |
| `empresas-front/quasar.config.js`                                                             | Configuração de build e dev server do Quasar          |
| `empresas-front/src/router/routes.js`                                                         | Rotas principais da SPA                               |
| `empresas-front/src/services/api.js`                                                          | Instância Axios com a base URL da API                 |
| `empresas-front/src/pages/BuscaCadastroPage.vue`                                              | Tela central do sistema                               |

## 8. Guia de Implementação    

package br.com.crud.empresas_filiais.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "categoria_empresa")
public class CategoriaEmpresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}

### Exemplo: Criando um novo caso de uso

package br.com.crud.empresas_filiais.repository;

import br.com.crud.empresas_filiais.entity.CategoriaEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaEmpresaRepository extends JpaRepository<CategoriaEmpresa, Long> {
    Optional<CategoriaEmpresa> findByNome(String nome);
}

## 9. Dependências Externas e Integrações

N/A

## 10. Histórico de Ondas

Registro cronológico das ondas de desenvolvimento e as principais mudanças arquiteturais introduzidas em cada uma.

- **Onda 1 - Implementação do CRUD de Empresas e Filiais**
  - **Principais Alterações Arquiteturais:** 
  - Definição da arquitetura em camadas (Controller, Service, Repository, Entity).
  - Implementação do backend utilizando Spring Boot.
  - Estruturação do banco de dados relacional com MySQL.
  - Implementação do relacionamento entre empresa Sede e filial.
  - Implementação do relacionamento entre empresa e Pessoa.
  - Criação da estrutura do frontend com Vue.js e Quasar.
  - Integração entre frontend e backend.

  - **ADRs Relacionados:** [ADR-001](\1-mvp\adr.md)

## 11. Diretriz de Diagramas

Para a documentação deste projeto, foi adotado o uso de diagramas **ASCII** por ser um projeto simples.
