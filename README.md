Projeto DAO com JDBC
Este projeto demonstra a implementação do padrão de design DAO (Data Access Object) em Java, usando JDBC para realizar operações de CRUD no banco de dados. A abordagem DAO permite isolar a lógica de acesso a dados da lógica de negócio, facilitando a manutenção, a escalabilidade e a reutilização de código.

Estrutura do Projeto
O projeto está organizado com as seguintes camadas principais:

Modelo (ou Entidade): Contém classes que representam a estrutura dos dados no banco de dados. Cada classe de entidade reflete uma tabela.
Interface DAO: Define operações que serão realizadas no banco, como salvar, atualizar, excluir e buscar dados.
Implementação do DAO: Implementa os métodos definidos na interface DAO, utilizando JDBC para executar as operações de CRUD.
Classe de Conexão com o Banco de Dados: Gerencia a abertura e fechamento das conexões com o banco.
Tecnologias Utilizadas
Java JDK 10 ou superior
JDBC para acesso a banco de dados
MySQL (ou outro banco de dados relacional)
IDE: IntelliJ IDEA, Eclipse ou outra compatível






