package ethens.mqttfactory;

import android.content.Context;
import android.util.Log;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by ethens on 09/11/17.
 */

public class MqttHelper {
  public MqttAndroidClient mqttAndroidClient;

  final String serverUri = "tcp://m23.cloudmqtt.com:13159";

  final String clientId = "clientId-Q8iJfi9e5f";
  final String subscriptionTopic = "#";

  final String username = "oyjjwuno";
  final String password = "awmuOvY-4ndl";

  Context mContext;

  public MqttHelper(Context context){
    mContext = context;
    mqttAndroidClient = new MqttAndroidClient(context, serverUri, clientId);
    mqttAndroidClient.setCallback(new MqttCallbackExtended() {
      @Override
      public void connectComplete(boolean b, String s) {
        Log.w("mqtt", s);
      }

      @Override
      public void connectionLost(Throwable throwable) {

      }

      @Override
      public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        Log.w("Mqtt", mqttMessage.toString());
      }

      @Override
      public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

      }
    });
    connect(context);
  }

  private void connect(final Context context){
    MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
    mqttConnectOptions.setAutomaticReconnect(true);
    mqttConnectOptions.setCleanSession(false);
    mqttConnectOptions.setUserName(username);
    mqttConnectOptions.setPassword(password.toCharArray());

    try {

      mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {

          DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
          disconnectedBufferOptions.setBufferEnabled(true);
          disconnectedBufferOptions.setBufferSize(100);
          disconnectedBufferOptions.setPersistBuffer(false);
          disconnectedBufferOptions.setDeleteOldestMessages(false);
          mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
          subscribeToTopic(context);
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
          Log.w("Mqtt", "Failed to connect to: " + serverUri + exception.toString());
          exception.printStackTrace();
        }
      });


    } catch (MqttException ex){
      ex.printStackTrace();
    }
  }

  public void sendMessageFromAndroid(String message,String channel){
    MqttMessage mqttMessage = new MqttMessage(("ANDROID sent :"+message).getBytes());
    mqttMessage.setQos(2);
    mqttMessage.setRetained(false);
    try {
      mqttAndroidClient.publish(channel, mqttMessage, mContext, new IMqttActionListener() {
        @Override public void onSuccess(IMqttToken asyncActionToken) {

        }

        @Override public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

        }
      });
    } catch (MqttException e) {
      e.printStackTrace();
    }
  }


  private void subscribeToTopic(Context context) {
    try {
      mqttAndroidClient.subscribe(subscriptionTopic, 0, context, new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
          Log.w("Mqtt","Subscribed!");
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
          Log.w("Mqtt", "Subscribed fail!");
        }
      });

    } catch (MqttException ex) {
      System.err.println("Exception whilst subscribing");
      ex.printStackTrace();
    }
  }


}
