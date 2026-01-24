package br.com.pucrs.tcc.domain.ai;

import dev.langchain4j.model.output.structured.Description;

public class Taxonomia {

    public enum Departamento {
        @Description("Problemas de fluxo de entrega e estoque")
        LOGISTICA("Logística"),

        @Description("Falhas técnicas ou de qualidade no produto recebido")
        PRODUTO_TECNICO("Produto/Dep. Técnico"),

        @Description("Falhas na comunicação ou suporte ao cliente")
        ATENDIMENTO_CLIENTE("Atendimento ao cliente"),

        @Description("Problemas com faturamento, pagamentos ou reembolsos")
        FINANCEIRO("Financeiro");

        private final String descricao;

        Departamento(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    public enum Categoria {
        // Logística
        @Description("Entrega fora do prazo")
        ATRASO_ENTREGA(Departamento.LOGISTICA, "Atraso na entrega"),

        @Description("Pedido consta como entregue mas não chegou")
        PRODUTO_NAO_ENTREGUE(Departamento.LOGISTICA, "Produto não entregue"),

        @Description("Divergência entre pedido e entrega (cor, modelo, voltagem, item trocado)")
        PRODUTO_ERRADO(Departamento.LOGISTICA, "Produto errado"),

        @Description("Carga perdida pela transportadora")
        EXTRAVIO(Departamento.LOGISTICA, "Extravio"),

        // Produto/Dep. Técnico
        @Description("O produto está correto, mas não é o que o cliente esperava")
        PRODUTO_NAO_ATENDE_EXPECTATIVAS(Departamento.PRODUTO_TECNICO, "Produto não atende as expectativas"),

        @Description("O produto está correto, mas apresenta falha técnica ou quebra")
        PRODUTO_COM_DEFEITO(Departamento.PRODUTO_TECNICO, "Produto com defeito"),

        @Description("Material ou acabamento de baixa qualidade")
        PRODUTO_QUALIDADE_INFERIOR(Departamento.PRODUTO_TECNICO, "Produto com qualidade inferior"),

        // Atendimento ao cliente
        @Description("Espera excessiva por uma resposta")
        DEMORA_ATENDIMENTO(Departamento.ATENDIMENTO_CLIENTE, "Demora no atendimento ou no retorno"),

        @Description("A resposta dada não resolve o problema")
        RESPOSTAS_INSATISFATORIAS(Departamento.ATENDIMENTO_CLIENTE, "Respostas insatisfatórias ou falta de solução"),

        @Description("Canais de contato não funcionam")
        SUPORTE_INACESSIVEL(Departamento.ATENDIMENTO_CLIENTE, "Suporte inacessível ou pouco eficiente"),

        // Financeiro
        @Description("Valor cobrado a mais ou sem autorização")
        COBRANCA_INDEVIDA(Departamento.FINANCEIRO, "Cobrança indevida"),

        @Description("Pagamento processado duas vezes")
        COBRANCA_DUPLICADA(Departamento.FINANCEIRO, "Cobrança duplicada"),

        @Description("Demora ou erro na devolução do dinheiro")
        DIFICULDADE_ESTORNO(Departamento.FINANCEIRO, "Dificuldade em obter estorno nos pagamentos"),

        @Description("Erro ao processar cartão ou boleto")
        DIFICULDADE_PAGAMENTO(Departamento.FINANCEIRO, "Dificuldade em realizar o pagamento");

        private final Departamento departamento;
        private final String descricao;

        Categoria(Departamento departamento, String descricao) {
            this.departamento = departamento;
            this.descricao = descricao;
        }

        public Departamento getDepartamento() {
            return departamento;
        }

        public String getDescricao() {
            return descricao;
        }
    }
}