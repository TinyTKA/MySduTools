package xyz.taouvw.mysdutools.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import xyz.taouvw.mysdutools.Activity.AboutActivity;
import xyz.taouvw.mysdutools.Activity.MainActivity;
import xyz.taouvw.mysdutools.Activity.MapActivity;
import xyz.taouvw.mysdutools.Activity.SearchFreeRoomActivity;
import xyz.taouvw.mysdutools.R;

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.MyViewHolder> {
    public String[] Options = {"课程表", "校区地图", "查询空教室", "关于我们"};
    public int[] imglist = {R.drawable.ic_baseline_grid_on_24, R.drawable.ic_baseline_map_24, R.drawable.ic_baseline_search_24, R.drawable.ic_baseline_chat_bubble_outline_24};
    Context context;


    static class MyViewHolder extends RecyclerView.ViewHolder {
        View OptionView;
        ImageView option_image;
        TextView option;

        public MyViewHolder(View optionView) {
            super(optionView);
            OptionView = optionView;
            option = optionView.findViewById(R.id.option);
            option_image = optionView.findViewById(R.id.optionImage);
        }
    }

    public OptionsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.option_list, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(inflate);

        myViewHolder.OptionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = myViewHolder.getAdapterPosition();
                switch (Options[position]) {
                    case "课程表": {
                        Intent intent = new Intent();
                        intent.setClass(view.getContext(), MainActivity.class);
                        context.startActivity(intent);
                    }
                    break;
                    case "校区地图": {
                        Intent intent = new Intent();
                        intent.setClass(view.getContext(), MapActivity.class);
                        context.startActivity(intent);
                    }
                    break;
                    case "查询空教室": {
                        Intent intent = new Intent(view.getContext(), SearchFreeRoomActivity.class);
                        context.startActivity(intent);
                    }
                    break;
                    case "关于我们": {
                        Intent intent = new Intent();
                        intent.setClass(view.getContext(), AboutActivity.class);
                        context.startActivity(intent);
                    }
                    break;
                    default:
                        break;
                }
            }
        });
        return myViewHolder;
    }


    //绑定视图
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.option.setText(Options[position]);
        holder.option_image.setImageResource(imglist[position]);
    }

    @Override
    public int getItemCount() {
        return Options.length;
    }
}
