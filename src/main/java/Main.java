public class Main {
    static final String HH = "https://hh.ru";

    public static void main(String[] args) {
        Controller controller = new Controller();
        Thread bot = new Thread(controller);
        bot.start();

        MyTimer myTimer = new MyTimer(controller);
        Thread timer = new Thread(myTimer);
        timer.start();
    }
}