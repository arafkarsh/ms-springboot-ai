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
package io.fusion.air.microservice.ai.genai.examples.openai;
// LangChain4J
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
// Custom
import io.fusion.air.microservice.ai.genai.core.assistants.Assistant;
import io.fusion.air.microservice.ai.genai.core.services.ChatMemoryFileStore;
import io.fusion.air.microservice.ai.genai.utils.AiBeans;
import io.fusion.air.microservice.ai.genai.utils.AiConstants;
import io.fusion.air.microservice.utils.Std;

/**
 * Chat Memory Persistent Store Example.
 * Storing the Data to file based on ChatMemoryFileStore.
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public class _12_Persistent_ChatMemory_Store_Example {

    public static Assistant setupContext(ChatLanguageModel openAiChatModel ) {
        Std.println("Setup in Progress.... ");
        // Create a File-based Persistent Store.
        ChatMemoryFileStore chatFileStore = new ChatMemoryFileStore();
        // Create Chat Memory Provider with the Persistent Store
        ChatMemoryProvider chatMemoryProviderOpenAI = memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(30)
                // Currently MapDB Store saving data into a File System.
                // Instead, this could be a RDBMS implementation too.
                .chatMemoryStore(chatFileStore)
                .build();
        // Create the Ai Assistant with model and Chat Memory Provider
        Assistant assistantOpenAI = AiServices.builder(Assistant.class)
                .chatLanguageModel(openAiChatModel)
                .chatMemoryProvider(chatMemoryProviderOpenAI)
                .build();
        Std.println("Setup Completed...");
        return assistantOpenAI;
    }

    public static void persistInitialData(Assistant assistantOpenAi) {
        String request1 = "UUID-1 >> Hello, my name is John Sam Doe";
        String response1 = assistantOpenAi.chat("UUID-1", request1);
        AiBeans.printResult(request1, response1);

        String request2 = "UUID-2, >> Hello, my name is Jane Daisy Doe";
        String response2 = assistantOpenAi.chat("UUID-2", request2);
        AiBeans.printResult(request2, response2);
    }

    public static void testThePersistedData(Assistant assistantOpenAi) {
        String request3 = "What is my name?";
        String response3 = assistantOpenAi.chat("UUID-1", "UUID-1 >> "+request3);
        AiBeans.printResult("UUID-1 >> "+request3, response3);

        String response4 = assistantOpenAi.chat("UUID-2", "UUID-2 >> "+request3);
        AiBeans.printResult("UUID-2 >> "+request3, response4);
    }

    public static void main(String[] args) {
        // Create Chat Language Model - Open AI GPT 4o
        ChatLanguageModel openAiChatModel = AiBeans.getChatLanguageModelOpenAi(AiConstants.GPT_4o);
        AiBeans.printModelDetails(AiConstants.LLM_OPENAI, AiConstants.GPT_4o);
        // Setup the Context
        Assistant assistantOpenAi = setupContext(openAiChatModel);
        // Initialize with Data
        persistInitialData(assistantOpenAi);
        // To DownloadAllData the persisted Data.
        // Comment out the previous call "persistInitialData()"
        // UnComment the following call "testThePersistedData()"
        // testThePersistedData(assistant);
    }
}
