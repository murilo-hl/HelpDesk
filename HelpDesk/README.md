# HelpDesk

**Sistema de Gerenciamento de Chamados de TI**

O **HelpDesk** é um sistema desktop desenvolvido em Java para facilitar
o registro, acompanhamento e gerenciamento de chamados de suporte
técnico.

O sistema permite que usuários registrem problemas, técnicos acompanhem
e atendam às solicitações e administradores gerenciem os usuários e seus
acessos.

O projeto foi desenvolvido utilizando Java Swing, com conexão a um banco
de dados MySQL.

## Funcionalidades

### Autenticação

-   Login utilizando nome de usuário e senha cadastrados no banco de
    dados.
-   Identificação do perfil de cada usuário.
-   Acesso às funcionalidades de acordo com o perfil.

### Gerenciamento de chamados

-   Cadastro de novos chamados.
-   Visualização dos chamados registrados.
-   Atualização das informações dos chamados.
-   Alteração do status do atendimento.
-   Registro da solução do problema.

### Gerenciamento de usuários

-   Cadastro de novos usuários.
-   Definição do perfil de acesso.
-   Controle dos usuários cadastrados.
-   Consulta dos usuários existentes.

## Perfis de acesso

O sistema possui três perfis, cada um com diferentes permissões:

  -----------------------------------------------------------------------
  Perfil                              Permissões
  ----------------------------------- -----------------------------------
  **Administrador**                   Visualiza todos os chamados,
                                      cadastra usuários e define seus
                                      perfis de acesso.

  **Técnico**                         Visualiza todos os chamados, pode
                                      assumir atendimentos, atualizar
                                      status e registrar soluções.

  **Usuário**                         Pode cadastrar chamados e
                                      visualizar somente os chamados
                                      registrados por ele.
  -----------------------------------------------------------------------

## Tecnologias utilizadas

-   **Java:** linguagem utilizada no desenvolvimento.
-   **Java Swing:** criação das interfaces gráficas.
-   **MySQL:** armazenamento dos dados do sistema.
-   **JDBC:** conexão entre a aplicação Java e o banco de dados.
-   **Maven:** gerenciamento do projeto e de suas dependências.
-   **NetBeans:** ambiente de desenvolvimento utilizado.

## Estrutura do projeto

O projeto está organizado utilizando o padrão MVC
(Model-View-Controller), separando as responsabilidades de cada parte do
sistema.

``` text
HelpDesk/
├── pom.xml
├── db.sql
├── README.md
└── src/
    ├── Main.java
    ├── model/
    │   ├── Usuario.java
    │   ├── Chamado.java
    │   ├── Perfil.java
    │   ├── Prioridade.java
    │   └── Status.java
    ├── view/
    │   ├── TelaLogin.java
    │   ├── TelaPrincipal.java
    │   ├── TelaChamados.java
    │   ├── TelaUsuarios.java
    │   └── Util.java
    ├── controller/
    │   ├── UsuarioController.java
    │   ├── ChamadoController.java
    │   └── Sessao.java
    ├── dao/
    │   ├── UsuarioDAO.java
    │   └── ChamadoDAO.java
    └── database/
        └── Conexao.java
```

### Organização das pastas

-   **model:** classes que representam os dados e objetos do sistema.
-   **view:** telas e componentes da interface gráfica.
-   **controller:** responsáveis por receber as ações da interface e
    executar as operações correspondentes.
-   **dao:** responsáveis pelas operações de consulta, cadastro,
    atualização e exclusão no banco de dados.
-   **database:** configuração e conexão com o banco de dados.

## Como executar o projeto

### 1. Requisitos

Antes de executar, é necessário ter instalado:

-   Java JDK 21 ou superior.
-   MySQL Server.
-   Apache Maven.
-   NetBeans ou outra IDE compatível com projetos Maven.

### 2. Configuração do banco de dados

1.  Inicie o MySQL Server.
2.  Abra o MySQL Workbench ou outro cliente MySQL.
3.  Execute o arquivo `db.sql`, localizado na raiz do projeto.

Esse arquivo cria o banco de dados `helpdesk` e suas tabelas, além de
inserir os dados iniciais necessários para utilizar o sistema.

### 3. Configuração da conexão

Abra o arquivo:

``` text
src/database/Conexao.java
```

Configure as informações de acesso ao seu MySQL:

``` java
private static final String URL = "jdbc:mysql://localhost:3306/helpdesk";
private static final String USUARIO = "root";
private static final String SENHA = "sua_senha";
```

Substitua `sua_senha` pela senha configurada no seu MySQL.

### 4. Execução

É possível executar o projeto de duas maneiras:

**Pelo NetBeans:** 1. Abra o NetBeans. 2. Selecione
`File > Open Project`. 3. Escolha a pasta `HelpDesk`. 4. Execute o
projeto pela opção `Run Project`.

**Pelo terminal:**

Na pasta raiz do projeto, execute:

``` bash
mvn clean compile
mvn exec:java
```

## Logins de exemplo

Após executar o arquivo `db.sql`, os seguintes usuários podem ser
utilizados para testar o sistema:

  Usuário   Senha      Perfil
  --------- ---------- ---------------
  admin     admin123   Administrador
  tecnico   1234       Técnico
  maria     1234       Usuário
  joao      1234       Usuário

Os usuários de exemplo permitem testar as diferentes permissões e
funcionalidades do sistema.

## Observações

Este projeto foi desenvolvido com finalidade educacional, buscando
aplicar na prática conceitos de programação orientada a objetos,
interfaces gráficas com Java Swing, organização MVC, operações CRUD e
integração com banco de dados utilizando JDBC.

As senhas são armazenadas em texto puro no banco de dados para
simplificar a implementação acadêmica. Essa abordagem não é recomendada
para aplicações reais, nas quais devem ser utilizados mecanismos seguros
de armazenamento de senhas.

------------------------------------------------------------------------

**HelpDesk --- Sistema de Gerenciamento de Chamados de TI.**
