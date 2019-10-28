import logger.Logs;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import repository.RepositoryVacancy;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

class HTMLParser {
    private long countEmployee = RepositoryVacancy.countOfferDay();

    private Map<String, String> getAllEmployeesOnPage(WebDriver driver) {
        Document document = Jsoup.parse(driver.getPageSource());
        Elements elements = document.getElementsByClass("resume-search-item__content-wrapper");
        Map<String, String> employeesLink = new HashMap<>();
        for (Element element : elements) {
            if (getLinkEmployee(element) != null) {
                employeesLink.put(getLinkEmployee(element), getNameEmployee(element));
            }
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
        String link = element.getElementsByClass("bloko-link_dimmed").attr("href");
        if (!link.isEmpty()) {
            return Main.HH + link;
        }
        return null;
    }

    private String getNameEmployee(Element element) {
        return element.getElementsByClass("resume-search-item__fullname").text();
    }

    void parseUniqueEmployees(WebDriver driver, Browser browser) {

        for (int i = Configuration.START_PAGE - 1; i <= Configuration.END_PAGE; i++) {
            try {
                driver.get(browser.getWebPageWithEmployees(i));
                if (isPageFound(driver)) {

                    Map<String, String> uniqueEmployeesLink = getUniqueEmployees(getAllEmployeesOnPage(driver));

                    for (Map.Entry<String, String> uniqueLink : uniqueEmployeesLink.entrySet()) {
                        String UUIDEmployeeFromURL = getUUIDEmployeeFromURL(uniqueLink.getKey());
                        Logs.invitedLog.info("Employee name: " + uniqueLink.getValue() + " Page " + (i + 1));
                        Logs.invitedLog.info("Link employee: " + Main.HH + "/resume/" + UUIDEmployeeFromURL);
                        driver.get(uniqueLink.getKey());
                        if (countEmployee == Configuration.MAX_LIMIT_SEND_OFFER) {
                            Logs.infoLog.warning("Indicated count people were invited!");
                            driver.quit();
                            System.exit(0);
                        }
                        browser.sendOffer(driver);
                        new RepositoryVacancy().addVacancy(UUIDEmployeeFromURL);
                        countEmployee++;
                    }
                } else {
                    Logs.infoLog.warning("Page not found!");
                    driver.quit();
                    System.exit(0);
                }
            } catch (NoSuchElementException | TimeoutException e) {
                driver.get(driver.getCurrentUrl());
                browser.sendOffer(driver);
                Logs.infoLog.log(Level.SEVERE, "Error, element not found!", e);
            }
        }
    }

    private boolean isPageFound(WebDriver driver) {
        return !driver.findElement(By.className("header")).getText().equals("Ошибка 404");
    }

    private static String getUUIDEmployeeFromURL(String url) {
        return url.substring(51);
    }
}