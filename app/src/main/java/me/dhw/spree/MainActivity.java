package me.dhw.spree;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements TaskAdapter.ItemClickListener {

    private ArrayList<String> items;
    private TaskAdapter adapter;
    private RecyclerView rView;
    private Button button;
    private EditText taskInput;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private MediaPlayer mp;

    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mp = MediaPlayer.create(this, R.raw.kids_cheering);
        notificationManager = NotificationManagerCompat.from(this);

        rView = findViewById(R.id.recyclerView);
        rView.setLayoutManager(new LinearLayoutManager(this));
        button = findViewById(R.id.additem);

        button.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          addItem(view);
                                      }
                                  }
        );

        items = new ArrayList<>();

        prefs = getSharedPreferences(getString(R.string.shared_preferences_file), MODE_PRIVATE);
        Set<String> taskSet = prefs.getStringSet("tasks", null);
        if (taskSet != null) items.addAll(taskSet);
        adapter = new TaskAdapter(this, items);
        adapter.setClickListener(this);
        rView.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rView.getContext(),
                RecyclerView.VERTICAL);
        rView.addItemDecoration(dividerItemDecoration);
        taskInput = findViewById(R.id.taskInput);
    }



    private void addItem(View view) {
        String task = taskInput.getText().toString();
        if (task.isEmpty()) notification();
        else {
            if (items.contains(task)) removeItem(items.indexOf(task));
            items.add(task);
            adapter.notifyDataSetChanged();
            saveTasks();

            taskInput.setText("");
        }
    }
    private void removeItem(int position){
        items.remove(position);
        adapter.notifyItemRemoved(position);
        saveTasks();
    }


    @Override
    public void onItemClick(View view, TaskAdapter.ViewHolder viewHolder, int position) {
        if (view == viewHolder.cancelButton) {
            removeItem(position);
        }
        else if (view == viewHolder.doneButton) {
            removeItem(position);
            playSound();
        }
    }

    private void saveTasks(){
        HashSet<String> taskSet = new HashSet<>(items);
        editor = prefs.edit();
        editor.putStringSet("tasks", taskSet);
        editor.apply();
    }

    private void playSound(){
        mp.start();
    }

    private void notification(){
//        Intent intent = new Intent(this, AlertDetails.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Intent activityIntent = new Intent(this, MainActivity.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, activityIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notificationicon)
                .setContentTitle("Todo list")
                .setContentText("Time to make a todo list!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setColor(Color.YELLOW)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .build();

        notificationManager.notify(1, notification);


    }


}