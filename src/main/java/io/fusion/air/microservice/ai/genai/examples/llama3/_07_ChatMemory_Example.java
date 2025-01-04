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
package io.fusion.air.microservice.ai.genai.examples.llama3;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.model.Tokenizer;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.service.AiServices;
import io.fusion.air.microservice.ai.genai.core.assistants.Assistant;
import io.fusion.air.microservice.ai.genai.utils.AiBeans;
import io.fusion.air.microservice.ai.genai.utils.AiConstants;

import static dev.langchain4j.data.message.UserMessage.userMessage;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public class _07_ChatMemory_Example {

    public static void chatMemoryConversations(ChatLanguageModel llamaChatModel) {
        Tokenizer tokenizer = new OpenAiTokenizer(AiConstants.GPT_4_TURBO);
        ChatMemory chatMemoryLlama = TokenWindowChatMemory.withMaxTokens(2000, tokenizer);

        // Setting the Context
        SystemMessage systemMessage = SystemMessage.from(
                """
                        You are an Architect explaining to team leads & developers, 
                        the Design you are working on is an health-care platform with 
                        Java back-end, PostgreSQL Database,and Spring Data JPA.
                        You are checking the knowledge and skill-set of the team. 
                        """);
        chatMemoryLlama.add(systemMessage);

        // Conversation - 1
        UserMessage userMessage1 = userMessage(
                """
                        1. How to optimize database queries for a large-scale health-care platform? 
                        2. How to add Security Features from Spring Security perspective?
                        Answer short in three to five lines maximum.
                 """);
        chatMemoryLlama.add(userMessage1);
        Response<AiMessage> response1 = llamaChatModel.generate(chatMemoryLlama.messages());
        chatMemoryLlama.add(response1.content());
        // Print Result
        AiBeans.printResult(userMessage1.text(), response1.content().text());

        // Conversation - 2
        UserMessage userMessage2 = userMessage(
                """
                Give a concrete example implementation for the 2 points. 
                Be short, 10 lines of code maximum.
                """);
        chatMemoryLlama.add(userMessage2);
        Response<AiMessage> response2 = llamaChatModel.generate(chatMemoryLlama.messages());
        chatMemoryLlama.add(response2.content());
        // Print Result
        AiBeans.printResult(userMessage2.text(), response2.content().text());
    }

    public static void chatMemoryWithMultipleUsers(ChatLanguageModel llamaChatModel) {
        Assistant assistantLlama = AiServices.builder(Assistant.class)
                .chatLanguageModel(llamaChatModel)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                .build();

        String request1 = "UUID-1 >> Hello, my name is John Sam Doe";
        String response1 = assistantLlama.chat("UUID-1", request1);
        AiBeans.printResult(request1, response1);

        String request2 = "UUID-2, >> Hello, my name is Jane Daisy Doe";
        String response2 = assistantLlama.chat("UUID-2", request2);
        AiBeans.printResult(request2, response2);

        String request3 = "What is my name?";
        String response3 = assistantLlama.chat("UUID-1", "UUID-1 >> "+request3);
        AiBeans.printResult("UUID-1 >> "+request3, response3);

        String response4 = assistantLlama.chat("UUID-2", "UUID-2 >> "+request3);
        AiBeans.printResult("UUID-2 >> "+request3, response4);

    }

    public static void main(String[] args)  {
        // Create Chat Language Model llama3
        ChatLanguageModel llamaChatModel = AiBeans.getChatLanguageModelLlama(AiConstants.OLLAMA_LLAMA3);
        AiBeans.printModelDetails(AiConstants.LLM_OLLAMA, AiConstants.OLLAMA_LLAMA3);
        // Chat Memory Conversations
        chatMemoryConversations(llamaChatModel);

        // Chat Memory with Multiple user
        chatMemoryWithMultipleUsers(llamaChatModel);
    }
}
