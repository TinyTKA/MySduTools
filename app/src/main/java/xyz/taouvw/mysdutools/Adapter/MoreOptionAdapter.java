package xyz.taouvw.mysdutools.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import xyz.taouvw.mysdutools.R;

public class MoreOptionAdapter extends RecyclerView.Adapter<MoreOptionAdapter.OptionViewHolder> {


    private String[] Options = {"开源协议", "使用教程", "功能反馈", "官方网站", "加入我们", "检查更新"};
    private int[] imgLists = {R.drawable.ic_baseline_accessibility_24, R.drawable.ic_baseline_menu_book_24, R.drawable.ic_baseline_send_24, R.drawable.ic_baseline_web_24, R.drawable.ic_baseline_group_24, R.drawable.ic_baseline_help_24};
    Context context;

    public MoreOptionAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public OptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.more_optionlist, parent, false);
        OptionViewHolder optionViewHolder = new OptionViewHolder(inflate);
        optionViewHolder.optionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = optionViewHolder.getAdapterPosition();
                switch (Options[position]) {
                    case "开源协议": {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("开源协议").setView(R.layout.opensource);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                    break;
                    case "使用教程": {
                        Uri uri = Uri.parse("https://www.taouvw.xyz/archives/645");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        context.startActivity(intent);
                    }
                    break;
                    case "功能反馈": {
                        Uri uri = Uri.parse("https://github.com/xt-9931/VMySduTools");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        context.startActivity(intent);
                    }
                    break;
                    case "官方网站": {
                        Toast.makeText(context, "建设中", Toast.LENGTH_SHORT).show();
                    }
                    break;
                    case "加入我们": {
                        Uri uri = Uri.parse("https://www.taouvw.xyz/archives/645");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        context.startActivity(intent);
                    }
                    break;
                    case "检查更新": {
                        Toast.makeText(context, "当前已是最新版，感谢支持", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        });
        return optionViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OptionViewHolder holder, int position) {
        holder.optionText.setText(Options[position]);
        holder.optionImage.setImageResource(imgLists[position]);
    }

    @Override
    public int getItemCount() {
        return Options.length;
    }

    static class OptionViewHolder extends RecyclerView.ViewHolder {
        TextView optionText;
        ImageView optionImage;
        View optionView;


        public OptionViewHolder(@NonNull View itemView) {
            super(itemView);
            optionView = itemView;
            optionImage = itemView.findViewById(R.id.moreOption_image);
            optionText = itemView.findViewById(R.id.moreOptionList);
        }
    }
}
