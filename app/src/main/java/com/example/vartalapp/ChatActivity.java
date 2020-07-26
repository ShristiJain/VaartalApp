package com.example.vartalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class ChatActivity extends AppCompatActivity {
   private  Toolbar toolbar;
    private TabLayout tablayout;
    private ViewPager viewPager;
    private FragmentAdapterClass fragmentAdapterClass;
    private FirebaseAuth myAuth;
    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        myAuth=FirebaseAuth.getInstance();
        currentUser=myAuth.getCurrentUser();


        if(currentUser!= null){

        toolbar=findViewById(R.id.toolbar1);
        toolbar.setTitle("VaartalApp");
        setSupportActionBar(toolbar);
        fragmentAdapterClass=new FragmentAdapterClass(getSupportFragmentManager(),0);
        viewPager=findViewById(R.id.viewPage);
        viewPager.setAdapter(fragmentAdapterClass);
        tablayout=findViewById(R.id.tabLayout);
        tablayout.setupWithViewPager(viewPager);
    }
        else
        {
            Intent intent=new Intent(ChatActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
         getMenuInflater().inflate(R.menu.chat_menu,menu);
         return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);
         switch(item.getItemId()){
             case R.id.settings_option:
                 sendUserToSetingsActivity();
                 return true;
             case R.id.logout_option:
                 sendUserToMainActivity();
                 return true;
             case R.id.new_option:
                 sendUserToChoiceActiviy();
                 return true;
             default:return false;
         }
    }

    private void sendUserToSetingsActivity() {
        Intent intent = new Intent(ChatActivity.this,SettingsActivity.class);
        startActivity(intent);
    }

    private void sendUserToChoiceActiviy() {
        Intent intent=new Intent(ChatActivity.this,ChoiceActivity.class);
        startActivity(intent);
    }

    private void sendUserToMainActivity() {
        Intent intent =new Intent(ChatActivity.this,MainActivity.class);
        startActivity(intent);
        currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            if(task.isSuccessful()){
                Toast.makeText(ChatActivity.this,"Your account is successfully deleted",Toast.LENGTH_SHORT).show();
            }
            }
        });
    }
}
