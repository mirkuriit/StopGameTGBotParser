import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.InlineQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.InlineQueryResultArticle;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

import java.util.HashMap;

public class Bot {
    private final TelegramBot bot = new TelegramBot(System.getenv("BOT_TOKEN"));
    private HashMap<String, String> hashMapLinks = new HashMap<>();

    public void serve(){
        //Listener каждые 100 миллисекунд получает updates от telegram api (любое действие пользователя)
        bot.setUpdatesListener(updates -> {
            updates.forEach(this::process);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void process(Update update) {
        //Получили message
        Message message = update.message();

        BaseRequest request = null;
        CallbackQuery callbackQuery = update.callbackQuery();

        if (message != null){
            long chatId = message.chat().id();
            InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(
                    new InlineKeyboardButton("Статьи").callbackData(
                            String.format("%d %s %s", message.chat().id(), message.from().firstName(), "artciles")
                    ),
                    new InlineKeyboardButton("Nовости").callbackData(
                            String.format("%d %s %s", message.chat().id(), message.from().firstName(), "news")
                    )
            );
            request = new SendMessage(chatId, message.text()).replyMarkup(inlineKeyboard);
        } else if (callbackQuery != null){
            hashMapLinksInit();
            String[] data = callbackQuery.data().split(" ");

            Long chatId = Long.valueOf(data[0]);
            //String nameOfUser = data[1];
            String linkForParser = getLink(data[2]);
            Parser parser = new Parser(linkForParser);
            String content = parser.getInformationFromLink();

            request = new SendMessage(chatId, content);
        }

        //отправили пользователю ответ
        if (request != null){
            bot.execute(request);
        }
    }
    private void hashMapLinksInit(){
        hashMapLinks.put("news", "https://stopgame.ru/news");
        hashMapLinks.put("artciles", "https://stopgame.ru/articles/new");
    }

    private String getLink(String nameLink){
        return hashMapLinks.get(nameLink);
    }
}
