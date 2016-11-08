package com.aaroncampbell.budget.Views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.CalendarView;
import android.widget.LinearLayout;

import com.aaroncampbell.budget.Adapters.ExpenseAdapter;
import com.aaroncampbell.budget.MainActivity;
import com.aaroncampbell.budget.Models.Expense;
import com.aaroncampbell.budget.Network.RestClient;
import com.aaroncampbell.budget.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by aaroncampbell on 11/3/16.
 */

public class CalendarListView extends LinearLayout {
    private Context context;

    @Bind(R.id.calendar_view)
    CalendarView calendarView;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private ExpenseAdapter expenseAdapter;
    private RestClient restClient;
    private Map<Date, ArrayList<Expense>> monthExpenses;
    private int currentMonth;
    private int currentYear;
    private int currentDay;

    public CalendarListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        //Start setting up RecyclerView
        restClient = new RestClient(); // talks to server to get the data it needs
        expenseAdapter = new ExpenseAdapter(new ArrayList<Expense>(), context); // Set up adapter because the data needs to know where to go
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context); // Strictly formatting our data
        recyclerView.setLayoutManager(linearLayoutManager); // Tells RecyclerView to put the data it received from RestClient and format it
        recyclerView.setAdapter(expenseAdapter); // Tells the RecyclerView to use the custom adapter to inflate the data from RestClient into view
        monthExpenses = new HashMap<>();

        ((MainActivity)context).showMenuItem(false);

        calendarView.setFirstDayOfWeek(1);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                loadExpenses(year, month, day);
            }
        });

        Calendar today = Calendar.getInstance();
        loadExpenses(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
    }

    private void loadExpenses(final int year, final int month, final int day) {

        final Calendar cal = (Calendar)Calendar.getInstance().clone();
        cal.set(year, month, day, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        currentDay = day;
        expenseAdapter.expenses = monthExpenses.get(cal.getTime());
        expenseAdapter.notifyDataSetChanged();

        if ((currentMonth != month) || (currentYear != year)) {
            currentYear = year;
            currentMonth = month;
            restClient.getApiService().getMonthExpenses(month + 1, year).enqueue(new Callback<Expense[]>() {
                @Override
                public void onResponse(Call<Expense[]> call, Response<Expense[]> response) {
                    Map<Date, ArrayList<Expense>> newExpenses = new HashMap<>();

                    for (Expense expense : response.body()) {
                        Calendar thisCalendar = (Calendar)Calendar.getInstance().clone();
                        thisCalendar.setTime(expense.getDate());
                        thisCalendar.set(Calendar.HOUR_OF_DAY, 0);
                        thisCalendar.set(Calendar.MINUTE, 0);
                        thisCalendar.set(Calendar.SECOND, 0);
                        thisCalendar.set(Calendar.MILLISECOND, 0);

                        if (newExpenses.get(thisCalendar.getTime()) == null) {
                            newExpenses.put(thisCalendar.getTime(), new ArrayList<Expense>());
                        }
                        newExpenses.get(thisCalendar.getTime()).add(expense);
                    }

                    for (Map.Entry<Date, ArrayList<Expense>> entry : newExpenses.entrySet()) {
                        monthExpenses.put(entry.getKey(), entry.getValue());
                    }

                    if (currentDay == day) {
                        expenseAdapter.expenses = monthExpenses.get(cal.getTime());
                        expenseAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<Expense[]> call, Throwable t) {

                }
            });
        }
    }
}
