
package io.fusion.air.microservice.ai.genai.core.services;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.structured.StructuredPromptProcessor;
import dev.langchain4j.service.AiServices;
import io.fusion.air.microservice.ai.genai.core.assistants.HealthCareAssistant;
import io.fusion.air.microservice.ai.genai.core.assistants.PatientDataExtractorAssistant;
import io.fusion.air.microservice.ai.genai.core.models.Patient;
import io.fusion.air.microservice.ai.genai.core.models.PatientRequest;
import io.fusion.air.microservice.ai.genai.core.prompts.StructuredPromptDiagnosisDetails;
import io.fusion.air.microservice.ai.genai.core.prompts.StructuredPromptDiagnosisSummary;
import io.fusion.air.microservice.ai.genai.core.prompts.StructuredPromptPatientId;
import io.fusion.air.microservice.ai.genai.core.prompts.StructuredPromptPatientName;
import io.fusion.air.microservice.ai.genai.utils.AiBeans;
import io.fusion.air.microservice.ai.genai.utils.AiConstants;
import io.fusion.air.microservice.utils.Std;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * RAG HealthCare Service
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Service
public class RAGHealthCareService implements HealthCareAssistant {

    private final ChatLanguageModel model;
    private final HealthCareAssistant assistant;

    /**
     * Create RAG Health Care Service
     */
    @Autowired
    public RAGHealthCareService() {
        this(AiConstants.LLM_OPENAI, AiConstants.GPT_4o);
    }

    /**
     * Create RAG HealthCare Assistant with a Language Model
     * @param groupName
     * @param modelName
     */
    public RAGHealthCareService(String groupName, String modelName) {
        this.model = createLanguageModel( groupName,  modelName);
        assistant = RAGHealthCareBuilder.createHealthCareAssistant( model);
    }

    /**
     * Create a RAG HealthCare Assistant with a Language Model
     * @param model
     */
    public RAGHealthCareService(ChatLanguageModel model) {
        this.model = model;
        assistant = RAGHealthCareBuilder.createHealthCareAssistant( model);
    }

    /**
     * Analyze the User Query and Return the response
     * @param userMessage
     * @return
     */
    public String chat(String userMessage) {
        if(userMessage == null) {
            return "Invalid Input!";
        }
        PatientRequest request = analyzeTheQuery(userMessage);
        if(request.isValidData()) {
            String patientRef = (request.getPatientName() != null) ? request.getPatientName() : request.getPatientId();
            return assistant.chat(patientRef, request.getUserQuery());
        }
        return assistant.chat(request.getUserQuery());
    }

    /**
     * Chat Memory with a Memory ID
     *
     * @param memoryId
     * @param userMessage
     * @return
     */
    @Override
    public String chat(String memoryId, String userMessage) {
        return assistant.chat(memoryId, userMessage);
    }

    /**
     * Return the User Query
     * @param uQuery
     * @return
     */
    private PatientRequest analyzeTheQuery(String uQuery) {
        String patientName = null;
        String patientId = null;
        String userQuery = null;
        // Extract Patient Name
        Patient patient = patientNameExtractor(uQuery);
        if(patient.isValid()) {
            patientName = patient.toString();
            userQuery = getPatientPrompt(patient).text();
            Std.println("Debug>> Patient:"+patientName+" << "+userQuery);
        } else {
            // Extract Patient ID
            long patientLongId = patientIdExtractor(uQuery);
            if (patientLongId > 0) {
                patientId = "" + patientLongId;
                userQuery = getPatientIdPrompt(patientLongId).text();
                Std.println("Debug>> Patient:"+patientId+" << "+userQuery);
            }
        }
        // If UserQuery is still NULL then check for special commands starting with [P:
        if(userQuery == null) {
            userQuery = getDiagnosisQuery(uQuery);
        }
        return new PatientRequest(patientName, patientId, userQuery) ;
    }

    /**
     * Get the Patient
     * @param request
     * @return
     */
    private Patient patientNameExtractor(String request) {
        // Create Chat Language Model - Open AI GPT 3.5 Turbo
        ChatLanguageModel clModel = AiBeans.getChatLanguageModelOpenAi(AiConstants.GPT_3_5_TURBO);
        PatientDataExtractorAssistant extractor = AiServices.create(PatientDataExtractorAssistant.class, clModel);
        Patient patient = extractor.extractPatientNameFrom(request);
        if(patient.isValid()) {
            AiBeans.printResult(request, patient.toString());
        }
        return patient;
    }

    /**
     * Extract Patient ID
     * @param request
     * @return
     */
    private long patientIdExtractor(String request) {
        PatientDataExtractorAssistant extractor = AiServices.create(PatientDataExtractorAssistant.class, model);
        long patientId = -1;
        try { patientId = extractor.extractPatientId(request); } catch (NumberFormatException e) {
            // Nothing to Print
        }
        if(patientId > 0) {
            AiBeans.printResult(request, "" + patientId);
        }
        return patientId;
    }

    /**
     * Return Patient Prompt
     * @param patient
     * @return
     */
    private Prompt getPatientPrompt(Patient patient) {
        return StructuredPromptProcessor.toPrompt(new StructuredPromptPatientName(patient.toString()));
    }

    /**
     * Return the Patient Id Prompt
     * @param patientId
     * @return
     */
    private Prompt getPatientIdPrompt(long patientId) {
        return StructuredPromptProcessor.toPrompt(new StructuredPromptPatientId(patientId));
    }

    /**
     * Return User Query
     * @param userQuery
     * @return
     */
    private String getDiagnosisQuery(String userQuery) {
        if (userQuery.startsWith("[P: ") || userQuery.startsWith("[p: ")) {
            String[] input = userQuery.split(":");
            // Input Array
            String[] ia = input[1].split(",");
            if (ia.length == 2) {
                // Structured Prompt
                StructuredPromptDiagnosisDetails details = new StructuredPromptDiagnosisDetails(ia[0], ia[1]);
                // Created Prompt
                Prompt prompt = StructuredPromptProcessor.toPrompt(details);
                userQuery = prompt.text();
            } else {
                // Structured Prompt
                StructuredPromptDiagnosisSummary summary = new StructuredPromptDiagnosisSummary(ia[0]);
                // Created Prompt
                Prompt prompt = StructuredPromptProcessor.toPrompt(summary);
                userQuery = prompt.text();
            }
        }
        return userQuery;
    }

    /**
     * Return Chat Language Model
     * @param groupName
     * @param modelName
     * @return
     */
    private  ChatLanguageModel createLanguageModel(String groupName, String modelName) {
        ChatLanguageModel clModel = null;
        // If Not Valid Go with the default Model OpenAI - GPT 3.5 Turbo
        if(!isValidModel(groupName ,modelName)) {
            clModel = AiBeans.getChatLanguageModelOpenAi(AiConstants.GPT_3_5_TURBO);
            AiBeans.printModelDetails(groupName, AiConstants.GPT_3_5_TURBO);
        } else {
            // IF Valid and If Open AI then go with GPT 3.5 Turbo
            if(groupName.equalsIgnoreCase(AiConstants.LLM_OPENAI)) {
                clModel = AiBeans.getChatLanguageModelOpenAi(AiConstants.GPT_3_5_TURBO);
                AiBeans.printModelDetails(groupName, AiConstants.GPT_3_5_TURBO);
            } else {
                // Else Any other
                clModel = getChatLanguageModel( groupName,  modelName);
                AiBeans.printModelDetails(groupName, modelName);
            }
        }
        return clModel;
    }

    /**
     * Returns ChatLanguageModel based on the LLM Group
     * @param groupName
     * @param modelName
     * @return
     */
    private ChatLanguageModel getChatLanguageModel(String groupName, String modelName) {
        switch(groupName) {
            case AiConstants.LLM_ANTHROPIC: return AiBeans.getChatLanguageModelAnthropic(modelName);
            case AiConstants.LLM_VERTEX: return AiBeans.getChatLanguageModelGoogle(modelName);
            case AiConstants.LLM_OLLAMA:
                // Fall Thru to the default
            default:
                return AiBeans.getChatLanguageModelLlama(modelName);
        }
    }

    /**
     * Checks if the Group and Model Name are valid
     * @param groupName
     * @param modelName
     * @return
     */
    private boolean isValidModel(String groupName, String modelName) {
        return (groupName != null && modelName != null);
    }
}
