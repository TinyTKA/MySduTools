package xyz.taouvw.mysdutools.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import xyz.taouvw.mysdutools.Bean.ClassRoomDetail;
import xyz.taouvw.mysdutools.MyView.ShapeTextView;
import xyz.taouvw.mysdutools.R;

public class ClassRoomAdapter extends RecyclerView.Adapter<ClassRoomAdapter.SelfViewHolder> {
    List<ClassRoomDetail> classRoomDetailList = new ArrayList<>();

    public ClassRoomAdapter(List<ClassRoomDetail> list) {
        this.classRoomDetailList.addAll(list);
    }


    static class SelfViewHolder extends RecyclerView.ViewHolder {
        TableRow tableRow;
        View view;

        public SelfViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tableRow = itemView.findViewById(R.id.dayList);
        }
    }

    @NonNull
    @Override
    public SelfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.eachclassroom, parent, false);
        SelfViewHolder selfViewHolder = new SelfViewHolder(inflate);
        return selfViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelfViewHolder holder, int position) {
        ShapeTextView virtualChildAt = (ShapeTextView) holder.tableRow.getVirtualChildAt(0);
        ClassRoomDetail classRoomDetail = classRoomDetailList.get(position);
        virtualChildAt.setText(classRoomDetail.getName());
        for (int i = 1; i <= 5; i++) {
            ShapeTextView virtualChildAt1 = (ShapeTextView) holder.tableRow.getVirtualChildAt(i);
            if (classRoomDetail.getOccupiedTime()[i - 1] == 1) {
                virtualChildAt1.setBackgroundResource(R.drawable.occupiedroom_background);
            } else if (classRoomDetail.getFreeTime()[i - 1] == 1) {
                virtualChildAt1.setBackgroundResource(R.drawable.freeroom_background);
            }
        }
    }

    @Override
    public int getItemCount() {
        return classRoomDetailList.size();
    }

    public void addData(ClassRoomDetail classRoomDetail) {
        classRoomDetailList.add(classRoomDetail);
        notifyItemChanged(classRoomDetailList.size() - 1);
    }

    public void removeAll() {
        int previousSize = classRoomDetailList.size();
        classRoomDetailList.clear();
        notifyItemRangeRemoved(0, previousSize);
    }

    public void AddAllData(List<ClassRoomDetail> list) {
        classRoomDetailList.addAll(list);
        notifyItemRangeInserted(0, list.size());
    }
}
