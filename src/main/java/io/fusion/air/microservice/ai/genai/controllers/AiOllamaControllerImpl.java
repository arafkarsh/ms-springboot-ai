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
package io.fusion.air.microservice.ai.genai.controllers;

// LangChain

import dev.langchain4j.model.chat.ChatLanguageModel;
import io.fusion.air.microservice.domain.ports.services.ChatMessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Ollama AI Controller for the Service
 *
 * @author arafkarsh
 * @version 1.0
 * 
 */
@Configuration
@RestController
// "/ms-ai/api/v1"
@RequestMapping("${service.api.path}/ai/ollama")
@Tag(name = "AI - Ollama", description = "Llama3, llama2, mistral, codellama, phi or tinyllama")
public class AiOllamaControllerImpl extends AbstractAiController {

	/**
	 * Auto Wire the Language Model
	 * Loading the Bean with the name ChatLanguageModelOllama (defined in AiBeans).
	 * The Qualifier is to ensure that the right Bean is Autowired.
	 *
	 * @param chatLanguageModel
	 */
	public AiOllamaControllerImpl(@Qualifier("ChatLanguageModelOllama")
							ChatLanguageModel chatLanguageModel, ChatMessageService chatMessageService) {
		super(chatLanguageModel, chatMessageService);
	}
 }