package com.aaroncampbell.budget.Views;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aaroncampbell.budget.Adapters.ExpenseAdapter;
import com.aaroncampbell.budget.MainActivity;
import com.aaroncampbell.budget.Models.Expense;
import com.aaroncampbell.budget.Network.RestClient;
import com.aaroncampbell.budget.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by aaroncampbell on 11/2/16.
 */

public class ExpenseView extends LinearLayout {
    private Context context;

    @Bind(R.id.amount_field)
    EditText amountField;

    @Bind(R.id.date_field)
    EditText dateField;

    @Bind(R.id.note_field)
    EditText noteField;

    @Bind(R.id.add_expense_button)
    FloatingActionButton addExpenseButton;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private int categoryId;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private SimpleDateFormat dateFormatter;
    private RestClient restClient;
    private ExpenseAdapter expenseAdapter;

    public ExpenseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);

        calendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        dateField.setText(dateFormatter.format(calendar.getTime()));
        ((MainActivity)context).showMenuItem(false);
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
        restClient = new RestClient();

        //initialize recyclerView and expense adapter
        expenseAdapter = new ExpenseAdapter(new ArrayList<Expense>(), context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(expenseAdapter);
        //load expenses
        loadExpenses();
    }

    @OnClick(R.id.date_field)
    public void dateTapped(EditText editText) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(year, month, day);
                dateField.setText(dateFormatter.format(calendar.getTime()));
            }
        };

        datePickerDialog = new DatePickerDialog(context,
                dateSetListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @OnClick(R.id.add_expense_button)
    public void addTapped() {
        Double amount;
        String note = noteField.getText().toString();


        try {
            amount = Double.parseDouble(amountField.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(context, R.string.invalid_amount, Toast.LENGTH_LONG).show();
            return;
        }

//        if (amount.isEmpty() || note.isEmpty()) {
//            Toast.makeText(context, R.string.fill_out_all_fields, Toast.LENGTH_LONG).show();
//        } else if (amount < 0) {
//            Toast.makeText(context, R.string.invalid_amount, Toast.LENGTH_LONG).show();
//        } else {
//            addExpenseButton.isEnabled();
//        }

        Expense expense = new Expense(categoryId, amount, calendar.getTime(), noteField.getText().toString());
        restClient = new RestClient();
        restClient.getApiService().addExpense(expense).enqueue(new Callback<Expense>() {
            @Override
            public void onResponse(Call<Expense> call, Response<Expense> response) {
                if (response.isSuccessful()) {
                     loadExpenses();
                    amountField.setText("");

                    dateField.setText(dateFormatter.format(calendar.getTime()));
                    noteField.setText("");
                    Toast.makeText(context, R.string.expense_added, Toast.LENGTH_LONG).show();
                    resetView();
                } else {
                    resetView();
                    Toast.makeText(context, context.getString(R.string.exp_add_failed) + ":" + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Expense> call, Throwable t) {
                resetView();
                Toast.makeText(context, R.string.exp_add_failed, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadExpenses() {
        restClient.getApiService().getRecentExpenses(categoryId).enqueue(new Callback<Expense[]>() {
            @Override
            public void onResponse(Call<Expense[]> call, Response<Expense[]> response) {
                if (response.isSuccessful()) {
                    expenseAdapter.expenses = new ArrayList<>(Arrays.asList(response.body()));
                    expenseAdapter.notifyDataSetChanged();
                } else {

                }
            }

            @Override
            public void onFailure(Call<Expense[]> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void resetView() {
        addExpenseButton.setEnabled(true);
    }
}
