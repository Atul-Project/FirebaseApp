package com.example.myreaalfirebaseapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyHolder> {
   private Context mContext;
   private List<StudentInfo> studentList;
   private MyUpdateListner myListner;
   private MyDeleteListner myDeleteListner;
    public StudentAdapter(Context mContext, List<StudentInfo> studentList) {
        this.mContext = mContext;
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.my_student_view,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        StudentInfo studentInfo=studentList.get(position);
        holder.sname.setText(studentInfo.getName());
        holder.sroll.setText(String.valueOf(studentInfo.getRollno()));
        holder.scity.setText(studentInfo.getCity());
        holder.smobile.setText(studentInfo.getMobile());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myListner.updateListner(v,position);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                myDeleteListner.deleteListner(v,position);

                return false;
            }
        });



    }
    public interface  MyDeleteListner
    {
        void deleteListner(View view,int position);

    }
    public  void setOnDeleteListener(MyDeleteListner myDeleteListner)
    {
        this.myDeleteListner=myDeleteListner;

    }
    public  void setOnUpdateListener(MyUpdateListner myUpdateListner)
    {
        this.myListner=myUpdateListner;

    }


    public interface  MyUpdateListner
    {
        void updateListner(View view,int position);

    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        private TextView sname,sroll,scity,smobile;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            sname=itemView.findViewById(R.id.sname);
            sroll=itemView.findViewById(R.id.sroll);
            scity=itemView.findViewById(R.id.scity);
            smobile=itemView.findViewById(R.id.smobile);

        }
    }
}
