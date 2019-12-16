package com.example.newpost;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class FeedAvtivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ArrayList<String>  userEmailFromFB;
    ArrayList<String> userCommentFromFB;
    ArrayList<String> userImageFromFB;

    FeedRecyclerAdapter feedRecyclerAdapter;
    RecyclerView recyclerView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.add_post){
            Intent intent=new Intent(FeedAvtivity.this,UploadActivity.class);
            startActivity(intent);
            finish();
        }else if(item.getItemId()==R.id.sign_out){
            firebaseAuth.signOut();
            Intent intent=new Intent(FeedAvtivity.this,SignInActivity.class);
            startActivity(intent);

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_avtivity);
        firebaseAuth=FirebaseAuth.getInstance();

        firebaseFirestore=FirebaseFirestore.getInstance();

        userCommentFromFB=new ArrayList<>();
        userEmailFromFB=new ArrayList<>();
        userImageFromFB=new ArrayList<>();

    getDataFromFirestore();


    //RecyclerView
    recyclerView =findViewById(R.id.recyclerView);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

        feedRecyclerAdapter=new FeedRecyclerAdapter(userEmailFromFB,userCommentFromFB,userImageFromFB);
        recyclerView.setAdapter(feedRecyclerAdapter);




    }
// snapshot veritabanı güncelleme

    public void getDataFromFirestore(){
        CollectionReference collectionReference=firebaseFirestore.collection("Post");
        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e!=null){
                    Toast.makeText(FeedAvtivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
               if(queryDocumentSnapshots!=null){

                   for(DocumentSnapshot snapshot:queryDocumentSnapshots.getDocuments()){
                       Map<String,Object> data =snapshot.getData();
                       String comment=(String) data.get("comment");
                       String downloadUrl=(String) data.get("downloadUrl");
                       String email=(String) data.get("email");

                       userImageFromFB.add(downloadUrl);
                       userEmailFromFB.add(email);
                       userCommentFromFB.add(comment);

                       feedRecyclerAdapter.notifyDataSetChanged();


                   }

               }




            }
        });




    }


}
