package by.lykashenko.alarm.Fragments;

import android.animation.Animator;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import by.lykashenko.alarm.Dialog.DialogTypeAlarm;
import by.lykashenko.alarm.MainActivity;
import by.lykashenko.alarm.R;

/**
 * Created by Дмитрий on 03.10.16.
 */
public class FragmentAddAlarm extends Fragment {

    private EditText editTextNote;
    private Spinner spinnerDate, spinnerTime;
    private Switch switchReapete;
    private LinearLayout linearLayoutType, linearLayoutMelody;
    private TextView textViewSave;
    private SimpleDateFormat dateFormat, timeFormat;
    private Date date;
    private ArrayAdapter<String> adapterDate, adapterTime;
    private Integer dif, type;
    private final static int REQUEST_TYPE = 1;

//    @Override
//    public void onDialogSaveTypeDif(Integer dif, Integer type) { //параметры сложности и отключения
//        this.dif = dif;
//        this.type = type;
//    }

    public interface InterfaceClickOption {
        void onInterfaceClickOption(String note, Long timeAlarm, Boolean repeate, Integer dif, Integer type);
    }


    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_alarm, container, false);

        editTextNote = (EditText) view.findViewById(R.id.editTextNote);
        spinnerDate = (Spinner) view.findViewById(R.id.spinnerDate);
        spinnerTime = (Spinner) view.findViewById(R.id.spinnerTime);
        switchReapete = (Switch) view.findViewById(R.id.switchReapete);
        linearLayoutType = (LinearLayout) view.findViewById(R.id.LinearLayoutType);
        linearLayoutMelody = (LinearLayout) view.findViewById(R.id.LinearLayoutMelody);
        textViewSave = (TextView) view.findViewById(R.id.textViewSave);

        Calendar calendar_date_time = Calendar.getInstance();
        final long current_time = calendar_date_time.getTimeInMillis();
        date = new Date(current_time);

        dateFormat = new SimpleDateFormat("EEE, MMM dd, yyyy");
        final String[] dateStringOutput = {dateFormat.format(date)};
        timeFormat = new SimpleDateFormat("HH:mm");
        final String[] timeStringOutput = {timeFormat.format(date)};

        adapterDate = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, dateStringOutput);
        spinnerDate.setAdapter(adapterDate);
        spinnerDate.setSelection(0);
        spinnerDate.setClickable(false);

        spinnerDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
//                Toast.makeText(getContext(),"Нажата выбор даты", Toast.LENGTH_LONG).show();
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, monthOfYear);
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            Date date_set = calendar.getTime();
                            dateStringOutput[0] = dateFormat.format(date_set);
                            adapterDate.notifyDataSetChanged();
                        }
                    }, Integer.parseInt((new SimpleDateFormat("yyyy")).format(date)), Integer.parseInt((new SimpleDateFormat("MM")).format(date)), Integer.parseInt((new SimpleDateFormat("dd")).format(date)));
                    datePickerDialog.show();
                }
                return true;
            }
        });

        adapterTime = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, timeStringOutput);
        spinnerTime.setAdapter(adapterTime);
        spinnerTime.setSelection(0);
        spinnerTime.setClickable(false);

        spinnerTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            calendar.set(Calendar.MINUTE, minute);
                            Date time_set = calendar.getTime();
                            timeStringOutput[0] = timeFormat.format(time_set);
                            adapterTime.notifyDataSetChanged();

                        }
                    }, date.getHours(), date.getMinutes(), true);
                    timePickerDialog.show();
                }
                return true;
            }
        });

        linearLayoutType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogTypeAlarm dialogTypeAlarm = new DialogTypeAlarm();
                dialogTypeAlarm.setTargetFragment(getFragmentManager().findFragmentByTag(MainActivity.ADD_ALARM), REQUEST_TYPE);
                dialogTypeAlarm.show(getActivity().getSupportFragmentManager(), "TypeAlarm");
            }
        });

        linearLayoutMelody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        textViewSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note_alarm = editTextNote.getText().toString();//описание будильника

                String date_time = new StringBuilder().append(dateStringOutput[0]).append(" ").append(timeStringOutput[0]).toString();
                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("EEE, MMM dd, yyyy HH:mm", Locale.getDefault());
                Date date = null;
                try {
                    date = dateTimeFormat.parse(date_time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Long time_alarm = date.getTime();//время будильника

                Boolean stateRepeate = false; //проверка состояния переключателя повтора
                if (switchReapete.isChecked()) {
                    stateRepeate = true;
                }

                InterfaceClickOption interfaceClickOption = (InterfaceClickOption) getActivity();
                interfaceClickOption.onInterfaceClickOption(note_alarm, time_alarm, stateRepeate, dif, type);


            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_TYPE:
                    dif = data.getIntExtra("dif", 0);
                    type = data.getIntExtra("type", 0);
                    break;
            }
        }
    }
}
