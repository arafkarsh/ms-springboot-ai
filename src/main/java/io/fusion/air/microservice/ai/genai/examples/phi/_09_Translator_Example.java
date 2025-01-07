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
package io.fusion.air.microservice.ai.genai.examples.phi;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import io.fusion.air.microservice.ai.genai.core.assistants.LanguageAssistant;
import io.fusion.air.microservice.ai.genai.utils.AiBeans;
import io.fusion.air.microservice.ai.genai.utils.AiConstants;

import java.util.List;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public class _09_Translator_Example {

    public static void main(String[] args) {

        // Create Chat Language Model Microsoft PHI - 3
        ChatLanguageModel model = AiBeans.getChatLanguageModelLlama(AiConstants.OLLAMA_PHI);
        AiBeans.printModelDetails(AiConstants.LLM_OLLAMA, AiConstants.OLLAMA_PHI);

        // Create Ai Assistant
        LanguageAssistant utils = AiServices.create(LanguageAssistant.class, model);
        String request = """
                    Hello, how are you?
                    What do you think about current weather and climate change?
                    Do you think climate change is real?
                """;
        String response = utils.translate(request, "french");
        AiBeans.printResult(request, response);

        request = """
                    AI, or artificial intelligence, is a branch of computer science that aims to create 
                    machines that mimic human intelligence. This can range from simple tasks such as recognizing 
                    patterns or speech to more complex tasks like making decisions or predictions.
                """;

        List<String> bulletPoints = utils.summarize(request, 5);
        AiBeans.printResult(request, bulletPoints.toString());

    }
}
