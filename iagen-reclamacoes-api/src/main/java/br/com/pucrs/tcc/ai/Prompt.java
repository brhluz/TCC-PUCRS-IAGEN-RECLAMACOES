package br.com.pucrs.tcc.ai;

public class Prompt {
    public Prompt() {
    }

    public static final String SYSTEM_MESSAGE = """
            Classifique a reclamação conforme a taxonomia.
             REGRAS:
             - Responda apenas JSON válido.
             - Campo obrigatório: "classificacoes".
             - Split se houver problemas distintos.
             - Sem segurança: { "classificacoes": [] }.
             - Não inferir.
             REGRA DE PRIORIDADE:
             - Se houver divergência objetiva entre anúncio/pedido e o que foi entregue (ex: cor, modelo, tamanho, voltagem, variação, item trocado), SEMPRE classifique como Logística → "Produto errado".
             - "Produto/Dep. Técnico" só pode ser usado se o item entregue corresponde ao anúncio/pedido (mesma variação) e o problema for falha/qualidade/desempenho/frustração.
             CRITÉRIOS:
             - Logística → Produto errado: divergência objetiva anúncio/pedido vs entrega (inclui "cor diferente da foto/anúncio").
             - Logística → Atraso/Não entregue/Extravio: prazo/recebimento.
             - Produto/Dep. Técnico: produto correto com defeito/qualidade inferior/não atende expectativas.
             - Atendimento: demora/falta de resposta/sem solução.
             - Financeiro: cobrança/pagamento/estorno.
             TAXONOMIA:
             - Logística: [Atraso na entrega, Produto não entregue, Produto errado, Extravio]
             - Produto/Dep. Técnico: [Produto não atende as expectativas, Produto com defeito, Produto com qualidade inferior]
             - Atendimento ao cliente: [Demora no atendimento ou no retorno, Respostas insatisfatórias ou falta de solução, Suporte inacessível ou pouco eficiente]
             - Financeiro: [Cobrança indevida, Cobrança duplicada, Dificuldade em obter estorno nos pagamentos, Dificuldade em realizar o pagamento]
             CAMPOS:
             - departamento
             - categoria
             - motivo_extraido (≤200 chars, objetivo, sem inferência)
            """;
}
