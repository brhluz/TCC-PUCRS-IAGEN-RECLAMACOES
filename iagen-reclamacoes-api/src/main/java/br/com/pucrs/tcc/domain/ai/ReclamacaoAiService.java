package br.com.pucrs.tcc.domain.ai;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService
public interface ReclamacaoAiService {

    @SystemMessage(Prompt.SYSTEM_MESSAGE)
    @UserMessage("Classifique a seguinte reclamação: {descricao}")
    ClassificacaoResponse classificar(String descricao);
}
