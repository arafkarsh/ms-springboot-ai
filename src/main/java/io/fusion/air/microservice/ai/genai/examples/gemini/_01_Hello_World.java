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

import dev.langchain4j.chain.ConversationalChain;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import io.fusion.air.microservice.ai.genai.utils.AiBeans;
import io.fusion.air.microservice.ai.genai.utils.AiConstants;

import static dev.langchain4j.data.message.UserMessage.userMessage;

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

        // Create Chat Language Model - Google Gemini 1.5 Pro
        ChatLanguageModel geminiChatModel = AiBeans.getChatLanguageModelGoogle(AiConstants.GOOGLE_GEMINI_PRO);

        helloWorldWithGemini(geminiChatModel);
        conversationChatWithGemini(geminiChatModel);
        conversationChatWithMemoryGemini(geminiChatModel);
    }

    /**
     * Simple Hello World
     * @param geminiChatModel
     */
    public static void helloWorldWithGemini(ChatLanguageModel geminiChatModel) {
        // Start interacting
        String request = "Hello My Space... Google Gemini 1.5 Pro ";
        String response = geminiChatModel.generate(request);
        AiBeans.printModelDetails(AiConstants.LLM_VERTEX, AiConstants.GOOGLE_GEMINI_PRO);
        AiBeans.printResult(request, response);
    }

    /**
     * Conversation Chain
     * @param geminiChatModel
     */
    public static void conversationChatWithGemini(ChatLanguageModel geminiChatModel) {
        ConversationalChain chainAnthropic = ConversationalChain.builder()
                .chatLanguageModel(geminiChatModel)
                // .chatMemory() // you can override default chat memory
                .build();
        String request1 = "Hello, my name is karsh";
        String response1 = chainAnthropic.execute(request1);
        AiBeans.printResult(request1, response1);


        String request2 = "What is my name?";
        String response2 = chainAnthropic.execute(request2);
        AiBeans.printResult(request2, response2);
    }

    /**
     * Conversation with Memory
     * @param geminiChatModel
     */
    public static void conversationChatWithMemoryGemini(ChatLanguageModel geminiChatModel) {
        ChatMemory chatMemoryGemini = TokenWindowChatMemory.withMaxTokens(300,
                new OpenAiTokenizer(AiConstants.GPT_4o));

        // You have full control over the chat memory.
        // You can decide if you want to add a particular message to the memory
        // (e.g. you might not want to store few-shot examples to save on tokens).
        // You can process/modify the message before saving if required.
        String request1 = "Hello, my name is karsh";
        chatMemoryGemini.add(userMessage(request1));
        AiMessage answer1 = geminiChatModel.generate(chatMemoryGemini.messages()).content();
        chatMemoryGemini.add(answer1);
        AiBeans.printResult(request1, answer1.text());

        String request2 = "What is my name?";
        chatMemoryGemini.add(userMessage(request2));
        AiMessage answer2 = geminiChatModel.generate(chatMemoryGemini.messages()).content();
        chatMemoryGemini.add(answer2);
        AiBeans.printResult(request2, answer2.text());
    }
}
