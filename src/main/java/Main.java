public class Main {
    static final String HH = "https://hh.ru";
    public static void main(String[] args) {
        System.setProperty("webdriver.gecko.driver", Configuration.GECKO_DRIVER);
        new Browser();
    }
}