# Sistema de Gerenciamento de Doações para Desabrigados de Enchentes

## Descrição

Este projeto é um sistema backend em Java desenvolvido para auxiliar na gestão de doações e abrigos em situações de enchentes. O sistema permite o registro de doações, cadastro de abrigos, criação de ordens de pedido, checkout de itens e transferência de doações entre centros de distribuição.

## Requisitos

- Java 17: Certifique-se de ter o Java Development Kit (JDK) 17 instalado. Você pode verificar a versão do Java instalada com o comando `java -version`.
- Maven: Instale a última versão do Maven. Você pode verificar se o Maven está instalado com o comando `mvn -v`.
- Git: Instale o Git para clonar o repositório do projeto. Verifique se o Git está instalado com o comando `git --version`.
- MySQL: Instale o MySQL (ou outro banco de dados compatível com JPA) na sua máquina.

## Instruções de Uso

1. Clone o repositório com o Git.
```bash
git clone https://github.com/ReinanPL/donation-hub.git
```

2. Configurar o Banco de Dados:
Crie um banco de dados MySQL (ou outro de sua escolha).
Configure as informações de conexão com o banco de dados no arquivo persistence.xml.

3. Compilar e Executar:
- Usando uma IDE (ex: Eclipse, IntelliJ):

Importe o projeto como um projeto Maven.
Execute a classe principal Program.java.
