package org.modilius.microai.cdi.extension;

import org.modilius.microai.cdi.extension.spi.RegisterAIService;

@RegisterAIService(
        model = DummyChatLanguageModel.class
)
public interface MyDummyAIService  {

}
