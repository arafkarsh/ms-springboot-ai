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
public class _56_RAG_Meta_Data_Example {

    /**
     * RAG - Content Meta Data
     * This example illustrates how to include document source and other metadata into the LLM prompt.
     */

    public static void main(String[] args) {
        // Create Chat Language Model - Google Gemini 1.5 Pro
        ChatLanguageModel modelGemini = AiBeans.getChatLanguageModelGoogle(AiConstants.GOOGLE_GEMINI_PRO);
        AiBeans.printModelDetails(AiConstants.LLM_VERTEX, AiConstants.GOOGLE_GEMINI_PRO);
        // Create Ai Assistant
        // Setting up the Gen AI Context with Open AI LLM, and RAG
        Assistant assistant = RAGBuilder.createAssistantWithMetaData(modelGemini);
        // Start the Conversation with Multi Data Source ChatBot
        // - I am Sam. Can I cancel my reservation?
        // - Please explain the refund policy.
        // - What is the name of the file where cancellation policy is defined?
        ConsoleRunner.startConversationWith(assistant, "Ozazo Car Rental Service - MetaData");
    }
}
