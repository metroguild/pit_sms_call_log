package com.padimas.pit_sms_call_log;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.provider.Telephony;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PitSmsCallLogPlugin
 */
public class PitSmsCallLogPlugin implements MethodCallHandler {
    Context context;


    public PitSmsCallLogPlugin(Registrar registrar) {
        context = registrar.context();
    }

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "pit_sms_call_log");
        channel.setMethodCallHandler(new PitSmsCallLogPlugin(registrar));
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        if (call.method.equals("getSmsLog")) {
            double startTime = call.argument("startTime");
            try {
                List<Map<String, Object>> smsLogs = getSmsLog(startTime);
                result.success(smsLogs);
            } catch (Exception e1) {
                e1.printStackTrace();
                result.error("Error ", "getSmsLog Error : " + e1.getLocalizedMessage(), e1);
            }

        } else if (call.method.equals("getCallLog")) {
            double startTime = call.argument("startTime");
            try {
                List<Map<String, Object>> callLogs = getCallLog(startTime);
                result.success(callLogs);
            } catch (Exception e) {
                e.printStackTrace();
                result.error("Error ", "getCallLog Error : " + e.getLocalizedMessage(), e);
            }

        } else {
            result.notImplemented();
        }
    }

    private List<Map<String, Object>> getCallLog(final double startTime) throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(0);
        String formattedStartTime = df.format(startTime);

        String condition = CallLog.Calls.DATE + " > " + formattedStartTime;
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
                condition, null, null);

        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Map<String, Object> result = new HashMap<>();
                String[] colNames = cursor.getColumnNames();
                for (int i = 0; i < colNames.length; i++) {
                    Object value = cursor.getString(cursor.getColumnIndex(colNames[i]));
                    result.put(colNames[i], value);
                }
                list.add(result);
            }
            cursor.close();
        }

        return list;
    }

    private List<Map<String, Object>> getSmsLog(double startTime) throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(0);
        String formattedStartTime = df.format(startTime);

        String condition = Telephony.Sms.DATE + " > " + startTime;
        Cursor cursor = context.getContentResolver().query(Telephony.Sms.CONTENT_URI, null, condition, null, null);

        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Map<String, Object> result = new HashMap<>();
                String[] colNames = cursor.getColumnNames();
                for (int i = 0; i < colNames.length; i++) {
                    Object value = cursor.getString(cursor.getColumnIndex(colNames[i]));
                    result.put(colNames[i], value);
                }
                list.add(result);
            }
            cursor.close();
        }

        return list;
    }
}
