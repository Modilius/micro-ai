package org.modilius.microai.cdi.extension;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;

import java.util.List;

public class DummyChatLanguageModel implements ChatLanguageModel {
    @Override
    public Response<AiMessage> generate(List<ChatMessage> list) {
        return null;
    }
}
