package com.example.questionnaireapplication;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class QuestionnaireActivity extends AppCompatActivity {

    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private ImageButton signOutBtn;

    private DatabaseReference mDatabase;

    private RecyclerView recyclerView;

    private List<Poll> polls = new ArrayList<>();

    private PollAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        recyclerView = findViewById(R.id.recyclerViewQuestion);
         adapter = new PollAdapter(polls);

        mDatabase = FirebaseDatabase.getInstance("https://questionnaireapplication-49ae8-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(QuestionnaireActivity.this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(QuestionnaireActivity.this);

        if(acct != null){
            MyAccount.name = acct.getDisplayName();
//            String personEmail = acct.getEmail();

            Toast toast = Toast.makeText(QuestionnaireActivity.this, "Добро пожаловать " +  MyAccount.name, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        }

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                polls.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Poll poll = ds.getValue(Poll.class);
                    polls.add(poll);
                }
                adapter.notifyItemRangeChanged(0,adapter.getItemCount());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        };

        mDatabase.child("polls").addValueEventListener(postListener);

        signOutBtn = findViewById(R.id.signout);

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

//
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public class PollAdapter extends RecyclerView.Adapter<PollAdapter.ViewHolder>{

        private List<Poll> pollList;

        public PollAdapter(List<Poll> polls){
            pollList=polls;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View pollView = inflater.inflate(R.layout.poll_item, parent, false);

            ViewHolder viewHolder = new ViewHolder(pollView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Poll poll = pollList.get(position);

            holder.textView.setText(poll.question);
            holder.b1.setText(poll.c1);
            holder.b2.setText(poll.c2);
            holder.b3.setText(poll.c3);
            holder.b4.setText(poll.c4);

        }

        @Override
        public int getItemCount() {
            return pollList.size();
        }

        public class ViewHolder extends  RecyclerView.ViewHolder {

            private TextView textView;
            private Button b1,b2,b3,b4;

            public ViewHolder(@NonNull View itemView){
                super(itemView);
                textView = itemView.findViewById(R.id.question_text);
                b1 = itemView.findViewById(R.id.b1);
                b2 = itemView.findViewById(R.id.b2);
                b3 = itemView.findViewById(R.id.b3);
                b4 = itemView.findViewById(R.id.b4);

//                b1.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        b1.setBackgroundColor(Color.parseColor("00FF00"));
//                        b2.setEnabled(false);
//                        b3.setEnabled(false);
//                        b4.setEnabled(false);
//                    }
//                });

//                b2.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        b2.setBackgroundColor(Color.parseColor("00FF00"));
//                        b1.setEnabled(false);
//                        b3.setEnabled(false);
//                        b4.setEnabled(false);
//
//                    }
//                });

//                b3.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        b3.setBackgroundColor(Color.parseColor("00FF00"));
//                        b1.setEnabled(false);
//                        b2.setEnabled(false);
//                        b4.setEnabled(false);
//
//                    }
//                });

//                b4.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        b4.setBackgroundColor(Color.parseColor("00FF00"));
//                        b1.setEnabled(false);
//                        b2.setEnabled(false);
//                        b3.setEnabled(false);
//
//                    }
//                });
            }
        }

    }



    void signOut(){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                finish();
                startActivity(new Intent(QuestionnaireActivity.this, MainActivity.class));
            }
        });
    }
}