package com.sigm.fetchyourpet;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.android.material.navigation.NavigationView;
import com.sigm.fetchyourpet.Question;

public class QuizActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    // Need to decide how many questions our quiz will be, and allocate the correct amount of data (3*numOfQuestions)
    int[] values = new int [50];
    int currentQuestion = 0;
    ArrayList<Question> userQuestions = new ArrayList<>();
    Class c;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("FETCH! QUIZ");

        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        type = getIntent().getStringExtra("user");

        if(type.equals("adopter")){
            c = AdopterDashboard.class;
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.adopter_drawer);
        }
        else if(type.equals("rescue")){
            c = Dashboard.class;

            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.rescue_drawer);
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        userQuestions.add(new Question(0, "How active do you want your dog to be?", "Very active","kinda active", "not active at all"));
        userQuestions.add(new Question(1, "How sleepy do you want your dog to be?", "Very sleepy","kinda sleepy", "not sleepy at all"));
        userQuestions.add(new Question(3, "How happy do you want your dog to be?", "Very happy","kinda happy", "not happy at all"));
        userQuestions.add(new Question(4, "How badly do you want to go to bed right now?", "I am sleep","Plz lemme sleep", "I simply cannot go on any longer"));


        TextView question = findViewById(R.id.question1);
        CheckBox q1Answer1 = findViewById(R.id.Q1Answer1);
        CheckBox q1Answer2 = findViewById(R.id.q1Answer2);
        CheckBox q1Answer3 = findViewById(R.id.q1Answer3);

        question.setText(userQuestions.get(0).questionText);

        q1Answer1.setText(userQuestions.get(0).answer1);
        q1Answer1.setChecked(false);
        q1Answer2.setText(userQuestions.get(0).answer2);
        q1Answer1.setChecked(false);
        q1Answer3.setText(userQuestions.get(0).answer3);
        q1Answer1.setChecked(false);
    }
    public void onClickNext(View view) {
        Log.d("PET", Integer.toString(currentQuestion));
        TextView question = findViewById(R.id.question1);
        CheckBox q1Answer1 = findViewById(R.id.Q1Answer1);
        CheckBox q1Answer2 = findViewById(R.id.q1Answer2);
        CheckBox q1Answer3 = findViewById(R.id.q1Answer3);


            if (q1Answer1.isChecked()) {
                values[currentQuestion * 3] = 1;
            } else values[currentQuestion* 3] = 0;

            if (q1Answer2.isChecked()) {
                values[currentQuestion * 3 + 1] = 1;
            } else values[currentQuestion * 3 + 1] = 0;
            if (q1Answer3.isChecked()) {
                values[currentQuestion * 3 + 2] = 1;
            } else values[currentQuestion * 3 + 2] = 0;

        currentQuestion ++;

        if(currentQuestion < userQuestions.size()) {



            question.setText(userQuestions.get(currentQuestion).questionText);
            q1Answer1.setText(userQuestions.get(currentQuestion).answer1);
            q1Answer1.setChecked(false);
            q1Answer2.setText(userQuestions.get(currentQuestion).answer2);
            q1Answer2.setChecked(false);
            q1Answer3.setText(userQuestions.get(currentQuestion).answer3);
            q1Answer3.setChecked(false);

        }
        else {
            Button next = findViewById(R.id.nextQuestion);
            next.setVisibility(View.INVISIBLE);
        }
    }

    public void onClickSubmitQuiz(View view){
        Log.d("PET", Arrays.toString(values));

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.take_quiz) {
            //startActivity(new Intent(this, QuizActivity.class));

        } else if (id == R.id.browse_all_animals) {
            startActivity(new Intent(this, Collection.class).putExtra("user",type));


        } else if (id == R.id.home) {
            startActivity(new Intent(this, c));
        }
        else if (id==R.id.logout){
            startActivity(new Intent(this, MainActivity.class));
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void editProfile(View v){

    }

    public void viewYourMatches(View v){

    }



}



