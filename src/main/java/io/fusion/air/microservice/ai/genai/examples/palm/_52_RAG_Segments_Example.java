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
package io.fusion.air.microservice.ai.genai.examples.palm;

// Custom
import dev.langchain4j.model.chat.ChatLanguageModel;
import io.fusion.air.microservice.ai.genai.core.assistants.CarRentalAssistant;
import io.fusion.air.microservice.ai.genai.core.services.RAGBuilder;
import io.fusion.air.microservice.ai.genai.utils.AiBeans;
import io.fusion.air.microservice.ai.genai.utils.AiConstants;
import io.fusion.air.microservice.ai.genai.utils.ConsoleRunner;

/**
 * RAG Segment Example
 *
 * @author: Araf Karsh HamidHell
 * @version:
 * @date:
 */
public class _52_RAG_Segments_Example {

    /**
     * This example demonstrates how to implement a naive Retrieval-Augmented Generation (RAG) application.
     *
     * In each interaction with the Large Language Model (LLM), we will:
     * 1. Take the user's query as-is.
     * 2. Embed it using an embedding model.
     * 3. Use the query's embedding to search an embedding store (containing small segments of your documents)
     * for the X most relevant segments.
     * 4. Append the found segments to the user's query.
     * 5. Send the combined input (user query + segments) to the LLM.
     * 6. Hope that:
     * - The user's query is well-formulated and contains all necessary details for retrieval.
     * - The found segments are relevant to the user's query.
     *
     */
    public static void main(String[] args) {
        // Create Chat Language Model Google Vertex AI - PaLM 2
        ChatLanguageModel modelPalm = AiBeans.getChatLanguageModelGoogle(AiConstants.GOOGLE_PALM_CHAT_BISON);
        AiBeans.printModelDetails(AiConstants.LLM_VERTEX, AiConstants.GOOGLE_PALM_CHAT_BISON);
        // Create the Assistant
        // Setting up the Gen AI Context with Open AI LLM, and RAG
        CarRentalAssistant assistant = RAGBuilder.createCarRentalAssistantWithSegments(modelPalm);
        // Start the Conversation with Ozazo Rental Service ChatBot
        // - Hello
        // - I am Sam. Can I cancel my reservation?
        // - What will be the charge if I cancel 48 hours before the reservation date?
        // - Can you elaborate the usage policy?
        // - If I had an accident, should I pay extra?
        // - Do you have a refund policy?
        // - No, thank you.
        ConsoleRunner.startConversationWith(assistant, "OZAZO Car Rental Premium Service ChatBot");
    }
}
