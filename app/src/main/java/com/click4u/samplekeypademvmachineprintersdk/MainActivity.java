package com.click4u.samplekeypademvmachineprintersdk;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.click4u.samplekeypademvmachineprintersdk.printing.PrnStrFormat;
import com.click4u.samplekeypademvmachineprintersdk.printing.PrnTextFont;
import com.click4u.samplekeypademvmachineprintersdk.printing.PrnTextStyle;
import com.click4u.samplekeypademvmachineprintersdk.printing.SetupPrinter;
import com.pos.sdk.printer.POIPrinterManage;
import com.pos.sdk.printer.models.BitmapPrintLine;
import com.pos.sdk.printer.models.PrintLine;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_test_print;
    PrinterListener printerListener = new PrinterListener();
    private POIPrinterManage printerManage;
    SetupPrinter setupPrinter;
    String header, footer, middle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_test_print = findViewById(R.id.btn_test_print);
        setupPrinter();
        btn_test_print.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupPrinter();
    }

    private void setupPrinter() {
        printerManage = POIPrinterManage.getDefault(this);
        setupPrinter = SetupPrinter.getInstance(MainActivity.this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_test_print) {
            if (printerManage.ifHavePaper()) {
                String person_name = "Test";
                Calendar mCalendar = Calendar.getInstance();
                Date mDate = mCalendar.getTime();

                String currentDate = getDateString("dd/MM/yy", mDate) + " " + setCurrentTime();

                String agent_no = "01";
                String rec_acc_no = "01";
                String rec_id = "001";
                String rec_acc_type = "CURR";
                String pre_bal = "2500";
                String dep_amt = "500";
                String curr_bal = "3000";
                String acc_open_date = "28-01-21";

                header = "Test App" +
                        "\n" +
                        "*--- RECEIPT ---*";

                StringBuilder mStringBuilder1 = new StringBuilder();
                mStringBuilder1.append(String.format(Locale.ENGLISH, "AGENT NO.    : %-17s", agent_no));
                mStringBuilder1.append(String.format(Locale.ENGLISH, "RECEIPT DATE : %-17s", currentDate));
                mStringBuilder1.append(String.format(Locale.ENGLISH, "RECEIPT NO.  : %-17s", rec_id));
                mStringBuilder1.append(String.format(Locale.ENGLISH, "ACCOUNT NO.  : %-17s", rec_acc_no));
                mStringBuilder1.append(String.format(Locale.ENGLISH, "ACCOUNT NAME : %-17s", person_name));
                mStringBuilder1.append(String.format(Locale.ENGLISH, "PREVIOUS BALANCE : %-13s", pre_bal));
                mStringBuilder1.append(String.format(Locale.ENGLISH, "DEPOSIT AMOUNT   : %-13s", dep_amt));
                mStringBuilder1.append(String.format(Locale.ENGLISH, "CURRENT BALANCE  : %-13s", curr_bal));
                mStringBuilder1.append(String.format(Locale.ENGLISH, "ACC OPENING DATE : %-13s", acc_open_date));
                middle = mStringBuilder1.toString();
                footer = "Thanks You !!!\n\n\n";
                print_test();
            } else {
                Toast.makeText(MainActivity.this, "no_paper", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static String getDateString(String dateFormat, Date mDate) {
        String dateString = null;
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());
        dateString = mSimpleDateFormat.format(mDate).toUpperCase();
        return dateString;
    }

    public String setCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");
        String strDate = mdformat.format(calendar.getTime());
        return strDate;
    }

    private void print_test() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PrnStrFormat format = new PrnStrFormat();
                PrnStrFormat format1 = new PrnStrFormat();
                format1.setFont(PrnTextFont.CUSTOM);
                format1.setPath("fonts/simsun.ttf");
                format1.setTextSize(28);
                format1.setAli(Layout.Alignment.ALIGN_CENTER);
                format1.setStyle(PrnTextStyle.BOLD);
                format1.setLetterSpacing(0.1f);
                setupPrinter.setPrintAppendString(header, format1);
                format.setFont(PrnTextFont.CUSTOM);
                format.setPath("fonts/simsun.ttf");
                format.setTextSize(25);
                format.setStyle(PrnTextStyle.NORMAL);
                format.setAli(Layout.Alignment.ALIGN_NORMAL);
                setupPrinter.setPrintAppendString(middle, format);

                format1.setFont(PrnTextFont.CUSTOM);
                format1.setPath("fonts/simsun.ttf");
                format1.setTextSize(18);
                format1.setAli(Layout.Alignment.ALIGN_CENTER);
                format1.setStyle(PrnTextStyle.BOLD);
                format1.setLetterSpacing(0.1f);
                setupPrinter.setPrintAppendString(footer, format1);

                //printerManage.setPrintGray(1000);
                printerManage.setLineSpace(1);
                printerManage.cleanCache();
                BitmapPrintLine bitmapPrintLine = new BitmapPrintLine();
                Bitmap bitmap = setupPrinter.setPrintStart();
                bitmapPrintLine.setType(PrintLine.BITMAP);
                bitmapPrintLine.setPosition(PrintLine.CENTER);
                bitmapPrintLine.setBitmap(bitmap);
                printerManage.addPrintLine(bitmapPrintLine);
                printerManage.beginPrint(printerListener);

            }
        }).start();
    }

    class PrinterListener implements POIPrinterManage.IPrinterListener {

        @Override
        public void onStart() {
            Log.e("xggd", "start print");
        }

        @Override
        public void onFinish() {
            Log.e("fgfdgfdgd", "pint success");
        }

        @Override
        public void onError(int errorCode, String detail) {
            Log.e("", "print error" + " errorCode = " + errorCode + " detail = " + detail);
            Toast.makeText(MainActivity.this, detail, Toast.LENGTH_SHORT).show();
        }
    }

}