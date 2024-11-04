package com.group_name.connectfourgame.activities;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.animation.ObjectAnimator;
import android.view.View;
import com.group_name.connectfourgame.MatchHistory.ResultType;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.group_name.connectfourgame.R;
import com.group_name.connectfourgame.User;
import com.group_name.connectfourgame.db.MatchHistoryModel;
import com.group_name.connectfourgame.fragments.GridDimensionFragment;
import com.group_name.connectfourgame.resource.ResourceColorStateList;
import com.group_name.connectfourgame.types.ArrayList2d;
import com.group_name.connectfourgame.types.PointInt;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    public final static String GAME_MODE = "GAME_ACTIVITY-GAME_MODE";
    public final static String PLAYER1 = "GAME_ACTIVITY-PLAYER1";
    public final static String PLAYER2 = "GAME_ACTIVITY-PLAYER2";
    public final static String GRID_DIMENSION = "GAME_ACTIVITY-GRID_DIMENSION";

    private final String GRID_DATA = "GAME_ACTIVITY-GRID_DATA";
    private final String GRID_COLS = "GAME_ACTIVITY-GRID_COLS";
    private final String PLAYER1_COLOR = "GAME_ACTIVITY-PLAYER1_COLOR";
    private final String PLAYER2_COLOR = "GAME_ACTIVITY-PLAYER2_COLOR";
    private final String CURRENT_PLAYER = "GAME_ACTIVITY-CURRENT_PLAYER";
    private final String EMPTY_SPACES = "GAME_ACTIVITY-EMPTY_SPACES";
    private final String MENU_POPUP = "GAME_ACTIVITY-MENU_POPUP";
    private final String GAMEOVER_POPUP = "GAME_ACTIVITY-GAMEOVER_POPUP";
    private final String GAME_FINISHED = "GAME_ACTIVITY-GAME_FINISHED";
    private final String MOVE_HISTORY = "GAME_ACTIVITY-MOVE_HISTORY";
    private final String MOVE_HISTORY_POINTER = "GAME_ACTIVITY-MOVE_HISTORY_POINTER";

    public static class GameActivityModel extends ViewModel {
        public final MutableLiveData<Integer> currentPlayer = new MutableLiveData<>();
    }

    private GameActivityModel gameActivityModel;

    private GridLayout gridLayout;
    private TextView turnPlayer1;
    private TextView turnPlayer2;
    private ConstraintLayout gameOverPopup;
    private View gameMenu;

    private ArrayList2d<Integer> gridState;
    private ArrayList<Integer> moveHistory;
    private int currentMoveHistory = 0;

    private boolean isAiGameMode;
    private User player1;
    private User player2;
    private ResourceColorStateList player1_color;
    private ResourceColorStateList player2_color;

    private int emptySpaces = 0;
    private boolean gameFinished = false;
    private Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_board);
        // bind views
        gridLayout = findViewById(R.id.gameGrid);
        turnPlayer1 = findViewById(R.id.player1_turn);
        turnPlayer2 = findViewById(R.id.player2_turn);
        gameOverPopup = findViewById(R.id.game_over_popup);
        gameMenu = findViewById(R.id.menu_container);

        gameActivityModel = new ViewModelProvider(this).get(GameActivityModel.class);

        // get data
        Intent data = getIntent();
        isAiGameMode = data.getBooleanExtra(GAME_MODE, false);
        player1 = (User) data.getSerializableExtra(PLAYER1);
        player2 = (User) data.getSerializableExtra(PLAYER2);
        final String dimension = data.getStringExtra(GRID_DIMENSION);

        // preserve aspect ratio of grid layout
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) gridLayout.getLayoutParams();
        params.dimensionRatio = (
            getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT?
                "H" : "W")+
            ", "+dimension.replace('x', ':'
        );
        gridLayout.setLayoutParams(params);

        // set colCount and rowCount of gridLayout
        final int[] dimensions = new int[]{(int)(dimension.charAt(0) - '0'), (int)(dimension.charAt(2) - '0')};
        gridLayout.setColumnCount(dimensions[0]);
        gridLayout.setRowCount(dimensions[1]);

        random = new Random();

        final int[] colors = new int[]{
            R.color.red, R.color.yellow, R.color.green,
            R.color.purple, R.color.brown, R.color.black
        };

        gameActivityModel.currentPlayer.observe(this, (newValue) -> {
            if(newValue == 1) {
                turnPlayer1.setVisibility(View.VISIBLE);
                turnPlayer2.setVisibility(View.INVISIBLE);
            } else {
                turnPlayer1.setVisibility(View.INVISIBLE);
                turnPlayer2.setVisibility(View.VISIBLE);
            }
        });

        findViewById(R.id.undo_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!gameFinished)
                    undoMove();
            }
        });
        findViewById(R.id.redo_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!gameFinished)
                    redoMove();
            }
        });
        // set State
        if(savedInstanceState != null) {
            gridState = new ArrayList2d<>(
                savedInstanceState.getIntegerArrayList(GRID_DATA),
                savedInstanceState.getInt(GRID_COLS)
            );
            final int player1Color = savedInstanceState.getInt(PLAYER1_COLOR);
            final int player2Color = savedInstanceState.getInt(PLAYER2_COLOR);
            gameFinished = savedInstanceState.getBoolean(GAME_FINISHED);
            player1_color = new ResourceColorStateList(this, player1Color, colors[player1Color]);
            player2_color = new ResourceColorStateList(this, player2Color, colors[player2Color]);
            emptySpaces = savedInstanceState.getInt(EMPTY_SPACES);
            moveHistory = savedInstanceState.getIntegerArrayList(MOVE_HISTORY);
            currentMoveHistory = savedInstanceState.getInt(MOVE_HISTORY_POINTER);
            gameActivityModel.currentPlayer.setValue(savedInstanceState.getInt(CURRENT_PLAYER));
            gameOverPopup.setVisibility(savedInstanceState.getBoolean(GAMEOVER_POPUP) ? View.VISIBLE : View.GONE);
            gameMenu.setVisibility(savedInstanceState.getBoolean(MENU_POPUP) ? View.VISIBLE : View.GONE);
            if(savedInstanceState.getBoolean(MENU_POPUP))
                ((GridDimensionFragment)getSupportFragmentManager().findFragmentById(R.id.grid_dimension_fragment)).setDimensionOnLoad(dimension);
        } else {
            gridState = ArrayList2d.asFill(0, dimensions[0], dimensions[1]);
            emptySpaces = gridState.getTotalCells();
            moveHistory = new ArrayList<>(emptySpaces);
            gameActivityModel.currentPlayer.setValue(random.nextBoolean()? 1 : -1);
            ((GridDimensionFragment)getSupportFragmentManager().findFragmentById(R.id.grid_dimension_fragment)).setDimensionOnLoad(dimension);
            // set color of discs
            if(!isAiGameMode) {
                if ( !(player1 == null || player2 == null || player1.disc1 != player2.disc1) ) {
                    // same primary disc color, decide which one to take randomly
                    if (random.nextBoolean()) { // player1 gets the disc color. player2 randomly selects from disc2 and disc3
                        player1_color = new ResourceColorStateList(this, player1.disc1, colors[player1.disc1]);
                        final int player2_colorId = random.nextBoolean() ? player2.disc2 : player2.disc3;
                        player2_color = new ResourceColorStateList(this, player2_colorId, colors[player2_colorId]);
                    } else { // player2 gets disc color. player1 randomly selects from disc2 and disc3
                        final int player1_colorId = random.nextBoolean() ? player1.disc2 : player1.disc3;
                        player1_color = new ResourceColorStateList(this, player1_colorId, colors[player1_colorId]);
                        player2_color = new ResourceColorStateList(this, player2.disc1, colors[player2.disc1]);
                    }
                } else {
                    // default red for player1 and yellow for player2 when all players are Guest
                    // When playing as guest, use other players default disc2
                    // Typically takes primary player disc color player1.disc1
                    player1_color = player1 == null? (player2 == null?
                            new ResourceColorStateList(this, 0, R.color.red)
                            : new ResourceColorStateList(this, player2.disc2, colors[player2.disc2])
                    ) : new ResourceColorStateList(this, player1.disc1, colors[player1.disc1]);
                    player2_color = player2 == null? (player1 == null?
                            new ResourceColorStateList(this, 1, R.color.yellow)
                            : new ResourceColorStateList(this, player1.disc2, colors[player1.disc2])
                    ): new ResourceColorStateList(this, player2.disc1, colors[player2.disc1]);
                }
            } else {
                if(player1 != null) {
                    player1_color = new ResourceColorStateList(this, player1.disc1, colors[player1.disc1]);
                    player2_color = new ResourceColorStateList(this, player1.disc2, colors[player1.disc2]);
                } else {
                    player1_color = new ResourceColorStateList(this, 0, R.color.red);
                    player2_color = new ResourceColorStateList(this, 1, R.color.yellow); // AI is yellow
                }
            }
        }

        final int[] avatars = new int[]{
            R.drawable.avatar1, R.drawable.avatar2, R.drawable.avatar3,
            R.drawable.avatar4, R.drawable.avatar5, R.drawable.avatar6
        };

        // set avatars
        if(player1 != null) {
            ((ImageView)findViewById(R.id.player1_avatar)).setImageDrawable(ResourcesCompat.getDrawable(getResources(), avatars[player1.avatar], null));
            ((TextView)findViewById(R.id.player1_name)).setText(player1.username);
        } else {
            findViewById(R.id.player1_avatar).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.player1_name)).setText(R.string.text_land_guest);
        }
        if(player2 != null) {
            ((ImageView)findViewById(R.id.player2_avatar)).setImageDrawable(ResourcesCompat.getDrawable(getResources(), avatars[player2.avatar], null));
            ((TextView)findViewById(R.id.player2_name)).setText(player2.username);
        } else {
            findViewById(R.id.player2_avatar).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.player2_name)).setText(R.string.text_land_guest);
        }
        findViewById(R.id.player1_container).setBackgroundTintList(player1_color);
        turnPlayer1.setTextColor(player1_color);
        findViewById(R.id.player2_container).setBackgroundTintList(player2_color);
        turnPlayer2.setTextColor(player2_color);

        setupGridLayout();
        if(savedInstanceState != null)
            refreshGrid();

        findViewById(R.id.menu_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameMenu.setVisibility(View.VISIBLE);
            }
        });
        findViewById(R.id.menu_resume).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameMenu.setVisibility(View.GONE);
            }
        });
        findViewById(R.id.menu_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.exit_option).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.play_again_option).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetGame();
            }
        });
        findViewById(R.id.see_board_option).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int row = 0; row < gridState.getRowLength(); row++) {
                    for (int col = 0; col < gridState.getColLength(); col++) {
                        final View cell = gridLayout.getChildAt(row * gridState.getColLength() + col);
                        cell.setOnClickListener(null);
                    }
                }
                gameOverPopup.setVisibility(View.GONE);
            }
        });
        findViewById(R.id.menu_reset_game).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String dimension = ((GridDimensionFragment)getSupportFragmentManager().findFragmentById(R.id.grid_dimension_fragment)).getDimension();
                changeGridDimensions(dimension);
                gameMenu.setVisibility(View.GONE);
            }
        });
        if(isAiGameMode && gameActivityModel.currentPlayer.getValue() == -1)
            makeAiMove();
    }

    private void makeAiMove() {
        final int numRows = gridState.getRowLength();
        while(true) {
            boolean success = false;
            final int aiNumber = random.nextInt(gridState.getColLength()); // generate random number between 0 and number of columns
            for (int row = numRows - 1; row >= 0; row--) {
                if (gridState.get(aiNumber, row) != 0)
                    continue;
                handleColumnClick(aiNumber);
                success = true;
                break;
            }
            if(success)
                break;
        }
    }

    private void refreshGrid() {
        int i = 0;
        for(Integer cellData : gridState) {
            if(cellData == 0) {
                ++i;
                continue;
            }
            ImageView cell = (ImageView) gridLayout.getChildAt(i);
            if (cellData == 1) {
                cell.setImageResource(R.drawable.disc);
                cell.setImageTintList(player1_color);
            } else {
                cell.setImageResource(R.drawable.disc);
                cell.setImageTintList(player2_color);
            }
            ++i;
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList(GRID_DATA, gridState.getRawArrayList());
        outState.putInt(GRID_COLS, gridState.getColLength());
        outState.putInt(PLAYER1_COLOR, player1_color.getResId());
        outState.putInt(PLAYER2_COLOR, player2_color.getResId());
        outState.putInt(EMPTY_SPACES, emptySpaces);
        outState.putInt(CURRENT_PLAYER, gameActivityModel.currentPlayer.getValue());
        outState.putBoolean(GAMEOVER_POPUP, gameOverPopup.getVisibility() == View.VISIBLE);
        outState.putBoolean(GAME_FINISHED, gameFinished);
        outState.putInt(MOVE_HISTORY_POINTER, currentMoveHistory);
        outState.putIntegerArrayList(MOVE_HISTORY, moveHistory);
        outState.putBoolean(MENU_POPUP, gameMenu.getVisibility() == View.VISIBLE);
    }

    private void changeGridDimensions(final String dimension) {
        gridLayout.removeAllViews();
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) gridLayout.getLayoutParams();
        params.dimensionRatio = (
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT?
                        "H" : "W")+
                ", "+dimension.replace('x', ':'
        );
        gridLayout.setLayoutParams(params);
        PointInt dimensions = new PointInt((int)(dimension.charAt(0) - '0'), (int)(dimension.charAt(2) - '0'));
        gridLayout.setColumnCount(dimensions.get(0));
        gridLayout.setRowCount(dimensions.get(1));
        resetGame(ArrayList2d.asFill(0, dimensions.x, dimensions.y));
    }

    private void setupGridLayout() {
        gridLayout.removeAllViews();
        for (int row = 0; row < gridState.getRowLength(); row++) {
            for (int col = 0; col < gridState.getColLength(); col++) {
                final ImageView cell = new ImageView(this);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height = 0;
                params.columnSpec = GridLayout.spec(col, 1f);
                params.rowSpec = GridLayout.spec(row, 1f);
                params.setMargins(1, 1, 1, 1);
                cell.setLayoutParams(params);
                cell.setBackgroundResource(R.drawable.blank_disc);

                final int finalCol = col;
                if(!gameFinished)
                    cell.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(isAiGameMode && gameActivityModel.currentPlayer.getValue() != 1)
                                return;
                            handleColumnClick(finalCol);
                            if(isAiGameMode)// do players turn and AIs turn in the same time so that no clicks are required for AI
                                makeAiMove();
                        }
                    });
                gridLayout.addView(cell);
            }
        }
    }

    private void handleColumnClick(int clickedCol) {
        handleColumnClick(clickedCol, false);
    }

    private void handleColumnClick(int clickedCol, boolean fromRedo) {
        // Find the lowest available row in the clicked column
        final int numRows = gridState.getRowLength();
        for (int row = numRows - 1; row >= 0; row--) {
            if (gridState.get(clickedCol, row) != 0)
                continue;

            gridState.set(clickedCol, row, gameActivityModel.currentPlayer.getValue());

            int cellIndex = row * gridState.getColLength() + clickedCol;
            ImageView cell = (ImageView) gridLayout.getChildAt(cellIndex);

            // Set the initial disc image and tint based on the current player
            if (gameActivityModel.currentPlayer.getValue() == 1) {
                cell.setImageResource(R.drawable.disc);
                cell.setImageTintList(player1_color);
            } else {
                cell.setImageResource(R.drawable.disc);
                cell.setImageTintList(player2_color);
            }

            ObjectAnimator dropAnimation = ObjectAnimator.ofFloat(cell, "translationY", -gridLayout.getHeight(), 0);
            dropAnimation.setDuration(500);
            dropAnimation.start();

            if (!fromRedo) {
                if (currentMoveHistory != moveHistory.size())
                    moveHistory.subList(currentMoveHistory, moveHistory.size()).clear();
                moveHistory.add(clickedCol);
                currentMoveHistory = moveHistory.size();
            }

            checkWinCondition(row, clickedCol);
            gameActivityModel.currentPlayer.setValue(gameActivityModel.currentPlayer.getValue() * -1);
            break;
        }
    }

    private void removeDisc(final int col) {
        final int numRows = gridState.getRowLength();
        for (int row = 0; row < numRows; row++) {
            if (gridState.get(col, row) == 0)
                continue;
            gridState.set(col, row, 0);
            int cellIndex = row * gridState.getColLength() + col;
            ImageView cell = (ImageView) gridLayout.getChildAt(cellIndex);
            cell.setImageResource(0);
            cell.setImageTintList(null);
            gameActivityModel.currentPlayer.setValue(gameActivityModel.currentPlayer.getValue() * -1);
            break;
        }
        ++emptySpaces;
    }

    private void undoMove() {
        if(currentMoveHistory == 0)
            return;
        removeDisc(moveHistory.get(currentMoveHistory -= 1));
        if (isAiGameMode) {
            if(currentMoveHistory != 0)
                removeDisc(moveHistory.get(currentMoveHistory -= 1));
            if(gameActivityModel.currentPlayer.getValue() == -1)
                makeAiMove();
        }
    }

    private void redoMove() {
        if(currentMoveHistory == moveHistory.size())
            return;
        handleColumnClick(moveHistory.get(currentMoveHistory), true);
        currentMoveHistory += 1;
        if(isAiGameMode) {
            if(currentMoveHistory != moveHistory.size()) {
                handleColumnClick(moveHistory.get(currentMoveHistory), true);
                currentMoveHistory += 1;
            } else if(gameActivityModel.currentPlayer.getValue() == -1)
                makeAiMove();
        }
    }

    private void checkWinCondition(final int row, final int col) {
        final int numRows = gridState.getRowLength();
        int player = gridState.get(col, row);

        PointInt vertical = new PointInt(), horizontal = new PointInt(),
            diagonalTopLeftToBottomRight = new PointInt(),
            diagonalBottomLeftToTopRight = new PointInt();

        final int maxTop = Math.max(row-3, 0);
        final int minBottom = Math.min(row+4, numRows);
        final int minLeft = Math.max(col - 3, 0);
        final int maxRight = Math.min(col+4, gridState.getColLength());
        for(int r = row+1; r < minBottom; ++r) { // bottom
            if(gridState.get(col, r) != player)
                break;
            vertical.x++;
        }
        for(int r = row-1; r >= maxTop; --r) { // top
            if(gridState.get(col, r) != player)
                break;
            vertical.y++;
        }
        for(int c = col+1; c < maxRight; ++c) { // right
            if(gridState.get(c, row) != player)
                break;
            horizontal.x++;
        }
        for(int c = col-1; c >= minLeft; --c) { // left
            if(gridState.get(c, row) != player)
                break;
            horizontal.y++;
        }
        for(int c = col-1, r = row-1; c >= minLeft && r >= maxTop; --c, --r) { // top left
            if(gridState.get(c, r) != player)
                break;
            ++diagonalTopLeftToBottomRight.x;
        }
        for(int c = col+1, r = row-1; c < maxRight && r >= maxTop; ++c, --r) { //top right
            if(gridState.get(c, r) != player)
                break;
            ++diagonalBottomLeftToTopRight.y;
        }
        for(int c = col-1, r = row+1; c >= minLeft && r < minBottom; --c, ++r) { // bottom left
            if(gridState.get(c, r) != player)
                break;
            ++diagonalBottomLeftToTopRight.x;
        }
        for(int c = col+1, r = row+1; c < maxRight && r < minBottom; ++c, ++r) { //bottom right
            if(gridState.get(c, r) != player)
                break;
            ++diagonalTopLeftToBottomRight.y;
        }

        if(
            (horizontal.x+horizontal.y) >= 3 ||
            (vertical.x+vertical.y) >= 3 ||
            (diagonalBottomLeftToTopRight.x+diagonalBottomLeftToTopRight.y) >= 3 ||
            (diagonalTopLeftToBottomRight.x+diagonalTopLeftToBottomRight.y) >= 3
        ) {
            displayWinMessage(player);
            return;
        }
        --emptySpaces;
        if(emptySpaces == 0)
            displayDrawMessage();
    }

    private boolean checkLine(int startRow, int startCol, int rowStep, int colStep, int player) {
        for (int i = 0; i < 4; i++) {
            int r = startRow + i * rowStep;
            int c = startCol + i * colStep;
            if (r < 0 || r >= gridState.getRowLength() || c < 0 || c >= gridState.getColLength() || gridState.get(c,r) != player)
                return false;
        }
        return true;
    }

    private void displayDrawMessage() {
        gameFinished = true;
        if(isAiGameMode)
            MatchHistoryModel.storeAIResults(player1, ResultType.DRAW);
        else
            MatchHistoryModel.storeResults(player1, ResultType.DRAW, player2);

        findViewById(R.id.winner_container).setVisibility(View.GONE);
        ((TextView)findViewById(R.id.congratulation_text)).setText(getResources().getString(R.string.text_draw));
        gameOverPopup.setVisibility(View.VISIBLE);
    }

    private void displayWinMessage(int player) {
        gameFinished = true;
        final int[] avatars = new int[]{
            R.drawable.avatar1, R.drawable.avatar2, R.drawable.avatar3,
            R.drawable.avatar4, R.drawable.avatar5, R.drawable.avatar6
        };

        ((TextView)findViewById(R.id.congratulation_text)).setText(getResources().getString(
            player != 1 && isAiGameMode? R.string.text_lose : R.string.text_winner
        ));

        if(isAiGameMode)
            MatchHistoryModel.storeAIResults(player1, player == 1? ResultType.PLAYER1_WIN : ResultType.PLAYER2_WIN);
        else
            MatchHistoryModel.storeResults(player1, player == 1? ResultType.PLAYER1_WIN : ResultType.PLAYER2_WIN, player2);

        if(player == 1) {
            ImageView avatar = findViewById(R.id.winner_avatar);
            avatar.setVisibility(View.VISIBLE);
            if(player1 != null)
                avatar.setImageDrawable(ResourcesCompat.getDrawable(getResources(), avatars[player1.avatar], null));
            ((TextView)findViewById(R.id.winner_name)).setText(player1 == null? getResources().getString(R.string.text_land_guest) : player1.username);
        } else {
            ImageView avatar = findViewById(R.id.winner_avatar);
            avatar.setVisibility(View.VISIBLE);
            if(isAiGameMode) {
                if(player1 != null)
                    avatar.setImageDrawable(ResourcesCompat.getDrawable(getResources(), avatars[player1.avatar], null));
                ((TextView)findViewById(R.id.winner_name)).setText(player1 == null? getResources().getString(R.string.text_land_guest) : player1.username);
            } else {
                if(player2 != null)
                    avatar.setImageDrawable(ResourcesCompat.getDrawable(getResources(), avatars[player2.avatar], null));
                ((TextView)findViewById(R.id.winner_name)).setText(player2 == null? getResources().getString(R.string.text_land_guest) : player2.username);
            }
        }
        gameOverPopup.setVisibility(View.VISIBLE);
    }

    private void resetGame() {
        resetGame(ArrayList2d.asFill(0, gridState.getColLength(), gridState.getRowLength()));
    }
    private void resetGame(final ArrayList2d<Integer> newGridState) {
        gameFinished = false;
        gameOverPopup.setVisibility(View.GONE);
        gridState = newGridState;
        setupGridLayout();
        moveHistory = new ArrayList<>(gridState.getTotalCells());
        currentMoveHistory = 0;
        emptySpaces = gridState.getTotalCells();
        gameActivityModel.currentPlayer.setValue(random.nextBoolean()? 1 : -1);
        if(isAiGameMode && gameActivityModel.currentPlayer.getValue() == -1)
            makeAiMove();
    }
}
