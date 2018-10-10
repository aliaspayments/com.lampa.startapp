package com.lampa.startapp;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class startApp extends CordovaPlugin {
		private Intent intent = null;

		@Override
    public void onNewIntent(Intent intent) {
			this.intent = intent;
    }

		@Override
		public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    	if (action.equals("start")) {
				JSONObject config = args.getJSONObject(0);
				JSONObject data = args.getJSONObject(1);

        Intent intent = cordova
					.getActivity()
					.getPackageManager()
					.getLaunchIntentForPackage(config.getString("application"));

				// Extras
				intent.putExtra("bundleID", data.getString("bundleID"));
        intent.putExtra("jsonData", data.getString("jsonData"));
        intent.putExtra("cartReferenceId", data.getString("cartReferenceId"));

				long timeout = 600000;
        intent.putExtra("purchaseTimeOut", timeout);

				// Flags
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

				cordova.getActivity().startActivity(intent);
				callbackContext.success();

			} else if (action.equals("getExtras")) {
				Intent theIntent = null;

				if (this.intent != null) {
					theIntent = this.intent;
				} else {
					theIntent = cordova.getActivity().getIntent();
				}

				Bundle extras = theIntent.getExtras();
				JSONObject info = new JSONObject();

				if (extras != null) {
					for (String key : extras.keySet()) {
						info.put(key.toString(), extras.get(key).toString());
					}
				}
				callbackContext.success(info);
				theIntent.removeExtra("paymentResult");
			}
    	return false;
		}
}
