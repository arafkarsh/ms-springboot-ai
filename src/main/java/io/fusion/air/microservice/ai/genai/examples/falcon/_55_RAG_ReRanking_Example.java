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
package io.fusion.air.microservice.ai.genai.examples.falcon;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
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
public class _55_RAG_ReRanking_Example {

    /**
     * This example illustrates the implementation of a more advanced RAG application
     * using a technique known as "re-ranking".
     * <p>
     * Frequently, not all results retrieved by {@link ContentRetriever} are truly relevant to the user query.
     * This is because, during the initial retrieval stage, it is often preferable to use faster
     * and more cost-effective models, particularly when dealing with a large volume of data.
     * The trade-off is that the retrieval quality may be lower.
     * Providing irrelevant information to the LLM can be costly and, in the worst case, lead to hallucinations.
     * Therefore, in the second stage, we can perform re-ranking of the results obtained in the first stage
     * and eliminate irrelevant results using a more advanced model (e.g., Cohere Rerank).
     */
    public static void main(String[] args) {
        // Create Chat Language Model Google Falcon 2
        ChatLanguageModel modelFalcon = AiBeans.getChatLanguageModelLlama(AiConstants.OLLAMA_FALCON);
        AiBeans.printModelDetails(AiConstants.LLM_OLLAMA, AiConstants.OLLAMA_FALCON);
        // Create Ai Assistant
        // Setting up the Gen AI Context with Open AI LLM, and RAG
        Assistant assistant = RAGBuilder.createAssistantWithReRanking(modelFalcon);
        // Start the Conversation with Multi Data Source ChatBot
        // - Can I cancel my reservation?
        // - Please explain the refund policy.
        ConsoleRunner.startConversationWith(assistant, "Ozazo Car Rental Service - ReRanking");
    }
}
