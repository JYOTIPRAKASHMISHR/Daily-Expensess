package com.example.dailyexpenses;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Add extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Moneyspend");
    }

    private void showBottomSheet(String category) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_amount, null);
        bottomSheetDialog.setContentView(sheetView);

        TextView categoryTitle = sheetView.findViewById(R.id.categoryTitle);
        EditText amountInput = sheetView.findViewById(R.id.amountInput);
        Button saveButton = sheetView.findViewById(R.id.saveButton);

        categoryTitle.setText("Enter amount for " + category);

        saveButton.setOnClickListener(v -> {
            String amount = amountInput.getText().toString().trim();
            if (amount.isEmpty()) {
                amountInput.setError("Amount required");
            } else {
                saveToFirebase(category, amount);
                Toast.makeText(Add.this, category + " amount saved: ₹" + amount, Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }

    private void saveToFirebase(String category, String amount) {
        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "anonymous";
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        HashMap<String, Object> expenseData = new HashMap<>();
        expenseData.put("category", category);
        expenseData.put("amount", amount);
        expenseData.put("timestamp", timestamp);

        databaseReference.child(userId).child(category).push().setValue(expenseData)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Saved successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to save: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    public void food(View view) { showBottomSheet("Food"); }
    public void rent(View view) { showBottomSheet("Rent"); }
    public void shopping(View view) { showBottomSheet("Shopping"); }
    public void transport(View view) { showBottomSheet("Transport"); }
    public void entertement(View view) { showBottomSheet("Entertainment"); }
    public void clothing(View view) { showBottomSheet("Clothing"); }
    public void sports(View view) { showBottomSheet("Sports"); }
    public void eaducation(View view) { showBottomSheet("Education"); }
    public void electronics(View view) { showBottomSheet("Electronics"); }
    public void alcohol(View view) { showBottomSheet("Alcohol"); }
    public void donation(View view) { showBottomSheet("Donation"); }
    public void lotary(View view) { showBottomSheet("Lottery"); }
    public void kids(View view) { showBottomSheet("Kids"); }
    public void healsth(View view) { showBottomSheet("Health"); }
    public void snacks(View view) { showBottomSheet("Snacks"); }
    public void fruits(View view) { showBottomSheet("Fruits"); }


    private void showIncomeBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_income, null);
        bottomSheetDialog.setContentView(sheetView);

        TextView categoryTitle = sheetView.findViewById(R.id.categoryTitle);
        EditText amountInput = sheetView.findViewById(R.id.amountInput);
        Button saveButton = sheetView.findViewById(R.id.saveButton);

        categoryTitle.setText("Enter your income");

        saveButton.setOnClickListener(v -> {
            String amount = amountInput.getText().toString().trim();
            if (amount.isEmpty()) {
                amountInput.setError("Amount required");
            } else {
                saveToFirebase("Income", amount);  // Saving under 'Income' category
                Toast.makeText(Add.this, "Income saved: ₹" + amount, Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }

}
