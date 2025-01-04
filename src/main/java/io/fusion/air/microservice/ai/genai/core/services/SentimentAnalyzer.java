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

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import io.fusion.air.microservice.ai.genai.core.assistants.SentimentAssistant;
import io.fusion.air.microservice.ai.genai.core.models.Sentiment;
import io.fusion.air.microservice.ai.genai.utils.AiBeans;

/**
 * Sentiment Analyzer
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public class SentimentAnalyzer {

    private SentimentAnalyzer() {}

    /**
     * Analyze the Sentiment of a text.
     * Rating based on Positive, Neutral and Negative
     * Analyze the feelings of the user based on the content.
     *
     * @param request
     * @return
     */
    public static String analyzeSentiment(String request) {
        ChatLanguageModel model = new AiBeans().createChatLanguageModelOpenAi();
        return analyzeSentiment(request, model, false);
    }

    /**
     * Analyze the Sentiment of a text.
     * Rating based on Positive, Neutral and Negative
     * Analyze the feelings of the user based on the content.
     *
     * @param request
     * @param print
     * @return
     */
    public static String analyzeSentiment(String request, boolean print) {
        ChatLanguageModel model = new AiBeans().createChatLanguageModelOpenAi();
        return analyzeSentiment( request, model, print);
    }

    /**
     * Analyze the Sentiment of a text.
     * Rating based on Positive, Neutral and Negative
     * Analyze the feelings of the user based on the content.
     *
     * @param request
     * @param model
     * @param print
     * @return
     */
    public static String analyzeSentiment(String request, ChatLanguageModel model,  boolean print) {
        if(request == null || model == null) {
            return "Invalid Inputs";
        }
        SentimentAssistant sentimentAssistant = AiServices.create(SentimentAssistant.class, model);
        Sentiment sentiment = sentimentAssistant.analyzeSentimentOf(request);
        boolean positive = sentimentAssistant.isPositive(request);
        String feelings = TemplateManager.structuredPromptFeelings(sentiment.name(), request);
        String response = "Rating of the content = "+sentiment.name() + "\n"
                        +"Is Sentiment Positive?     = "+((positive) ? "Yes" : "No")
                        +"\n"+feelings;
        if(print) {
            AiBeans.printResult(request,response);
        }
        return response;
    }
}
