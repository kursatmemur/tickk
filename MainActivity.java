package com.example.tick;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText etCalories, etWater;
    Button btnSave;
    TextView tvSummary, tvWeeklyReport;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCalories = findViewById(R.id.etCalories);
        etWater = findViewById(R.id.etWater);
        btnSave = findViewById(R.id.btnSave);
        tvSummary = findViewById(R.id.tvSummary);
        tvWeeklyReport = findViewById(R.id.tvWeeklyReport);

        sharedPreferences = getSharedPreferences("TickPrefs", MODE_PRIVATE);

        loadData();

        btnSave.setOnClickListener(v -> {
            String calStr = etCalories.getText().toString();
            String waterStr = etWater.getText().toString();

            if (calStr.isEmpty() || waterStr.isEmpty()) {
                Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
                return;
            }

            int calories = Integer.parseInt(calStr);
            int water = Integer.parseInt(waterStr);

            // Hedefler: Kalori <= 2200 ve Su >= 2000 ml -> Başarılı (Kırmızı tik ✓)
            boolean isSuccess = (calories <= 2200 && water >= 2000);
            String symbol = isSuccess ? "Kırmızı ✓" : "Mavi ✕";

            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            sharedPreferences.edit()
                    .putString(currentDate, symbol + " (Cal: " + calories + ", Su: " + water + "ml)")
                    .putInt(currentDate + "_cal", calories)
                    .apply();

            Toast.makeText(this, "Bugün kaydedildi: " + symbol, Toast.LENGTH_SHORT).show();
            loadData();
        });
    }

    private void loadData() {
        String currentDateStr = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
        String keyDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String savedRecord = sharedPreferences.getString(keyDate, "Henüz kayıt yok");

        tvSummary.setText("Bugün (" + currentDateStr + "):\n" + savedRecord);
        tvWeeklyReport.setText("Haftalık Durum: Çevrimdışı kayıt aktif.");
    }
}
