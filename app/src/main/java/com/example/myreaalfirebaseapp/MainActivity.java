package com.example.myreaalfirebaseapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText sroll,sname,scity,smobile;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private RecyclerView myRecycler;
    private List<StudentInfo> showlist;
    private StudentAdapter studentAdapter;
    private ChildEventListener childEventListener;
    private EditText u_Name,u_Roll,u_City,u_Mobile;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase=FirebaseDatabase.getInstance();
        mRef=mDatabase.getReference("student");
        showlist=new ArrayList<>();
        myRecycler = findViewById(R.id.myRecycler);



            myRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            studentAdapter = new StudentAdapter(this, showlist);

            //update code
            studentAdapter.setOnUpdateListener(new StudentAdapter.MyUpdateListner() {
                @Override
                public void updateListner(View view, int position) {
                    update(view,position);
                }
            });

            //swap delete

            studentAdapter.setOnDeleteListener(new StudentAdapter.MyDeleteListner() {
                @Override
                public void deleteListner(View view, int position) {
                    StudentInfo studentInfo=showlist.get(position);
                    String uid=studentInfo.getUid();

                    mRef.child(uid).setValue(null);
                    showlist.remove(studentInfo);
                    studentAdapter.notifyDataSetChanged();
                }
            });




        // listener code
        childEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                StudentInfo studentInfo=snapshot.getValue(StudentInfo.class);
                studentInfo.setUid(snapshot.getKey());
                showlist.add(studentInfo);
                studentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                StudentInfo studentInfo1=snapshot.getValue(StudentInfo.class);

                showlist.add(studentInfo1);
                studentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

             }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mRef.addChildEventListener(childEventListener);
        myRecycler.setAdapter(studentAdapter);
    }

    private void update(View view, final int position) {
        String uid=showlist.get(position).getUid();
        //update code here
        final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(this,R.style.myBottomSheetDialogTheme);
        View view1=LayoutInflater.from(this).inflate(R.layout.update_layout,(LinearLayout)findViewById(R.id.update_container),false);
        bottomSheetDialog.setContentView(view1);
        final Button update=view1.findViewById(R.id.update_this_info);
        u_Name=view1.findViewById(R.id.uname);
        u_Roll=view1.findViewById(R.id.urollno);
        u_City=view1.findViewById(R.id.ucity);
        u_Mobile=view1.findViewById(R.id.umobile);


        //set Uri to layout
        final StudentInfo studentInfo=showlist.get(position);
        u_Name.setText(studentInfo.getName());
        u_Roll.setText(String.valueOf(studentInfo.getRollno()));
        u_City.setText(studentInfo.getCity());
        u_Mobile.setText(studentInfo.getMobile());
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validDetails())
                {
                    String key=showlist.get(position).getUid();
                    StudentInfo studentInfo1=new StudentInfo();
                    studentInfo.setName(uName);
                    studentInfo.setRollno(Integer.parseInt(uRoll));
                    studentInfo.setCity(uCity);
                    studentInfo.setMobile(uMobile);
                    mRef.child(key).setValue(studentInfo);



                    Toast.makeText(MainActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                    bottomSheetDialog.dismiss();



                }
            }
        });
bottomSheetDialog.setCanceledOnTouchOutside(false);
bottomSheetDialog.show();

    }
    private String uName,uRoll,uCity,uMobile;
    private boolean validDetails() {
        uName=u_Name.getText().toString();
        uRoll=u_Roll.getText().toString();
        uCity=u_City.getText().toString();
        uMobile=u_Mobile.getText().toString();
        if(TextUtils.isEmpty(uRoll))
        {
            u_Roll.setError("please Enter your roll no");
            u_Roll.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(uName))
        {
            u_Name.setError("please Enter your name");
            u_Name.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(uCity))
        {
            u_City.setError("please Enter your city");
            u_City.requestFocus();
            return false;

        }
        else if(TextUtils.isEmpty(uMobile))
        {
            u_Mobile.setError("please Enter your mobile no");
            u_Mobile.requestFocus();
            return false;

        }
        else
            return true;

    }

    @Override
    protected void onDestroy() {
        mRef.removeEventListener(childEventListener);
        super.onDestroy();
    }

    public void addInfo(View view) {
        final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(this,R.style.myBottomSheetDialogTheme);

        View view1= LayoutInflater.from(this).inflate(R.layout.bottom_sheet,(LinearLayout)findViewById(R.id.my_container),false);
        initView(view1);
        Button add=view1.findViewById(R.id.add_this_info);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Valid())
                {
                    StudentInfo studentInfo=new StudentInfo();
                    studentInfo.setRollno(Integer.parseInt(rollno));
                    studentInfo.setName(name);
                    studentInfo.setCity(city);
                    studentInfo.setMobile(mobile);

                    String key=mRef.push().getKey();
                    mRef.child(key).setValue(studentInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity.this, "Inserted", Toast.LENGTH_SHORT).show();
                            bottomSheetDialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });




                }
            }
        });
        bottomSheetDialog.setContentView(view1);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.show();

    }
    String rollno,name,city,mobile;
    private boolean Valid() {
        rollno=sroll.getText().toString();
        name=sname.getText().toString();
        city=scity.getText().toString();
        mobile=smobile.getText().toString();
        if(TextUtils.isEmpty(rollno))
        {
            sroll.setError("please Enter your roll no");
            sroll.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(name))
        {
            sname.setError("please Enter your name");
            sname.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(city))
        {
            scity.setError("please Enter your city");
            scity.requestFocus();
            return false;

        }
        else if(TextUtils.isEmpty(mobile))
        {
            smobile.setError("please Enter your mobile no");
            smobile.requestFocus();
            return false;

        }
        else
        return true;
    }

    private void initView(View view1) {
        sroll=view1.findViewById(R.id.s_rollno);
        sname=view1.findViewById(R.id.s_name);
        scity=view1.findViewById(R.id.s_city);
        smobile=view1.findViewById(R.id.s_mobile);


    }
}