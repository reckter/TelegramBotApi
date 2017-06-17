package me.reckter.telegram

import me.reckter.telegram.listener.*
import me.reckter.telegram.model.Message
import me.reckter.telegram.model.update.CallbackQuery
import me.reckter.telegram.requests.InlineKeyboardButton
import me.reckter.telegram.requests.InlineKeyboardMarkup
import me.reckter.telegram.requests.inlineMode.InlineQueryAnswer
import me.reckter.telegram.requests.inlineMode.InlineQueryResultArticle
import me.reckter.telegram.requests.inlineMode.InputTextMessageContent

/**
 * @author Hannes Güdelhöfer
 */
open class Test {

    lateinit var telegram: Telegram


    @OnLocation
    fun lcation(message: Message) {
        message.reply("You are here: (" + message.location!!.latitude + "|" + message.location!!.longitude + ")")
    }


    @OnCallBack
    fun callBack(callbackQuery: CallbackQuery) {
        telegram.answerCallback(callbackQuery, "you just pressed " + callbackQuery.data)
    }

    @OnCommand("inline")
    fun inlineKeyboard(message: Message, args: List<String>) {

        telegram.buildMessage()
                .chat(message.chat)
                .text("test")
                .buildInlineKeyboard()
                .button("weiter", callBackData = "next")
                .nextRow()
                .button("zurück", null, "back", null)
                .button("abbrechen", null, "cancel", null)
                .nextRow()
                .button("website", "http://google.com", null, null)
                .build()
                .send()

        telegram.sendMessage {
            chat(message.chat)
            text("test2")
            buildInlineKeyboard {
                row {
                    button("weiter", callBackData = "next")
                }
                row {
                    button("zurück", null, "back", null)
                    button("abbrechen", null, "cancel", null)
                }
                row {
                    button("website", "http://google.com", null, null)
                }
            }
        }

    }

    @OnCommand("test")
    fun testCommand(message: Message, args: List<String>) {

        var out = "Your args are:\n"
        for (i in args.indices) {
            out += i.toString() + ": " + args[i] + "\n"
        }
        message.reply(out)
    }

    @OnMessage
    fun test(message: Message) {
        message.reply("passed!")
    }


    @OnDocument()
    fun onVideo(message: Message) {
        if(message.document != null) {
            val document = message.document!!
            if(document.mimeType.contains("video")) {
                val id = document.id
                val message = telegram.sendDocument(message.chat.id, id, "lol")

                Thread.sleep(3000)

                telegram.editCaption(message.chat.id, message.id, "test")
            }
        }

    }


    companion object {

        @JvmStatic fun main(args: Array<String>) {
            val test = Test()
            val telegram = Telegram.Builder()
                    .apiToken(System.getenv("telegram.bot-token"))
                    .adminChat(System.getenv("telegram.admin-chat"))
                    .build()
            test.telegram = telegram;
            telegram.addListener(test)

            telegram.inlineQueryHandler { query ->
                val answer = InlineQueryAnswer(query.id, cacheTime = 0)
                (1..10).forEach {
                    val article = InlineQueryResultArticle()
                    article.title = "result $it"
                    article.inputMessageContent = InputTextMessageContent().apply {
                        text = "result $it text"
                    }
                    article.replyMarkup = InlineKeyboardMarkup().apply {
                        this.inlineKeyboard = mutableListOf(mutableListOf(
                                InlineKeyboardButton().apply {
                                    text = "test"
                                    callbackData = "test"
                                }
                        ))
                    }
                    article.id = it.toString()
                    answer.results.add(article)

                }

                answer
            }.onResult { result ->
                print("result!")
            }

        }
    }
}
