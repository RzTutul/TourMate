package com.example.tourmate.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourmate.R;
import com.example.tourmate.helper.EventUtils;
import com.example.tourmate.pojos.EventExpensePojo;
import com.example.tourmate.viewmodels.ExpenseViewModel;

import java.util.Collections;
import java.util.List;

public class ExpenseListRVAdpater extends RecyclerView.Adapter<ExpenseListRVAdpater.ExpenseViewHolder>{
    private Context context;
    private List<EventExpensePojo> expensePojos;
    private ExpenseViewModel expenseViewModel = new ExpenseViewModel();
    private String expenseID;
    private String eventID;

    public ExpenseListRVAdpater(Context context, List<EventExpensePojo> expensePojos) {
        Collections.reverse(expensePojos);
        this.context = context;
        this.expensePojos = expensePojos;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.expense_row,parent,false);


        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, final int position) {

        expenseID = expensePojos.get(position).getExpenseId();
        eventID = expensePojos.get(position).getEventId();
        final EventExpensePojo expensePojo = expensePojos.get(position);

        holder.expenseName.setText(expensePojos.get(position).getExpenseName());
        holder.expenseAmount.setText("৳ "+String.valueOf(expensePojos.get(position).getAmount()));
        holder.expenseDate.setText(expensePojos.get(position).getExpenseDateTime());

        holder.editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showExpenseDilog(expenseID,eventID,position);
            }
        });
  holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                expenseViewModel.DeleteExpense(expensePojo);
            }
        });


    }

    @Override
    public int getItemCount() {
        return expensePojos.size();
    }

    public class ExpenseViewHolder extends RecyclerView.ViewHolder
    {
        TextView expenseName,expenseAmount,expenseDate;
        ImageView editbtn,deletebtn;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);

            expenseName = itemView.findViewById(R.id.row_expenseName);
            expenseAmount = itemView.findViewById(R.id.row_expenseAmount);
            expenseDate = itemView.findViewById(R.id.row_expense_date);
            editbtn = itemView.findViewById(R.id.row_expenseEdit);
            deletebtn = itemView.findViewById(R.id.row_expenseDelete);


        }
    }


    private void showExpenseDilog(final String expenseID,final String eventID,final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Update Expense");
        View view1 = LayoutInflater.from(context).inflate(R.layout.add_expense_dialog,null);

        builder.setView(view1);
        final EditText expenseNameET = view1.findViewById(R.id.expenseNameET);
        final EditText expenseAmoutET = view1.findViewById(R.id.expenseAmountET);
        Button updatebtn = view1.findViewById(R.id.addbtn);
        Button cancelbtn = view1.findViewById(R.id.cancelBtn);

        updatebtn.setText("Update");
        expenseNameET.setText(expensePojos.get(position).getExpenseName());
        expenseAmoutET.setText(String.valueOf(expensePojos.get(position).getAmount()));


        final AlertDialog dialog = builder.create();

        dialog.show();

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ename = expenseNameET.getText().toString();
                String amount = expenseAmoutET.getText().toString();

                EventExpensePojo expensePojo = new EventExpensePojo(expenseID,eventID,ename,Integer.parseInt(amount), EventUtils.getDateWithTime());

                expenseViewModel.updateExpense(expensePojo);

                Toast.makeText(context,"Updated", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



    }
}
