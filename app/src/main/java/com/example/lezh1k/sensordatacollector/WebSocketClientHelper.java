package com.example.lezh1k.sensordatacollector;

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

public class WebSocketClientHelper {
    private static final String TAG = "WebSocketClientHelper";
    private WebSocketClient mWebSocketClient;

    public void open(String websocketUrl) {
        URI uri;
        try {
            uri = new URI(websocketUrl);
        } catch (URISyntaxException e) {
            Log.e(TAG, "URI Syntax Exception");
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i(TAG, "Opened");
            }

            @Override
            public void onMessage(String s) {
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i(TAG, "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
    }

    public void send(double latitude, double longitude, float speed, float accuracy, long timestamp) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", "gps");
            obj.put("value", String.format(Locale.ENGLISH, "%f,%f", latitude, longitude));
            obj.put("timestamp", timestamp);
            obj.put("speed", speed * 3.6);
            obj.put("accuracy", accuracy);
            obj.put("provider", "mad");
            String json = obj.toString();

            if (mWebSocketClient.isOpen()) {
                mWebSocketClient.send(json + "\n");
            }

        } catch (JSONException e) {
            Log.e(TAG, "Json Exception : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void close() {
        mWebSocketClient.close();
    }
}
