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
package io.fusion.air.microservice.ai.genai.examples.anthropic;
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

    public static Assistant setupContext(ChatLanguageModel anthropicChatModel) {
        // Create Persistent Store
        ChatMemoryFileStore store = new ChatMemoryFileStore();
        // Create Chat Memory Provider with the Store
        ChatMemoryProvider chatMemoryProviderAnthropic = memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(10)
                .chatMemoryStore(store)
                .build();
        // Create the Ai Assistant with model and Chat Memory Provider
        return AiServices.builder(Assistant.class)
                .chatLanguageModel(anthropicChatModel)
                .chatMemoryProvider(chatMemoryProviderAnthropic)
                .build();
    }

    public static void persistInitialData(Assistant assistantAnthropic) {
        String request1 = "UUID-1 >> Hello, my name is John Sam Doe";
        String response1 = assistantAnthropic.chat("UUID-1", request1);
        AiBeans.printResult(request1, response1);
        AiBeans.sleep(55);

        String request2 = "UUID-2, >> Hello, my name is Jane Daisy Doe";
        String response2 = assistantAnthropic.chat("UUID-2", request2);
        AiBeans.printResult(request2, response2);
        AiBeans.sleep(55);
    }

    public static void testThePersistedData(Assistant assistantAnthropic) {
        String request3 = "What is my name?";
        String response3 = assistantAnthropic.chat("UUID-1", "UUID-1 >> "+request3);
        AiBeans.printResult("UUID-1 >> "+request3, response3);
        AiBeans.sleep(55);

        String response4 = assistantAnthropic.chat("UUID-2", "UUID-2 >> "+request3);
        AiBeans.printResult("UUID-2 >> "+request3, response4);
        AiBeans.sleep(55);
    }

    public static void main(String[] args) {
        // Create Chat Language Model - Anthropic Claude 3 Haiku
        ChatLanguageModel anthropicChatModel = AiBeans.getChatLanguageModelAnthropic(AiConstants.ANTHROPIC_CLAUDE_3_HAIKU);
        // Create Ai Assistant
        Assistant assistantAnthropic = setupContext(anthropicChatModel);
        AiBeans.printModelDetails(AiConstants.LLM_ANTHROPIC, AiConstants.ANTHROPIC_CLAUDE_3_HAIKU);
        // Initialize with Data
        persistInitialData(assistantAnthropic);
        // To DownloadAllData the persisted Data.
        // Comment out the previous call "persistInitialData()"
        // UnComment the following call "testThePersistedData()"
        testThePersistedData(assistantAnthropic);
    }
}
