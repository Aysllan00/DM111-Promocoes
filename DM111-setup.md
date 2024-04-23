# Pre requisitos e instalações

## Premissa

Todos os softwares necessários para essa disciplina são softwares de código aberto ou livres para uso.

## Listas de softwares

1. Open JDK 17
2. Maven
3. IntelliJ Community Edition (Free)
4. Postman
5. Google Could SDK

## Tutorial para instalação 

Para uma configuração bem sucedida, peço que sigam os passos descritos abaixo para cada um dos softwares.

### Open JDK 17

Para realizar a instalação do Open JDK é necessário baixar o arquivo [.zip](https://jdk.java.net/archive/) que contém todos os arquivos binários necessários para a execução de códigos escritos na linguagem de programação Java. Por motivos de compatibilidade com o Google App Engine, a versão adequada é o Java 17.

Após completar o download o usuário deve descompactar o aqruivo .zip baixado em um diretório voltado para desenvolvimento, por exemplo: C:\desenvolvimento\java, ou no diretório que o usuário desejar.

Feito isso, é necessário adicionar o diretório cujo o arquivo .zip fora extraído na variável de sistema chamada PATH.
Isso nos permitirá a execução de códigos Java de qualquer diretório do Sistema Operacional.

#### Para configurar JAVA_HOME em um sistema Windows, execute as seguintes ações.

- No Painel de Controle:
- Clique em Sistema.
- Clique em Configurações avançadas do sistema.
- A janela Propriedades do Sistema será aberta.
- Clique no botão Variáveis de Ambiente .
- Clique no botão Novo na seção de variáveis do sistema.
- Adicionar o nome da variável JAVA_HOME e especificar um caminho para o diretório do jdk extraído. 
Por exemplo: C:\desenvolvimento\java\jdk
Alguns comandos do controlador coletivo requerem que o caminho para o diretório de instalação do Java jdk\bin esteja disponível no caminho do sistema, portanto, inclua também um caminho no diretório jdk\bin.

Para validar a instalação abra uma nova instância (janela) do prompt de comandos e digite o seguinte comando:

```command
java --version
```

### Maven

Para configurar o maven (gerenciador de dependências para aplicações Java) devemos baixar o arquivo contendo todos os binários para a execução de comandos maven.

Para realizar o download acesse o seguinte [link](https://maven.apache.org/download.cgi) e selecione **apache-maven-3.9.6-src.zip** o download irá inciar automaticamente.

Após completar o download, deve-se adicionar o diretório onde o aqruivo que fora baixado foi extraído e descomprimido. Para tanto, deve-se seguir os mesmos passos realizados na variável PATH para o Java, porém adicionando o diretório da pasta onde o Maven foi extraído.

Para validar a instalação abra uma nova instância (janela) do prompt de comandos e digite o seguinte comando:

```command
mvn --version
```

### Intellij IDEA Community version

Para instalar o Intellij IDEA para nos ajudar na escrita do código na linguagem Java, basta acessar o seguinte [link](https://www.jetbrains.com/idea/download/other.html) e clicar no link de acordo com a plataforma desejada. Por exemplo: **Windows X64 (exe)**.

Após a realização do download, basta executar o arquivo baixado e seguir o procedimento padrão para instalação de qualquer outro software (Next, Next, Next, Finish).

### Postman

Para realizarmos os testes de nosso webservice será necessário um cliente para criarmos e enviarmos solicitações via uma API REST utilizando o protocolo HTTP. Para tanto, uma excelente ferramenta que podemos utilizar é o **Postman**, para realizar o downlaod desta aplicação use o seguinte [link](https://www.postman.com/downloads/) e seguir os passos padrões de instalação de qualquer outra software, isto é, Next, Next, Next, Finish.

### Google Cloud SDK

Ao final da disciplina, iremos deployar nossa aplicação no Google App Engine utilizando o Google Cloud SDK. Para realizar o download e a instalação do mesmo, basta seguir os passos descritos no seguinte [link](https://cloud.google.com/sdk/docs/install#windows). Sua instalação por padrão segue o mesmo formato de outros Softwares, isto é, Next, Next, Next, Finish.

Para validar a instalação abra uma nova instância (janela) do prompt de comandos e digite o seguinte comando:

```command
gcloud --version
```
