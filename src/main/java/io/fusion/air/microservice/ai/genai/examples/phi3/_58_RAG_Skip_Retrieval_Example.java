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
package io.fusion.air.microservice.ai.genai.examples.phi3;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.query.router.QueryRouter;
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
public class _58_RAG_Skip_Retrieval_Example {

    /**
     * RAG - Skip Content Retrieval
     *
     * This example demonstrates how to conditionally skip retrieval.
     * Sometimes, retrieval is unnecessary, for instance, when a user simply says "Hi".
     * <p>
     * There are multiple ways to implement this, but the simplest one is to use a custom {@link QueryRouter}.
     * When retrieval should be skipped, QueryRouter will return an empty list,
     * meaning that the query will not be routed to any {@link ContentRetriever}.
     * <p>
     * Decision-making can be implemented in various ways:
     * - Using rules (e.g., depending on the user's privileges, location, etc.).
     * - Using keywords (e.g., if a query contains specific words).
     * - Using semantic similarity (see EmbeddingModelTextClassifierExample in this repository).
     * - Using an LLM to make a decision.
     * <p>
     * In this example, we will use an LLM to decide whether a user query should do retrieval or not.
     */

    public static void main(String[] args) {
        // Create Chat Language Model Microsoft PHI - 3
        ChatLanguageModel modelPhi = AiBeans.getChatLanguageModelLlama(AiConstants.OLLAMA_PHI_3);
        AiBeans.printModelDetails(AiConstants.LLM_OLLAMA, AiConstants.OLLAMA_PHI_3);
        // Create Assistant
        // Setting up the Gen AI Context with Open AI LLM, and RAG
        Assistant assistant = RAGBuilder.createAssistantWithRetrievalSkipping(modelPhi);
        // Start the Conversation with Multi Data Source ChatBot
        // - Hi
        // Notice how this query is not routed to any retrievers.
        // - I am Sam. Can I cancel my reservation?
        // - Please explain the refund policy.
        // This query has been routed to our retriever.

        ConsoleRunner.startConversationWith(assistant, "Ozazo Car Rental Service - Skipping Content Retrievers");
    }
}
