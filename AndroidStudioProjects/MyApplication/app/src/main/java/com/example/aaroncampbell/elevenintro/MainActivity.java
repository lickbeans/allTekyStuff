package com.example.aaroncampbell.elevenintro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private GridView gameBoard;
    private EditText playerChar;
    private Button checkWinner;

    private int currentPlayer;
    private Player[] players = new Player[2];
    private GameTokenAdapter adapter;
    private ArrayList<Player> gameState = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("----", "onCreate");

        gameBoard = (GridView)findViewById(R.id.gameboard);
        playerChar = (EditText)findViewById(R.id.playerChar);
        checkWinner = (Button)findViewById(R.id.checkWinner);

        checkWinner.setOnClickListener(gameCheck);
        gameBoard.setOnItemClickListener(cellTapped);

        adapter = new GameTokenAdapter(this, R.layout.game_token, gameState);
        gameBoard.setAdapter(adapter);

        createGame();
    }

    private void createGame() {
        currentPlayer = 0;

        playerChar.setText("X");
        players[0]=null;
        players[1]=null;

        gameState.clear();

        for (int i = 0; i < 9; i++) {
            gameState.add(new Player(2, ' '));
        }
        adapter.notifyDataSetChanged();

    }

    private View.OnClickListener gameCheck = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Click event code
            //initialize all the winning combinations
            int[][] winstate =
                    {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};

            Player winner = null;

            for (int[] check : winstate) {
                if (gameState.get(check[0]).id != 2
                        && gameState.get(check[0]).id == gameState.get(check[1]).id
                        && gameState.get(check[0]).id == gameState.get(check[2]).id) {
                    //iterate through the winning states
                    winner = gameState.get(check[0]);
                    break;
                }
            }
            Intent i;

            i = new Intent(MainActivity.this, ResultsActivity.class);
            i.putExtra("char", winner.symbol);

            startActivity(i);

            if (winner == null) {
                Toast.makeText(MainActivity.this, "No winner", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "Winner is " + winner.symbol, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private AdapterView.OnItemClickListener cellTapped = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (players[currentPlayer] == null) {
                // if player doesn't exist, create one!
                char symbol;
                symbol = playerChar.getText().charAt(0);
                players[currentPlayer] = new Player(currentPlayer, symbol);
            }
            //update the gameboard
            gameState.set(position, players[currentPlayer]); //Setting a certain position in ArrayList to player value
            adapter.notifyDataSetChanged();

            currentPlayer++;

            if (currentPlayer == 2) {
                currentPlayer = 0;
            }

            if (players[currentPlayer] == null) {
                if (currentPlayer == 0) {
                    playerChar.setText("X");
                } else {
                    playerChar.setText("O");
                }
            }
            else {
                playerChar.setText(String.valueOf(players[currentPlayer].symbol));
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("----", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("----", "onResume");
    }

    @Override
    protected void onPause() {
        Log.d("----", "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d("----", "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("----", "onDestroy");
        super.onDestroy();
    }
}
