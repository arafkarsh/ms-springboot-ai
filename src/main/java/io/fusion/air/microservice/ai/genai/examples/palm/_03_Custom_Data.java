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
package io.fusion.air.microservice.ai.genai.examples.palm;

import dev.langchain4j.model.chat.ChatLanguageModel;
import io.fusion.air.microservice.ai.genai.core.services.CustomDataAnalyzer;
import io.fusion.air.microservice.ai.genai.utils.AiBeans;
import io.fusion.air.microservice.ai.genai.utils.AiConstants;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public class _03_Custom_Data {

    public static void main(String[] args) {
        // Create Chat Language Model Google Vertex AI - PaLM 2
        ChatLanguageModel model = AiBeans.getChatLanguageModelGoogle(AiConstants.GOOGLE_PALM_CHAT_BISON);
        AiBeans.printModelDetails(AiConstants.LLM_VERTEX, AiConstants.GOOGLE_PALM_CHAT_BISON);

        String request1 = """
            Who were the Key Characters in the movie Bramayugam?
            What was the rating?
            Elaborate the Characters in the movie.
            """;
        CustomDataAnalyzer.processFile(request1,  "bramayugam.txt", model);

        String request2 = """
                Elaborate the key ideas behind the movie Malaikotai Vaaliban.
                Elaborate each stage (in bullet points) in the movie and its significance.
                What was the movie rating?
                """;
        CustomDataAnalyzer.processFile(request2,  "vaaliban.txt", model);

        // Multi File Analysis
        CustomDataAnalyzer.processMultiFiles("Malaikotai Vaaliban", model);
        CustomDataAnalyzer.processMultiFiles("Bramayugam", model);

        System.exit(0);
    }
}
