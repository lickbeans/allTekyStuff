package com.aaroncampbell.budget.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaroncampbell.budget.BudgetApplication;
import com.aaroncampbell.budget.Components.Utils;
import com.aaroncampbell.budget.Models.Category;
import com.aaroncampbell.budget.R;
import com.aaroncampbell.budget.Stages.ExpenseStage;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;
import flow.Flow;
import flow.History;

/**
 * Created by aaroncampbell on 11/2/16.
 */

public class BudgetListAdapter extends RecyclerView.Adapter<BudgetListAdapter.CategoryHolder> {
    public ArrayList<Category> categories;
    private Context context;

    public BudgetListAdapter(ArrayList<Category> categories, Context context) {
        this.categories = categories;
        this.context = context;
    } //Call immediately in onCreate to start process

    @Override // Getting our layout inflated into this memory space to deal with layout in code, applies to every row
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(context)
                .inflate(R.layout.budget_list_item, parent, false);
        // Life cycle of cells - created, then binded
        return new CategoryHolder(inflatedView);
    }

    @Override // Where we actually start the work of getting position of each row
    public void onBindViewHolder(BudgetListAdapter.CategoryHolder holder, int position) {
        if (position < categories.size()) {
            Category category = categories.get(position);
            holder.bindCategory(category);
        } else { // Iterate counts every object in the array
            Double total = 0.0;
            Iterator<Category> iterator = categories.iterator();
            while (iterator.hasNext()) {
                total += iterator.next().getAmount();
            }
            Category category = new Category(context.getString(R.string.total), total);
            holder.bindCategory(category);
        }
    }

    @Override // First thing called by Recycler View
    public int getItemCount() {
        return categories.size() + 1;
    }

    class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.category_textview)
        TextView categoryTextView;
        // Combine both text views in our XML
        @Bind(R.id.amount_textview)
        TextView amountTextView;

        public CategoryHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        // Puts data into UI
        public void bindCategory(Category category) {
            categoryTextView.setText(category.getName());
            amountTextView.setText(Utils.formatDouble(category.getAmount())); //Calls the Utils class to limit our double to 2 decimal points
            if (category.getAmount() < 0) {
                amountTextView.setTextColor(context.getResources().getColor(R.color.negativeColor));
            } else {
                amountTextView.setTextColor(context.getResources().getColor(R.color.positiveColor));
            } // .getColor is deprecated, but we are pandering to older systems who still use it.
        }

        @Override
        public void onClick(View view) {
            if (getAdapterPosition() < categories.size()) {
                Category category = categories.get(getAdapterPosition());
                Flow flow = BudgetApplication.getMainFlow();
                History newHistory = flow.getHistory().buildUpon()
                        .push(new ExpenseStage(category.getId()))
                        .build();
                flow.setHistory(newHistory, Flow.Direction.FORWARD);
            } else {

            }
        }
    }
}
