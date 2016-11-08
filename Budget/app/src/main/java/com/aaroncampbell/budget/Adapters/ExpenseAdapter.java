package com.aaroncampbell.budget.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaroncampbell.budget.Components.Utils;
import com.aaroncampbell.budget.Models.Expense;
import com.aaroncampbell.budget.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by aaroncampbell on 11/3/16.
 */

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseHolder> {
    SimpleDateFormat dateFormatter;
    public ArrayList<Expense> expenses;
    private Context context;

    public ExpenseAdapter(ArrayList<Expense> expenses, Context context) {
        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        this.expenses = expenses;
        this.context = context;
    }

    @Override
    public ExpenseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(context).inflate(R.layout.expense_item, parent, false);
        return new ExpenseHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(ExpenseHolder holder, int position) {
        Expense expense = expenses.get(position);
        holder.bindExpense(expense);
    }

    @Override
    public int getItemCount() {
        return expenses == null ? 0 : expenses.size();
    }

    class ExpenseHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.amount_field)
        TextView amountField;

        @Bind(R.id.note_field)
        TextView noteField;

        @Bind(R.id.date_field)
        TextView dateField;

        public ExpenseHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindExpense(Expense expense) {
            amountField.setText(Utils.formatDouble(expense.getAmount()));
            noteField.setText("(" + expense.getNote() + ")");
            dateField.setText(dateFormatter.format(expense.getDate()));
        }
    }
}
