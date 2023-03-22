package com.example.questionnaireapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private ImageView googleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        googleBtn = findViewById(R.id.google_btn);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(MainActivity.this, gso);

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = gsc.getSignInIntent();
                GoogleSignInLauncher.launch(signInIntent);
            }
        });


    }

   ActivityResultLauncher<Intent> GoogleSignInLauncher = registerForActivityResult(
           new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
               @Override
               public void onActivityResult(ActivityResult result) {
                   Task<GoogleSignInAccount>task=GoogleSignIn.getSignedInAccountFromIntent(
                           result.getData()
                   );
                   try {
                       GoogleSignInAccount account = task.getResult(ApiException.class);
                       MyAccount.email = account.getEmail();
                       MyAccount.name = account.getDisplayName();
                       Toast.makeText(MainActivity.this, "Успешный вход в " + account.getEmail(), Toast.LENGTH_LONG).show();
                       finish();
                       Intent intent = new Intent(MainActivity.this, QuestionnaireActivity.class);
                       startActivity(intent);
                   }catch (ApiException e){
                       Toast.makeText(MainActivity.this, "Неудачный вход", Toast.LENGTH_LONG).show();
                   }

               }
           });

}