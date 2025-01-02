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
package io.fusion.air.microservice.ai.genai.core.assistants;

import dev.langchain4j.service.UserMessage;

import io.fusion.air.microservice.ai.genai.core.models.Sentiment;

/**
 * Sentiment Analyzer
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public interface SentimentAssistant {

    /**
     * Analyze Sentiment of a Text
     * @param text
     * @return
     */
    @UserMessage("Analyze sentiment of {{it}}")
    public Sentiment analyzeSentimentOf(String text);

    /**
     * Returns True if the Sentiment is Positive
     * @param text
     * @return
     */
    @UserMessage("Does {{it}} have a positive sentiment?")
    public boolean isPositive(String text);
}
