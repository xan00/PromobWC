package za.co.rdata.r_datamobile.meterReadingModule;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import za.co.rdata.r_datamobile.R;

public class InputReadingActivity extends AppCompatActivity {

    private TextView textView_Display;
    private TextView textView_Message;
    private Double highReading;
    private Double lowReading;
    private Double reading_0 = -999d;
    private Double reading_1 = -999d;
    private int retries = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_reading);
        Button inputButton_0 = findViewById(R.id.inputButton_0);
        Button inputButton_1 = findViewById(R.id.inputButton_1);
        Button inputButton_2 = findViewById(R.id.inputButton_2);
        Button inputButton_3 = findViewById(R.id.inputButton_3);
        Button inputButton_4 = findViewById(R.id.inputButton_4);
        Button inputButton_5 = findViewById(R.id.inputButton_5);
        Button inputButton_6 = findViewById(R.id.inputButton_6);
        Button inputButton_7 = findViewById(R.id.inputButton_7);
        Button inputButton_8 = findViewById(R.id.inputButton_8);
        Button inputButton_9 = findViewById(R.id.inputButton_9);
        Button inputButton_dec = findViewById(R.id.inputButton_decimal);
        Button inputButton_CLEAR = findViewById(R.id.inputButton_CLEAR);
        Button inputButton_ENTER = findViewById(R.id.inputButton_ENTER);
        textView_Display = findViewById(R.id.textViewDisplay);
        textView_Message = findViewById(R.id.textViewMessage);

        inputButton_0.setOnClickListener(buttonClicked);
        inputButton_1.setOnClickListener(buttonClicked);
        inputButton_2.setOnClickListener(buttonClicked);
        inputButton_3.setOnClickListener(buttonClicked);
        inputButton_4.setOnClickListener(buttonClicked);
        inputButton_5.setOnClickListener(buttonClicked);
        inputButton_6.setOnClickListener(buttonClicked);
        inputButton_7.setOnClickListener(buttonClicked);
        inputButton_8.setOnClickListener(buttonClicked);
        inputButton_9.setOnClickListener(buttonClicked);
        inputButton_CLEAR.setOnClickListener(buttonClicked);
        inputButton_ENTER.setOnClickListener(buttonClicked);
        inputButton_dec.setOnClickListener(buttonClicked);

        textView_Message.setText("");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            lowReading = extras.getDouble("LowReading");
            highReading = extras.getDouble("HighReading");
            double OrigReading = extras.getDouble("OrigReading");
            setDisplay(Double.toString(OrigReading));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("DefaultLocale")
    private void setDisplay(String displayValue) {
        if (displayValue.equals("-999.0"))
            textView_Display.setText("");
        else
           textView_Display.setText(displayValue);
    }

    private Boolean isInRange() {
        Double reading = Double.parseDouble(textView_Display.getText().toString());
        return (reading > lowReading) && (reading < highReading);
    }

    private View.OnClickListener buttonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button clickedButton = (Button) v;
            String buttonText = clickedButton.getText().toString();

            if (buttonText.equals("ENTER")) {
                if (textView_Display.getText().toString().equals("")) {
                    textView_Message.setText("Please enter a reading first");
                    return;
                }

                String readingvalue = textView_Display.getText().toString();
                if (readingvalue.endsWith(".")) {
                    readingvalue = readingvalue.replace(".","");
                }

                if (reading_0 == -999d)
                    reading_0 = Double.parseDouble(readingvalue);
                else
                    reading_1 = Double.parseDouble(readingvalue);

                if (((reading_1 == -999d) && isInRange()) | (lowReading == 0d & highReading == 0d)) {
                    sendResult();
                } else {
                    if (reading_1 == -999d) {
                        textView_Message.setText("Please re-enter the reading to verify");
                        setDisplay("");
                        retries += 1;
                    } else {
                        if (Double.compare(reading_0, reading_1) == 0) {
                            sendResult();
                        } else {
                            textView_Message.setText("Second entry does not match the first, please enter again.");
                            setDisplay("");
                            reading_0 = reading_1;
                            reading_1 = -999d;
                            retries += 1;
                        }
                    }
                }
                buttonText = "";
            }

            StringBuilder sb = new StringBuilder(textView_Display.getText().toString());

            if (buttonText.equals("CLEAR")) {
                if (sb.length() > 0)
                    sb.deleteCharAt(sb.length() - 1);
                buttonText = "";
            }

            sb.append(buttonText);
            if (sb.toString().equals(""))
                setDisplay("");
            else {
                //double convert_value = 0d;
                try {/*
                    if (sb.indexOf(".")==(sb.length()-1)) {
                        sb.append("0");
                    }*/

                    //convert_value = Double.parseDouble(sb.toString());
                    setDisplay(sb.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    setDisplay("");
                }
            }
        }
    };

    private void sendResult() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("Reading", reading_0);
        resultIntent.putExtra("Retries", retries);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
