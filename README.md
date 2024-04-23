# simple-crud-javafx-jdbc

Projeto simples de uma aplicação CRUD padrão MVC que utiliza JavaFX para criar a interface e MySQL como banco de dados.

#### Stack

- Java JDK 20
- MySQL
- Scene Builder
- JavaFX
- Java JDBC

#### Requisitos

- Java JDK 20
- MySQL
- JavaFX SDK
- MySQL Connector

#### Instalação

Siga estas etapas para configurar e executar o projeto:

##### 1. Clonar o repositório

```bash
git clone https://github.com/ArthurRod/simple-crud-javafx-jdbc.git
```

##### 2. Inicie sua IDE

##### 3. Baixar e instalar as SDK's na sua IDE

    3.1 - Baixe o JavaFX sdk de acordo com a versão da sua jdk
    3.2 - Baixe o MySQLConnector
    3.3 - Extraia os arquivos baixados para alguma pasta. ex: C:\JavaLibs
    3.4 - Crie uma nova user library e adicione os Jar's do JavaFX sdk. ex: "C:\JavaLibs\javafx-sdk-20\lib"
    3.5 - Crie uma nova user library, adicione os Jar's do MySQLConnector e salve. ex: "C:\JavaLibs\mysql-connector-j-8.3.0"

##### 4. Abra o projeto clonado em sua IDE e adicione as user libraries criadas nos passos "3.4" e "3.5" como dependencias

##### 5. Crie um banco de dados utilizando o MySQL Workbench e crie as tabelas utilizando o arquivo database.sql

##### 6. Crie um arquivo chamado db.properties de acordo com o exemplo fornecido em db.properties.example (Ajuste conforme o banco de dados que foi criado no passo "5")

##### 7. Ajuste as configurações de execução do seu projeto

    7.1 - Exemplo (Eclipse): Botão direito no projeto -> Run As -> Run Configurations -> Arguments -> VM Arguments
    7.2 - Copie e cole o argumento abaixo

```bash
--module-path "C:\java-libs\javafx-sdk\lib" --add-modules=javafx.fxml,javafx.controls
```

    4.3 - Ajuste o caminho da pasta "C:\java-libs\javafx-sdk\lib" de acordo com a pasta que foram extraídos os arquivos do JavaFX sdk e salve

##### 8. Execute o projeto

Obs: Caso for utilizar o Scene Builder para editar a interface, é necessário fazer o download e apontar a pasta que o .exe dele está localizado na sua IDE
