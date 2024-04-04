![logo.png](/images/logo.png)
 <h1 align="center"> Mini Projeto 02 </h1>

## ✒️ Desenvolvedores
- Daniel Correia Araujo
- James Garcia Lucas
- Pâmela Vieira da Silva

## ✏️ Descrição
Este projeto foi desenvolvido conforme as especificações passadas pelo professor para realização do segundo
mini projeto da turma FullStack Education.
O objetivo é ser um sistema simples para cálculo da média final dos alunos em diferentes disciplinas.

## 📌 Como usar
1. Clone este repositório: [https://github.com/Bruno-FMT/FullStack-Education--P1.git](https://github.com/Bruno-FMT/FullStack-Education--P1.git)
2. Crie um container no docker: 
    ```bash
       docker run -d  --name meu-postgres-container  -e POSTGRES_PASSWORD=minhaSenha  -e POSTGRES_USER=meuUsuario  -e POSTGRES_DB=meuBancoDeDados  -p 1432:5432  postgres:latest
    ```
3. Execute a aplicação na Main que está na raiz do projeto.

## 🚀 Entidades do projeto
### Aluno
- Id
- Nome
- Nascimento

### Professor
- ID
- Nome

### Disciplina
- Id
- Nome
- Professor_Id

### Disciplina Matricula
- ID
- Aluno_Id
- Disciplina_Id
- Data_Matricula
- Media_Final

### Notas
- Id
- Disciplina_Matricula_Id
- Nota
- Coeficiente

---

## 🎯 Endpoints
### Create
- POST ```/alunos```
  - RequestBody:
    ```Json
        {
            "nome": "dummy",
            "nascimento": "2024-04-02"
        }
    ```
- POST ```/professores```
    - RequestBody:
      ```Json
          {
              "nome": "dummy"
          }
      ```
- POST ```/diciplinas```
    - RequestBody:
      ```Json
          {
              "nome": "dummy",
              "professorId": 1
          }
      ```
- POST ```/matriculas```
    - RequestBody:
      ```Json
          {
              "alunoId": 1,
              "disciplinaId": 1
          }
      ```
- POST ```/notas```
    - RequestBody:
      ```Json
          {
              "disciplinaMatriculaId": 1,
              "professorId": 1,
              "nota": 10,
              "coeficiente": 0.4
          }
      ```
## Read
Buscar todos:
- GET ```/alunos```
- GET ```/professores```
- GET ```/diciplinas```
- GET ```/matriculas```
- GET ```/notas```

Buscar por Id:
- GET ```/alunos/:id```
- GET ```/professores/:id```
- GET ```/diciplinas/:id```
- GET ```/matriculas/:id```
- GET ```/notas/:id```

Buscar por Aluno:
- GET ```/matriculas/alunos/:alunoId```
- GET ```/matriculas/alunos/:alunoId/media-geral```

Buscar por Disciplina:
- GET ```/matriculas/disciplinas/:disciplinaId```

Buscar por Matrícula:
- GET ```/notas/matriculas/:matriculaId```

## Update
- PUT ```/alunos/:id```
    - RequestBody:
      ```Json
          {
              "nome": "dummy",
              "nascimento": "2024-04-02"
          }
      ```
- PUT ```/professores/:id```
    - RequestBody:
      ```Json
          {
              "nome": "dummy"
          }
      ```
- PUT ```/diciplinas/:id```
    - RequestBody:
      ```Json
          {
              "nome": "dummy",
              "professorId": 1
          }
      ```
- PUT ```/matriculas/:id```
    - RequestBody:
      ```Json
          {
              "alunoId": 1,
              "disciplinaId": 1
          }
      ```
- PUT ```/notas/:id```
    - RequestBody:
      ```Json
          {
              "disciplinaMatriculaId": 1,
              "professorId": 1,
              "nota": 10,
              "coeficiente": 0.4
          }
      ```

## Delete
- DELETE ```/alunos/:id```
- DELETE ```/professores/:id```
- DELETE ```/diciplinas/:id```
- DELETE ```/matriculas/:id```
- DELETE ```/notas/:id```

--- 
## 📋 Todo List
- [x] [Ex1 - Setup do projeto de notas](#-ex1---setup-do-projeto-de-notas)
- [x] [Ex 2 - CRUD Alunos](#-ex-2---crud-alunos)
- [x] [Ex 3 - CRUD Professores](#-ex-3---crud-professores)
- [x] [Ex 4 - CRUD Disciplinas](#-ex-4---crud-disciplinas)
- [x] [Ex 5 - Matricular alunos](#-ex-5---matricular-alunos)
- [x] [Ex 6 - Lançamento de nota](#-ex-6---lançamento-de-nota)
- [ ] [Ex 7 - Média geral do aluno](#-ex-7---média-geral-do-aluno)

## 📂 Descrição dos exercícios
### 📖 Ex1 - Setup do projeto de notas
Desenvolva um sistema simples para cálculo da média final dos alunos em diferentes disciplinas.<br/>
A plataforma deve conter:
- Matrícula de alunos em disciplinas;
- Lançamento de notas;
- Cálculo de média final de cada aluno por disciplina.
Para uma especificação básica das entidades, siga o seguinte [diagrama](/images/diagrama.pdf).

### 📖 Ex 2 - CRUD Alunos
Criar CRUD para a entidade Aluno.<br/>
Utilize adequadamente os padrões REST, MVC e os Logs.<br/>
Não se esqueça do tratamento de exceções, status de resposta e seus métodos HTTP.

### 📖 Ex 3 - CRUD Professores
Criar CRUD para a entidade Professor.<br/>
Utilize adequadamente os padrões REST, MVC e os Logs.<br/>
Não se esqueça do tratamento de exceções, status de resposta e seus métodos HTTP.

### 📖 Ex 4 - CRUD Disciplinas
Criar CRUD para a entidade Disciplina.<br/>
Utilize adequadamente os padrões REST, MVC e os Logs.<br/>
Não se esqueça do tratamento de exceções, status de resposta e seus métodos HTTP.

### 📖 Ex 5 - Matricular alunos
1. Matricular Alunos em Disciplinas.
2. Métodos disponíveis no controlador:
- POST:
  - Deve receber no body apenas os IDs de aluno e disciplina. 
  - Os demais campos devem ser utilizados valores padrões.
- DELETE:
  - Deve receber apenas o id no PathVariable;
  - Validar se existe notas já lançadas:
    - Caso exista, informar a falha ao cliente;
    - Caso NÃO exista, deve excluir a matrícula.
- GET Por ID:
  - Deve receber apenas o id no PathVariable;
  - Retornar uma matrícula que tenha o ID informado.
- GET Por aluno:
  - Deve receber apenas o id de aluno no PathVariable;
  - Retornar todas as matrículas pertencentes a um aluno.
- GET Por disciplina:
  - Deve receber apenas o id da matrícula no PathVariable
  - Retornar todas as matrículas pertencentes a uma disciplina

1. Utilize adequadamente os padrões REST, MVC e os Logs;
2. Não se esqueça do tratamento de exceções, status de resposta e seus métodos HTTP.

### 📖 Ex 6 - Lançamento de nota
1. Lançamento de notas;
2. Métodos disponíveis no controlador:
- POST:
  - Deve receber no body:
    - ID da matrícula;
    - nota;
    - coeficiente.
  - O professor deve ser atribuído o mesmo existente na disciplina;
  - Ao incluir uma nova nota, a média final da matrícula deve ser atualizada automaticamente:
    - Atenção: Ao calcular a média final, respeite sempre o coeficiente informado;
    - Exemplo:
      - NOTAS:
        - nota1: 5,0 → Coeficiente: 0.4;
        - nota2: 8,0 → Coeficiente: 0.4;
        - nota3: 1,0 → Coeficiente: 0.2.
      - MÉDIA FINAL: 5,4
- DELETE:
  - Deve receber apelas o id no PathVariable;
  - Ao excluir nota, a média final da matrícula deve ser atualizada automaticamente:
    - Atenção: Ao calcular a média final; respeite sempre o coeficiente informado.
    - Exemplo:
      - NOTAS:
        - nota1: 5,0 → Coeficiente: 0.4;
        - nota2: 8,0 → Coeficiente: 0.4;
        - nota3: 1,0 → Coeficiente: 0.2 (Excluído);
      - MÉDIA FINAL: 5,2
- GET Por matrícula:
  - Deve receber apelas o id da matrícula no PathVariable;
  - Retornar as notas da matrícula que tenha o ID informado;
1. Utilize adequadamente os padrões REST, MVC e os Logs;
2. Não se esqueça do tratamento de exceções, status de resposta e seus métodos HTTP.

### 📖 Ex 7 - Média geral do aluno
1. Calcular a média geral de um aluno.
2. Criar no controlador de matrículas:
- Novo método GET Por aluno:
  - Deve receber apelas o id do aluno no PathVariable;
  - A média deverá ser calculada com a fórmula comum de média: (n1 + n2 + n3 ......)/quantidadeNotas
  - Retornar um DTO com apenas a média geral do aluno em todas as disciplinas.
    - Exemplo:
      - Disciplina 1: Média final 8,0;
      - Disciplina 2: Média final 6,0;
      - Disciplina 3: Média final 7,0;
      - Média geral: 7,0
1. Utilize adequadamente os padrões REST, MVC e os Logs.
2. Não se esqueça do tratamento de exceções, status de resposta e seus métodos HTTP.
