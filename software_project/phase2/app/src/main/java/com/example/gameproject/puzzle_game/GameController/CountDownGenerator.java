package com.example.gameproject.puzzle_game.GameController;

import android.os.CountDownTimer;

class CountDownGenerator {

    private CountDownRequester requester;

    //Total time for one game
    private long countDownInMillis;
    //Timer
    private CountDownTimer countDownTimer;
    //Time left during game
    private long timeLeftInMillis;
    //Time left during pause
    private long pauseTimeLeft;

    void startCountDown(CountDownRequester requester, long countDownInMillis) {
        this.requester = requester;
        this.countDownInMillis = countDownInMillis;
        timeLeftInMillis = countDownInMillis;
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                requester.updateCountDown(timeLeftInMillis);
                //showCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                requester.updateCountDown(timeLeftInMillis);
                //showCountDownText();
                //showFinalScore();

            }
        }.start();
    }

    void pause() {
        pauseTimeLeft = timeLeftInMillis;
        countDownTimer.cancel();
    }

    void resume() {
        timeLeftInMillis = pauseTimeLeft;
        startCountDown(requester, timeLeftInMillis);
    }

    long getCurrentTime(){
        return timeLeftInMillis;
    }

    long getTotalTime(){
        return countDownInMillis;
    }

}
