package xyz.taouvw.mysdutools.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import xyz.taouvw.mysdutools.Bean.DdlBean;
import xyz.taouvw.mysdutools.Interface.ItemTouchHelperAdapter;
import xyz.taouvw.mysdutools.R;
import xyz.taouvw.mysdutools.utils.DdlListUtils;

public class DDlAdapter extends RecyclerView.Adapter<DDlAdapter.DDlViewHolder> implements ItemTouchHelperAdapter {

    ArrayList<DdlBean> arrayLists = new ArrayList<>();
    DdlListUtils mDdlListUtils;
    Random random = new Random();
    Calendar calendar = Calendar.getInstance();
    private static final int[] backgroundColor = new int[]{
            R.drawable.ddl_list_1,
            R.drawable.ddl_list_2,
            R.drawable.ddl_list_3,
            R.drawable.ddl_list_4,
            R.drawable.ddl_list_5,
            R.drawable.ddl_list_6,
            R.drawable.ddl_list_7,
            R.drawable.ddl_list_8

    };
    private MyItemClickListener myItemClickListener;


    public DDlAdapter(ArrayList<DdlBean> arrayLists, DdlListUtils ddlListUtils) {
        this.arrayLists.addAll(arrayLists);
        this.mDdlListUtils = ddlListUtils;
    }

    @NonNull
    @Override
    public DDlViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_ddl_recycle_layout, null);
        DDlViewHolder dDlViewHolder = new DDlViewHolder(inflate);
        return dDlViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DDlViewHolder holder, int position) {
        holder.name.setText(arrayLists.get(position).getDDl_name());
        holder.desc.setText(arrayLists.get(position).getDDl_detail());
        holder.time.setText(arrayLists.get(position).getDDl_date());
        int day = this.getReversedTime(arrayLists.get(position));
        if (day < 0) {
            holder.reversedTime.setText("已经过了" + Math.abs(day) + "天");
        } else {
            holder.reversedTime.setText("还剩" + Math.abs(day) + "天");
        }
        holder.linearLayout.setBackgroundResource(backgroundColor[random.nextInt(8)]);
        int i = position;
        if (myItemClickListener != null) {
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myItemClickListener.onItemClick(view, i);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return arrayLists.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(arrayLists, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDissmiss(int position) {
        this.removeData(position);
    }

    class DDlViewHolder extends RecyclerView.ViewHolder {

        View view;
        LinearLayout linearLayout;
        TextView name;
        TextView desc;
        TextView time;
        TextView reversedTime;


        public DDlViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            linearLayout = itemView.findViewById(R.id.inner_layout);
            name = itemView.findViewById(R.id.ddl_name);
            desc = itemView.findViewById(R.id.ddl_desc);
            time = itemView.findViewById(R.id.ddl_time);
            reversedTime = itemView.findViewById(R.id.reversedTime);
        }
    }

    //创建一个监听回调接口
    public interface MyItemClickListener {
        void onItemClick(View view, int position);

        void onCheckIfIsEmpty();
    }

    //在activity里面adapter就是调用的这个方法,将点击事件监听传递过来,并赋值给全局的监听
    public void setItemClickListener(DDlAdapter.MyItemClickListener ItemClickListener) {
        this.myItemClickListener = ItemClickListener;
    }


    public void addData(DdlBean ddlBean) {
        arrayLists.add(ddlBean);
        notifyItemChanged(arrayLists.size() - 1);
    }

    public void removeData(int position) {


        int previousSize = arrayLists.size();
        // 数据库的删除操作
        mDdlListUtils.removeDdl(arrayLists.get(position).getDDl_name());
        arrayLists.remove(position);
        myItemClickListener.onCheckIfIsEmpty();
        notifyItemRemoved(position);
        notifyItemRangeRemoved(position, previousSize);
    }

    public void removeAll() {
        int previousSize = arrayLists.size();
        arrayLists.clear();
        notifyItemRangeRemoved(0, previousSize);
    }

    public void AddAllData(List<DdlBean> list) {
        arrayLists.addAll(list);
        notifyItemRangeInserted(0, list.size());
    }

    private int getReversedTime(DdlBean ddlBean) {
        Date date = new Date();
        long time = date.getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ddlBean.getYear_of_ddl());
        stringBuilder.append("年");
        if (ddlBean.getMonth_of_ddl() <= 9) {
            stringBuilder.append(0);
        }
        stringBuilder.append(ddlBean.getMonth_of_ddl());
        stringBuilder.append("月");
        if (ddlBean.getDay_of_ddl() <= 9) {
            stringBuilder.append(0);
        }
        stringBuilder.append(ddlBean.getDay_of_ddl());
        stringBuilder.append("日");
        Date parse = null;
        try {
            parse = simpleDateFormat.parse(stringBuilder.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert parse != null;
        long delta = parse.getTime() - time;
        return (int) (delta / 1000 / 60 / 60 / 24);
    }
}
