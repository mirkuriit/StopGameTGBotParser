import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Parser {
    private URL url;
    private Document page;
    ArrayList<String> articleNamesArray = new ArrayList<>();
    ArrayList<String> articleInfoArray = new ArrayList<>();
    ArrayList<String> articlePlatformsArray = new ArrayList<>();

    public Parser(String url){
        try {
            this.url = new URL(url);
            this.page = Jsoup.parse(this.url, 10000);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getInformationFromLink(){
        switch(url.toString()){
            case "https://stopgame.ru/news":
                return getInfromationAboutNews();
            case "https://stopgame.ru/articles/new":
                return getInfromationAboutArticles();
        }
        return "Npne2";
    }

    private String getInfromationAboutArticles(){
        String content = "";
        //Вытащил в stopGameState блок со всеми статьями
        Elements stopGameState =
                page.select("div[class=tiles tiles-details tiles-duo]").first()
                        .select("div[class=items]");

        //Список элементов с div[class=caption caption-bold]. (Все названия статей)
        Elements artcileNames = stopGameState.select("div[class=caption caption-bold]");
        //Список элементов с div[class=info]. (Вся информация о статьях)
        Elements artcileInfo = stopGameState.select("div[class=info]");

        for (Element element : artcileNames){
            articleNamesArray.add(element.select("a").text());
        }
        for (Element element : artcileInfo){
            articleInfoArray.add("(статья опубликована "+element.select("span[class=info-item timestamp]").text()+")");
        }
        for (int i = 0; i < artcileNames.size(); i++) {
            content += String.format("\uD83D\uDCAC %s %s \n\n", articleNamesArray.get(i), articleInfoArray.get(i));
        }
        articleInfoArray.clear();
        articleNamesArray.clear();
        return content;
    }

    private String getInfromationAboutNews(){
        String content = "";

        Elements stopGameState =
                page.select("div[class=tiles tiles-details]").first()
                        .select("div[class=items]");

        //Список элементов с div[class=caption caption-bold]. (Все названия статей)
        Elements artcileNames = stopGameState.select("div[class=caption caption-bold]");
        //Список элементов с div[class=info]. (Вся информация о статьях)
        Elements artcileInfo = stopGameState.select("div[class=info]");
        //Список элементов о принадлежности новостей к одной из тем по устройствам
        Elements articlePlatform = stopGameState.select("div[class=tags]");

        for (Element element : artcileNames){
            articleNamesArray.add(element.select("a").text());
        }
        for (Element element : artcileInfo){
            articleInfoArray.add(element.select("span[class=info-item timestamp]").text());
        }
        for (Element element : articlePlatform){
            articlePlatformsArray.add(element.text() + ",");
        }
        for (int i = 0; i < artcileNames.size(); i++) {
            // Я хз, как еще удалять последнюю запятую в строке
            content += String.format("⚙ %s (опубликовано %s. Темы - %s)\n\n",
                    articleNamesArray.get(i), articleInfoArray.get(i), articlePlatformsArray.get(i).
                            substring(0, articlePlatformsArray.get(i).length()-1));
        }
        return content;
    }


    private String formatPLatforms(ArrayList<String> platformsArray){
        for (int i = 0; i < platformsArray.size(); i++) {
            platformsArray.add(i, platformsArray.get(i) + ", ");
        }
        return "platformsArray";
    }

    public URL getUrl() {
        return url;
    }

    public Document getPage() {
        return page;
    }

    public void setUrl(URL newUrl) throws IOException {
        url = newUrl;
        page = Jsoup.parse(url, 10000);
    }
}
