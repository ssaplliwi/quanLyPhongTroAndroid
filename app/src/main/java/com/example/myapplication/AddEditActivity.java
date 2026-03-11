package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.models.Room;
import com.example.myapplication.repository.RoomRepository;
import com.google.android.material.textfield.TextInputEditText;
import java.util.UUID;

public class AddEditActivity extends AppCompatActivity {
    private TextInputEditText etRoomName, etPrice, etTenantName, etPhoneNumber;
    private CheckBox cbIsRented;
    private LinearLayout layoutTenant;
    private Button btnSave;
    private TextView tvTitle;
    private Room existingRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        initViews();
        
        // Kiểm tra xem là Sửa hay Thêm
        existingRoom = (Room) getIntent().getSerializableExtra("ROOM_DATA");
        if (existingRoom != null) {
            tvTitle.setText("Chỉnh Sửa Phòng");
            etRoomName.setText(existingRoom.getName());
            etPrice.setText(String.valueOf(existingRoom.getPrice()));
            cbIsRented.setChecked(existingRoom.isRented());
            etTenantName.setText(existingRoom.getTenantName());
            etPhoneNumber.setText(existingRoom.getPhoneNumber());
            layoutTenant.setVisibility(existingRoom.isRented() ? View.VISIBLE : View.GONE);
        }

        cbIsRented.setOnCheckedChangeListener((buttonView, isChecked) -> {
            layoutTenant.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        btnSave.setOnClickListener(v -> saveRoom());
    }

    private void initViews() {
        tvTitle = findViewById(R.id.tvTitle);
        etRoomName = findViewById(R.id.etRoomName);
        etPrice = findViewById(R.id.etPrice);
        cbIsRented = findViewById(R.id.cbIsRented);
        layoutTenant = findViewById(R.id.layoutTenant);
        etTenantName = findViewById(R.id.etTenantName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        btnSave = findViewById(R.id.btnSave);
    }

    private void saveRoom() {
        String name = etRoomName.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        boolean isRented = cbIsRented.isChecked();
        String tenantName = etTenantName.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();

        // Validation
        if (name.isEmpty()) {
            etRoomName.setError("Tên phòng không được để trống");
            return;
        }
        if (priceStr.isEmpty() || Double.parseDouble(priceStr) <= 0) {
            etPrice.setError("Giá phải lớn hơn 0");
            return;
        }
        if (isRented) {
            if (tenantName.isEmpty()) {
                etTenantName.setError("Tên khách không được để trống");
                return;
            }
            if (phoneNumber.length() != 10) {
                etPhoneNumber.setError("Số điện thoại phải đúng 10 số");
                return;
            }
        }

        double price = Double.parseDouble(priceStr);
        String id = (existingRoom != null) ? existingRoom.getId() : UUID.randomUUID().toString();
        Room room = new Room(id, name, price, isRented, tenantName, phoneNumber);

        if (existingRoom != null) {
            RoomRepository.getInstance().update(room);
            Toast.makeText(this, "Đã cập nhật", Toast.LENGTH_SHORT).show();
        } else {
            RoomRepository.getInstance().add(room);
            Toast.makeText(this, "Đã thêm mới", Toast.LENGTH_SHORT).show();
        }
        finish(); // Đóng Activity
    }
}
