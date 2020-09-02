package com.demo.payo.viewmodel;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.demo.payo.model.SmsDto;
import com.demo.payo.model.Trasaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SmsViewModel extends ViewModel {
    float valTotalIncome, valtotalExpenses;
    List<SmsDto> smsDtoList;
    Trasaction smsDto1 = new Trasaction();
    private static MutableLiveData<List<SmsDto>> userDetails = new MutableLiveData<>();
    private final MutableLiveData<Trasaction>countTrasactionDetails = new MutableLiveData<>();

    public MutableLiveData<Trasaction> getCountTrasactionDetails(){

        smsDto1.setTotalIncome(valTotalIncome);
        smsDto1.setTotalExpenses(valtotalExpenses);
        Log.e("AllCredit",""+valTotalIncome);
        Log.e("Alldebit",""+valtotalExpenses);
        countTrasactionDetails.postValue(smsDto1);
        return countTrasactionDetails;
    }
 public MutableLiveData<List<SmsDto>> getTrascationSMS( Context context) {
     smsDtoList = new ArrayList<>();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateVal = "";
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
                String msgData = cursor.getString(cursor.getColumnIndexOrThrow("body")).toString();
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date")).toString();
                Long dateV = Long.parseLong(date);
                dateVal = formatter.format(new Date(dateV));
                Pattern regEx = Pattern.compile("(?i)(?:(?:Rs.|INR|MRP)\\.?\\s?)(\\d+(:?\\,\\d+)?(\\,\\d+)?(\\.\\d{1,2})?)");
                // Find instance of pattern matches
                Matcher m = regEx.matcher(msgData);
                if (m.find()) {
                    try {
                        String amount = (m.group(0).replaceAll("INR", "").replaceAll(". ", ""));
                        // amount = amount.replaceAll(":Rs.", "");
                        amount.replaceAll(".", "");
                        amount = amount.replaceAll("Rs", "");
                        amount = amount.replaceAll(" ", "");
                        amount = amount.replaceAll(" .", "");
                        amount = amount.replaceAll(" . ", "");
                        amount = amount.replaceAll("", "");
                        amount = amount.replaceAll(" ", "");
                        amount = amount.replaceAll("Rs ", "");
                        amount = amount.replaceAll("R", "");
                        amount = amount.replaceAll("RS", "");
                        amount = amount.replaceAll("INR", "");

                        if (msgData.contains("debited") || msgData.contains("withdrawn")) {
                           valtotalExpenses += Float.parseFloat(amount);

                           // smsDtoList.add(new SmsDto().setTotalExpenses(valdebit));
                            smsDtoList.add(new SmsDto(msgData, Float.parseFloat(amount), dateVal, 0));
                        } else if (msgData.contains("credited") || msgData.contains("withdrawn")) {
                            valTotalIncome += Float.parseFloat(amount);
                           // smsDtoList.add(new SmsDto().setTotalIncome(valcredit));
                            smsDtoList.add(new SmsDto(msgData, Float.parseFloat(amount), dateVal, 1));
                        }
                       // getCountTrasactionDetails(valTotalIncome,valtotalExpenses);

                    // String.valueOf(valtotalExpenses);
                      //  String.valueOf(valTotalIncome);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("No_matchedValue ", "No_matchedValue ");
                }
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(context,
                    "empty box, no SMS",
                    Toast.LENGTH_SHORT)
                    .show();
        }
     userDetails.postValue(smsDtoList);
     return userDetails;

    }
}
