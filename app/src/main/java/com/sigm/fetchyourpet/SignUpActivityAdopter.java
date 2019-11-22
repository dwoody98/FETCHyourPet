package com.sigm.fetchyourpet;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.provider.MediaStore;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

import static com.sigm.fetchyourpet.SignUpActivityRescue.isValid;

public class SignUpActivityAdopter extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    EditText firstNameView, lastNameView, zipView, emailView, passwordView, password2;
    String firstName,lastName, zip, email, password, confirmPassword;
    ImageView picView;
    Button picButton, createAccount;
    Bitmap bitmap = null;
    Boolean edit;
    PotentialAdopter currentAdopter;

    private static int GET_FROM_GALLERY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_adopter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        edit = getIntent().getBooleanExtra("editProfile",false);
        setSupportActionBar(toolbar);
        createAccount=findViewById(R.id.createAccount);
        picView = findViewById(R.id.pic);
        picButton = findViewById(R.id.picButton);
        firstNameView = findViewById(R.id.firstName);
        lastNameView = findViewById(R.id.lastName);
        emailView = findViewById(R.id.email);
        passwordView = findViewById(R.id.password);
        password2 = findViewById(R.id.confirmPassword);
        zipView = findViewById(R.id.zip);
        if(edit){
            toolbar.setTitle("EDIT PROFILE");
            createAccount.setText("UPDATE PROFILE");
            PotentialAdopter pa = new PotentialAdopter();
            currentAdopter = pa.getCurrentAdopter();
            firstNameView.setText(currentAdopter.getFirstName());
            lastNameView.setText(currentAdopter.getLastname());
            emailView.setText(currentAdopter.getEmail());
            zipView.setText(currentAdopter.getZip());
        }
        else {
            toolbar.setTitle("SIGN UP!");
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        if(edit){
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.adopter_drawer);

        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


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

        if (id == R.id.sign_in) {
            startActivity(new Intent(this, SignInActivity.class));

        } else if (id == R.id.browse_all_animals) {
            startActivity(new Intent(this, Collection.class).putExtra("user","adopter"));


        } else if (id == R.id.home) {
            startActivity(new Intent(this, MainActivity.class));
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void createAccount(View view) {
        String action = "";




        firstName = firstNameView.getText().toString().trim();
        lastName = lastNameView.getText().toString().trim();
        if(firstName.equals("") | lastName.equals("")){
            action = "Please enter your first and last name!";
        }

        zip = zipView.getText().toString().trim();

        email = emailView.getText().toString().trim();
        //here, we will say:
        //if email not in database, then we will create the account
        //if(db.getAccounts.contains(email)){
        //  action="An account already exists with this email!"
        //}


        if (!zip.matches("[0-9]+") | zip.length() < 5) {
            action = "Zip code is invalid! Be sure to enter only numbers.";

        }

        if(!isValid(email)){
            action = "The email address entered is not in a valid format.";
        }



        //we probably need to sanitize the password input
        password = passwordView.getText().toString().trim();
        if(password.equals("")){
            action = "Please enter a password";
        }
        else if(!password.equals(password2.getText().toString().trim())){
            action = "Passwords do not match. Please enter them again.";
            passwordView.setText("");
            password2.setText("");
        }
        boolean test=false;

        if(firstName.equals("test")){
            action = "";
            test=true;
        }


        if(!action.equals("")){
            Toast t = Toast.makeText(this, action,
                    Toast.LENGTH_SHORT);
            t.setGravity(Gravity.TOP, Gravity.CENTER, 150);
            t.show();
        }
        else{
            //    public Rescue(String name, String street, String city, String state, int zip, String email, String password){
           if(edit){
               currentAdopter.setBitmap(bitmap);
               currentAdopter.setFirstName(firstName);
               currentAdopter.setLastname(lastName);
               currentAdopter.setZip(Integer.parseInt(zip));
               currentAdopter.setEmail(email);
               currentAdopter.setPassword(password);




           }

               PotentialAdopter p = new PotentialAdopter(bitmap, firstName, lastName, Integer.parseInt(zip), email, password);
               p.setCurrentAdopter(p);

               Intent dashboard = new Intent(this, AdopterDashboard.class);
               startActivity(dashboard);
               finish();


        }


    }
    public void uploadPhoto(View view){
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, GET_FROM_GALLERY);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                Glide
                        .with(this)
                        .load(bitmap)
                        .into(picView);
                picButton.setText("CHANGE PHOTO");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


