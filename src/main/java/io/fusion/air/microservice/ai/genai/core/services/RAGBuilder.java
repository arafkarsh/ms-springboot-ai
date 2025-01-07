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
package io.fusion.air.microservice.ai.genai.core.services;
// LangChain4J
import dev.langchain4j.chain.ConversationalRetrievalChain;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.bge.small.en.v15.BgeSmallEnV15QuantizedEmbeddingModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.scoring.ScoringModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.aggregator.ContentAggregator;
import dev.langchain4j.rag.content.aggregator.ReRankingContentAggregator;
import dev.langchain4j.rag.content.injector.ContentInjector;
import dev.langchain4j.rag.content.injector.DefaultContentInjector;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.rag.query.router.DefaultQueryRouter;
import dev.langchain4j.rag.query.router.LanguageModelQueryRouter;
import dev.langchain4j.rag.query.router.QueryRouter;
import dev.langchain4j.rag.query.transformer.CompressingQueryTransformer;
import dev.langchain4j.rag.query.transformer.QueryTransformer;
import dev.langchain4j.retriever.EmbeddingStoreRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import dev.langchain4j.model.cohere.CohereScoringModel;
// Custom
import io.fusion.air.microservice.ai.genai.core.assistants.Assistant;
import io.fusion.air.microservice.ai.genai.core.assistants.CarRentalAssistant;
import io.fusion.air.microservice.ai.genai.utils.AiBeans;
import io.fusion.air.microservice.ai.genai.utils.AiConstants;
import io.fusion.air.microservice.utils.Std;
import io.fusion.air.microservice.utils.Utils;
// Java
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;
import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocuments;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Arrays.asList;

/**
 * Retrieval-Augmented Generation (RAG) Builder
 * Retrieval-Augmented Generation (RAG) is a natural language processing (NLP) technique that
 * combines generative-based AI models with retrieval-based techniques to improve the output of
 * large language models (LLMs). RAG AI can provide accurate results by using pre-existing
 * knowledge to create unique, context-aware answers, instructions, or explanations in human-like
 * language.
 *
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public class RAGBuilder {

    public static final String OZAZO_CAR_RENTAL_FILE = "ozazo-car-rental-services.txt";

    private RAGBuilder() {}

    /**
     * Get Default Language Model
     * @return
     */
    public static ChatLanguageModel getDefaultLanguageModel() {
        return new AiBeans().createChatLanguageModelOpenAi();
    }
    /**
     * Read and Analyze the Local Data
     * @param fileName
     * @return
     */
    public static ConversationalRetrievalChain createConversationalRetrievalChain(String fileName) {
        return createConversationalRetrievalChain( fileName, AiBeans.getDefaultLanguageModel());
    }
    /**
     * Read and Analyze the Local Data
     *
     * @param fileName
     * @param chatLanguageModel
     * @return
     */
    public static ConversationalRetrievalChain createConversationalRetrievalChain(String fileName,
                                                                                  ChatLanguageModel chatLanguageModel) {
        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(5000, 0))
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        Document document = loadDocument(Utils.toPath("static/data/p/"+fileName), new TextDocumentParser());
        ingestor.ingest(document);

        return ConversationalRetrievalChain.builder()
                .chatLanguageModel(chatLanguageModel)
                .retriever(EmbeddingStoreRetriever.from(embeddingStore, embeddingModel))
                .chatMemory(MessageWindowChatMemory.withMaxMessages(20)) // you can override default chat memory
                // .promptTemplate() // you can override default prompt template
                .build();
    }

    /**
     * Multi Document
     * @return
     */
    public static ConversationalRetrievalChain createMovieDatabaseChain() {
        return createMovieDatabaseChain(AiBeans.getDefaultLanguageModel());
    }

    /**
     * Multi Document
     * @param chatLanguageModel
     * @return
     */
    public static ConversationalRetrievalChain createMovieDatabaseChain(ChatLanguageModel chatLanguageModel) {
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
                .chatLanguageModel(chatLanguageModel)
                .retriever(EmbeddingStoreRetriever.from(embeddingStore, embeddingModel))
                // .promptTemplate(TemplateManager.createMoviePrompt()) // you can override default prompt template
                .chatMemory(MessageWindowChatMemory.withMaxMessages(20)) // you can override default chat memory
                .build();
    }

    // =========================================================================================================================
    // RAG - Retrieval Augmented Generation
    // =========================================================================================================================

    /**
     * Create a Simple RAG for the Car Rental Service
     * @return
     */
    public static Assistant createCarRentalAssistantSimple() {
        return createCarRentalAssistantSimple( AiBeans.getDefaultLanguageModel());
    }

    /**
     * Create a Simple RAG for the Car Rental Service
     * @param model
     * @return
     */
    public static Assistant createCarRentalAssistantSimple(ChatLanguageModel model) {
        // Documents for processing
        List<Document> documents = loadDocuments(Utils.toPath("static/data/e/"), Utils.getPathMatcher("ozazo-car-*.txt"));
        // Embedding Model
        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();
        // Here, we create and empty in-memory store for our documents and their embeddings.
        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        // Here, we are ingesting our documents into the store.
        // Under the hood, a lot of "magic" is happening, but we can ignore it for now.
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();
        ingestor.ingest(documents);
        // Lastly, let's create a content retriever from an embedding store.
        ContentRetriever retriever =  EmbeddingStoreContentRetriever.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        // The final step is to build our AI Service,
        // Return the Assistant
        return  AiServices.builder(Assistant.class)
                .chatLanguageModel(model)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(20))
                .contentRetriever(retriever)
                .build();
    }

    /**
     * RAG - Query Segements
     * @return
     */
    public static CarRentalAssistant createCarRentalAssistantWithSegments() {
        return createCarRentalAssistantWithSegments( AiBeans.getDefaultLanguageModel() );
    }

    /**
     * RAG - Query Segments
     * @param chatLanguageModel
     * @return
     */
    public static CarRentalAssistant createCarRentalAssistantWithSegments(ChatLanguageModel chatLanguageModel ) {
        // Now, let's load a document that we want to use for RAG.
        // We will use the terms of use from an imaginary car rental company, "Miles of Smiles".
        // For this example, we'll import only a single document, but you can load as many as you need.
        // LangChain4j offers built-in support for loading documents from various sources:
        // File System, URL, Amazon S3, Azure Blob Storage, GitHub, Tencent COS.
        // Additionally, LangChain4j supports parsing multiple document types:
        // text, pdf, doc, xls, ppt.
        // However, you can also manually import your data from other sources.
        DocumentParser documentParser = new TextDocumentParser();
        Document document = loadDocument(Utils.toPath("static/data/e/ozazo-car-rental-services.txt"), documentParser);

        // Now, we need to split this document into smaller segments, also known as "chunks."
        // This approach allows us to send only relevant segments to the LLM in response to a user query,
        // rather than the entire document. For instance, if a user asks about cancellation policies,
        // we will identify and send only those segments related to cancellation.
        // A good starting point is to use a recursive document splitter that initially attempts
        // to split by paragraphs. If a paragraph is too large to fit into a single segment,
        // the splitter will recursively divide it by newlines, then by sentences, and finally by words,
        // if necessary, to ensure each piece of text fits into a single segment.
        DocumentSplitter splitter = DocumentSplitters.recursive(300, 0);
        List<TextSegment> segments = splitter.split(document);

        // Now, we need to embed (also known as "vectorize") these segments.
        // Embedding is needed for performing similarity searches.
        // For this example, we'll use a local in-process embedding model, but you can choose any supported model.
        // Langchain4j currently supports more than 10 popular embedding model providers.
        BgeSmallEnV15QuantizedEmbeddingModel embeddingModel = new BgeSmallEnV15QuantizedEmbeddingModel();
        List<Embedding> embeddings = embeddingModel.embedAll(segments).content();

        // Next, we will store these embeddings in an embedding store (also known as a "vector database").
        // This store will be used to search for relevant segments during each interaction with the LLM.
        // For simplicity, this example uses an in-memory embedding store, but you can choose from any supported store.
        // Langchain4j currently supports more than 15 popular embedding stores.
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        embeddingStore.addAll(embeddings, segments);

        // We could also use EmbeddingStoreIngestor to hide manual steps above behind a simpler API.
        // See an example of using EmbeddingStoreIngestor in _01_Advanced_RAG_with_Query_Compression_Example.

        // The content retriever is responsible for retrieving relevant content based on a user query.
        // Currently, it is capable of retrieving text segments, but future enhancements will include support for
        // additional modalities like images, audio, and more.
        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(2) // on each interaction we will retrieve the 2 most relevant segments
                .minScore(0.5) // we want to retrieve segments at least somewhat similar to user query
                .build();

        // Optionally, we can use a chat memory, enabling back-and-forth conversation with the LLM
        // and allowing it to remember previous interactions.
        // Currently, LangChain4j offers two chat memory implementations:
        // MessageWindowChatMemory and TokenWindowChatMemory.
        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(20);

        // The final step is to build our AI Service,
        // configuring it to use the components we've created above.
        return AiServices.builder(CarRentalAssistant.class)
                .chatLanguageModel(chatLanguageModel)
                .contentRetriever(contentRetriever)
                .chatMemory(chatMemory)
                .build();
    }

    /**
     * RAG - Query Transformer
     * @return
     */
    public static Assistant createAssistantWithQueryTransformer() {
        return createAssistantWithQueryTransformer( AiBeans.getDefaultLanguageModel() );
    }

    /**
     * RAG - Query Transformer
     *
     * @param chatLanguageModel
     * @return
     */
    public static Assistant createAssistantWithQueryTransformer(ChatLanguageModel chatLanguageModel) {
        // Load the documents
        Document document = loadDocument(Utils.toPath("static/data/e/akiera-kiera_biography.txt"), new TextDocumentParser());
        EmbeddingModel embeddingModel = new BgeSmallEnV15QuantizedEmbeddingModel();
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(300, 0))
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        ingestor.ingest(document);
        // We will create a CompressingQueryTransformer, which is responsible for compressing
        // the user's query and the preceding conversation into a single, stand-alone query.
        // This should significantly improve the quality of the retrieval process.
        QueryTransformer queryTransformer = new CompressingQueryTransformer(chatLanguageModel);
        // Content Retriever
        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(2)
                .minScore(0.6)
                .build();

        // The RetrievalAugmentor serves as the entry point into the RAG flow in LangChain4j.
        // It can be configured to customize the RAG behavior according to your requirements.
        // In subsequent examples, we will explore more customizations.
        RetrievalAugmentor retrievalAugmentor = DefaultRetrievalAugmentor.builder()
                .queryTransformer(queryTransformer)
                .contentRetriever(contentRetriever)
                .build();

        return AiServices.builder(Assistant.class)
                .chatLanguageModel(chatLanguageModel)
                .retrievalAugmentor(retrievalAugmentor)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(20))
                .build();
    }

    /**
     * RAG - Query Router
     * @return
     */
    public static Assistant createAssistantWithQueryRouter() {
        return createAssistantWithQueryRouter( AiBeans.getDefaultLanguageModel() );
    }

    /**
     * RAG - Query Router
     * @param chatLanguageModel
     * @return
     */
    public static Assistant createAssistantWithQueryRouter(ChatLanguageModel chatLanguageModel) {
        // Load the documents
        EmbeddingModel embeddingModel = new BgeSmallEnV15QuantizedEmbeddingModel();
        // Create Embedding Store for Biography
        EmbeddingStore<TextSegment> biographyES =
                createEmbeddingStore("akiera-kiera_biography.txt", embeddingModel);
        // Content Retriever for Biography
        ContentRetriever biographyCR = createContentRetriever(biographyES, embeddingModel);
        // Create Embedding Store for Car Rental Service
        EmbeddingStore<TextSegment> rentalES =
                createEmbeddingStore(OZAZO_CAR_RENTAL_FILE, embeddingModel);
        //Content Retriever for Car Rental Service.
        ContentRetriever rentalCR = createContentRetriever(rentalES, embeddingModel);
        // Let's create a query router.
        Map<ContentRetriever, String> retrieverToDescription = new HashMap<>();
        retrieverToDescription.put(biographyCR, "Biography of Akiera Kiera");
        retrieverToDescription.put(rentalCR, "Terms of use of Ozazo Car Rental Company");
        QueryRouter queryRouter = new LanguageModelQueryRouter(chatLanguageModel, retrieverToDescription);
        // Retrieval Augmentor
        RetrievalAugmentor retrievalAugmentor = DefaultRetrievalAugmentor.builder()
                .queryRouter(queryRouter)
                .build();
        // Create Assistant
        return AiServices.builder(Assistant.class)
                .chatLanguageModel(chatLanguageModel)
                .retrievalAugmentor(retrievalAugmentor)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();
    }

    /**
     * RAG - ReRanking
     * @return
     */
    public static Assistant createAssistantWithReRanking() {
        return createAssistantWithReRanking( AiBeans.getDefaultLanguageModel() );
    }

    /**
     * RAG - ReRanking
     * @param chatLanguageModel
     * @return
     */
    public static Assistant createAssistantWithReRanking(ChatLanguageModel chatLanguageModel ) {
        // Load the documents
        EmbeddingModel embeddingModel = new BgeSmallEnV15QuantizedEmbeddingModel();
        // Create Embedding Store for Car Rental Service
        EmbeddingStore<TextSegment> rentalES =
                createEmbeddingStore(OZAZO_CAR_RENTAL_FILE, embeddingModel);
        //Content Retriever for Car Rental Service.
        ContentRetriever rentalCR = createContentRetriever(rentalES, embeddingModel);
        // To register and get a free API key for Cohere, please visit the following link:
        // https://dashboard.cohere.com/welcome/register
        ScoringModel scoringModel = CohereScoringModel.withApiKey(AiConstants.COHERE_API_KEY);

        ContentAggregator contentAggregator = ReRankingContentAggregator.builder()
                .scoringModel(scoringModel)
                .minScore(0.8) // we want to present the LLM with only the truly relevant segments for the user's query
                .build();

        RetrievalAugmentor retrievalAugmentor = DefaultRetrievalAugmentor.builder()
                .contentRetriever(rentalCR)
                .contentAggregator(contentAggregator)
                .build();

        return AiServices.builder(Assistant.class)
                .chatLanguageModel(chatLanguageModel)
                .retrievalAugmentor(retrievalAugmentor)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();
    }

    /**
     * RAG - Meta Data
     * @return
     */
    public static Assistant createAssistantWithMetaData() {
        return createAssistantWithMetaData( AiBeans.getDefaultLanguageModel()  );
    }

    /**
     * RAG - Meta Data
     * @param chatLanguageModel
     * @return
     */
    public static Assistant createAssistantWithMetaData(ChatLanguageModel chatLanguageModel ) {
        // Load the documents
        EmbeddingModel embeddingModel = new BgeSmallEnV15QuantizedEmbeddingModel();
        // Create Embedding Store for Car Rental Service
        EmbeddingStore<TextSegment> rentalES =
                createEmbeddingStore(OZAZO_CAR_RENTAL_FILE, embeddingModel);
        //Content Retriever for Car Rental Service.
        ContentRetriever rentalCR = createContentRetriever(rentalES, embeddingModel);
        // Each retrieved segment should include "file_name" and "index" metadata values in the prompt
        ContentInjector contentInjector = DefaultContentInjector.builder()
                // .promptTemplate(...) // Formatting can also be changed
                .metadataKeysToInclude(asList("file_name", "index"))
                .build();
        // Retrieval Augmentor
        RetrievalAugmentor retrievalAugmentor = DefaultRetrievalAugmentor.builder()
                .contentRetriever(rentalCR)
                .contentInjector(contentInjector)
                .build();
        // Create Assistant
        return AiServices.builder(Assistant.class)
                .chatLanguageModel(chatLanguageModel)
                .retrievalAugmentor(retrievalAugmentor)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();
    }

    /**
     * RAG - Multiple Content Retrievers
     * @return
     */
    public static Assistant createAssistantWithMultiContentRetrievers() {
        return createAssistantWithMultiContentRetrievers( AiBeans.getDefaultLanguageModel() );
    }

    /**
     * RAG - Multiple Content Retrievers
     * @param chatLanguageModel
     * @return
     */
    public static Assistant createAssistantWithMultiContentRetrievers(ChatLanguageModel chatLanguageModel) {
        // Load the documents
        EmbeddingModel embeddingModel = new BgeSmallEnV15QuantizedEmbeddingModel();
        // Create Embedding Store for Biography
        EmbeddingStore<TextSegment> biographyES =
                createEmbeddingStore("akiera-kiera_biography.txt", embeddingModel);
        // Content Retriever for Biography
        ContentRetriever biographyCR = createContentRetriever(biographyES, embeddingModel);
        // Create Embedding Store for Car Rental Service
        EmbeddingStore<TextSegment> rentalES =
                createEmbeddingStore(OZAZO_CAR_RENTAL_FILE, embeddingModel);
        //Content Retriever for Car Rental Service.
        ContentRetriever rentalCR = createContentRetriever(rentalES, embeddingModel);

        // Let's create a query router that will route each query to both retrievers.
        QueryRouter queryRouter = new DefaultQueryRouter(biographyCR, rentalCR);

        // Retrieval Augmentor
        RetrievalAugmentor retrievalAugmentor = DefaultRetrievalAugmentor.builder()
                .queryRouter(queryRouter)
                .build();
        // Create Assistant
        return AiServices.builder(Assistant.class)
                .chatLanguageModel(chatLanguageModel)
                .retrievalAugmentor(retrievalAugmentor)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();
    }

    /**
     * RAG - Content Retrieval Skipping
     * @return
     */
    public static Assistant createAssistantWithRetrievalSkipping( ) {
        return createAssistantWithRetrievalSkipping( AiBeans.getDefaultLanguageModel()  );
    }

    /**
     * RAG - Content Retrieval Skipping
     * @param chatLanguageModel
     * @return
     */
    public static Assistant createAssistantWithRetrievalSkipping(ChatLanguageModel chatLanguageModel) {
        // Load the documents
        EmbeddingModel embeddingModel = new BgeSmallEnV15QuantizedEmbeddingModel();
        // Create Embedding Store for Car Rental Service
        EmbeddingStore<TextSegment> rentalES =
                createEmbeddingStore(OZAZO_CAR_RENTAL_FILE, embeddingModel);
        //Content Retriever for Car Rental Service.
        ContentRetriever rentalCR = createContentRetriever(rentalES, embeddingModel);

        // Create a query router.
        QueryRouter queryRouter = new QueryRouter() {
            private final PromptTemplate PROMPT_TEMPLATE = PromptTemplate.from(
                    """
                                Is the following query related to the business of the car rental company?
                                Answer only 'allow access', 'skip' or 'maybe allow access'.
                                Query: {{it}}
                            """
            );

            @Override
            public Collection<ContentRetriever> route(Query query) {
                Prompt prompt = PROMPT_TEMPLATE.apply(query.text());
                AiMessage aiMessage = chatLanguageModel.generate(prompt.toUserMessage()).content();
                Std.println(">>> HAL9000 decided to: " + aiMessage.text());
                if (aiMessage.text().toLowerCase().contains("skip")) {
                    return emptyList();
                }
                return singletonList(rentalCR);
            }
        };
        // Retrieval Augmentor
        RetrievalAugmentor retrievalAugmentor = DefaultRetrievalAugmentor.builder()
                .queryRouter(queryRouter)
                .build();
        // Create Assistant
        return AiServices.builder(Assistant.class)
                .chatLanguageModel(chatLanguageModel)
                .retrievalAugmentor(retrievalAugmentor)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();
    }

    // ------------------------------------------------------------------------------------------------------
    /**
     * Create EmbeddingStore<TextSegment>
     *
     * @param documentName
     * @param embeddingModel
     * @return
     */
    private static EmbeddingStore<TextSegment> createEmbeddingStore(String documentName, EmbeddingModel embeddingModel) {
        DocumentParser documentParser = new TextDocumentParser();
        Document document = loadDocument(Utils.toPath("static/data/e/"+ documentName), documentParser);

        DocumentSplitter splitter = DocumentSplitters.recursive(300, 0);
        List<TextSegment> segments = splitter.split(document);

        List<Embedding> embeddings = embeddingModel.embedAll(segments).content();
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        embeddingStore.addAll(embeddings, segments);
        return embeddingStore;
    }

    /**
     * Create Content Retriever
     *
     * @param embeddingStore
     * @param embeddingModel
     * @return
     */
    private static ContentRetriever createContentRetriever(
            EmbeddingStore<TextSegment> embeddingStore, EmbeddingModel embeddingModel) {
        return  EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(2)
                .minScore(0.6)
                .build();
    }
}
