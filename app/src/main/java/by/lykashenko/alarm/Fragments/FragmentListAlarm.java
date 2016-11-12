package by.lykashenko.alarm.Fragments;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import by.lykashenko.alarm.BD.AlarmTable;
import by.lykashenko.alarm.R;

/**
 * Created by Дмитрий on 01.10.16.
 */
public class FragmentListAlarm extends Fragment {

    private RecyclerView recyclerViewListAlarm;
    private FloatingActionButton fabAddAlarm;
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<AlarmTable> listAlarm;

    public interface InterfaceAddAlarm {
        void onInterfaceAddAlarm();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_alarm, container, false);

        recyclerViewListAlarm = (RecyclerView) view.findViewById(R.id.recyclerViewAlarm);
        fabAddAlarm = (FloatingActionButton) view.findViewById(R.id.floatingActionButtonAddAlarm);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayoutList);

        fabAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InterfaceAddAlarm interfaceAddAlarm = (InterfaceAddAlarm) getActivity();
                interfaceAddAlarm.onInterfaceAddAlarm();
            }
        });

        recyclerViewListAlarm.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewListAlarm.setLayoutManager(mLayoutManager);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerViewListAlarm.setItemAnimator(itemAnimator);
        listAlarm = null;
        Boolean state = false;
        try{
        listAlarm = new Select().from(AlarmTable.class).execute();}
        catch (Exception e){
            Log.d("Alarm","Error: "+e.toString());
            state = true;
        }finally {
            coordinatorLayout.setBackgroundResource(R.drawable.no_alarm);
        }
        if (state) {
            coordinatorLayout.setBackgroundResource(R.drawable.no_alarm);
        } else {
            if (listAlarm.isEmpty()){
                coordinatorLayout.setBackgroundResource(R.drawable.no_alarm);
            }else {
                mAdapter = new MyAdapter(listAlarm);
                recyclerViewListAlarm.setAdapter(mAdapter);
                coordinatorLayout.setBackgroundColor(getResources().getColor(R.color.colorBackground));
            }
        }


        return view;
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.PersonViewHolder> {

        private List<AlarmTable> m_list_alarm;

        public MyAdapter(List<AlarmTable> listAlarm) {
            m_list_alarm = listAlarm;
        }

        public class PersonViewHolder extends RecyclerView.ViewHolder {
            public ImageView typeOff, dif;
            public TextView timeAlarm, dateAlarm, noteAlarm;
            public CardView cv;
            public TextView dayOfWeek;
            public Switch switchStateAlarm;

            public PersonViewHolder(final View item_view) {
                super(item_view);

                cv = (CardView) item_view.findViewById(R.id.cardViewAlarm);
                typeOff = (ImageView) item_view.findViewById(R.id.imageViewTypeOff1);
                dif = (ImageView) item_view.findViewById(R.id.imageViewDif1);
                timeAlarm = (TextView) item_view.findViewById(R.id.textViewTimeAlarm);
                dateAlarm = (TextView) item_view.findViewById(R.id.textViewDateAlarm);
                noteAlarm = (TextView) item_view.findViewById(R.id.textView12);
                dayOfWeek = (TextView) item_view.findViewById(R.id.textViewNameDate);
                switchStateAlarm = (Switch) item_view.findViewById(R.id.switch1);


                item_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Integer position_click = getAdapterPosition();
                        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                        popupMenu.inflate(R.menu.popup_menu_spisok);
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()){
                                    case R.id.itemDelete:
                                        new Delete().from(AlarmTable.class).where("timeAlarm = ?",m_list_alarm.get(position_click).timeAlarm).execute();
                                        listAlarm.clear();
                                        listAlarm = new Select().from(AlarmTable.class).execute();
                                        m_list_alarm=listAlarm;
                                        if (listAlarm.isEmpty()){
                                            coordinatorLayout.setBackgroundResource(R.drawable.no_alarm);
                                        }
                                        mAdapter.notifyDataSetChanged();
//                                        NotificationManager notify = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
//                                        notify.cancel(Integer.parseInt(Long.toString(m_list_alarm.get(position_click).getId())));


                                        break;
                                    case R.id.itemUpdate:
                                        Toast.makeText(getContext(),"Редактирование будильника", Toast.LENGTH_LONG).show();
                                }
                                return false;
                            }
                        });
                        popupMenu.show();

                    }
                });

                switchStateAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        final Integer position_click = getAdapterPosition();
                        ActiveAndroid.beginTransaction();
                        AlarmTable table = AlarmTable.load(AlarmTable.class,m_list_alarm.get(position_click).getId());
                        table.stateAlarm=isChecked;
                        table.save();
                        ActiveAndroid.setTransactionSuccessful();
                        ActiveAndroid.endTransaction();

                        if (isChecked) {
                            Toast.makeText(getContext(), "Будильник " + Integer.toString(position_click) + " включён", Toast.LENGTH_LONG).show();
                        }else{ Toast.makeText(getContext(), "Будильник " + Integer.toString(position_click) + " вsключtн", Toast.LENGTH_LONG).show();}
                    }
                });


            }
        }

        @Override
        public MyAdapter.PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_alarm, parent, false);
            PersonViewHolder pvh = new PersonViewHolder(v);
            return pvh;

        }

        @Override
        public void onBindViewHolder(PersonViewHolder holder, int position) {
            holder.noteAlarm.setHint(m_list_alarm.get(position).note);
            switch (m_list_alarm.get(position).difficult) {
                case 0:
                    holder.dif.setImageResource(R.drawable.lite);
                    break;
                case 1:
                    holder.dif.setImageResource(R.drawable.medium);
                    break;
                case 2:
                    holder.dif.setImageResource(R.drawable.strong);
                    break;
            }
            switch (m_list_alarm.get(position).typeOffAlarm) {
                case 0:
                    holder.typeOff.setImageResource(R.drawable.plus_math);
                    break;
                case 1:
                    holder.typeOff.setImageResource(R.drawable.points);
                    break;
                case 2:
                    holder.typeOff.setImageResource(R.drawable.points_and_plus);
                    break;
            }

            Calendar calendar_date_time = Calendar.getInstance();
            Long current_time = calendar_date_time.getTimeInMillis();

            Long timeAlarm = m_list_alarm.get(position).timeAlarm;
            Date date = new Date(timeAlarm);

            if (timeAlarm< current_time){
                holder.switchStateAlarm.setChecked(false);
            } else{
                holder.switchStateAlarm.setChecked(m_list_alarm.get(position).stateAlarm);
            }

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
            SimpleDateFormat dateOfWeek = new SimpleDateFormat("EEEE");

            holder.timeAlarm.setText(timeFormat.format(date));
            holder.dateAlarm.setText(dateFormat.format(date));
            holder.dayOfWeek.setText(dateOfWeek.format(date));


        }

        @Override
        public int getItemCount() {
            return m_list_alarm.size();
        }
    }
}
