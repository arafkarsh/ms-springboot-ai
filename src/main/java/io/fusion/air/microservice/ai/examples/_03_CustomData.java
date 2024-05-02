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
package io.fusion.air.microservice.ai.examples;

import dev.langchain4j.chain.ConversationalRetrievalChain;
import io.fusion.air.microservice.ai.utils.AiBeans;
import io.fusion.air.microservice.ai.utils.CustomDataAnalyzer;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public class _03_CustomData {

    public static void main(String[] args) {

        String request1 = """
            Who were the Key Characters in the movie Bramayugam?
            What was the rating?
            Elaborate the Characters in the movie.
            """;
        CustomDataAnalyzer.processFile(request1,  "bramayugam.txt");

        String request2 = """
                Elaborate the key ideas behind the movie.
                Elaborate each stage (in bullet points) in the movie and its significance.
                What was the movie rating?
                """;
        CustomDataAnalyzer.processFile(request2,  "vaaliban.txt");

        System.exit(0);
    }
}
