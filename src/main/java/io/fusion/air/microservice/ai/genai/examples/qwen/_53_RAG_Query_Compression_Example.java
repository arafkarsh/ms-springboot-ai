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
public class _53_RAG_Query_Compression_Example {

    /**
     * Please refer to {@link _53_RAG_Query_Compression_Example} for a basic context.
     *
     * This example illustrates the implementation of a more sophisticated RAG application
     * using a technique known as "query compression".
     * Often, a query from a user is a follow-up question that refers back to earlier parts of the conversation
     * and lacks all the necessary details for effective retrieval.
     * For example, consider this conversation:
     * User: What is the legacy of Akiera Kiera?
     * AI: Akiera Kiera was a...
     * User: When was he born?
     * <p>
     * In such scenarios, using a basic RAG approach with a query like "When was he born?"
     * would likely fail to find articles about Akiera Kiera, as it doesn't contain "Akiera Kiera" in the query.
     * Query compression involves taking the user's query and the preceding conversation, then asking the LLM
     * to "compress" this into a single, self-contained query.
     * The LLM should generate a query like "When was Akiera Kiera born?".
     * This method adds a bit of latency and cost but significantly enhances the quality of the RAG process.
     * It's worth noting that the LLM used for compression doesn't have to be the same as the one
     * used for conversation. For instance, you might use a smaller local model trained for summarization.
     */
    public static void main(String[] args) {
        // Create Chat Language Model Ollama Qwen 2.5
        ChatLanguageModel modelQwen = AiBeans.getChatLanguageModelLlama(AiConstants.OLLAMA_QWEN);
        AiBeans.printModelDetails(AiConstants.LLM_OLLAMA, AiConstants.OLLAMA_QWEN);
        // Setting up the Gen AI Context with Open AI LLM, and RAG
        Assistant assistant = RAGBuilder.createAssistantWithQueryTransformer(modelQwen);
        // Start the Conversation with Akiera Kiera Biography ChatBot
        // - What is the legacy of Akiera Kiera?
        // - When was he born?
        ConsoleRunner.startConversationWith(assistant, "Akiera Kiera Biography ChatBot");
    }
}
