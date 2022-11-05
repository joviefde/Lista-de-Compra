/*
    Code written by IJApps
    github.com/IJ-Apps
    Tutorial Series: https://www.youtube.com/watch?v=9nFGR8dIu_w&list=PLLmkb5CTw5rRsR6reO-ZkbE-QJF-GstwG
*/

package app.ij.listviews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.gesture.Gesture;
import android.media.Image;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    static ListView listView;
    EditText input;
    ImageView enter;
    static ListViewAdapter adapter;
    static ArrayList<String> items;
    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list);
        input = findViewById(R.id.input);
        enter = findViewById(R.id.add);
        context = getApplicationContext();

        items = new ArrayList<>();
        items.add("Queso");
        items.add("Pan pa migas");
        items.add("Piminetos de piquillo");
        items.add("Turrón");
        items.add("Berenjena de Almagro");

        listView.setLongClickable(true);
        adapter = new ListViewAdapter(this, items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem = (String) listView.getItemAtPosition(position);
                Toast.makeText(MainActivity.this, clickedItem, Toast.LENGTH_SHORT).show();
            }
        });
        // Borrar item
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                removeItem(i);
                return false;
            }
        });

        // Meter item
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = input.getText().toString();
                if (text == null || text.length() == 0) {
                    makeToast("Enter an item.");
                } else {
                    addItem(text);
                    input.setText("");
                    makeToast("Added " + text);
                }
            }
        });
        loadContent();
    }

    // Lector de fichero
    public void loadContent() {
        File path = getApplicationContext().getFilesDir();
        File readFrom = new File(path, "list.txt");
        byte[] content = new byte[(int) readFrom.length()];

        FileInputStream stream = null;
        try {
            stream = new FileInputStream(readFrom);
            stream.read(content);

            String s = new String(content);
            s = s.substring(1, s.length() - 1);
            String split[] = s.split(", ");

            if (split.length == 1 && split[0].isEmpty())
                items = new ArrayList<>();
            else items = new ArrayList<>(Arrays.asList(split));

            adapter = new ListViewAdapter(this, items);
            listView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        File path = getApplicationContext().getFilesDir();
        try {
            FileOutputStream writer = new FileOutputStream(new File(path, "list.txt"));
            writer.write(items.toString().getBytes());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    public static void removeItem(int i) {
        makeToast("Eliminar: " + items.get(i));
        items.remove(i);
        listView.setAdapter(adapter);
    }

    public static void addItem(String item) {
        items.add(item);
        listView.setAdapter(adapter);
    }

    static Toast t;

    private static void makeToast(String s) {
        if (t != null) t.cancel();
        t = Toast.makeText(context, s, Toast.LENGTH_SHORT);
        t.show();
    }
}