package com.example.myapplication.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.models.Room;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    private List<Room> roomList;
    private List<Room> roomListFull; // Dùng cho tìm kiếm
    private OnRoomClickListener listener;

    public interface OnRoomClickListener {
        void onItemClick(Room room);
        void onItemLongClick(Room room);
    }

    public RoomAdapter(List<Room> roomList, OnRoomClickListener listener) {
        this.roomList = roomList;
        this.roomListFull = new ArrayList<>(roomList);
        this.listener = listener;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);
        holder.tvRoomName.setText(room.getName());
        
        // Định dạng tiền tệ VNĐ
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        holder.tvPrice.setText("Giá: " + currencyFormat.format(room.getPrice()));

        if (room.isRented()) {
            holder.tvRoomName.setTextColor(Color.RED);
            holder.tvStatus.setText("Trạng thái: Đã thuê");
            holder.tvStatus.setTextColor(Color.RED);
            holder.tvTenant.setVisibility(View.VISIBLE);
            holder.tvTenant.setText("Khách: " + room.getTenantName() + " (" + room.getPhoneNumber() + ")");
        } else {
            holder.tvRoomName.setTextColor(Color.parseColor("#4CAF50")); // Green
            holder.tvStatus.setText("Trạng thái: Còn trống");
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50"));
            holder.tvTenant.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(room));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onItemLongClick(room);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    // Cập nhật danh sách khi tìm kiếm
    public void filter(String text) {
        roomList.clear();
        if (text.isEmpty()) {
            roomList.addAll(roomListFull);
        } else {
            text = text.toLowerCase();
            for (Room item : roomListFull) {
                if (item.getName().toLowerCase().contains(text)) {
                    roomList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    // Cập nhật lại list full khi data thay đổi
    public void updateList(List<Room> newList) {
        this.roomList = newList;
        this.roomListFull = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomName, tvPrice, tvStatus, tvTenant;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvTenant = itemView.findViewById(R.id.tvTenant);
        }
    }
}
