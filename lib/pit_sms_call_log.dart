import 'dart:async';
import 'dart:core';

import 'package:flutter/services.dart';


class PitSmsCallLog {
  static const MethodChannel _channel =
  const MethodChannel('pit_sms_call_log');


  static Future<List<dynamic>> getSmsLog(double startTime) async {
    final List<dynamic> sms = await _channel.invokeMethod("getSmsLog", {"startTime": startTime});
    return sms;
  }

  static Future<List<dynamic>> getCallLog(double startTime) async {
    final List<dynamic> call = await _channel.invokeMethod("getCallLog", {"startTime": startTime});
    return call;
  }
}
