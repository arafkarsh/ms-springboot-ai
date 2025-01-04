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

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.model.Tokenizer;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import dev.langchain4j.model.output.Response;

import static dev.langchain4j.data.message.UserMessage.userMessage;

import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import io.fusion.air.microservice.ai.genai.core.assistants.Assistant;
import io.fusion.air.microservice.ai.genai.utils.AiBeans;
import io.fusion.air.microservice.ai.genai.utils.AiConstants;
import io.fusion.air.microservice.utils.Std;

import java.util.List;

/**
 * Chat Memory Examples
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public class _07_ChatMemory_Example {

    /**
     * Chat Memory Conversation
     * @param openAIModel
     */
    public static void chatMemoryConversations(ChatLanguageModel openAIModel) {
        Tokenizer tokenizer = new OpenAiTokenizer(AiConstants.getOpenAIDefaultModel());
        ChatMemory chatMemoryOpenAi = TokenWindowChatMemory.withMaxTokens(2000, tokenizer);

        // Setting the Context
        SystemMessage systemMessage = SystemMessage.from(
                """
                        You are an Architect explaining to team leads & developers, 
                        the Design you are working on is an health-care platform with 
                        Java back-end, PostgreSQL Database,and Spring Data JPA.
                        You are checking the knowledge and skill-set of the team. 
                        """);
        chatMemoryOpenAi.add(systemMessage);

        // Conversation - 1
        UserMessage userMessage1 = userMessage(
                """
                        1. How to optimize database queries for a large-scale health-care platform? 
                        2. How to add Security Features from Spring Security perspective?
                        Answer short in three to five lines maximum.
                 """);
        chatMemoryOpenAi.add(userMessage1);
        Response<AiMessage> response1 = openAIModel.generate(chatMemoryOpenAi.messages());
        chatMemoryOpenAi.add(response1.content());
        // Print Result
        AiBeans.printResult(userMessage1.text(), response1.content().text());

        // Conversation - 2
        UserMessage userMessage2 = userMessage(
                """
                Give a concrete example implementation for the 2 points. 
                Be short, 10 lines of code maximum.
                """);
        chatMemoryOpenAi.add(userMessage2);
        Response<AiMessage> response2 = openAIModel.generate(chatMemoryOpenAi.messages());
        chatMemoryOpenAi.add(response2.content());
        // Print Result
        AiBeans.printResult(userMessage2.text(), response2.content().text());
    }

    /**
     * Chat Memory with Multiple Users
     * @param openAiModel
     */
    public static void chatMemoryWithMultipleUsers(ChatLanguageModel openAiModel) {
        Assistant assistantOpenAI = AiServices.builder(Assistant.class)
                .chatLanguageModel(openAiModel)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                .build();

        String request1 = "UUID-1 >> Hello, my name is John Sam Doe";
        String response1 = assistantOpenAI.chat("UUID-1", request1);
        AiBeans.printResult(request1, response1);

        String request2 = "UUID-2, >> Hello, my name is Jane Daisy Doe";
        String response2 = assistantOpenAI.chat("UUID-2", request2);
        AiBeans.printResult(request2, response2);

        String request3 = "What is my name?";
        String response3 = assistantOpenAI.chat("UUID-1", "UUID-1 >> "+request3);
        AiBeans.printResult("UUID-1 >> "+request3, response3);

        String response4 = assistantOpenAI.chat("UUID-2", "UUID-2 >> "+request3);
        AiBeans.printResult("UUID-2 >> "+request3, response4);
    }

    /**
     * In Memory Embedding Example
     */
    private static void inMemoryEmbeddingOpenAI(String dataC1, String dataC2, String request) {
        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();
        // Set Data 1
        TextSegment segment1 = TextSegment.from(dataC1);
        Embedding embedding1 = embeddingModel.embed(segment1).content();
        embeddingStore.add(embedding1, segment1);
        // Set Data 2
        TextSegment segment2 = TextSegment.from(dataC2);
        Embedding embedding2 = embeddingModel.embed(segment2).content();
        embeddingStore.add(embedding2, segment2);
        // Embed Query Request
        Embedding queryEmbedding = embeddingModel.embed(request).content();
        List<EmbeddingMatch<TextSegment>> relevant = embeddingStore.findRelevant(queryEmbedding, 1);
        EmbeddingMatch<TextSegment> embeddingMatch = relevant.get(0);
        // Show the Score and Matched Response
        Std.println("--[Data]-----------------------------------------------------------");
        Std.println("Data 1: "+dataC1);
        Std.println("Data 2: "+dataC2);
        AiBeans.printResult(request,
                "Score:  "+embeddingMatch.score()
                          +"\nResult: "+embeddingMatch.embedded().text());
    }

    public static void main(String[] args)  {
        // Create Chat Language Model - Open AI GPT 4o
        ChatLanguageModel openAIModel = AiBeans.getChatLanguageModelOpenAi(AiConstants.GPT_4o);
        AiBeans.printModelDetails(AiConstants.LLM_OPENAI, AiConstants.GPT_4o);
        // Chat Memory Conversations
        chatMemoryConversations(openAIModel);
        // Chat Memory with Multiple user
        chatMemoryWithMultipleUsers(openAIModel);
        // InMemory Embedding Example
        inMemoryEmbeddingOpenAI(
                "I like football, Chess, Tennis and Cricket. However, I like Cricket most!",
                "The weather is good today. It's neither hot nor cold.",
                "What is your favourite sport?");

        inMemoryEmbeddingOpenAI(
                "I like movies. My favorite genre is Sci-Fi. I am not a sports person.",
                "The weather is good today. It's neither hot nor cold.",
                "What is your favourite sport?");
    }
}
