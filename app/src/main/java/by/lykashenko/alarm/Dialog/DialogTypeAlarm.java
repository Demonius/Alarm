package by.lykashenko.alarm.Dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import by.lykashenko.alarm.R;

/**
 * Created by Дмитрий on 04.10.16.
 */
public class DialogTypeAlarm extends DialogFragment {

    private RadioGroup radioGroupDif, radioGroupTypeOff;
    private TextView textViewSaveDifType;
    private Integer difOutput, typeOutput;

    public interface DialogSaveTypeDif {
        void onDialogSaveTypeDif(Integer dif, Integer type);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Material_Light);
    }

    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().setCanceledOnTouchOutside(false);
        View v = inflater.inflate(R.layout.add_alarm_type, null);
        radioGroupDif = (RadioGroup) v.findViewById(R.id.radioGroupDif);

        radioGroupTypeOff = (RadioGroup) v.findViewById(R.id.radioGroupTypeOff);
        textViewSaveDifType = (TextView) v.findViewById(R.id.textViewSaveType);

        radioGroupDif.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioLite:
                        difOutput = 0;
                        Toast.makeText(getContext(), "Lite = ",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.radioMedium:
                        difOutput = 1;
                        Toast.makeText(getContext(), "Medium = ",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.radioStrong:
                        difOutput = 2;
                        Toast.makeText(getContext(), "Strong = ",Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });

//        radioGroupDif.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (radioGroupDif.getId()) {
//                    case R.id.radioLite:
//                        difOutput = 0;
//                        break;
//                    case R.id.radioMedium:
//                        difOutput = 1;
//                        break;
//                    case R.id.radioStrong:
//                        difOutput = 2;
//                        break;
//                }
//            }
//        });

        radioGroupTypeOff.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioMath:
                        typeOutput = 0;
                        break;
                    case R.id.radioPoints:
                        typeOutput = 1;
                        break;
                    case R.id.radioPointsMath:
                        typeOutput = 2;
                        break;
                }
            }
        });

        radioGroupTypeOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        textViewSaveDifType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DialogSaveTypeDif dialogSaveTypeDif =(DialogSaveTypeDif) getActivity();
//                dialogSaveTypeDif.onDialogSaveTypeDif(difOutput, typeOutput);
//                Toast.makeText(getContext(), "dif = "+Integer.toString(difOutput)+"; type = "+Integer.toString(typeOutput), Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.putExtra("dif",difOutput);
                intent.putExtra("type", typeOutput);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                dismiss();
            }
        });

        return v;
    }
}
