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
package io.fusion.air.microservice.ai.genai.examples.gemini;
// LangChain4J

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import io.fusion.air.microservice.ai.genai.core.assistants.Assistant;
import io.fusion.air.microservice.ai.genai.core.services.ChatMemoryFileStore;
import io.fusion.air.microservice.ai.genai.utils.AiBeans;
import io.fusion.air.microservice.ai.genai.utils.AiConstants;

/**
 * Chat Memory Persistent Store Example.
 * Storing the Data to file based on ChatMemoryFileStore.
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public class _12_Persistent_ChatMemory_Store_Example {

    public static Assistant setupContext(ChatLanguageModel geminiChatModel) {
        // Create Persistent Store
        ChatMemoryFileStore store = new ChatMemoryFileStore();
        // Create Chat Memory Provider with the Store
        ChatMemoryProvider chatMemoryProviderGemini = memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(10)
                .chatMemoryStore(store)
                .build();
        // Create the Ai Assistant with model and Chat Memory Provider
        return AiServices.builder(Assistant.class)
                .chatLanguageModel(geminiChatModel)
                .chatMemoryProvider(chatMemoryProviderGemini)
                .build();
    }

    public static void persistInitialData(Assistant assistantGemini) {
        String request1 = "UUID-1 >> Hello, my name is John Sam Doe";
        String response1 = assistantGemini.chat("UUID-1", request1);
        AiBeans.printResult(request1, response1);

        String request2 = "UUID-2, >> Hello, my name is Jane Daisy Doe";
        String response2 = assistantGemini.chat("UUID-2", request2);
        AiBeans.printResult(request2, response2);
    }

    public static void testThePersistedData(Assistant assistantGemini) {
        String request3 = "What is my name?";
        String response3 = assistantGemini.chat("UUID-1", "UUID-1 >> "+request3);
        AiBeans.printResult("UUID-1 >> "+request3, response3);

        String response4 = assistantGemini.chat("UUID-2", "UUID-2 >> "+request3);
        AiBeans.printResult("UUID-2 >> "+request3, response4);
    }

    public static void main(String[] args) {
        // Create Chat Language Model - Google Gemini 1.5 Pro
        ChatLanguageModel geminiChatModel = AiBeans.getChatLanguageModelGoogle(AiConstants.GOOGLE_GEMINI_PRO);
        AiBeans.printModelDetails(AiConstants.LLM_VERTEX, AiConstants.GOOGLE_GEMINI_PRO);
        // Setup the Context
        Assistant assistantGemini = setupContext(geminiChatModel);
        // Initialize with Data
        persistInitialData(assistantGemini);
        // To DownloadAllData the persisted Data.
        // Comment out the previous call "persistInitialData()"
        // UnComment the following call "testThePersistedData()"
        testThePersistedData(assistantGemini);
    }
}
