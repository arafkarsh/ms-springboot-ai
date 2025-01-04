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

import dev.langchain4j.chain.ConversationalChain;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import static dev.langchain4j.data.message.UserMessage.userMessage;

import io.fusion.air.microservice.ai.genai.utils.AiBeans;
import io.fusion.air.microservice.ai.genai.utils.AiConstants;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public class _01_Hello_World {

    /**
     * DownloadAllData the Chat Language Model
     * @param args
     */
    public static void main(String[] args) {
        // Create Chat Language Model - Open AI GPT 4o
        ChatLanguageModel openAiChatModel = AiBeans.getChatLanguageModelOpenAi(AiConstants.GPT_4o);

        helloWorldWithOpenAI(openAiChatModel);
        conversationChatWithOpenAI(openAiChatModel);
        conversationChatWithMemoryWithOpenAI(openAiChatModel);
    }

    /**
     * Simple Hello World
     * @param openAiChatModel
     */
    public static void helloWorldWithOpenAI(ChatLanguageModel openAiChatModel) {
        // Start interacting
        String request = "Hello My Space... Open AI ChatGPT 4o ";
        String response = openAiChatModel.generate(request);
        AiBeans.printModelDetails(AiConstants.LLM_OPENAI, AiConstants.GPT_4o);
        AiBeans.printResult(request, response);
    }

    /**
     * Conversation Chain
     * @param openAiChatModel
     */
    public static void conversationChatWithOpenAI(ChatLanguageModel openAiChatModel) {
        ConversationalChain chainOpenAi = ConversationalChain.builder()
                .chatLanguageModel(openAiChatModel)
                // .chatMemory() // you can override default chat memory
                .build();
        String request1 = "Hello, my name is karsh";
        String response1 = chainOpenAi.execute(request1);
        AiBeans.printResult(request1, response1);


        String request2 = "What is my name?";
        String response2 = chainOpenAi.execute(request2);
        AiBeans.printResult(request2, response2);
    }

    /**
     * Conversation with Memory
     * @param openAiChatModel
     */
    public static void conversationChatWithMemoryWithOpenAI(ChatLanguageModel openAiChatModel) {
        ChatMemory chatMemoryOpenAi = TokenWindowChatMemory.withMaxTokens(300,
                new OpenAiTokenizer(AiConstants.GPT_4o));

        // You have full control over the chat memory.
        // You can decide if you want to add a particular message to the memory
        // (e.g. you might not want to store few-shot examples to save on tokens).
        // You can process/modify the message before saving if required.
        String request1 = "Hello, my name is karsh";
        chatMemoryOpenAi.add(userMessage(request1));
        AiMessage answer1 = openAiChatModel.generate(chatMemoryOpenAi.messages()).content();
        chatMemoryOpenAi.add(answer1);
        AiBeans.printResult(request1, answer1.text());

        String request2 = "What is my name?";
        chatMemoryOpenAi.add(userMessage(request2));
        AiMessage answer2 = openAiChatModel.generate(chatMemoryOpenAi.messages()).content();
        chatMemoryOpenAi.add(answer2);
        AiBeans.printResult(request2, answer2.text());
    }
}
