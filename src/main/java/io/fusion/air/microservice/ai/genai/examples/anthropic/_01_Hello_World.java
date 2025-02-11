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
        // Create Chat Language Model - Anthropic Claude 3 Haiku
        ChatLanguageModel anthropicChatModel = AiBeans.getChatLanguageModelAnthropic(AiConstants.ANTHROPIC_CLAUDE_3_HAIKU);

        helloWorldWithAnthropic(anthropicChatModel);
        conversationChatWithAnthropic(anthropicChatModel);
        conversationChatWithMemoryWithAnthropic(anthropicChatModel);
    }

    /**
     * Simple Hello World
     * @param anthropicChatModel
     */
    public static void helloWorldWithAnthropic(ChatLanguageModel anthropicChatModel) {
        // Start interacting
        String request = "Hello My Space... Anthropic Claude 3 Haiku ";
        String response = anthropicChatModel.generate(request);
        AiBeans.printModelDetails(AiConstants.LLM_ANTHROPIC, AiConstants.ANTHROPIC_CLAUDE_3_HAIKU);
        AiBeans.printResult(request, response);
    }

    /**
     * Conversation Chain
     * @param anthropicChatModel
     */
    public static void conversationChatWithAnthropic(ChatLanguageModel anthropicChatModel) {
        ConversationalChain chainAnthropic = ConversationalChain.builder()
                .chatLanguageModel(anthropicChatModel)
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
     * @param anthropicChatModel
     */
    public static void conversationChatWithMemoryWithAnthropic(ChatLanguageModel anthropicChatModel) {
        ChatMemory chatMemoryAnthropic = TokenWindowChatMemory.withMaxTokens(300,
                new OpenAiTokenizer(AiConstants.GPT_4o));

        // You have full control over the chat memory.
        // You can decide if you want to add a particular message to the memory
        // (e.g. you might not want to store few-shot examples to save on tokens).
        // You can process/modify the message before saving if required.
        String request1 = "Hello, my name is karsh";
        chatMemoryAnthropic.add(userMessage(request1));
        AiMessage answer1 = anthropicChatModel.generate(chatMemoryAnthropic.messages()).content();
        chatMemoryAnthropic.add(answer1);
        AiBeans.printResult(request1, answer1.text());

        String request2 = "What is my name?";
        chatMemoryAnthropic.add(userMessage(request2));
        AiMessage answer2 = anthropicChatModel.generate(chatMemoryAnthropic.messages()).content();
        chatMemoryAnthropic.add(answer2);
        AiBeans.printResult(request2, answer2.text());
    }
}
