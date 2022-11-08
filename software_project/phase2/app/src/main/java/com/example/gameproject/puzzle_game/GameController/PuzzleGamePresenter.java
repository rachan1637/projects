package com.example.gameproject.puzzle_game.GameController;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Toast;

import com.example.gameproject.puzzle_game.Activity.PuzzleGameView;
import com.example.gameproject.puzzle_game.GameElements.PuzzleGameData;
import com.example.gameproject.puzzle_game.GameElements.PuzzleGameDataGateway;

import java.util.ArrayList;
import java.util.Locale;

public class PuzzleGamePresenter implements CountDownRequester, PuzzleRequester {

    private PuzzleGameView puzzleGameView;
    private GestureDetectGridView gestureDetectGridView;
    private CountDownGenerator countDownGenerator = new CountDownGenerator();
    private PuzzleGenerator puzzleGenerator = new PuzzleGenerator();
    private PuzzleGameDataGateway puzzleGameDataGateway = new PuzzleGameData();
    private PuzzleListManager puzzleListManager;
    private MysteryShopManager mysteryShopManager;
    private AchievementsManager achievementsManager;

    private Context context;

    private BitmapDrawable[] puzzlePieces;
    private int mColumnWidth, mColumnHeight;
    private int numColumns = 3;

    static final String up = PuzzleGenerator.up;
    static final String down = PuzzleGenerator.down;
    static final String left = PuzzleGenerator.left;
    static final String right = PuzzleGenerator.right;

    public static final String smallHint = MysteryShopManager.smallHint;
    public static final String bigHint = MysteryShopManager.bigHint;
    public static final String skipPuzzle = MysteryShopManager.skipPuzzle;

    private boolean movable = true;

    //These are true if the player did not get these achievement in this game yet
    //They are false if the player has already got them in this game
    //Reset every game
    private boolean timeAchievement = true;
    private boolean levelScoreAchievement = true;

    /**
     * PuzzleGamePresenter constructor.
     * @param puzzleGameView the view that this presenter changes.
     */
    public PuzzleGamePresenter(PuzzleGameView puzzleGameView, Context context) {
        this.puzzleGameView = puzzleGameView;
        this.puzzleGenerator.setPuzzleRequester(this);
        this.context = context;
        puzzleListManager =
                new PuzzleListManager(context.getResources());
        this.mysteryShopManager = new MysteryShopManager(this, puzzleGameDataGateway,
                puzzleGenerator, context, puzzleListManager);
        this.achievementsManager = new AchievementsManager(puzzleGameDataGateway,
                countDownGenerator);
    }

    /**
     * Presenter tells view to show updated countdown text.
     */
    public void updateCountDown(long timeLeftInMillis) {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes,
                seconds);
        puzzleGameView.showCountDownText(timeFormatted);
        if (timeLeftInMillis == 0) {
            puzzleGameView.showFinalScore(puzzleGameDataGateway.getScore());
        }
    }

    /**
     * Presenter asks countDownGenerator to start a countdown.
     * @param countDownInMillis time limit for count down.
     */
    public void startCountDown(long countDownInMillis) {
        countDownGenerator.startCountDown(this, countDownInMillis);
    }

    public void pauseGame() {
        countDownGenerator.pause();
        puzzleGameView.setBackgroundClickable(false);
        movable = false;
    }

    public void resumeGame() {
        countDownGenerator.resume();
        puzzleGameView.setBackgroundClickable(true);
        movable = true;
    }

    public void setNumColumns(int numColumns) {
        this.numColumns = numColumns;
        puzzleGenerator.setColumns(numColumns);
        gestureDetectGridView.setNumColumns(numColumns);
        puzzleListManager.setImageSplitter(numColumns);
    }

    public void setPuzzlePieces(BitmapDrawable[] puzzlePieces) {
        this.puzzlePieces = puzzlePieces;
    }

    public void setPuzzles(ArrayList<Bitmap> puzzles) {
        puzzleListManager.setPuzzles(puzzles);
        puzzleListManager.setPuzzleGenerator(puzzleGenerator);
        puzzleListManager.setPuzzleRequester(this);
        puzzleGameDataGateway.setNumPuzzles(puzzles.size());
    }

    public void setGestureDetectGridView(GestureDetectGridView gestureDetectGridView) {
        this.gestureDetectGridView = gestureDetectGridView;
        gestureDetectGridView.setPresenter(this);
    }

    /**
     * Set dimensions for the  puzzles depending on the screen
     */
    public void setDimensions() {
        ViewTreeObserver vto = gestureDetectGridView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                gestureDetectGridView.getViewTreeObserver().
                        removeOnGlobalLayoutListener(this);
                int displayWidth = gestureDetectGridView.getMeasuredWidth();
                int displayHeight = gestureDetectGridView.getMeasuredHeight();

                int statusBarHeight = puzzleGameView.getStatusBarHeight();
                int requiredHeight = displayHeight - statusBarHeight;

                mColumnWidth = displayWidth / numColumns;
                mColumnHeight = requiredHeight / numColumns;
                puzzleListManager.showNextPuzzle(puzzleGameDataGateway.getNumCompleted());
            }
        });
    }

    void moveTiles(String direction, int position) {
        if (!movable) {
            return;
        }
        puzzleGenerator.moveTiles(context, direction, position);
    }

    @Override
    public void updatePuzzle() {
        String[] tileList = puzzleGenerator.getCurrentPosition();
        ArrayList<Button> buttons = new ArrayList<>();
        Button button;

        for (String s : tileList) {
            button = new Button(context);
            int tile = Integer.parseInt(s);
            button.setBackground(puzzlePieces[tile]);
            buttons.add(button);
        }
        gestureDetectGridView.setAdapter(new PuzzleAdapter(buttons, mColumnWidth, mColumnHeight));
        updateNumMoves();

        if (isSolved()) {
            Toast.makeText(context, "NEXT PUZZLE!", Toast.LENGTH_SHORT).show();

            // Update puzzleComplete
            updateNumCompleted();
            int score = puzzleGameDataGateway.getScore();
            Integer scoreInteger = (int) ((2000 * ((1 - (double) puzzleGameDataGateway.getNumMoves() /
                    100))));
            score += scoreInteger;
            puzzleGameDataGateway.setScore(score);
            // Update score
            updateScore();
            // Reset number of moves
            clearNumMoves();

            int numCompleted = puzzleGameDataGateway.getNumCompleted();
            int numPuzzles = puzzleGameDataGateway.getNumPuzzles();

            //check if time achievement is achieved
            if (achievementsManager.checkTimeAchievement() && timeAchievement){
                Toast.makeText(context, "Achievement Unlocked: Faster Solver",
                        Toast.LENGTH_SHORT).show();
                puzzleGameView.updateAchievement();
                timeAchievement = false;
            }

            //check if level score achievement is achieved
            //uses handler to avoid overriding the first toast
            if (achievementsManager.checkLevelScoreAchievement(scoreInteger) &&
                    levelScoreAchievement){
                Handler handler =new Handler();
                handler.postDelayed(() ->
                        Toast.makeText(context, "Achievement Unlocked: Easy-peasy",
                                Toast.LENGTH_SHORT).show(),3000);
                puzzleGameView.updateAchievement();
                levelScoreAchievement = false;
            }

            if (numCompleted < numPuzzles) {
                puzzleListManager.showNextPuzzle(numCompleted);
            } else {
                Toast.makeText(context, "END OF GAME!", Toast.LENGTH_SHORT).show();

                //check if total score achievement is achieved
                if (achievementsManager.checkTotalScoreAchievement()){
                    Toast.makeText(context, "Achievement Unlocked: Needs More Challenge",
                            Toast.LENGTH_SHORT).show();
                    puzzleGameView.updateAchievement();
                }
                pauseGame();
                puzzleGameView.showFinalScore(puzzleGameDataGateway.getScore());
            }
        }
    }

    public void buyItem(String item) {
        mysteryShopManager.buyItem(item);
        updateScore();
    }

    /**
     * Check whether the puzzle is solved
     */
    private boolean isSolved() {
        boolean solved = false;
        String[] tileList = puzzleGenerator.getCurrentPosition();
        for (int i = 0; i < tileList.length; i++) {
            if (tileList[i].equals(String.valueOf(i))) {
                solved = true;
            } else {
                solved = false;
                break;
            }
        }

        return solved;
    }

    private void updateNumMoves() {
        puzzleGameDataGateway.updateNumMoves();
        String numMovesFormatted = String.format(Locale.getDefault(),
                "# of Moves: %d", puzzleGameDataGateway.getNumMoves());
        puzzleGameView.showNumMoves(numMovesFormatted);
    }

    private void clearNumMoves() {
        puzzleGameDataGateway.clearNumMoves();
        String numMovesFormatted = String.format(Locale.getDefault(),
                "# of Moves: %d", puzzleGameDataGateway.getNumMoves());
        puzzleGameView.showNumMoves(numMovesFormatted);
    }

    private void updateNumCompleted() {
        puzzleGameDataGateway.updateNumCompleted();
        String puzzleFormatted = String.format(Locale.getDefault(),
                "Puzzles Completed: %d", puzzleGameDataGateway.getNumCompleted());
        puzzleGameView.showNumCompleted(puzzleFormatted);
    }

    private void updateScore() {
        int score = puzzleGameDataGateway.getScore();
        String scoreFormatted = String.format(Locale.getDefault(),
                "Score: %d", score);
        puzzleGameView.showScore(scoreFormatted);
    }
}