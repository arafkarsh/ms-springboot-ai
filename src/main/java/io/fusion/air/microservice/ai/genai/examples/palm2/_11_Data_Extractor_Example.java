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
import dev.langchain4j.service.AiServices;
import io.fusion.air.microservice.ai.genai.core.assistants.ChefAssistant;
import io.fusion.air.microservice.ai.genai.core.assistants.DataExtractorAssistant;
import io.fusion.air.microservice.ai.genai.core.models.Person;
import io.fusion.air.microservice.ai.genai.core.models.Recipe;
import io.fusion.air.microservice.ai.genai.core.prompts.StructuredPromptRecipe;
import io.fusion.air.microservice.ai.genai.utils.AiBeans;
import io.fusion.air.microservice.ai.genai.utils.AiConstants;
import io.fusion.air.microservice.utils.Std;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static io.fusion.air.microservice.ai.genai.utils.AiUtils.ERROR_MESSAGE;
import static java.util.Arrays.asList;

/**
 * Data Extractor
 *
 * 1. Extract Numbers
 * 2. Extract Date
 * 3. Extract Time
 * 4. Extract Date and Time
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public class _11_Data_Extractor_Example {

    public static void numberExtractor( DataExtractorAssistant palmDataExtractor ) {
        // Extract Numbers
        String request = """
                    After countless millennia of computation, the supercomputer Deep Thought 
                    finally announced that the answer to the ultimate question of life, the 
                    universe, and everything was forty two.""";

        int intNumber = palmDataExtractor.extractInt(request);
        AiBeans.printResult(request, "Number = "+intNumber);
    }

    public static void dateTimeExtractor(DataExtractorAssistant palmDataExtractor ) {
        // Extract Date and Time
        StringBuilder sb = new StringBuilder();
        String request = """
                    The tranquility pervaded the evening of 1968, just fifteen minutes 
                    shy of midnight, following the celebrations of Independence Day.""";

        LocalDate date = palmDataExtractor.extractDateFrom(request);
        sb.append("Date      = ").append(date).append("\n");
        LocalTime time = palmDataExtractor.extractTimeFrom(request);
        sb.append("Time      = ").append(time).append("\n");
        LocalDateTime dateTime = palmDataExtractor.extractDateTimeFrom(request);
        sb.append("DateTime = ").append(dateTime);

        AiBeans.printResult(request, sb.toString());
    }

    public static void pojoExtractor( DataExtractorAssistant palmDataExtractor ) {
        // POJO Person Extractor
        String request = """
                In 1968, amidst the fading echoes of Indian Independence Day, 
                a child named John arrived under the calm evening sky. 
                This newborn, bearing the surname Doe, marked the start of a new journey.""";

        Person person = palmDataExtractor.extractPersonFrom(request);
        AiBeans.printResult(request, person.toString());
    }

    public static void complexPojoExtractor(ChatLanguageModel palmChatModel) {
        ChefAssistant chefAssistant = AiServices.create(ChefAssistant.class, palmChatModel);
        Recipe recipe = chefAssistant.createRecipeFrom("cucumber", "tomato", "feta", "onion", "olives", "lemon");
        Std.println(recipe);

        StructuredPromptRecipe recipe2 = new StructuredPromptRecipe("oven dish",
                asList("cucumber", "tomato", "feta", "onion", "olives", "potatoes") );

        Recipe anotherRecipe = chefAssistant.createRecipe(recipe2);
        Std.println(anotherRecipe);
    }

    public static void main(String[] args) {
        // Create Chat Language Model Google Vertex AI - PaLM 2
        ChatLanguageModel palmChatModel = AiBeans.getChatLanguageModelGoogle(AiConstants.GOOGLE_PALM_CHAT_BISON);
        AiBeans.printModelDetails(AiConstants.LLM_VERTEX, AiConstants.GOOGLE_PALM_CHAT_BISON);
        DataExtractorAssistant palmDataExtractor = AiServices.create(DataExtractorAssistant.class, palmChatModel);
        try {
            Std.println("Number Extractor =================================================");
            // Extract Numbers
            numberExtractor(palmDataExtractor);
        } catch (Exception e) {
            Std.println(ERROR_MESSAGE+e.getMessage());
        }
        try {
            Std.println("Date & Time Extractor =============================================");
            // Extract Date and Time
            dateTimeExtractor(palmDataExtractor);
        } catch (Exception e) {
            Std.println(ERROR_MESSAGE+e.getMessage());
        }
        try {
            Std.println("Pojo Extractor ====================================================");
            // POJO Person Extractor
             pojoExtractor(palmDataExtractor);
        } catch (Exception e) {
            Std.println(ERROR_MESSAGE+e.getMessage());
        }
        try {
            Std.println("Complex Pogo Extractor ============================================");
            // Complex Pojo Extractor with Descriptions (rules)
            complexPojoExtractor(palmChatModel);
        } catch (Exception e) {
            Std.println(ERROR_MESSAGE+e.getMessage());
        }
    }
}
