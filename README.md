## 💻 Sobre o projeto

Aplicação de um e-commerce e seus microsserviços que se comunicam entre si por meio do sistema de mensageria Apache Kafka. Os microsserviços da aplicação são responsáveis por gerenciar um pedido feito pelo cliente, enviar e-mails, gerar relatórios, verificar fraudes, salvar dados no banco de dados, etc.

---
## 🔗 Microsserviços da aplicação:

* 🎲Common Database: Banco de dados em comum de todos os microsserviços;
* 📨Common Kafka: Classes para gerar consumidores e produtores, serializar e desserialziar payloads das mensagens, envelopar as mensagens com a classe Message, etc;
* 📧Email New Order: Gera e-mails após um pedido de compra;
* ✉️Email: Também responsável por gerar e-mails (veio antes da Email New Order);
* 👮Fraud: Verificação de fraude nos pedidos de compras gerados;
* 🛜HTTP e-commerce: Responsável pelas entradas HTTP da aplicação;
* ✍️Log: Gerar log para todas as mensagens produzidas na aplicação;
* 🏬New Order: Gerar vários pedidos;
* 📄Reading Report: Gerar relatórios;
* 🧑Service Users: Criar novos usuários no banco e salvar os off sets dos pedidos (cuidando da idempotência da aplicação).

---
## 🛠 Stack utilizada
As seguintes tecnologias foram utilizadas no desenvolvimento do projeto:
* `Java` v.21
* `Apache Kafka` v.2.12
* `Sqlite`
* `Maven`
* `GSON`
* `Jetty servlet` v.11.0.24
---

## ⚙️ Endpoints

A aplicação expõe os seguintes *endpoints* a partir da *base URL* `localhost:8080`:
* `GET /new` - Gerar um novo pedido de compra
* `GET /admin/generate-reports` - Gerar relatórios para todos os usuários cadastrados
