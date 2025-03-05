package com.example.dailyexpenses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView;
    private ExpenseAdapter expenseAdapter;
    private List<Expense> expenseList;
    private DatabaseReference expenseRef;
    private TextView tvDateValue, tvExpensesValue;
    private String userId;
    private Calendar selectedCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tvDateValue = findViewById(R.id.tvDateValue);
        tvExpensesValue = findViewById(R.id.tvExpensesValue);
        expenseList = new ArrayList<>();
        expenseAdapter = new ExpenseAdapter(this, expenseList);
        recyclerView.setAdapter(expenseAdapter);

        // Initialize selectedCalendar with the current date
        selectedCalendar = Calendar.getInstance();
        updateDateInView();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            userId = auth.getCurrentUser().getUid();
            Log.d(TAG, "User ID: " + userId);
            expenseRef = FirebaseDatabase.getInstance().getReference("Moneyspend").child(userId);
            loadExpenses();  // Load all expenses and calculate total
        } else {
            Log.e(TAG, "User not logged in!");
            Toast.makeText(this, "Please log in to view expenses.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadExpenses() {
        Log.d(TAG, "Loading expenses...");
        expenseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                expenseList.clear();
                double totalAmount = 0.0;  // Initialize total amount

                if (snapshot.exists()) {
                    for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                        for (DataSnapshot expenseSnapshot : categorySnapshot.getChildren()) {
                            Expense expense = expenseSnapshot.getValue(Expense.class);
                            if (expense != null) {
                                Log.d(TAG, "Loaded → Category: " + expense.getCategory() +
                                        ", Amount: " + expense.getAmount() +
                                        ", Date: " + expense.getTimestamp());

                                expenseList.add(expense);  // Add expense to list

                                // Safely parse amount and add to total
                                try {
                                    double amount = Double.parseDouble(expense.getAmount());
                                    totalAmount += amount;
                                } catch (NumberFormatException e) {
                                    Log.e(TAG, "Invalid amount format: " + expense.getAmount());
                                }
                            } else {
                                Log.w(TAG, "Null expense at: " + expenseSnapshot.getKey());
                            }
                        }
                    }
                } else {
                    Log.w(TAG, "No expenses found.");
                }

                // Update the total amount TextView
                tvExpensesValue.setText(String.format(Locale.getDefault(), "₹ %.2f", totalAmount));

                // Refresh RecyclerView
                expenseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Database error: " + error.getMessage(), error.toException());
                Toast.makeText(MainActivity.this, "Failed to load expenses.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onAddClick(View view) {
        startActivity(new Intent(this, Add.class));
    }

    public void onCalendarClick(View view) {
        int year = selectedCalendar.get(Calendar.YEAR);
        int month = selectedCalendar.get(Calendar.MONTH);
        int day = selectedCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                MainActivity.this,
                (DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) -> {
                    selectedCalendar.set(selectedYear, selectedMonth, selectedDay);
                    updateDateInView();
                    Toast.makeText(MainActivity.this, "Selected Date: " + formatDate(selectedCalendar), Toast.LENGTH_SHORT).show();
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    private void updateDateInView() {
        tvDateValue.setText(formatDate(selectedCalendar));
    }

    private String formatDate(Calendar calendar) {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.getTime());
    }

    public void onChartsClick(View view) {

    }

    public void onProfileClick(View view) {
        Intent intent = new Intent (this,Profile.class);
        startActivity(intent);
    }
}
