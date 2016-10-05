package by.lykashenko.alarm.BD;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Дмитрий on 02.10.16.
 */

@Table(name = "Alarm", id = "_id")
public class AlarmTable extends Model{

    @Column(name = "timeAlarm")
    public Long timeAlarm;

    @Column(name = "note")
    public String note;

    @Column(name = "stateRepeat")
    public Boolean stateRepeat;

    @Column(name = "difficult")
    public Integer difficult;

    @Column(name = "typeOffAlarm")
    public Integer typeOffAlarm;

    @Column(name = "stateAlarm")
    public Boolean stateAlarm;

    public AlarmTable(){
        super();
    }

    public  AlarmTable(Long timeAlarm,
                       String note,

                       Boolean stateRepeat,
                       Integer difficult,
                       Integer typeOffAlarm,
                        Boolean stateAlarm){

        this.timeAlarm = timeAlarm;
        this.note = note;

        this.stateRepeat = stateRepeat;
        this.difficult = difficult;
        this.typeOffAlarm = typeOffAlarm;
        this.stateAlarm = stateAlarm;
    }
}
