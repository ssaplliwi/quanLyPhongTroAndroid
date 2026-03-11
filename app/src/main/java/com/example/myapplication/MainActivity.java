package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapters.RoomAdapter;
import com.example.myapplication.models.Room;
import com.example.myapplication.repository.RoomRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * Màn hình chính hiển thị danh sách phòng trọ và thống kê.
 */
public class MainActivity extends AppCompatActivity implements RoomAdapter.OnRoomClickListener {

    private RecyclerView rvRooms;
    private RoomAdapter adapter;
    private TextView tvTotalRooms, tvAvailableRooms;
    private SearchView searchView;
    private FloatingActionButton fabAdd;
    private RoomRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repository = RoomRepository.getInstance();
        initViews();
        setupRecyclerView();
        updateDashboard();

        // Nút thêm mới phòng
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
            startActivity(intent);
        });

        setupSearch();
    }

    private void initViews() {
        rvRooms = findViewById(R.id.rvRooms);
        tvTotalRooms = findViewById(R.id.tvTotalRooms);
        tvAvailableRooms = findViewById(R.id.tvAvailableRooms);
        searchView = findViewById(R.id.searchView);
        fabAdd = findViewById(R.id.fabAdd);
    }

    private void setupRecyclerView() {
        adapter = new RoomAdapter(repository.getAll(), this);
        rvRooms.setLayoutManager(new LinearLayoutManager(this));
        rvRooms.setAdapter(adapter);
    }

    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return false;
            }
        });
    }

    // Cập nhật Dashboard thống kê
    private void updateDashboard() {
        List<Room> rooms = repository.getAll();
        int total = rooms.size();
        int available = 0;
        for (Room r : rooms) {
            if (!r.isRented()) available++;
        }

        tvTotalRooms.setText("Tổng số: " + total);
        tvAvailableRooms.setText("Còn trống: " + available);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Làm mới dữ liệu khi quay lại từ màn hình khác
        adapter.updateList(repository.getAll());
        updateDashboard();
    }

    @Override
    public void onItemClick(Room room) {
        // Chạm để sửa
        Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
        intent.putExtra("ROOM_DATA", room);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(Room room) {
        // Nhấn giữ để xóa
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa phòng: " + room.getName() + "?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    repository.delete(room.getId());
                    adapter.updateList(repository.getAll());
                    updateDashboard();
                    Toast.makeText(MainActivity.this, "Đã xóa thành công", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
