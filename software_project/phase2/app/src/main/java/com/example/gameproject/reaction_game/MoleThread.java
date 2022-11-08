package com.example.gameproject.reaction_game;


class MoleThread extends Thread {
    private boolean Running;
    private int next;
    private ReactionGameActivity reaction;
    private MoleManager moleManager;
    private Moles mole;
    private int step;// in order to let this thread remain in next status after clicking pause

    public void run() {
        try {
            while (true) {
                if (Running && step == 1) {
                    if (reaction.pause_before) {
                        Thread.sleep(mole.getRefreshTime());
                        reaction.pause_before = false;
                    }
                    next = 0;
                    moleManager.updateScreen(next, step);
                    setStep(2);
                    Thread.sleep(750);//time pause for 0.75s, allow the screen to stay in no mole for 0.75s
                    next = (int) (Math.random() * 9) + 1;
                }
                if (Running && step == 2) {
                    if (reaction.pause_before) {
                        Thread.sleep(750);
                        reaction.pause_before = false;
                    }
                    moleManager.updateScreen(next, step);
                    if (reaction.random)
                        mole.generateRefreshTime();
                    setStep(1);
                    Thread.sleep(mole.getRefreshTime());//time pause for 0.75s, by default, allow the screen to stay in mole for 0.75s
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setRunning(boolean setRunning) {
        this.Running = setRunning;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void setActivity(ReactionGameActivity action, MoleManager moleManager, Moles mole) {
        this.reaction = action;
        this.moleManager = moleManager;
        this.mole = mole;
    }
}
