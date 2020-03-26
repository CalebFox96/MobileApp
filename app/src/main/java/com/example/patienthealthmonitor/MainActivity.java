package com.example.patienthealthmonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity implements MqttCallback {
    public MqttAndroidClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String clientId = MqttClient.generateClientId();
        client =
                new MqttAndroidClient(this.getApplicationContext(), "tcp://10.13.3.124:1883",
                        clientId);
        //setting up mqttclient object, we need to give mqtt broker ip which is 192.168.0.59 (raspberry pi ip)
        //you can check ip on pi using ifconfig command and 1883 is port
        MqttConnectOptions options = new MqttConnectOptions();
        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);

        try {
            IMqttToken token = client.connect(options);

            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d("connection", "onSuccess");
                    client.setCallback(MainActivity.this);
                    final String topic = "hum";
                    int qos = 1;
                    try {
                        IMqttToken subToken = client.subscribe(topic, qos); //subscribing hum for humidity
                        client.subscribe("temp",1);//subscribing temp for temp
                        client.subscribe("bpm",1);//subscribing bpm for bpm
                        client.subscribe("bp",1);//subscribing bp for bp
                        subToken.setActionCallback(new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                // successfully subscribed
                                Toast.makeText(MainActivity.this, "Successfully subscribed to: " + topic, Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken,
                                                  Throwable exception) {
                                // The subscription could not be performed, maybe the user was not
                                // authorized to subscribe on the specified topic e.g. using wildcards
                                Toast.makeText(MainActivity.this, "Couldn't subscribe to: " + topic, Toast.LENGTH_SHORT).show();

                            }
                        });
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }


                    catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d("HomeAutomation", "onFailure");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

        /*
         * To test ,publish  "open"/"close" at topic you subscibed app to in above .
         * */
        if(topic.equals("temp")) { //if temp value arrives
            TextView val = (TextView) findViewById(R.id.t);

            val.setText(message.toString() + " °C"); //put temp val on id t (text view)
        }
        if(topic.equals("hum")) { //if hum value arrives
            TextView val2 = (TextView) findViewById(R.id.h);

            val2.setText(message.toString() + " %"); //put temp val on id h(text view)
        }
        if(topic.equals("bpm")) {//if bpm value arrives
            TextView val3 = (TextView) findViewById(R.id.bpm);

            val3.setText(message.toString());//put temp val on id bpm (text view)
        }
        if(topic.equals("bp")) {//if bp value arrives
            TextView val4 = (TextView) findViewById(R.id.bp);//put temp val on id bp (text view)

            val4.setText(message.toString());
        }


    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}
