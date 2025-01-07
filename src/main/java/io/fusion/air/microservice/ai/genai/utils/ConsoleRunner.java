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
// Custom
import io.fusion.air.microservice.ai.genai.core.assistants.Assistant;
import io.fusion.air.microservice.ai.genai.core.services.ConsoleChatService;
// Spring
import io.fusion.air.microservice.utils.Std;
import io.fusion.air.microservice.utils.Utils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Scanner;

/**
 * Console Runner
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Component
public class ConsoleRunner implements CommandLineRunner {

    public static final String CONSOLE_PROMPT = "Type exit or [q]uit, to QUIT the Prompt.";
    public static final String CONSOLE_DLINE  = "==========================================================================================";
    public static final String CONSOLE_SLINE  = "--------------------------------------------------------------------------------------------";
    /**
     * DownloadAllData the Chat GPT in Console with Custom Data, Image Creation etc.
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        try (Scanner scanner = new Scanner(System.in)) {
            Std.println(CONSOLE_DLINE);
            Std.println("Ask your questions to HAL 9000. LLM >> "+AiConstants.getOpenAIDefaultModel());
            Std.println("To create an image, prefix the text with IMAGE:");
            Std.println("To Analyze & Search Custom Data, prefix the text with CUSTOM:");
            Std.println("To Get Structured data use [P1 for Recipe, [P2 for Movies etc.. ");
            Std.println("Prompt Examples:");
            Std.println("Recipe = [P1: oven dish, cucumber, potato, tomato, red meat, olives, olive oil");
            Std.println("Movies = [P2: Bramayugam OR Malaikotai Vaaliban");
            Std.println(CONSOLE_PROMPT);
            Std.println(CONSOLE_SLINE);
            ConsoleChatService chatAssistant = new ConsoleChatService();
            while (true) {
                if( handleUserRequests( chatAssistant,  scanner) < 0) break;
            }
        }
    }

    /**
     * Start a Command Line Conversation with an Gen AI Assistant
     *
     * @param assistant
     */
    public static void startConversationWith(Assistant assistant) {
        startConversationWith(assistant, "OZAZO Car Rental Service ChatBot....");
    }

    /**
     * Start a Command Line Conversation with an Gen AI Assistant
     *
     * @param assistant
     * @param header
     */
    public static void startConversationWith(Assistant assistant, String header) {
        if(assistant == null || header == null) {
            Std.println("Invalid Inputs!!");
            return;
        }
        try (Scanner scanner = new Scanner(System.in)) {
            Std.println(CONSOLE_DLINE);
            Std.println(header);
            Std.println(CONSOLE_PROMPT);
            Std.println(CONSOLE_SLINE);
            while (true) {
                if( handleUserRequests( assistant,  scanner) < 0) break;
            }
        }
    }

    /**
     * Conversation with Structured Prompts
     * @param assistant
     * @param header
     */
    public static void startConversationWithPrompts(Assistant assistant, String header) {
        if(assistant == null || header == null) {
            Std.println("Invalid Inputs!!");
            return;
        }
        try (Scanner scanner = new Scanner(System.in)) {
            Std.println(CONSOLE_DLINE);
            Std.println(header);
            Std.println("Search Examples:");
            Std.println("I need the diagnosis history of Akiera Kiera for the past 3 years");
            Std.println("I need the diagnosis history of Jane Susan Wood for the past 4 years");
            Std.println("Prompts Examples: ");
            Std.println("[P: Patient Name");
            Std.println("[P: Patient Name, Disease");
            Std.println(CONSOLE_PROMPT);
            Std.println(CONSOLE_SLINE);
            while (true) {
                if( handleUserRequests( assistant,  scanner) < 0) break;
            }
        }
    }

    /**
     * Handle User Requests
     * @param assistant
     * @param scanner
     * @return
     */
    private static byte handleUserRequests(Assistant assistant, Scanner scanner) {
        Std.print("User: >>> ");
        if(assistant == null || scanner == null) {
            Std.println("Invalid Inputs... Quiting the chat..... Have a nice day!");
            return -2;
        }
        String userQuery = scanner.nextLine();
        if (userQuery == null || userQuery.isEmpty()) return 1;
        Std.println(CONSOLE_SLINE);
        if ("exit".equalsIgnoreCase(userQuery) || "quit".equalsIgnoreCase(userQuery)
                || "q".equalsIgnoreCase(userQuery)) {
            Std.println("Quiting the chat..... Have a nice day!");
            return -1;
        }
        try {
            long sTime = System.currentTimeMillis();
            String response = assistant.chat(userQuery);
            Std.println("--[HAL9000]---------------------------------------------------------------------------");
            Std.println(response);
            Std.println(CONSOLE_SLINE);
            calculateRunTime(sTime);
        } catch (Exception e) {
            Std.println("Chat Error: "+e.getMessage());
        }
        return 0;
    }

    private static void calculateRunTime(long sTime) {
        long diffTime = System.currentTimeMillis() - sTime;
        Std.println("Time HH:MM:SS = "+ Utils.printTimeHoursMinsSeconds(diffTime) + " Milli Seconds = "+diffTime);
        Std.println(CONSOLE_SLINE);
    }
}
