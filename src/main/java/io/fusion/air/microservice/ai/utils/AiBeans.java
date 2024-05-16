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
package io.fusion.air.microservice.ai.utils;

import dev.langchain4j.chain.ConversationalRetrievalChain;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiImageModel;
import dev.langchain4j.retriever.EmbeddingStoreRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

// Custom
import io.fusion.air.microservice.ai.examples.assistants.HAL9000Assistant;
// Spring
import io.fusion.air.microservice.utils.Utils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.time.Duration.ofSeconds;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Configuration
public class AiBeans {

    /**
     * Returns Chat Language Model with Default Model GPT 3.5 Turbo
     * @return
     */
    @Bean(name = "ChatLangugeModelGPT")
    public ChatLanguageModel createChatLanguageModel() {
        return createChatLanguageModel(AiConstants.getAlgo(), false, false);
    }

    /**
     * Returns Chat Language Model
     * @param _model
     * @return
     */
    public ChatLanguageModel createChatLanguageModel(String _model) {
        return createChatLanguageModel(_model, false, false);
    }

    /**
     * Returns Chat Language Model
     * @param _model
     * @return
     */
    public ChatLanguageModel createChatLanguageModel(String _model, boolean _req, boolean _res) {
       return OpenAiChatModel.builder()
                .apiKey(AiConstants.OPENAI_API_KEY)
                // Higher the Temperature, Higher the Randomness.
                // For Accurate deterministic results keep the temperature low
                .temperature(0.0)
                .timeout(ofSeconds(60))
                 // AI Models are defined in AiConstants -  GPT_4_TURBO, GPT_3_5_TURBO
                .modelName(_model)
                .logRequests(_req)
                .logResponses(_res)
                .build();
    }

    /**
     * Returns the Image Model
     * @return
     */
    @Bean
    public ImageModel createImageModel() {
        return createImageModel(AiConstants.DALL_E_3);
    }

    /**
     * Returns the Image Model
     * @param _model
     * @return
     */
    public ImageModel createImageModel(String _model) {
        return OpenAiImageModel.builder()
                .apiKey(AiConstants.OPENAI_API_KEY)
                .timeout(ofSeconds(60))
                // AI Models are defined in AiConstants -  DALL_E_3, DALL_E_2
                .modelName(_model)
                .logRequests(false)
                .logResponses(false)
                .build();
    }

    /**
     * Returns the Ai Assistant with GPT 3.5 Turbo
     * @return
     */
    @Bean
    public HAL9000Assistant createHAL9000() {
        return createHAL9000(AiConstants.getAlgo());
    }

    /**
     * Creates Ai Assistant
     * @param _model
     * @return
     */
    public HAL9000Assistant createHAL9000(String _model) {
        ChatLanguageModel chatLanguageModel = createChatLanguageModel(_model);
        return AiServices.builder(HAL9000Assistant.class)
                .chatLanguageModel(chatLanguageModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(20))
                // .tools(tool)
                .build();
    }

    /**
     * Read and Analyze the Data
     * @param _fileName
     * @return
     */
    public ConversationalRetrievalChain createConversationalRetrievalChain(String _fileName) {
        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(5000, 0))
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        Document document = loadDocument(Utils.toPath("static/data/p/"+_fileName), new TextDocumentParser());
        ingestor.ingest(document);

        return ConversationalRetrievalChain.builder()
                .chatLanguageModel(createChatLanguageModel())
                .retriever(EmbeddingStoreRetriever.from(embeddingStore, embeddingModel))
                // .chatMemory() // you can override default chat memory
                // .promptTemplate() // you can override default prompt template
                .build();
    }

    /**
     * Multi Document
     * @return
     */
    public ConversationalRetrievalChain createMovieDatabaseChain() {
        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(5000, 0))
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        Document document1 = loadDocument(Utils.toPath("static/data/p/bramayugam.txt"), new TextDocumentParser());
        ingestor.ingest(document1);
        Document document2 = loadDocument(Utils.toPath("static/data/p/vaaliban.txt"), new TextDocumentParser());
        ingestor.ingest(document2);

        return ConversationalRetrievalChain.builder()
                .chatLanguageModel(createChatLanguageModel())
                .retriever(EmbeddingStoreRetriever.from(embeddingStore, embeddingModel))
                // .promptTemplate(TemplateManager.createMoviePrompt()) // you can override default prompt template
                // .chatMemory() // you can override default chat memory
                .build();
    }

    /**
     * Print Request & Response
     *
     * @param _request
     * @param _response
     */
    public static void printResult(String _request, String _response) {
        System.out.println("--[Human]----------------------------------------------------------");
        System.out.println(_request);
        System.out.println("--[HAL9000Assistant]-------------------------------------------------------");
        System.out.println(_response);
        System.out.println("-------------------------------------------------------------------");
    }
}
