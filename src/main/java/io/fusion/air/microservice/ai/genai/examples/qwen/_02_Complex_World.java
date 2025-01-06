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

import dev.langchain4j.model.chat.ChatLanguageModel;
import io.fusion.air.microservice.ai.genai.core.assistants.HAL9000Assistant;
import io.fusion.air.microservice.ai.genai.utils.AiBeans;
import io.fusion.air.microservice.ai.genai.utils.AiConstants;
import io.fusion.air.microservice.utils.Std;

import static io.fusion.air.microservice.ai.genai.utils.AiUtils.validateCalc;


/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public class _02_Complex_World {

    public static void main(String[] args) {
        // Create Chat Language Model Ollama Qwen 2.5
        ChatLanguageModel model = AiBeans.getChatLanguageModelLlama(AiConstants.OLLAMA_QWEN_2_5);
        // Create the Ai Assistant
        HAL9000Assistant hal9kQwen = new AiBeans().createHAL9000(model);
        AiBeans.printModelDetails(AiConstants.LLM_OLLAMA, AiConstants.OLLAMA_QWEN_2_5);

        // Run the DownloadAllData Cases
        complexWorld1UsingQwen(hal9kQwen);
        complexWorld2UsingQwen(hal9kQwen);
        complexWorld3UsingQwen(hal9kQwen);
    }

    /**
     * Example 1
     * @param hal9K
     */
    public static void complexWorld1UsingQwen(HAL9000Assistant hal9K) {
        interactWithQwen(hal9K, "What is the square root of 144233377?");
        interactWithQwen(hal9K, "Capitalize every third letter except the sixth letter: abcdefghjiklmnop");
    }

    /**
     * Example 2
     * @param hal9K
     */
    public static void complexWorld2UsingQwen(HAL9000Assistant hal9K) {
        interactWithQwen(hal9K, "What are the hours between 06:00 on 7 Feb 1970 and 11:00 on 02 Jun 1980?");
        interactWithQwen(hal9K, "What is the sum of all the digits in the previous question? Is that a Prime Number?");
        interactWithQwen(hal9K, "What are the hours between 11:00 on 2 Jun 1980 and 12:00 on 11 Mar 2024?");
        validateCalc();
        Std.println("--------------------------------------------------------------");
    }

    /**
     * Example 3
     * @param hal9K
     */
    public static void complexWorld3UsingQwen(HAL9000Assistant hal9K) {
        interactWithQwen(hal9K, "Explain French Revolution in details with critical events.");
    }

    /**
     * Interact with HAL9000Assistant Ai Assistant
     * @param hal9K
     * @param userMessage
     */
    private static void interactWithQwen(HAL9000Assistant hal9K, String userMessage) {
        Std.println("[Human]: " + userMessage);
        String response = hal9K.chat(userMessage);
        Std.println("[HAL9K]: " + response);
        Std.println("--------------------------------------------------------------");
    }
}
