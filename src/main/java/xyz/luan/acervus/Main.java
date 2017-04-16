package xyz.luan.acervus;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import xyz.luan.facade.HttpFacade;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    private static final String DOMAIN = "http://acervus.unicamp.br/";

    public static void main(String[] args) throws IOException {
        String user = args[0];
        String pass = args[1];
        renewal(user, pass);
    }

    private static void renewal(String user, String pass) throws IOException {
        final String today = new SimpleDateFormat("DD/MM/yyyy").format(new Date());
        Map<String, String> cookies = doLogin(user, pass);
        List<Book> ids = extractBookIds(cookies);
        String sCods = ids.stream().filter(b -> b.date.equals(today)).map(b -> b.id).collect(Collectors.joining(","));
        if (sCods.isEmpty()) {
            System.out.println("Nothing to renewal!");
            return;
        }
        String url = "index.asp?content=circulacoes&acao=renovacao&num_circulacao=" + sCods;
        HttpFacade renewal = new HttpFacade(DOMAIN + url);
        renewal.cookies(cookies);
        renewal.get();
    }

    private static List<Book> extractBookIds(Map<String, String> cookies) throws IOException {
        HttpFacade books = new HttpFacade(DOMAIN + "index.asp?modo_busca=rapida&content=circulacoes&iFiltroBib=0&iBanner=0&iEscondeMenu=0&iSomenteLegislacao=0&iIdioma=0");
        books.header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2783.2 Safari/537.36");
        books.cookies(cookies);

        String html = books.get().content();
        Document doc = Jsoup.parse(html);
        List<Book> r = new ArrayList<>();
        for (Element tr : doc.select(".tab_circulacoes").first().select("tr")) {
            if (tr.select("td").first().text().contains("#")) {
                continue;
            }
            Elements cbx = tr.select("input[type=checkbox]");
            Elements td = tr.select("td").last().select("span");
            String date = td.text().trim();
            r.add(new Book(cbx.val(), date.substring(0, date.length() - 1)));
        }
        return r;
    }

    public static class Book {
        private final String id;
        private final String date;

        Book(String id, String date) {
            this.id = id;
            this.date = date;
        }

        @Override
        public String toString() {
            return "{" + id + "," + date + "}";
        }
    }

    private static Map<String, String> doLogin(String email, String senha) throws IOException {
        HttpFacade login = new HttpFacade(DOMAIN + "asp/login.asp?iIdioma=0&iBanner=0&content=mensagens");
        login.header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2783.2 Safari/537.36");
        login.body("codigo=" + email + "&senha=" + senha + "&sub_login=sim");

        return login.post().cookies();
    }
}
