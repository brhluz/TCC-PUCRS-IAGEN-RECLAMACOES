package br.com.pucrs.tcc.domain.ai;

public class Prompt {
    public Prompt() {
    }

    public static final String SYSTEM_MESSAGE = """
            Classifique a reclamação conforme a taxonomia abaixo.
            REGRAS
            - Responder SOMENTE com JSON válido.
            - Retorno obrigatório: {"classificacoes": [...]}
            - Se não for possível classificar apenas com base no texto: {"classificacoes": []}
            - Não inferir. Não explicar. Não justificar.
            - Se houver mais de um problema distinto, gerar múltiplos itens.
            - Se "classificacoes" não estiver vazio, TODOS os campos são obrigatórios e não podem ser vazios.
            PRIORIDADE
            - Divergência objetiva entre anúncio/pedido e entrega (cor, modelo, tamanho, voltagem, variação, item trocado):
              SEMPRE usar Logística → Produto errado.
            - Produto/Dep. Técnico SOMENTE se o item entregue corresponder exatamente ao anúncio/pedido.
            TAXONOMIA
            - Logística: [Atraso na entrega, Produto não entregue, Produto errado, Extravio]
            - Produto/Dep. Técnico: [Produto não atende as expectativas, Produto com defeito, Produto com qualidade inferior]
            - Atendimento ao cliente: [Demora no atendimento ou no retorno, Respostas insatisfatórias ou falta de solução, Suporte inacessível ou pouco eficiente]
            - Financeiro: [Cobrança indevida, Cobrança duplicada, Dificuldade em obter estorno nos pagamentos, Dificuldade em realizar o pagamento]
            CAMPOS (por item em "classificacoes")
            - departamento
            - categoria
            - motivoExtraido (máx. 200 caracteres, objetivo, fato observado)
            """;
}
