package com.example.user.joe120801;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    final String DB_NAME = "student.sqlite";
    String DB_FILE;
    EditText ed1, ed2, ed3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DB_FILE = getFilesDir() + File.separator + DB_NAME;
        ed1 = (EditText) findViewById(R.id.editText);
        ed2 = (EditText) findViewById(R.id.editText2);
        ed3 = (EditText) findViewById(R.id.editText2);
        copyDataBaseFile();
    }

        public void clickAdd(View v)
        {
            //新增資料至sqlite檔案
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_FILE, null);
            String strSName = ed1.getText().toString();
            String strphone = ed2.getText().toString();
            String straddr = ed3.getText().toString();
            //以下寫法有SQL語法,會遭受隱碼攻擊的風險
            //String sql = "insert into data (SName, phone, addr) values ('" + SName + "','" + phone + "','" + addr + "')";
            //String sql = String.format("insert into data (SName, phone, addr) values ('%s','%s','%s')", SName, phone, addr);
            //db.execSQL(sql);
            //改使用下列寫法,避免受到隱碼攻擊
            ContentValues cv = new ContentValues();
            cv.put("SName", strSName);
            cv.put("phone", strphone);
            cv.put("addr", straddr);
            db.insert("data", null, cv);
            db.close();
        }
        public void clickList(View v)
        {
            //以下是打開手機內部file位置的sqlite檔案
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_FILE, null);
            Cursor c = db.rawQuery("Select * from data", null);
            if (c.moveToFirst())
            {
                Log.d("DB", c.getString(1));
            }

            while(c.moveToNext())
            {
                Log.d("DB", c.getString(1));
            }
            c.close();
            db.close();
        }

        private void copyDataBaseFile()
        {
            //放在assets中的sqlite檔為唯讀檔,無法直接使用,故app啟動時需先將此檔拷貝至手機內部儲存空間(file)
            //以下為拷貝程式段
            File f = new File(DB_FILE);

            if (!f.exists())//若手機內部尚無拷貝的sqlite檔,便拷貝一個
            {
                InputStream is = null;
                try {
                    is = MainActivity.this.getAssets().open(DB_NAME);
                    OutputStream os = new FileOutputStream(f);

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = is.read(buffer)) > 0)
                    {
                        os.write(buffer, 0, length);
                    }
                    os.flush();
                    os.close();
                    is.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
}
