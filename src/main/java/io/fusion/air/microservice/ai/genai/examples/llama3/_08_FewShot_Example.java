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
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;
import io.fusion.air.microservice.ai.genai.utils.AiBeans;
import io.fusion.air.microservice.ai.genai.utils.AiConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public class _08_FewShot_Example {

    private static List<ChatMessage> fewShotHistoryLlama = new ArrayList<>();

    /**
     * Build the Context - Few Shot Message Context with Custom Action
     */
    public static void buildContext() {
        // Adding positive feedback example to history
        fewShotHistoryLlama.add(UserMessage.from(
                "I love the new update! The interface is very user-friendly and the new features are amazing!"));
        fewShotHistoryLlama.add(AiMessage.from(
                "Action: forward input to positive feedback storage\nReply: Thank you very much for this great feedback! We have transmitted your message to our product development team who will surely be very happy to hear this. We hope you continue enjoying using our product."));

        // Adding negative feedback example to history
        fewShotHistoryLlama.add(UserMessage
                .from("I am facing frequent crashes after the new update on my Android device."));
        fewShotHistoryLlama.add(AiMessage.from(
                "Action: open new ticket - crash after update Android\nReply: We are so sorry to hear about the issues you are facing. We have reported the problem to our development team and will make sure this issue is addressed as fast as possible. We will send you an email when the fix is done, and we are always at your service for any further assistance you may need."));

        // Adding another positive feedback example to history
        fewShotHistoryLlama.add(UserMessage
                .from("Your app has made my daily tasks so much easier! Kudos to the team!"));
        fewShotHistoryLlama.add(AiMessage.from(
                "Action: forward input to positive feedback storage\nReply: Thank you so much for your kind words! We are thrilled to hear that our app is making your daily tasks easier. Your feedback has been shared with our team. We hope you continue to enjoy using our app!"));

        // Adding another negative feedback example to history
        fewShotHistoryLlama.add(UserMessage
                .from("The new feature is not working as expected. It’s causing data loss."));
        fewShotHistoryLlama.add(AiMessage.from(
                "Action: open new ticket - data loss by new feature\nReply:We apologize for the inconvenience caused. Your feedback is crucial to us, and we have reported this issue to our technical team. They are working on it on priority. We will keep you updated on the progress and notify you once the issue is resolved. Thank you for your patience and support."));
    }

    /**
     * Send the Message
     * @param request
     */
    public static String sendChatMessage(ChatLanguageModel llamaModel, String request) {
        // Adding user message
        ChatMessage chatMessage = UserMessage.from(request);
        fewShotHistoryLlama.add(chatMessage);
        // Response from Ai
        Response<AiMessage> response = llamaModel.generate(fewShotHistoryLlama);
        // Print Result
        AiBeans.printResult(chatMessage.text(), response.content().text());
        return response.content().text();
    }

    public static void main(String[] args) {
        // Create Chat Language Model llama3
        ChatLanguageModel llamaModel = AiBeans.getChatLanguageModelLlama(AiConstants.OLLAMA_LLAMA3);
        AiBeans.printModelDetails(AiConstants.LLM_OLLAMA, AiConstants.OLLAMA_LLAMA3);
        // Build Chat Context
        buildContext();
        // Message 1
        sendChatMessage(llamaModel, "How can your app be so slow? Please do something about it!");
        // Message 2
        sendChatMessage(llamaModel, "The app is fantastic!");
        // Message 3
        sendChatMessage(llamaModel, "Simplified my daily tasks! Good work team.");
        // Message 4
        sendChatMessage(llamaModel, "App Crashes twice or thrice in a week. Stability is not that good.");
    }
}
