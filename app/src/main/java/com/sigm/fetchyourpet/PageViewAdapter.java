package com.sigm.fetchyourpet;

import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PageViewAdapter extends PagerAdapter {

    private Dog[] dogList;
    private LayoutInflater layoutInflater;
    private Context context;
    private Dog d;

    public PageViewAdapter(Dog[] dogs, Context context) {
        this.dogList = dogs;
        this.context = context;
    }

    @Override
    public int getCount() {
        return dogList.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.view_matches_item, container, false);
        d = dogList[position];

        ImageView image;
        TextView age, size,name;
        ImageButton email, maps,search;

        email = view.findViewById(R.id.email);
        maps = view.findViewById(R.id.location);
        search = view.findViewById(R.id.google);
        email.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //this will log the page number that was click
                intent("email");

            }
        });
        maps.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //this will log the page number that was click
                intent("maps");

            }
        });

        search.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //this will log the page number that was click
                intent("google");

            }
        });

        image = view.findViewById(R.id.image);


        if(d.bitmapImage != null){
            Glide.with(context)
                    // .using(new FirebaseImageLoader())
                    .load(d.bitmapImage)
                    .into(image);

        }
        else {


            Glide.with(context)
                    // .using(new FirebaseImageLoader())
                    .load(d.imageStorageReference)
                    .into(image);
        }








        name = view.findViewById(R.id.name);
        age = view.findViewById(R.id.age);
        size = view.findViewById(R.id.size);

       // imageView.setImageResource(dogs.get(position).getImage());
        String s = dogList[position].getName();
        String agetext = "Age: " + dogList[position].getAge();
        age.setText(agetext);
        size.setText(dogList[position].getSize());
        name.setText(dogList[position].getName());

//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, DetailActivity.class);
//                intent.putExtra("param", models.get(position).getTitle());
//                context.startActivity(intent);
//                // finish();
//            }
//        });

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    public void intent(final String reason){


        MainActivity.firestore.collection("rescue")
                .whereEqualTo("rescueID", d.getRescueID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {

                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Rescue r = document.toObject(Rescue.class);

                                if(reason.equals("email")) {


                                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                                    emailIntent.setData(Uri.parse("mailto:" + r.getEmail()));


                                    try {
                                        context.startActivity(emailIntent);
                                    } catch (ActivityNotFoundException e) {
                                        //TODO: Handle case where no email app is available

                                        Toast t = Toast.makeText(context, "Could not open an email app",
                                                Toast.LENGTH_SHORT);
                                        t.setGravity(Gravity.TOP, Gravity.CENTER, 150);
                                        t.show();
                                    }
                                }else if(reason.equals("maps")){

                                    String s;
                                    s = r.getStreet() + ", " + r.getCity().replaceAll(" ","+") + ", " + r.getState();


                                    Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + s);
                                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                    mapIntent.setPackage("com.google.android.apps.maps");
                                    context.startActivity(mapIntent);




                                }else if(reason.equals("google")){
                                    Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                                    intent.putExtra(SearchManager.QUERY, r.getOrganization()); // query contains search string
                                    context.startActivity(intent);

                                }



                            }
                        } else {
                            Log.d("test", "Error getting documents: ", task.getException());
                        }
                    }


                });

    }



}