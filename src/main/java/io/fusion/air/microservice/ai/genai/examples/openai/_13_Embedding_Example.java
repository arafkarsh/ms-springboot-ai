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
package io.fusion.air.microservice.ai.genai.examples.openai;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import io.fusion.air.microservice.ai.genai.utils.AiBeans;
import io.fusion.air.microservice.ai.genai.utils.AiConstants;
import dev.langchain4j.model.huggingface.HuggingFaceEmbeddingModel;
import io.fusion.air.microservice.utils.Std;

import static java.time.Duration.ofSeconds;
import java.util.List;
/**
 * Chat Memory Examples
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public class _13_Embedding_Example {

    /**
     * In Memory Embedding Example
     */
    private static void inMemoryEmbeddingExample(String data1, String data2, String request) {
        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();
        // Set Data 1
        TextSegment segment1 = TextSegment.from(data1);
        Embedding embedding1 = embeddingModel.embed(segment1).content();
        embeddingStore.add(embedding1, segment1);
        // Set Data 2
        TextSegment segment2 = TextSegment.from(data2);
        Embedding embedding2 = embeddingModel.embed(segment2).content();
        embeddingStore.add(embedding2, segment2);
        // Embed Query Request
        Embedding queryEmbedding = embeddingModel.embed(request).content();
        List<EmbeddingMatch<TextSegment>> relevant = embeddingStore.findRelevant(queryEmbedding, 1);
        EmbeddingMatch<TextSegment> embeddingMatch = relevant.get(0);
        // Show the Score and Matched Response
        Std.println("--[Data]-----------------------------------------------------------");
        Std.println("Data 1: "+data1);
        Std.println("Data 2: "+data2);
        AiBeans.printResult(request,
                "Score:  "+embeddingMatch.score()
                          +"\nResult: "+embeddingMatch.embedded().text());
    }

    /**
     * Hugging Face Embedding Model
     */
    private static void huggingFaceEmbeddingModelExample() {
        EmbeddingModel embeddingModel = HuggingFaceEmbeddingModel.builder()
                .accessToken(AiConstants.HF_API_KEY)
                .modelId("sentence-transformers/all-MiniLM-L6-v2")
                .waitForModel(true)
                .timeout(ofSeconds(60))
                .build();
        String request = "Hello, how are you?";
        Response<Embedding> response = embeddingModel.embed(request);
        AiBeans.printResult(request, response.toString());
    }

    public static void main(String[] args) {
        // Create Chat Language Model - Open AI GPT 4o
        ChatLanguageModel model = AiBeans.getChatLanguageModelOpenAi(AiConstants.GPT_4o);
        AiBeans.printModelDetails(AiConstants.LLM_OPENAI, AiConstants.GPT_4o);
        // InMemory Embedding Example
        inMemoryEmbeddingExample(
                "I like football, Chess, Tennis and Cricket. However, I like Cricket most!",
                "The weather is good today. It's neither hot nor cold.",
                "What is your favourite sport?");

        inMemoryEmbeddingExample(
                "I like movies. My favorite genre is Sci-Fi. I am not a sports person.",
                "The weather is good today. It's neither hot nor cold.",
                "What is your favourite sport?");

        huggingFaceEmbeddingModelExample();
    }
}
