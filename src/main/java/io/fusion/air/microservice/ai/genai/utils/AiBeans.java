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
package io.fusion.air.microservice.ai.genai.utils;
// LangChain
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.anthropic.AnthropicChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiImageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiModerationModel;
import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;
import dev.langchain4j.service.AiServices;
// Custom
import io.fusion.air.microservice.ai.genai.core.assistants.HAL9000Assistant;
// Spring
import io.fusion.air.microservice.server.config.AiConfig;
import io.fusion.air.microservice.utils.Std;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
// Java
import java.time.Duration;
import static java.time.Duration.ofSeconds;

/**
 * Ai Beans
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Component
public class AiBeans {

    private final AiConfig aiConfig;

    public AiBeans() {
        aiConfig = new AiConfig();
    }

    /**
     * Default Constructor
     */
    @Autowired
    public AiBeans(AiConfig aiConfig) {
        this.aiConfig = aiConfig;
    }

    /**
     * Returns AI Configuration
     * @return
     */
    public AiConfig getAiConfig() {
        return aiConfig;
    }

    /**
     * Get the default Language Model (OpenAPI ChatGPT 4o)
     * @return
     */
    public static ChatLanguageModel getDefaultLanguageModel() {
        return new AiBeans().createChatLanguageModelOpenAi();
    }

    /**
     * Get the Open AI Chat Language Model (ChatGPT 4o)
     * @return
     */
    public static ChatLanguageModel getChatLanguageModelOpenAi() {
        return getChatLanguageModelOpenAi(AiConstants.GPT_4o);
    }

    /**
     * Get the OpenAI Language Model
     * 1. GPT 3.5 Turbo
     * 2. GPT 4
     * 3. GPT 4o
     *
     * @param model
     * @return
     */
    public static ChatLanguageModel getChatLanguageModelOpenAi(String model) {
        return new AiBeans().createChatLanguageModelOpenAi(model);
    }

    /**
     * Get the Ollama Chat Language Model (llama)
     * @return
     */
    public static ChatLanguageModel getChatLanguageModelLlama() {
        return getChatLanguageModelLlama(AiConstants.OLLAMA_LLAMA);
    }

    /**
     * Get the Ollama Chat Language Model
     * 1. Llama 3 (llama)
     * 2. Misrtal (mistral)
     * 3. Phi-3 (phi-3)
     * 4. Gemma (gemma)
     *
     * @param model
     * @return
     */
    public static ChatLanguageModel getChatLanguageModelLlama(String model) {
        return new AiBeans().createChatLanguageModelLlama(model);
    }

    /**
     * Get the Anthropic Chat Language Models
     * 1. Claude 3 Haiku
     *
     * @return
     */
    public static ChatLanguageModel getChatLanguageModelAnthropic() {
        return getChatLanguageModelAnthropic(AiConstants.ANTHROPIC_CLAUDE_3_HAIKU);
    }

    /**
     * Get the Anthropic Chat Language Models
     * 1. Claude 3 Haiku
     *
     * @param model
     * @return
     */
    public static ChatLanguageModel getChatLanguageModelAnthropic(String model) {
        return new AiBeans().createChatLanguageModelAnthropic(model);
    }

    /**
     * Get the Google (Vertex AI) Chat Language Model
     * 1. Gemini Pro
     * @return
     */
    public static ChatLanguageModel getChatLanguageModelGoogle() {
        return getChatLanguageModelGoogle(AiConstants.GOOGLE_GEMINI_PRO);
    }

    /**
     * Get the Google (Vertex AI) Chat Language Model
     * 1. Gemini Pro
     * 2. Gemini Flash
     * 3. Gemini Nano
     *
     * @param model
     * @return
     */
    public static ChatLanguageModel getChatLanguageModelGoogle(String model) {
        return new AiBeans().createChatLanguageModelGoogle(model);
    }

    /**
     * Returns Open API Moderation Model
     * @return
     */
    public static OpenAiModerationModel getOpenAiModerationModel() {
        return new AiBeans().createOpenAPIModerationModel();
    }

    // ------------------------------------------------------------------------------------------------

    /**
     * Returns Chat Language Model based on ChatGPT 3.5, 4.0, 4o (Omni)
     * @return
     */
    @Bean(name = "chatLanguageModelGPT")
    public ChatLanguageModel createChatLanguageModelOpenAi() {
        return createChatLanguageModelOpenAi(AiConstants.getOpenAIDefaultModel(), false, false);
    }

    /**
     * Returns Chat Language Model based on ChatGPT 3.5, 4.0, 4o (Omni)
     * @param model
     * @return
     */
    public ChatLanguageModel createChatLanguageModelOpenAi(String model) {
        return createChatLanguageModelOpenAi(model, false, false);
    }

    /**
     * Returns Chat Language Model based on ChatGPT 3.5, 4.0, 4o (Omni)
     * @param req
     * @param res
     * @return
     */
    public ChatLanguageModel createChatLanguageModelOpenAi(boolean req, boolean res) {
        return createChatLanguageModelOpenAi(AiConstants.getOpenAIDefaultModel(), req, res);
    }

    /**
     * Returns Chat Language Model based on ChatGPT 3.5, 4.0, 4o (Omni)
     * @param model
     * @return
     */
    public ChatLanguageModel createChatLanguageModelOpenAi(String model, boolean req, boolean res) {
       return OpenAiChatModel.builder()
                .apiKey(AiConstants.OPENAI_API_KEY)
                // Higher the Temperature, Higher the Randomness.
                // For Accurate deterministic results keep the temperature low
                .temperature(0.0)
                .timeout(ofSeconds(60))
                 // AI Models are defined in AiConstants -  GPT_4_TURBO, GPT_3_5_TURBO
                .modelName(model)
                .logRequests(req)
                .logResponses(res)
                .build();
    }

    /**
     * Returns Chat Language Model based on Llama 3
     * 1. Llama 3
     * @return
     */
    @Bean(name = "chatLanguageModelOllama")
    public ChatLanguageModel createChatLanguageModelLlama() {
        return createChatLanguageModelLlama( AiConstants.OLLAMA_LLAMA);
    }

    /**
     * Returns Chat Language Model based on
     * 1. Llama 3 (llama)
     * 2. Misrtal (mistral)
     * 3. Phi-3 (phi-3)
     * 4. Gemma (gemma)
     * 5. Falcon (falcon)
     * 6. Wizard Math (wizard-math:7b)
     *
     * @param model
     * @return
     */
    public ChatLanguageModel createChatLanguageModelLlama(String model) {
            return OllamaChatModel.builder()
                    .baseUrl("http://localhost:11434/api/generate/")
                    .modelName(model)
                    // Higher the Temperature, Higher the Randomness.
                    // For Accurate deterministic results keep the temperature low
                    .temperature(0.0)
                    .timeout(ofSeconds(120))
                    .build();
    }

    /**
     * Returns Chat Language Model based on
     * 1. Claude 3 Haiku
     *
     * @return
     */
    @Bean(name = "chatLanguageModelAnthropic")
    public ChatLanguageModel createChatLanguageModelAnthropic() {
        return createChatLanguageModelAnthropic( AiConstants.ANTHROPIC_CLAUDE_3_HAIKU);
    }

    /**
     * Returns Chat Language Model based on
     * 1. Claude 3 Haiku
     *
     * @param model
     * @return
     */
    public ChatLanguageModel createChatLanguageModelAnthropic(String model) {
        return  AnthropicChatModel.builder()
                // API key can be created here: https://console.anthropic.com/settings/keys
                .apiKey(AiConstants.ANTHROPIC_API_KEY)
                .modelName(model) // claude-3-haiku-20240307
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    /**
     * Returns Chat Language Model based on Google
     * 1. Gemini Pro
     * 2. Gemini Flash
     * 3. Gemini Nano
     *
     * @return
     */
    @Bean(name = "chatLanguageModelGoogle")
    public ChatLanguageModel createChatLanguageModelGoogle() {
        return createChatLanguageModelGoogle( AiConstants.GOOGLE_GEMINI_PRO);
    }

    /**
     * Returns Chat Language Model based on Google
     * 1. Gemini Pro
     * 2. Gemini Flash
     * 3. Gemini Nano
     *
     * @param model
     * @return
     */
    public ChatLanguageModel createChatLanguageModelGoogle(String model) {
        // Create Chat Language Model - Google Gemini Pro
        return  VertexAiGeminiChatModel.builder()
                .project(AiConstants.GOOGLE_VERTEX_PROJECT_ID)
                .location(AiConstants.GOOGLE_VERTEX_LOCATION)
                .modelName(model)
                // Top-k changes how the model selects tokens for output. A top-k of 1 means the
                // selected token is the most probable among all tokens in the model’s vocabulary
                // (also called greedy decoding), while a top-k of 3 means that the next token is
                // selected from among the 3 most probable tokens (using temperature).
                .topK(1) // Gemini 1.5 Pro/Flash doesn't support this parameter
                // Top-p changes how the model selects tokens for output. Tokens are selected from
                // most probable to least until the sum of their probabilities equals the top-p value.
                // For example, if tokens A, B, and C have a probability of .3, .2, and .1 and the
                // top-p value is .5, then the model will select either A or B as the next token
                // (using temperature).
                .topP(0.95F)
                .maxRetries(1)
                // Temperature controls the randomness in token selection
                // - A lower temperature is good when you expect a true or correct response. A
                //   temperature of 0 means the highest probability token is always selected.
                // - A higher temperature can lead to diverse or unexpected results. Some models
                //   have a higher temperature max to encourage more random responses.
                // The selected model gemini-1.5-pro-preview-0514 has a temperature range of
                // 0 - 2 and a default of 1
                .temperature(1.0F)
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
     * @param model
     * @return
     */
    public ImageModel createImageModel(String model) {
        return OpenAiImageModel.builder()
                .apiKey(AiConstants.OPENAI_API_KEY)
                .timeout(ofSeconds(60))
                // AI Models are defined in AiConstants -  DALL_E_3, DALL_E_2
                .modelName(model)
                .logRequests(false)
                .logResponses(false)
                .build();
    }

    /**
     * Returns Open API Moderation Model
     * @return
     */
    @Bean(name = "openAPIModerationModel")
    public OpenAiModerationModel createOpenAPIModerationModel() {
        return OpenAiModerationModel.withApiKey(AiConstants.OPENAI_API_KEY);
    }

    /**
     * Returns the Ai Assistant with GPT 3.5 Turbo
     * @return
     */
    @Bean
    public HAL9000Assistant createHAL9000() {
        return createHAL9000(AiConstants.getOpenAIDefaultModel());
    }

    /**
     * Creates Ai Assistant
     * @param model
     * @return
     */
    public HAL9000Assistant createHAL9000(String model) {
        ChatLanguageModel chatLanguageModel = createChatLanguageModelOpenAi(model);
        return AiServices.builder(HAL9000Assistant.class)
                .chatLanguageModel(chatLanguageModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(20))
                // .tools(tool)
                .build();
    }

    /**
     * Create Ai Assistant
     * @param chatLanguageModel
     * @return
     */
    public HAL9000Assistant createHAL9000(ChatLanguageModel chatLanguageModel) {
        return AiServices.builder(HAL9000Assistant.class)
                .chatLanguageModel(chatLanguageModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(20))
                // .tools(tool)
                .build();
    }

    /**
     * This chat memory will be used by an {@link HAL9000Assistant}
     */
    @Bean(name = "simpleChatMemory")
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.withMaxMessages(20);
    }

    // ==============================================================================================
    // Utilities ----------------
    // ==============================================================================================

    /**
     * Extract the Model Name from ChatLanguageModel
     * @param model
     * @return
     */
    public static String getChatLanguageModelName(ChatLanguageModel model) {
        try {
            String[] p1 = model.toString().split("@");
            String[] p2 = p1[0].split("\\.");
            return p2[4];
        } catch (Exception e) {
            Std.println(e.getMessage());
            return "Unable to Extract Model Name from > "+model;
        }
    }

    /**
     * Print Model Details
     * @param llm
     * @param model
     */
    public static void printModelDetails(String llm, String model) {
        Std.println("--[Model]----------------------------------------------------------");
        Std.println(">>> "+llm+" : "+model);
    }

    /**
     * Print Request & Response
     *
     * @param request
     * @param response
     */
    public static void printResult(String request, String response) {
        Std.println("--[Human]----------------------------------------------------------");
        Std.println(request);
        Std.println("--[HAL9000]-------------------------------------------------------");
        Std.println(response);
        Std.println("-------------------------------------------------------------------");
    }

    /**
     * Sleep
     */
    public static void sleep() {
        sleep(45);
    }

    /**
     * Sleep
     * @param seconds
     */
    public static void sleep(long seconds) {
        try {
            Std.println("Sleeping for "+seconds+" Seconds to avoid per minute rate limit issues with Claude LLM...");
            Thread.sleep(Duration.ofSeconds(seconds));
        } catch (InterruptedException e) {
            Std.println("Sleep Interrupted... "+e.getMessage());
            /* Clean up whatever needs to be handled before interrupting  */
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        String test = "dev.langchain4j.model.ollama.OllamaChatModel@138b6bc5";
        String[] p1 = test.split("@");
        Std.println("L: "+p1[0]+" R: "+p1[1]);
        String[] p2 = p1[0].split("\\.");
        Std.println(p2[4]);
    }
}
