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
import io.fusion.air.microservice.ai.genai.utils.AiBeans;
import io.fusion.air.microservice.domain.entities.example.ChatMessageEntity;
import io.fusion.air.microservice.ai.genai.core.services.CustomDataAnalyzer;
import io.fusion.air.microservice.ai.genai.core.services.TemplateManager;
import io.fusion.air.microservice.ai.genai.utils.AiConstants;
import io.fusion.air.microservice.domain.exceptions.DataNotFoundException;
import io.fusion.air.microservice.domain.models.core.StandardResponse;
import io.fusion.air.microservice.domain.ports.services.ChatMessageService;
import io.fusion.air.microservice.server.controllers.AbstractController;
// Swagger
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
// Spring
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// Java
import java.util.LinkedHashMap;
import java.util.List;
import org.slf4j.Logger;
import org.springframework.web.util.HtmlUtils;
import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Abstract Ai Controller for the Service
 *
 * @author arafkarsh
 * @version 1.0
 * 
 */
public abstract class AbstractAiController extends AbstractController {

	// Set Logger -> Lookup will automatically determine the class name.
	private static final Logger log = getLogger(lookup().lookupClass());

	// Autowired using Constructor Injection
	private ChatMessageService chatMessageService;

	// Chat Language Model is automatically injected by the constructor
	// based on the Qualifier "ChatLanguageModelGPT"
	private final ChatLanguageModel chatLanguageModel;

	private static final String UNABLE_TO_FETCH_DATA = "Unable to Fetch data... !";

	/**
	 * Auto Wire the Language Model
	 * Loading the Bean with the name ChatLanguageModelGPT (defined in AiBeans).
	 * The Qualifier is to ensure that the right Bean is Autowired.
	 *
	 * @param chatLanguageModel
	 */
	protected AbstractAiController(ChatLanguageModel chatLanguageModel, ChatMessageService chatMessageService) {
		this.chatLanguageModel = chatLanguageModel;
		this.chatMessageService = chatMessageService;
	}

	/**
	 * Create the AI Chat Conversation
	 */
	@Operation(summary = "AI Chat - Generic")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "AI Conversations",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "404",
					description = "Unable to Create the Chat Response",
					content = @Content)
	})
	@PostMapping("/chat")
	public ResponseEntity<StandardResponse> chat( @RequestBody String msg) {
		msg = HtmlUtils.htmlEscape(msg);
		log.info("|Chat Request to AI... {} ...  {} ",getModelName(), msg);
		String response = chatLanguageModel.generate(msg);
		if(response != null) {
			return ResponseEntity.ok(createResponse(response, msg));
		}
		throw new DataNotFoundException(UNABLE_TO_FETCH_DATA);
	}

	@Operation(summary = "AI Chat - Custom Data")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = """
							Who were the Key Characters in the movie Bramayugam?
            				What was the rating?
            				Elaborate the Characters in the movie.
							""",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "404",
					description = "Unable to Create the Chat Response",
					content = @Content)
	})
	@PostMapping("/chat/custom")
	public ResponseEntity<StandardResponse> chatCustomData(@RequestBody String msg) {
		msg = HtmlUtils.htmlEscape(msg);
		log.info("|Custom Chat Request to AI Engine {} ...{} ",getModelName(), msg);
		String response = CustomDataAnalyzer.processFile(msg);
		if(response != null) {
			return ResponseEntity.ok(createResponse(response, msg));
		}
		throw new DataNotFoundException(UNABLE_TO_FETCH_DATA);
	}

	@Operation(summary = "AI Chat - Structured Data - Recipe")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Recipe: oven dish, cucumber, potato, tomato, salmon, olives, olive oil",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "404",
					description = "Unable to Create the Chat Response",
					content = @Content)
	})
	@PostMapping("/chat/structured")
	public ResponseEntity<StandardResponse> chatStructuredData(@RequestBody String msg) {
		msg = HtmlUtils.htmlEscape(msg);
		log.info("|Structured Chat Request to AI Engine {} ... {} ",getModelName(), msg);
		String response = TemplateManager.structuredTemplate("[P1: "+msg);
		if(response != null) {
			return ResponseEntity.ok(createResponse(response, msg));
		}
		throw new DataNotFoundException(UNABLE_TO_FETCH_DATA);
	}

	/**
	 * GET Method Call to ChatMessages by User Id
	 *
	 * @return
	 */
	@Operation(summary = "Get the ChatMessage By User ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "ChatMessages Retrieved for User ID",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "400",
					description = "Invalid User ID.",
					content = @Content)
	})
	@GetMapping("/chat/userid/{userId}")
	public ResponseEntity<StandardResponse> getProductStatus(@PathVariable("userId") String userId)  {
		userId = HtmlUtils.htmlEscape(userId);
		log.info("|Request to Get ChatMessages by User ID.. {} ... {} ",getModelName(), userId);
		List<ChatMessageEntity> chats = chatMessageService.fetchByUserId(userId);
		if(chats.isEmpty()) {
			StandardResponse stdResponse = createSuccessResponse("Chats Fetch Success!");
			stdResponse.setPayload(chats);
			return ResponseEntity.ok(stdResponse);
		}
		throw new DataNotFoundException("Chats not found for User Id "+ userId);
	}

	/**
	 * Create Response as Standard Response
	 *
	 * @param response
	 * @param msg
	 * @return
	 */
	private StandardResponse createResponse(String response, String msg) {
		response = HtmlUtils.htmlEscape(response);
		msg = HtmlUtils.htmlEscape(msg);
		String[] rows = response.split("\n");
		StandardResponse stdResponse = createSuccessResponse("AI Response");
		LinkedHashMap<String, Object> data = new LinkedHashMap<>();
		data.put("Model", getModelName());
		data.put("Request", msg);
		data.put("Response", rows);
		stdResponse.setPayload(data);
		return stdResponse;
	}

	// ======================================================================================
	// =========  Add More Methods for the String Controller Here ============================
	// ======================================================================================

	/**
	 * Create the AI Chat Conversation
	 */
	@Operation(summary = "AI Chat - Generic (String) ")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "AI Conversations",
					content = {@Content(mediaType = "application/text")}),
			@ApiResponse(responseCode = "404",
					description = "Unable to Create the Chat Response",
					content = @Content)
	})
	@PostMapping("/string/chat")
	public String chatString( @RequestBody String msg) {
		msg = HtmlUtils.htmlEscape(msg);
		log.info("|Chat Request to AI...  {} ...  {} ",getModelName(), msg);
		String response = chatLanguageModel.generate(msg);
		if(response != null) {
			return createResponseString(response, msg);
		}
		throw new DataNotFoundException(UNABLE_TO_FETCH_DATA);
	}

	@Operation(summary = "AI Chat - Custom Data (String) ")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = """
							Who were the Key Characters in the movie Bramayugam?
            				What was the rating?
            				Elaborate the Characters in the movie.
							""",
					content = {@Content(mediaType = "application/text")}),
			@ApiResponse(responseCode = "404",
					description = "Unable to Create the Chat Response",
					content = @Content)
	})
	@PostMapping("/string/chat/custom")
	public String chatCustomDataString(@RequestBody String msg) {
		msg = HtmlUtils.htmlEscape(msg);
		log.info("|Custom Chat Request to AI Engine {} ... {} ",getModelName(), msg);
		String response = CustomDataAnalyzer.processFile(msg);
		if(response != null) {
			return createResponseString(response, msg);
		}
		throw new DataNotFoundException(UNABLE_TO_FETCH_DATA);
	}

	@Operation(summary = "AI Chat - Structured Data - Recipe (String) ")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Recipe: oven dish, cucumber, potato, tomato, salmon, olives, olive oil",
					content = {@Content(mediaType = "application/text")}),
			@ApiResponse(responseCode = "404",
					description = "Unable to Create the Chat Response",
					content = @Content)
	})
	@PostMapping("/string/chat/structured")
	public String chatStructuredDataString(@RequestBody String msg) {
		msg = HtmlUtils.htmlEscape(msg);
		log.info("|Structured Chat Request to AI Engine {} ... {} ",getModelName(), msg);
		String response = TemplateManager.structuredTemplate("[P1: "+msg);
		if(response != null) {
			return createResponseString(response, msg);
		}
		throw new DataNotFoundException(UNABLE_TO_FETCH_DATA);
	}

	/**
	 * Create Response as Standard Response
	 *
	 * @param response
	 * @param msg
	 * @return
	 */
	private String createResponseString(String response, String msg) {
		response = HtmlUtils.htmlEscape(response);
		msg = HtmlUtils.htmlEscape(msg);
		StringBuilder sb = new StringBuilder();
		String request = msg.replace("\n", " ").trim();
		sb.append("Model = ").append(getModelName()).append("\n");
		sb.append("Request   = ").append(request).append("\n");
		sb.append("Response  = ").append("\n").append(response);
		return sb.toString();
	}

	// ======================================================================================

	/**
	 * Get the Model Name
	 * @return
	 */
	public String getModelName() {
		return AiBeans.getChatLanguageModelName(chatLanguageModel);
	}
 }