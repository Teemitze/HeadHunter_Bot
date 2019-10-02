import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.logging.Logger;

public class Main {
    public static final String HH = "https://hh.ru";

    public static void main(String[] args) {
        Logger logger = Logs.logs("invited");
        Browser browser = new Browser();

        WebDriver driver = browser.openBrowser();
        logger.info("opening " + HH);
        driver.get(HH + "/account/login");
        logger.info("opened " + HH);
        browser.authentication(driver);

        HTMLParser htmlParser = new HTMLParser();

        int countEmployees = 1;
        for (int i = Configuration.START_PAGE; i <= Configuration.END_PAGE; i++) {
            driver.get(browser.getPage(Configuration.PROFESSION, i));
            Elements elements = htmlParser.parsePageEmployees(driver);
            for (Element e : elements) {
                    logger.info(countEmployees + ") " + e.getElementsByClass("resume-search-item__fullname").text());
                    countEmployees++;
                    logger.info("Link employee: " + HH + e.getElementsByClass("resume-search-item__name").attr("href"));
                    driver.get(HH + e.getElementsByClass("resume-search-item__name").attr("href"));
                    if (Configuration.MAX_LIMIT_RESUME_VIEW) {
                        Browser.maxLimitResumeView(driver);
                    }
                    new WebDriverWait(driver, 10).until(
                            webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
                    browser.sendOffer(driver);
            }
        }
    }
}
