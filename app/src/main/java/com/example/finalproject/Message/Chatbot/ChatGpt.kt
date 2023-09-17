package com.example.finalproject.Message.Chatbot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.*
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatGpt @Inject constructor() : ViewModel() {

    var isCompleted = false;

    @OptIn(BetaOpenAI::class)
    fun getGPTResponse(message: String, container: ArrayList<String>, adapter: ChatbotAdapter) {
        viewModelScope.launch {
            val openAI = OpenAI(CHAT_GPT_API_KEY)

            try {
                val chatCompletionRequest = ChatCompletionRequest(
                    model = ModelId("gpt-3.5-turbo"),
                    messages = listOf(
                        ChatMessage(
                            role = ChatRole.User,
                            content = message
                        )
                    )
                )

                val completion: ChatCompletion = openAI.chatCompletion(chatCompletionRequest)

                val response = completion.choices.first().message?.content
                response?.let { container.add(it) }
                adapter.notifyDataSetChanged()

            } catch (e: Exception) {
                isCompleted = true
            }
        }
    }
    companion object {
        const val CHAT_GPT_API_KEY = "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"
    }
}