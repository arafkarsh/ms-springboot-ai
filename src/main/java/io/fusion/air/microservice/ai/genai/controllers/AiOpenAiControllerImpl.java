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
// Custom
import io.fusion.air.microservice.domain.ports.services.ChatMessageService;
// Swagger
import io.swagger.v3.oas.annotations.tags.Tag;
// Spring
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;

/**
 * Open AI Controller for the Service
 *
 * Only Selected Methods will be secured in this packaged - which are Annotated with
 * @AuthorizationRequired
 * @Operation(summary = "Cancel Product", security = { @SecurityRequirement(name = "bearer-key") })
 * 
 * @author arafkarsh
 * @version 1.0
 * 
 */
@Configuration
@RestController
// "/ms-ai/api/v1"
@RequestMapping("${service.api.path}/ai/openai")
@Tag(name = "AI - OpenAi", description = "GPT 3.5, GPT 3.5 Turbo, GPT 4, GPT 4o, Dall-E 3")
public class AiOpenAiControllerImpl extends AbstractAiController {

	/**
	 * Auto Wire the Language Model
	 * Loading the Bean with the name ChatLanguageModelGPT (defined in AiBeans).
	 * The Qualifier is to ensure that the right Bean is Autowired.
	 *
	 * @param chatLanguageModel
	 */
	public AiOpenAiControllerImpl(@Qualifier("chatLanguageModelGPT")
							ChatLanguageModel chatLanguageModel, ChatMessageService chatMessageService) {
		super(chatLanguageModel, chatMessageService);
	}
 }