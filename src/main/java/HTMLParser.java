import configuration.ConfigurationHHBot;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.RepositoryVacancy;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

class HTMLParser {
    RepositoryVacancy repositoryVacancy = new RepositoryVacancy();
    private long countEmployee = RepositoryVacancy.countOfferDay();

    private static final Logger log = LoggerFactory.getLogger(HTMLParser.class);

    private List<String> getAllEmployeesOnPage(WebDriver driver) {
        Document pageWithEmployees = Jsoup.parse(driver.getPageSource());
        Elements elements = pageWithEmployees.getElementsByClass("resume-search-item__content-wrapper");
        return elements.stream().map(this::getLinkEmployee).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private List<String> getUniqueEmployees(List<String> linkEmployees) {
        List<String> employees = linkEmployees.stream().map(HTMLParser::getUUIDEmployeeFromURL).collect(Collectors.toList());
        return repositoryVacancy.findVacancy(employees);
    }

    private String getLinkEmployee(Element element) {
        String link = element.getElementsByClass("bloko-link_dimmed").attr("href");
        if (!link.isEmpty()) {
            return Main.HH + link;
        }
        return null;
    }

    void parseUniqueEmployees(WebDriver driver, Browser browser) {

        for (int i = ConfigurationHHBot.START_PAGE - 1; i <= ConfigurationHHBot.END_PAGE; i++) {
            driver.get(browser.getWebPageWithEmployees(i));
            if (isPageFound(driver)) {

                List<String> uniqueEmployeesLink = getUniqueEmployees(getAllEmployeesOnPage(driver));

                if (!uniqueEmployeesLink.isEmpty()) {
                    for (String uniqueLink : uniqueEmployeesLink) {
                        driver.get(Main.HH + "/employer/negotiations/change_topic?r=" + uniqueLink);
                        if (countEmployee == ConfigurationHHBot.MAX_LIMIT_SEND_OFFER) {
                            Browser.closeBrowser(driver, "Indicated count people were invited!");
                        }

                        HandlingException attemptSendOffer = () -> {
                            try {
                                driver.get(driver.getCurrentUrl());
                                browser.sendOffer(driver);
                                repositoryVacancy.addVacancy(uniqueLink);
                                log.info("Employee " + Main.HH + "/resume/" + uniqueLink + " invited!");
                            } catch (NoSuchElementException | TimeoutException e2) {
                                log.warn("Warning, element not found again! This element will be skipped!", e2);
                            }
                        };

                        try {
                            browser.sendOffer(driver);
                            repositoryVacancy.addVacancy(uniqueLink);
                        } catch (NoSuchElementException | TimeoutException e) {
                            log.warn("Warning, element not found! Try repeating send offer " + Main.HH + "/resume/" + uniqueLink, e);
                            attemptSendOffer.repeatSendOffer();
                            continue;
                        } catch (NoSuchSessionException e) {
                            log.error("Network connection is interrupted, attempt to restore!", e);
                            final int minute = 1000 * 60;
                            browser.pause(minute);
                            attemptSendOffer.repeatSendOffer();
                            continue;
                        }
                        countEmployee++;
                    }
                }
            } else {
                Browser.closeBrowser(driver, "Page " + driver.getCurrentUrl() + " not found!");
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