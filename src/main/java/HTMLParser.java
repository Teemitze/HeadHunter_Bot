import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;

public class HTMLParser {

    public Elements parsePageEmployees(WebDriver driver) {
        Document document = Jsoup.parse(driver.getPageSource());
        Elements elements = document.getElementsByClass("resume-search-item");
        return elements;
    }
}
