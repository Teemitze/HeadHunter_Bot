import repository.RepositoryVacancy;

import java.util.Timer;
import java.util.TimerTask;

public class MyTimer implements Runnable {
    private Controller controller;
    private long count = -1;

    private final long minute = 60 * 1000;

    public MyTimer(Controller controller) {
        this.controller = controller;
    }

    private void checkBot() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (count == RepositoryVacancy.countOfferDay()) {
                    controller.reboot();
                } else {
                    count = RepositoryVacancy.countOfferDay();
                }
            }
        }, 0, minute * 5);
    }

    @Override
    public void run() {
        checkBot();
    }
}
