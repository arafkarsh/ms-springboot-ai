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
package io.fusion.air.microservice.domain.entities.example;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.fusion.air.microservice.domain.entities.core.AbstractBaseEntityWithUUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * Chat Message Store
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */

@Entity
@Table(name = "chatmessage_m")
public class ChatMessageEntity extends AbstractBaseEntityWithUUID {

    @NotBlank(message = "The User ID is required.")
    @Size(min = 36, max = 36, message = "The length of User ID must be 36 characters.")
    @Column(name = "userId")
    private String userId;

    @NotNull(message = "The Chat Message is  required.")
    @Column(name = "chatMessage")
    private String chatMessage;


    /**
     * Empty Chat Message Entity
     */
    public ChatMessageEntity() {
    }

    /**
     * Create ChatMessage Entity
     *
     * @param userId
     * @param chatMessage
     */
    public ChatMessageEntity(String userId, String chatMessage) {
        this.userId            = userId;
        this.chatMessage     = chatMessage;
    }

    /**
     * Get Chat ID
     * @return
     */
    @JsonIgnore
    public UUID getChatId() {
        return getUuid();
    }

    /**
     * Returns Chat ID as String
     * @return
     */
    @JsonIgnore
    public String getChatIdAsString() {
        return getUuid().toString();
    }

    /**
     * Returns User ID
     * @return
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Returns Chat Message
     * @return
     */
    public String getChatMessage() {
        return chatMessage;
    }

    /**
     * De-Activate Product
     */
    @JsonIgnore
    public void deActivateChatMessage() {
        deActivate();
    }

    /**
     * Activate Product
     */
    @JsonIgnore
    public void activateChatMessage() {
        activate();
    }

    /**
     * Return This Product ID / Name
     * @return
     */
    @Override
    public String toString() {
        return super.toString() + "|" + userId;
    }
}
