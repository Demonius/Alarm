package by.lykashenko.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.activeandroid.query.Select;

import java.util.List;

import by.lykashenko.alarm.BD.AlarmTable;
import by.lykashenko.alarm.Fragments.FragmentAddAlarm;
import by.lykashenko.alarm.Fragments.FragmentListAlarm;
import by.lykashenko.alarm.Reciever.AlarmReciever;


public class MainActivity extends AppCompatActivity implements FragmentListAlarm.InterfaceAddAlarm
        , FragmentAddAlarm.InterfaceClickOption {

    public final static  String  ADD_ALARM = "AddAlarm";
    public final static  String  LIST_ALARM = "ListAlarm";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Configuration configuration = new Configuration.Builder(this).setDatabaseName("Alarm.db").setModelClasses(AlarmTable.class).create();
        ActiveAndroid.initialize(configuration);

        FragmentListAlarm fragmentListAlarm = new FragmentListAlarm();
        FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
        ftrans.add(R.id.frameLayoutMain,fragmentListAlarm,LIST_ALARM);
        ftrans.commit();
    }

    @Override
    public void onInterfaceAddAlarm() {
        Toast.makeText(this, "Добавление будильника", Toast.LENGTH_SHORT).show();
        FragmentAddAlarm fragmentAddAlarm = new FragmentAddAlarm();
        FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
        ftrans.replace(R.id.frameLayoutMain, fragmentAddAlarm, ADD_ALARM);
        ftrans.addToBackStack("AddAlarm");
        ftrans.commit();
    }

    @Override
    public void onInterfaceClickOption(String note, Long timeAlarm, Boolean repeate, Integer dif, Integer type) {
        Boolean stateAlarm = true;
        ActiveAndroid.beginTransaction();
        AlarmTable alarmTable = new AlarmTable(timeAlarm,note,repeate,dif,type, stateAlarm);
        alarmTable.save();
        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();

        List<AlarmTable> list = new Select().from(AlarmTable.class).where("timeAlarm = ?",timeAlarm).execute();
        String id = list.get(0).getId().toString();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReciever.class);
        intent.putExtra("note", note);
        intent.putExtra("id", id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,Integer.parseInt(id),intent,PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pendingIntent);
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeAlarm, pendingIntent);

        Fragment fragment_list = getSupportFragmentManager().findFragmentByTag(LIST_ALARM);
        FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
        ftrans.replace(R.id.frameLayoutMain, fragment_list);
        ftrans.commit();
    }
}
