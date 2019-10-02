import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import repository.RepositoryVacancy;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class HTMLParser {
    Logger logger = Logs.logs("invited");

    public Map<String, String> getAllEmployeesOnPage(WebDriver driver) {
        Document document = Jsoup.parse(driver.getPageSource());
        Elements elements = document.getElementsByClass("resume-search-item__content-wrapper");
        Map<String, String> employeesLink = new HashMap<>();
        for (Element element : elements) {
            employeesLink.put(getLinkEmployee(element), getNameEmployee(element));
        }
        return employeesLink;
    }

    public Map<String, String> getUniqueEmployees(Map<String, String> linkEmployees) {
        RepositoryVacancy repositoryVacancy = new RepositoryVacancy();
        Map<String, String> uniqueEmployeesLink = new HashMap<>();
        for (Map.Entry employee : linkEmployees.entrySet()) {
            if (repositoryVacancy.findVacancy(getUUIDEmployeeFromURL(String.valueOf(employee.getKey()))) == null) {
                uniqueEmployeesLink.put(String.valueOf(employee.getKey()), String.valueOf(employee.getValue()));
            } else {
                logger.info("Такой водитель уже  был приглашён: " + employee.getValue());
            }
        }
        return uniqueEmployeesLink;
    }

    public String getLinkEmployee(Element element) {
        return Main.HH + element.getElementsByClass("resume-search-item__name").attr("href");
    }

    public String getNameEmployee(Element element) {
        return element.getElementsByClass("resume-search-item__fullname").text();
    }

    public void parseUniqueEmployees(WebDriver driver, Browser browser) {
        for (int i = Configuration.START_PAGE - 1; i <= Configuration.END_PAGE; i++) {
            driver.get(browser.getWebPageWithEmployees(Configuration.PROFESSION, i));
            Map<String, String> uniqueEmployeesLink = getUniqueEmployees(getAllEmployeesOnPage(driver));
            for (Map.Entry<String, String> uniqueLink : uniqueEmployeesLink.entrySet()) {
                logger.info("Employee name: " + uniqueLink.getValue() + " Page " + i + 1);
                logger.info("Link employee: " + uniqueLink.getKey());
                driver.get(uniqueLink.getKey());
                if (Configuration.MAX_LIMIT_RESUME_VIEW) {
                    Browser.maxLimitResumeView(driver);
                }
                new WebDriverWait(driver, 10).until(
                        webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
                browser.sendOffer(driver);
            }
        }
    }

    public static String getUUIDEmployeeFromURL(String url) {
        return url.substring(21, 59);
    }
}
