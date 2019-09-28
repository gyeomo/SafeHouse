package momos.org.safetyhouse;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class CreateDB {
    String seoulKey = "794855646863616e3339724d554e75";
    String dbName = "ListDB.db";
    String sql;
    Cursor cursor;
    int dbVersion = 1;
    DBHelper dbHelper;
    SQLiteDatabase db;
    Context context;
    XmlPullParser xpp;
    XmlPullParserFactory factory;
    public void CreateDB(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context, dbName, null, dbVersion);
        db = dbHelper.getWritableDatabase();
    }

    public ArrayList<Strings> getDB(){
        ArrayList<Strings> data = new ArrayList<Strings>();
        sql = "SELECT * FROM House ;";
        db = dbHelper.getWritableDatabase();
        cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        if(cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                data.add(new Strings(
                        cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4)));
            }
        }
        cursor.close();
        return data;
    }
    public void XmlParserSeoul() {
        boolean dbExist;

        sql = "SELECT * FROM House;";
        db = dbHelper.getWritableDatabase();
        cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        try {
            factory = XmlPullParserFactory.newInstance();
            xpp = factory.newPullParser();
        }
        catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        if (cursor.getCount() > 0) {
            dbExist = true;
            // Toast.makeText(this, "Exist", Toast.LENGTH_SHORT).show();
        } else {
            dbExist = false;
            //  Toast.makeText(this, "Not Exist", Toast.LENGTH_SHORT).show();
        }
        if (!dbExist) {
            InputStream is_ = null;
            FileOutputStream fos = null;
            File outDir = new File("/data/data/momos.org.safetyhouse/databases");
            outDir.mkdirs();
            // timeStart = System.nanoTime();
            try {
                is_ = context.getAssets().open("ListDB.mp3");
                int size = is_.available();
                byte[] buffer = new byte[size];
                File outfile = new File(outDir + "/" + "ListDB.db");
                fos = new FileOutputStream(outfile);
                for (int c = is_.read(buffer); c != -1; c = is_.read(buffer)) {
                    fos.write(buffer, 0, c);
                }
                is_.close();
                fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }/*
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String name = "";
                    String add = "";
                    Geocoder geocoder = new Geocoder(context);
                    List<Address> list;
                    String queryUrl = "http://openapi.seoul.go.kr:8088/794855646863616e3339724d554e75/xml/womanSafeAreaInfo/1/1000";
                    try {
                        URL url = new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
                        InputStream is = url.openStream(); //url위치로 입력스트림 연결
                        xpp.setInput(new InputStreamReader(is, "UTF-8")); //inputstream 으로부터 xml 입력받기

                        int eventType = xpp.getEventType();

                        while (eventType != XmlPullParser.END_DOCUMENT) {

                            if (eventType == XmlPullParser.START_TAG) {
                                String startTag = xpp.getName();
                                if (startTag.equals("ID")) {
                                    xpp.next();

                                }
                                if (startTag.equals("BR_NM")) {
                                    xpp.next();
                                    name = xpp.getText();
                                }
                                if (startTag.equals("NM")) {
                                    xpp.next();
                                    name = name+" "+xpp.getText();
                                }

                                if (startTag.equals("GU_NM")) {
                                    xpp.next();

                                }
                                if (startTag.equals("ADDR")) {
                                    xpp.next();
                                    add =xpp.getText();

                                    double latitude = 0, longitude=0;
                                    list = null;
                                    try {
                                        list = geocoder.getFromLocationName(
                                                add, // 지역 이름
                                                10); // 읽을 개수
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생");
                                    }
                                    if (list != null) {
                                        if (list.size() != 0) {
                                            Address addr = list.get(0);
                                            latitude = addr.getLatitude();
                                            longitude = addr.getLongitude();
                                        }
                                    }
                                    sql = String.format("INSERT INTO " + "House" + " VALUES(NULL,'%s','%s', '%s', '%s');"
                                            , name, add, latitude, longitude);
                                    db.execSQL(sql);
                                }
                            }
                            eventType = xpp.next();
                        }

                    } catch (Exception e) {
                        // TODO Auto-generated catch blocke.printStackTrace();
                        e.printStackTrace();
                        //Toast.makeText(context, turnString, Toast.LENGTH_SHORT).show();
                    }
                    cursor.close();
                    db.close();
                }
            }).start();*/
        }
    }
}
class Strings{
    private String names;
    private String addrs;
    private String latitude;
    private String longitude;
    public Strings(){
    }
    public Strings(String str1, String str2, String str3, String str4){
        this.names = str1;
        this.addrs = str2;
        this.latitude = str3;
        this.longitude = str4;
    }
    public String getName(){
        return this.names;
    }
    public String getAddr(){
        return this.addrs;
    }
    public String getLatitude(){
        return this.latitude;
    }
    public String getLongitude(){
        return this.longitude;
    }
}
