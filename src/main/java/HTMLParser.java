import logger.Logs;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import repository.RepositoryVacancy;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

class HTMLParser {
    private Map<String, String> getAllEmployeesOnPage(WebDriver driver) {
        Document document = Jsoup.parse(driver.getPageSource());
        Elements elements = document.getElementsByClass("resume-search-item__content-wrapper");
        Map<String, String> employeesLink = new HashMap<>();
        for (Element element : elements) {
            employeesLink.put(getLinkEmployee(element), getNameEmployee(element));
        }
        return employeesLink;
    }

    private Map<String, String> getUniqueEmployees(Map<String, String> linkEmployees) {
        RepositoryVacancy repositoryVacancy = new RepositoryVacancy();
        Map<String, String> uniqueEmployeesLink = new HashMap<>();
        for (Map.Entry employee : linkEmployees.entrySet()) {
            if (repositoryVacancy.findVacancy(getUUIDEmployeeFromURL(String.valueOf(employee.getKey()))) == null) {
                uniqueEmployeesLink.put(String.valueOf(employee.getKey()), String.valueOf(employee.getValue()));
            } else {
                Logs.invitedLog.info("Such a person has already been invited: " + employee.getValue() + "\n" + employee.getKey());
            }
        }
        return uniqueEmployeesLink;
    }

    private String getLinkEmployee(Element element) {
        return Main.HH + element.getElementsByClass("HH-VisitedResume-Href").attr("href");
    }

    private String getNameEmployee(Element element) {
        return element.getElementsByClass("resume-search-item__fullname").text();
    }

    void parseUniqueEmployees(WebDriver driver, Browser browser) {
        for (int i = Configuration.START_PAGE - 1; i <= Configuration.END_PAGE; i++) {
            try {
                final long COUNT_INVITATIONS_DAY = 470;
                driver.get(browser.getWebPageWithEmployees(i));

                Map<String, String> uniqueEmployeesLink = getUniqueEmployees(getAllEmployeesOnPage(driver));

                for (Map.Entry<String, String> uniqueLink : uniqueEmployeesLink.entrySet()) {
                    Logs.invitedLog.info("Employee name: " + uniqueLink.getValue() + " Page " + (i + 1));
                    Logs.invitedLog.info("Link employee: " + uniqueLink.getKey());
                    driver.get(uniqueLink.getKey());
                    if (Browser.maxLimitResumeView(driver) || RepositoryVacancy.countOfferDay() == COUNT_INVITATIONS_DAY) {
                        Logs.infoLog.warning("The daily limit for resume views has been reached!");
                        driver.quit();
                        System.exit(0);
                    }
                    browser.sendOffer(driver);
                }
            } catch (NoSuchElementException | TimeoutException e) {
                driver.get(driver.getCurrentUrl());
                browser.sendOffer(driver);
                Logs.infoLog.log(Level.SEVERE, "Error, element not found!", e);
            }
        }
    }

    static String getUUIDEmployeeFromURL(String url) {
        try {
            return url.substring(21, 59);
        } catch (StringIndexOutOfBoundsException e) {
            Logs.infoLog.log(Level.SEVERE, "java.lang.StringIndexOutOfBoundsException!\n" + url, e);
            return url;
        }
    }
}