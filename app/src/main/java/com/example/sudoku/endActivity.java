package com.example.sudoku;

import static com.example.sudoku.MainActivity.stateOfGame;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class endActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_end);

        //Вывод результатов игры
        Intent i = getIntent();
        ((TextView) findViewById(R.id.time)).setText(i.getStringExtra("time"));
        ((TextView) findViewById(R.id.mistakes)).setText(i.getStringExtra("mistakes"));
        ((TextView) findViewById(R.id.hints)).setText(i.getStringExtra("hints"));
        ((TextView) findViewById(R.id.reason)).setText(i.getStringExtra("reason"));
        ((TextView) findViewById(R.id.textView5)).setText(i.getStringExtra("result"));

        //Если игра завершилась неудачно, то даём пользователю второй шанс
        Button retry = findViewById(R.id.button10);
        if(i.getStringExtra("result").equals(getResources().getString(R.string.gameover))) {
            retry.setVisibility(View.VISIBLE);
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(endActivity.this, GameActivity.class);
                    stateOfGame.mistakes = 0;
                    stateOfGame.hints = 0;
                    stateOfGame.seconds = 0;
                    for(int i = 0; i < stateOfGame.max_num; i++){
                        for(int j = 0; j < stateOfGame.max_num; j++){
                            stateOfGame.current_field[i][j] = stateOfGame.cells.question[i][j];
                            stateOfGame.previous_field[i][j] = stateOfGame.cells.question[i][j];
                        }
                    }
                    stateOfGame.table_notes = new int[stateOfGame.max_num][stateOfGame.max_num][stateOfGame.max_num];
                    stateOfGame.previous_table_notes = new int[stateOfGame.max_num][stateOfGame.max_num][stateOfGame.max_num];
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        stateOfGame.types = new int[6];
        stateOfGame.cells = null;
        stateOfGame.mistakes = 0;
        stateOfGame.hints = 0;
        stateOfGame.seconds = 0;
        try{
            Gson gson = new Gson();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("GameState.json", Context.MODE_PRIVATE));
            outputStreamWriter.write(gson.toJson(stateOfGame));
            outputStreamWriter.close();
        }catch (IOException e){

        }
    }
}
