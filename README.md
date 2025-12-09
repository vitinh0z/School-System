# 🏫 Sistema Escolar (School System)

> **Desafio Técnico Fullstack** - Simulação de um Relatório Operável para Instituições de Ensino.

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.0-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Bootstrap](https://img.shields.io/badge/Bootstrap-5-563D7C?style=for-the-badge&logo=bootstrap&logoColor=white)
![Javascript](https://img.shields.io/badge/JavaScript-ES6-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)

## 📋 Índice

- [Visão Geral](#-visão-geral)
- [Funcionalidades](#-funcionalidades)
- [Tecnologias](#-tecnologias)
- [Arquitetura e Decisões Técnicas](#-arquitetura-e-decisões-técnicas)
- [Como Rodar o Projeto](#-como-rodar-o-projeto)
- [Documentação da API](#-documentação-da-api)
- [Estrutura do Banco de Dados](#-estrutura-do-banco-de-dados)
- [Contribuição](#-contribuição)

---

## 🔍 Visão Geral

O **School System** é uma solução fullstack desenvolvida para resolver problemas de alocação e distribuição em escolas. O sistema foca na gestão eficiente de alunos, turmas e grade horária de professores, oferecendo visualização de dados estatísticos em tempo real.

O projeto foi construído com foco em **Performance** (otimização de renderização no front-end) e **Integridade de Dados** (validações robustas no back-end).

---

## ✨ Funcionalidades

### 🎓 Módulo de Alunos (Tela 1)
- **Dashboard Estatístico:** Gráfico de pizza interativo mostrando a distribuição de alunos por série.
- **Gerador de Massa (Seeder):** Algoritmo capaz de gerar e distribuir 300+ alunos aleatoriamente entre as turmas com um clique.
- **Gestão Completa:**
  - Cadastro unitário de alunos.
  - Edição de nome e remanejamento de turma.
  - Exclusão de registros.
- **Filtros Dinâmicos:** Filtragem em tempo real por Série (Degree) e Turma (Class).

### 👨‍🏫 Módulo de Grade Horária (Tela 2)
- **Gestão de Professores:** Cadastro rápido de novos docentes e suas especialidades.
- **Agendamento de Aulas:** Criação de relacionamentos entre Professor, Matéria e Turma.
- **Visualização Hierárquica:** Tabela clara mostrando quem dá aula, de quê, para qual turma.
- **Visualização de Alunos ("Drill-down"):** Botão para visualizar a lista de alunos matriculados na série daquele professor específico.

---

## 🛠️ Tecnologias

### Backend
- **Linguagem:** Java 17
- **Framework:** Spring Boot 3 (Web, Data JPA, Validation)
- **Banco de Dados:** H2 Database (Em memória, para agilidade nos testes)
- **Ferramentas:** Lombok (Redução de boilerplate), Maven.

### Frontend
- **Linguagem:** JavaScript Puro (Vanilla JS) - *Sem frameworks pesados.*
- **Estilização:** Bootstrap 5.3 (Responsividade e Componentes).
- **Gráficos:** Chart.js.
- **Comunicação:** Fetch API para consumo dos endpoints REST.

### Por que Spring Boot?
A escolha foi estratégica para o desafio:
1.  **Produtividade:** A configuração automática permitiu focar 100% na regra de negócio (distribuição de turmas).
2.  **Embedded Server:** O Tomcat embutido facilita a execução da aplicação em qualquer ambiente sem instalações complexas.
3.  **Ecossistema:** A integração nativa com JPA simplificou a modelagem complexa dos relacionamentos (Many-to-One).

---

## 🧩 Arquitetura e Decisões Técnicas

### 1. Database Seeder Inteligente (`DatabaseSeeder.java`)
Havia um desafio lógico nos dados originais: distribuir alunos entre 13 Séries diferentes tendo apenas 6 nomes de Turmas disponíveis.
* **Solução:** Implementação de uma lógica de **Round Robin** (usando o operador resto `%`). Isso garante que todas as séries recebam turmas e nenhum aluno fique "órfão" ou sem classe no gráfico.

### 2. Otimização de Renderização (HTML Buffer)
Para evitar travamentos no navegador (*freezing*) ao renderizar listas grandes (ex: geração de 900+ alunos), foi utilizada a técnica de **Buffer de String** no JavaScript.
* **Solução:** Em vez de manipular o DOM a cada linha (`innerHTML +=` que causa *Reflow*), o HTML da tabela é montado inteiramente na memória e injetado na tela uma única vez.

---

## 📦 Como Rodar o Projeto

### Pré-requisitos
- Java 17 instalado.
- Maven (opcional, o projeto possui wrapper).
- Porta 8080 livre.

### Passo a Passo

1. **Clone o repositório**
   ```bash
   git clone [https://github.com/seu-usuario/School-System.git](https://github.com/seu-usuario/School-System.git)
   cd School-System

   ## ⚙️ Execute a aplicação

* **Via IntelliJ/Eclipse:** Abra o arquivo `SchoolSystemApplication.java` e clique em ▶️ **Run**.
* **Via Terminal:**
    ```bash
    ./mvnw spring-boot:run
    ```

### Acesse no Navegador
* **Tela de Alunos:** [http://localhost:8080/index.html](http://localhost:8080/index.html)
* **Tela de Professores:** [http://localhost:8080/teachers.html](http://localhost:8080/teachers.html)
* **Console do Banco H2:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

---

## 📚 Documentação da API

Abaixo estão exemplos de como testar os endpoints principais via Postman ou Insomnia.

### 🎓 Students (Alunos)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/students/all` | Lista todos os alunos. |
| `POST` | `/students/create` | Cria um único aluno. |
| `POST` | `/students/generate` | Gera 300 alunos aleatórios. |
| `PUT` | `/students/update/{id}` | Atualiza dados do aluno. |
| `DELETE` | `/students/delete/{id}` | Remove um aluno. |

**JSON para Criar Aluno (POST):**
```json
{
  "name": "Novo Aluno Exemplo",
  "classId": 5
}
