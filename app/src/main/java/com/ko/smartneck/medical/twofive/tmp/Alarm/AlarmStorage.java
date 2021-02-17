package com.ko.smartneck.medical.twofive.tmp.Alarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.ko.smartneck.medical.twofive.tmp.Alarm.AlarmActivity.alarms;

public class AlarmStorage {
    String TAG = "AlarmStorage";
    Context context;

    public AlarmStorage(Context context) {
        this.context = context;
    }


    public void saveData() {
        SharedPreferences pre = context.getSharedPreferences("alarm", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();

        try {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < alarms.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                if (alarms.get(i).getType() == 0){

                }else if (alarms.get(i).getType() == 1){
                    jsonObject.put("sun", alarms.get(i).isSun());
                    jsonObject.put("mon", alarms.get(i).isMon());
                    jsonObject.put("tue", alarms.get(i).isTue());
                    jsonObject.put("wed", alarms.get(i).isWed());
                    jsonObject.put("thu", alarms.get(i).isThu());
                    jsonObject.put("fri", alarms.get(i).isFri());
                    jsonObject.put("sat", alarms.get(i).isSat());
                }else if (alarms.get(i).getType() == 2){
                    jsonObject.put("year", alarms.get(i).getYear());
                    jsonObject.put("month", alarms.get(i).getMonth());
                    jsonObject.put("date", alarms.get(i).getDate());
                }

                jsonObject.put("type", alarms.get(i).getType());
                jsonObject.put("use", alarms.get(i).isUse());
                jsonObject.put("hour", alarms.get(i).getHour());
                jsonObject.put("minute", alarms.get(i).getMinute());
                jsonObject.put("dayTime", alarms.get(i).getDayTime());
                jsonArray.put(jsonObject);

            }
            editor.putString("alarm", jsonArray.toString());
            editor.commit();
        } catch (JSONException e) {
            Log.d(TAG, "saveData: json parsing error" + e);

        }
        Log.i(TAG, "saveData: data save");
    }


    boolean sun, mon, tue, wed, thu, fri, sat, use;
    int hour, minute, year , month , date , type;
    String dayTime;

    public void loadData() {
        SharedPreferences pre = context.getSharedPreferences("alarm", Context.MODE_PRIVATE);
        String json = pre.getString("alarm", null);
        alarms.clear();
        JSONObject jsonObject = null;
        try {
                if (json == null)return;
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {

                    jsonObject = jsonArray.getJSONObject(i);


                    if (!jsonObject.isNull("sun"))sun = jsonObject.getBoolean("sun");
                    if (!jsonObject.isNull("mon"))mon = jsonObject.getBoolean("mon");
                    if (!jsonObject.isNull("tue"))tue = jsonObject.getBoolean("tue");
                    if (!jsonObject.isNull("wed"))wed = jsonObject.getBoolean("wed");
                    if (!jsonObject.isNull("thu"))thu = jsonObject.getBoolean("thu");
                    if (!jsonObject.isNull("fri"))fri = jsonObject.getBoolean("fri");
                    if (!jsonObject.isNull("sat"))sat = jsonObject.getBoolean("sat");
                    if (!jsonObject.isNull("year"))year = jsonObject.getInt("year");
                    if (!jsonObject.isNull("month"))month = jsonObject.getInt("month");
                    if (!jsonObject.isNull("date"))date = jsonObject.getInt("date");
                    use = jsonObject.getBoolean("use");
                    type = jsonObject.getInt("type");
                    dayTime = jsonObject.getString("dayTime");
                    hour = jsonObject.getInt("hour");
                    minute = jsonObject.getInt("minute");

                    if (type == 0){
                        alarms.add(new AlarmItem(type, hour, minute));

                    }else if (type == 1){

                        alarms.add(new AlarmItem(type, mon, tue, wed, thu,
                                fri, sat, sun, hour, minute));

                    }else if (type == 2){
                        alarms.add(new AlarmItem(type, year, month, date, hour, minute));
                    }

                    alarms.get(i).setUse(use);
                    alarms.get(i).setDayTime(dayTime);

                }



        } catch (JSONException e) {
            Log.e(TAG, "loadData: json parsing error " + e);
        }
        Log.i(TAG, "loadData: data load");

    }
}
