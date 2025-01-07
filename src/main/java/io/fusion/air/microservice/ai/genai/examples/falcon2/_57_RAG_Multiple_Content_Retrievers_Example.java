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
package io.fusion.air.microservice.ai.genai.examples.falcon2;

import dev.langchain4j.model.chat.ChatLanguageModel;
import io.fusion.air.microservice.ai.genai.core.assistants.Assistant;
import io.fusion.air.microservice.ai.genai.core.services.RAGBuilder;
import io.fusion.air.microservice.ai.genai.utils.AiBeans;
import io.fusion.air.microservice.ai.genai.utils.AiConstants;
import io.fusion.air.microservice.ai.genai.utils.ConsoleRunner;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public class _57_RAG_Multiple_Content_Retrievers_Example {

    /**
     * RAG - Multiple Content Retrievers
     * This example demonstrates how to use multiple content retrievers.
     */

    public static void main(String[] args) {
        // Create Chat Language Model Google Falcon 2
        ChatLanguageModel modelFalcon = AiBeans.getChatLanguageModelLlama(AiConstants.OLLAMA_FALCON_2);
        AiBeans.printModelDetails(AiConstants.LLM_OLLAMA, AiConstants.OLLAMA_FALCON_2);
        // Create Ai Assistant
        // Setting up the Gen AI Context with Open AI LLM, and RAG
        Assistant assistant = RAGBuilder.createAssistantWithMultiContentRetrievers(modelFalcon);
        // Start the Conversation with Multi Data Source ChatBot
        // - I am Sam. Can I cancel my reservation?
        // - Please explain the refund policy.
        ConsoleRunner.startConversationWith(assistant, "Ozazo Car Rental Service - Multiple Content Retrievers");
    }
}
