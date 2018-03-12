package com.mc.virtuali;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class SearchBarActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText search_edit_text;
    RecyclerView recyclerView1;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    HashMap<String,medicine> med;
    ArrayList<String> medname;
    boolean calledAlready=false;

    SearchAdapter searchAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (!calledAlready) {
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                calledAlready = true;
            }
        }
        catch(Exception e){}

        setContentView(R.layout.activity_search_bar);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        search_edit_text = (EditText) findViewById(R.id.search_edit_text);
        recyclerView1 = (RecyclerView) findViewById(R.id.recyclerView12);


        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView1.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        /*
        * Create a array list for each node you want to use
        * */
        med = new HashMap<>();
        medname = new ArrayList<>();

        Log.d("CRE","kkkkkk");

        search_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    setAdapter(s.toString());
                    Log.d("CRE","CHANGE");
                } else {
                    /*
                    * Clear the list when editText is empty
                    * */
                    med.clear();
                    medname.clear();
                    recyclerView1.removeAllViews();
                }
            }
        });
    }

    private void setAdapter(final String searchedString) {
        Log.d("CRE","INSIDE ADAP");
        String key=databaseReference.getKey();
        databaseReference.child("medicine").addListenerForSingleValueEvent(new ValueEventListener() {



            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("CRE","HEY FROM SNAP");

                med.clear();
                medname.clear();
                recyclerView1.removeAllViews();

                int counter = 0;
                Boolean x=dataSnapshot.hasChildren();
                String x12=x.toString();
                Log.d("CRE", x12);

                /*
                * Search all users for matching searched string
                * */
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("CRE","kkkkkk"+snapshot.getKey());
                    String uid = snapshot.getKey();
                    Log.d("CRE","HI"+uid);
                    String dosage = snapshot.child("Dosage").getValue(String.class);
                    String prescription = snapshot.child("Prescribed").getValue(String.class);
                    String intake = snapshot.child("How It Should be Taken").getValue(String.class);


                    if (uid.toLowerCase().contains(searchedString.toLowerCase())) {
                        medicine m=new medicine();
                        m.name=uid;
                        m.dosage=dosage;
                        m.prescription=prescription;
                        m.intake=intake;
                        med.put(uid,m);
                        medname.add(uid);
                        counter++;
                    } /*else if (uid.toLowerCase().contains(searchedString.toLowerCase())) {
                        fullNameList.add(uid);
                        userNameList.add(uid);
                        counter++;
                    }*/

                    /*
                    * Get maximum of 15 searched results only
                    * */
                    if (counter == 2)
                        break;
                }

                searchAdapter = new SearchAdapter(SearchBarActivity.this,med,medname);
                recyclerView1.setAdapter(searchAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == 0x7f080017/*R.id.action_settings*/) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent intent1 = new Intent(this, ocr.class);
//            intent1.putExtra("lang",lang_var);
            this.startActivity(intent1);
        } else if (id == R.id.nav_search) {
            Intent intent1 = new Intent(this, SearchBarActivity.class);
//            intent1.putExtra("lang",lang_var);
            this.startActivity(intent1);

        } else if (id == R.id.nav_notes) {
            Intent intent1 = new Intent(this, NotesActivity.class);
            this.startActivity(intent1);

        } else if (id == R.id.nav_reminders) {

        }
        else if (id == R.id.nav_exit) {
            ActivityCompat.finishAffinity(SearchBarActivity.this);

            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
