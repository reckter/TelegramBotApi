package me.reckter.telegram.requests

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by hannesgudelhofer on 13.05.17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
data class PhotoRequest (

    @JsonProperty("chat_id")
    val id: String,

    val photo: String,

    val caption: String? = null,

    @JsonProperty("disable_notification")
    val disableNotification: Boolean? = null,

    @JsonProperty("reply_to_message_id")
    val replyTo: Int? = null,

    @JsonProperty("reply_markup")
    val replyMarkup: ReplyMarkup? = null
)