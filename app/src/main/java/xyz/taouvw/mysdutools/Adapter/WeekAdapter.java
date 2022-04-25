package xyz.taouvw.mysdutools.Adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import xyz.taouvw.mysdutools.R;

public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.MyViewHolder> {
    List<String> stringList = new ArrayList<>();
    private MyItemClickListener myItemClickListener;
    int nowweek;

    public WeekAdapter(int nowWeek) {
        nowweek = nowWeek;
        for (int i = 1; i <= 22; i++) {

            if (i != nowWeek) {
                stringList.add("第" + i + "周");
            } else {
                stringList.add("第" + i + "周  本周");
            }
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            textView = itemView.findViewById(R.id.WeekText);
        }


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.week_list, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(inflate);
        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView.setText(stringList.get(position));
        if ((position + 1) == nowweek) {
            holder.textView.setBackgroundResource(R.color.lightgray);
            Log.e("", "onBindViewHolder: " + position);
        }
        if (myItemClickListener != null) {
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View view) {
                    int pos = holder.getLayoutPosition();
                    myItemClickListener.onItemClick(view, pos + 1);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    //创建一个监听回调接口
    public interface MyItemClickListener {
        void onItemClick(View view, int position);
    }

    //在activity里面adapter就是调用的这个方法,将点击事件监听传递过来,并赋值给全局的监听
    public void setItemClickListener(MyItemClickListener ItemClickListener) {
        this.myItemClickListener = ItemClickListener;
    }
}
