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
package io.fusion.air.microservice.ai.genai.examples.palm2;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.query.router.LanguageModelQueryRouter;
import dev.langchain4j.rag.query.router.QueryRouter;
import dev.langchain4j.store.embedding.EmbeddingStore;
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
public class _54_RAG_Query_Routing_Example {

    /**
     * This example showcases the implementation of a more advanced RAG application
     * using a technique known as "query routing".
     * <p>
     * Often, private data is spread across multiple sources and formats.
     * This might include internal company documentation on Confluence, your project's code in a Git repository,
     * a relational database with user data, or a search engine with the products you sell, among others.
     * In a RAG flow that utilizes data from multiple sources, you will likely have multiple
     * {@link EmbeddingStore}s or {@link ContentRetriever}s.
     * While you could route each user query to all available {@link ContentRetriever}s,
     * this approach might be inefficient and counterproductive.
     * <p>
     * "Query routing" is the solution to this challenge. It involves directing a query to the most appropriate
     * {@link ContentRetriever} (or several). Routing can be implemented in various ways:
     * 1. Using rules (e.g., depending on the user's privileges, location, etc.).
     * 2. Using keywords (e.g., if a query contains words X1, X2, X3, route it to {@link ContentRetriever} X, etc.).
     * 3. Using semantic similarity (see EmbeddingModelTextClassifierExample in this repository).
     * 4. Using an LLM to make a routing decision.
     * <p>
     * For scenarios 1, 2, and 3, you can implement a custom {@link QueryRouter}.
     * For scenario 4, this example will demonstrate how to use a {@link LanguageModelQueryRouter}.
     */
    public static void main(String[] args) {
        // Create Chat Language Model Google Vertex AI - PaLM 2
        ChatLanguageModel modelPalm = AiBeans.getChatLanguageModelGoogle(AiConstants.GOOGLE_PALM_CHAT_BISON);
        AiBeans.printModelDetails(AiConstants.LLM_VERTEX, AiConstants.GOOGLE_PALM_CHAT_BISON);
        // Create the Assistant
        // Setting up the Gen AI Context with Open AI LLM, and RAG
        Assistant assistant = RAGBuilder.createAssistantWithQueryRouter(modelPalm);
        // Start the Conversation with Multi Data Source ChatBot
        // - What is the legacy of Akiera Kiera?
        // - When was he born?
        // - I am Sam. Can I cancel my reservation?
        // - Please explain the refund policy.
        ConsoleRunner.startConversationWith(assistant, "Multi-Data Source Query Routing Service");
    }
}
