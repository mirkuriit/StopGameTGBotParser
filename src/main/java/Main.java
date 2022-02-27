import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String args[]) throws IOException {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Введите Url\n");
//        Parser parser = new Parser("https://stopgame.ru/articles/new");
//        parser.getInfromationAboutArticles();
        new Bot().serve();
    }
}
