package com.demo.payo.view;

        import android.Manifest;
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.content.pm.PackageManager;
        import android.os.Bundle;
        import android.provider.Telephony;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.demo.payo.R;
        import com.demo.payo.adapter.RecyleViewAdapter;
        import com.demo.payo.model.SmsDto;
        import com.demo.payo.model.Trasaction;
        import com.demo.payo.utils.IsNewSMS;
        import com.demo.payo.viewmodel.SmsViewModel;

        import java.util.ArrayList;
        import java.util.List;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.core.app.ActivityCompat;
        import androidx.core.content.ContextCompat;
        import androidx.lifecycle.Observer;
        import androidx.lifecycle.ViewModelProvider;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements IsNewSMS {
    private static final int READSMS_PERMISSION_CODE = 101;
    float totalExpenses;
    float totalIncome;
    List<SmsDto> smsDtoList;
    SmsViewModel smsViewModel;
    RecyclerView recyclerView;
    RecyleViewAdapter adapter;
    private TextView tvCreditAmount, tvDebitAmount;
    private Button btnShowPieGraph;
    // The BroadcastReceiver get new sms
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        IsNewSMS isNewSMS;

        @Override
        public void onReceive(Context context, Intent intent) {
            isNewSMS.IsReceivedSMS(smsDtoList);
        }
    };


    protected void onStart() {
        super.onStart();
        // register sms receiver
        IntentFilter filter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        smsDtoList = new ArrayList<>();
        checkPermission(Manifest.permission.READ_SMS, READSMS_PERMISSION_CODE);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECEIVE_SMS}, 0);
        } else {
            // register sms receiver
            IntentFilter filter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
            this.registerReceiver(broadcastReceiver, filter);
        }
        initUI();


        btnShowPieGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, ReportActivity.class);
                intent.putExtra("totalExpenses", Float.toString(totalExpenses));
                intent.putExtra("totalIncome", Float.toString(totalIncome));

                startActivity(intent);
            }
        });

    }

    void IntializeData() {

        smsViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(SmsViewModel.class);
        smsViewModel.getTrascationSMS(MainActivity.this).observe(this, new Observer<List<SmsDto>>() {
            @Override
            public void onChanged(List<SmsDto> smsDtos) {
                System.out.println("onChanged: " + smsDtos);
                adapter = new RecyleViewAdapter(MainActivity.this, smsDtos);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(adapter);
            }
        });
        smsViewModel.getCountTrasactionDetails().observe(this, new Observer<Trasaction>() {
            @Override
            public void onChanged(Trasaction value) {
                totalExpenses = value.getTotalExpenses();
                totalIncome = value.getTotalIncome();
                tvDebitAmount.setText("Total debit Rs.:" + value.getTotalExpenses());
                tvCreditAmount.setText("Total Credit Rs.:" + value.getTotalIncome());


            }


        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void IsReceivedSMS(List<SmsDto> newlist) {

        smsDtoList.addAll(newlist);
        adapter.notifyDataSetChanged();
    }
    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{permission},
                    requestCode);
        } else {
            Toast.makeText(MainActivity.this,
                    "Permission already granted",
                    Toast.LENGTH_SHORT)
                    .show();
            IntializeData();

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == READSMS_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this,
                        "Read SMS Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
                IntializeData();

            } else {
                //Read SMS Permission Denied
                Toast.makeText(MainActivity.this,
                        "The application cannot run because it does not have the permission.",
                        Toast.LENGTH_SHORT)
                        .show();
                finish();
            }
        } else if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {

        } else {
            // register sms receiver
            IntentFilter filter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
            registerReceiver(broadcastReceiver, filter);
        }
    }

    private void initUI() {
        tvCreditAmount = findViewById(R.id.credit);
        tvDebitAmount = findViewById(R.id.debit);
        btnShowPieGraph = findViewById(R.id.totalExpensesIncome);
        recyclerView = findViewById(R.id.recyclerView);
    }

}
