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
package io.fusion.air.microservice.ai.genai.examples.qwen;

import dev.langchain4j.model.chat.ChatLanguageModel;
import io.fusion.air.microservice.ai.genai.core.assistants.HealthCareAssistant;
import io.fusion.air.microservice.ai.genai.core.services.RAGHealthCareService;
import io.fusion.air.microservice.ai.genai.utils.AiBeans;
import io.fusion.air.microservice.ai.genai.utils.AiConstants;
import io.fusion.air.microservice.ai.genai.utils.ConsoleRunner;

/**
 * RAG Health Care Diagnosis Service Example
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public class _59_RAG_HealthCare_Example {

    /**
     * This example demonstrates how to implement Health Care Diagnosis Service Example
     */
    public static void main(String[] args) {
        // Create Chat Language Model Ollama Qwen 2.5
        ChatLanguageModel modelQwen = AiBeans.getChatLanguageModelLlama(AiConstants.OLLAMA_QWEN_2_5);
        AiBeans.printModelDetails(AiConstants.LLM_OLLAMA, AiConstants.OLLAMA_QWEN_2_5);
        // Create the Assistant
        // Setting up the Gen AI Context with Open AI LLM, and RAG
        HealthCareAssistant assistant = new RAGHealthCareService(AiConstants.LLM_OLLAMA, AiConstants.OLLAMA_QWEN_2_5);
        // Start the Conversation with iCare Health Care Diagnosis Service ChatBot
        // - Hi
        // - I need the diagnosis history of Akiera Kiera for the past 3 years
        // - I need the diagnosis history of Patient 300100202
        // - I need the diagnosis history of Jane Susan Wood for the past 4 years
        // - I need the diagnosis history of Patient 400100201
        // - No, thank you.
        ConsoleRunner.startConversationWithPrompts(assistant, "iCare - Health Care Hospitals");
    }
}
