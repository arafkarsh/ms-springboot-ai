/**
 * Copyright (c) 2025 Araf Karsh Hamid
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * <p>
 * This program and the accompanying materials are dual-licensed under
 * either the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation
 * <p>
 * or (per the licensee's choosing)
 * <p>
 * under the terms of the Apache 2 License version 2.0
 * as published by the Apache Software Foundation.
 */
package io.fusion.air.microservice.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * ms-springboot-324-ai / AiConfig 
 *
 * @author: Araf Karsh Hamid
 * @version: 0.1
 * @date: 2025-01-04T3:18 PM
 */
@Component
@Configuration
@PropertySource(
        name = "aiConfig",
        // Expects file in the directory the jar is executed
        value = "file:./application.properties")
// Expects the file in src/main/resources folder
// value = "classpath:application.properties")
// value = "classpath:application2.properties,file:./application.properties")
public class AiConfig {

    // langchain4j.open-ai.model=gpt-4o-2024-05-13
    @Value("${langchain4j.open-ai.model:gpt-4o-2024-05-13}")
    private String modelGPT;

    // langchain4j.ollama.url=http://localhost:11434/api/generate/
    @Value("${langchain4j.ollama.url:http://localhost:11434/api/generate/}")
    private String ollamaURL;

    // langchain4j.ollama.model=llama3
    @Value("${langchain4j.ollama.model:llama3}")
    private String modelOllama;

    // langchain4j.cohere-api-key
    @Value("${langchain4j.cohere-api-key}")
    private String cohereAPIKey;

    // langchain4j.open-ai.chat-model.api-key
    @Value("${langchain4j.open-ai.chat-model.api-key}")
    private String openAIKey;

    // langchain4j.anthropic.api.key
    @Value("${langchain4j.anthropic.api.key}")
    private String anthropicAPIKey;

    // langchain4j.open-ai.chat-model.log-requests = true
    @Value("${langchain4j.open-ai.chat-model.log-requests:true}")
    private boolean logRequests;

    // langchain4j.open-ai.chat-model.log-responses = true
    @Value("${langchain4j.open-ai.chat-model.log-responses:true}")
    private boolean logResponses;

    // logging.level.dev.langchain4j = DEBUG
    @Value("${logging.level.dev.langchain4j:DEBUG}")
    private String logLevel;

    // logging.level.dev.ai4j.openai4j = DEBUG
    @Value("${logging.level.dev.ai4j.openai4j:DEBUG}")
    private String openAILogLevel;

}
