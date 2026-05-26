# Serviço Auditor de DLQ (servico_auditor_dlq) 🏛️

Este serviço é um componente de apoio assíncrono e independente desenvolvido em **Spring Boot**, cuja responsabilidade principal é consumir com segurança as mensagens de falha estacionadas em uma fila de mensagens mortas (**DLQ - Dead Letter Queue** da AWS SQS), realizar uma triagem baseada na severidade do erro e persistir os detalhes em um banco de dados para auditoria posterior.

---

## 🏛️ Decisão Arquitetural & Justificativa

Por se tratar de um serviço de apoio e afim de evitar uma complexidade desnescessária, optou-se por implementar uma **Arquitetura em Camadas (Layered Architecture)** adaptada para serviços do tipo *Worker/Consumer*, inspirada nos princípios de segregação de responsabilidades do ecossistema Spring/MVC.

### Prevenção ao *Overengineering* (Complexidade Desnecessária)
Padrões como a *Hexagonal Architecture (Ports and Adapters)* ou *Clean Architecture* são excelentes para sistemas centrais complexos, onde as regras de negócio mudam frequentemente e precisam ser totalmente isoladas de frameworks externos. No entanto, para um microsserviço de auditoria focado em uma única responsabilidade (consumir, triar e persistir), aplicar esses padrões introduziria:
* Excesso de interfaces e mapeadores de dados (mappers) redundantes.
* Uma curva de aprendizado artificial que não traria retorno prático.
* Maior volume de código para manter, sem ganho real de performance ou flexibilidade.
  
### A Adaptação em Camadas (Layered)
Para manter o projeto limpo, altamente testável e dentro das boas práticas do mercado, a estrutura foi dividida em três responsabilidades claras:

1. **Camada de Entrada (Consumer):** O ponto de entrada (gatilho) da aplicação. Substitui o Controller tradicional pelo `@SqsListener`, que faz o polling ativo e assíncrono na AWS.
2. **Camada de Negócio (Service / Domain):** Onde reside o "coração" do sistema. É aqui que é executada a lógica da Triagem de Severidade.
3. **Camada de Dados (Model / Repository):** Onde as entidades do banco de dados são mapeadas e os métodos abstratos de persistência são expostos pelo Spring Data JPA.

---

## 📂 Estrutura de Pastas (Packages)

A organização das classes segue estritamente a divisão de responsabilidades da arquitetura escolhida:

```text
src/main/java/com/gabrielsalvador/servico_auditor_dlq/
│
├── consumer/
│   └── DlqConsumer.java             # Escuta a DLQ, captura a String e gerencia o Acknowledge.
│
├── service/
│   └── AuditService.java            # Executa a regra de negócio (Triagem de Severidade).
│
├── model/
│   └── AuditLog.java                # Entidade JPA que define o contrato da tabela de auditoria.
│
├── repository/
│   └── AuditLogRepository.java      # Interface de persistência de dados (Spring Data JPA).
│
└── dto/
    ├── OrderEventDto.java           # Transportador imutável do evento principal da fila.
