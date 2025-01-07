/**
 * (C) Copyright 2024 Araf Karsh Hamid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.fusion.air.microservice.ai.genai.examples.qwen;


import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import io.fusion.air.microservice.ai.genai.core.assistants.Assistant;
import io.fusion.air.microservice.ai.genai.core.tools.CalculatorTool;
import io.fusion.air.microservice.ai.genai.utils.AiBeans;
import io.fusion.air.microservice.ai.genai.utils.AiConstants;
import io.fusion.air.microservice.utils.Std;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public class _06_Tools_Example {

    /**
     * Tools Are NOT Supported by Ollama Models - Llama3
     *
     * @param args
     */
    public static void main(String[] args) {

        // Create Chat Language Model Ollama Qwen 2.5
        ChatLanguageModel modelQwen = AiBeans.getChatLanguageModelLlama(AiConstants.OLLAMA_QWEN);
        AiBeans.printModelDetails(AiConstants.LLM_OLLAMA, AiConstants.OLLAMA_QWEN);

        Assistant qwenAssistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(modelQwen)
                .tools(new CalculatorTool())
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();

        String question1 = "What is the square root of the sum of the numbers of letters in the words \"Hello\" and \"my Fusion world\"?";
        String answer1 = qwenAssistant.chat(question1);
        Std.println(answer1);
        // The square root of the sum of the number of letters in the words "hello" and "world" is approximately 4.47.
        String question2 = "What is the sum of the numbers of letters in the words \"Hello\" and \"my Fusion world\"?";
        String answer2 = qwenAssistant.chat(question2);
        Std.println(answer2);
    }
}
