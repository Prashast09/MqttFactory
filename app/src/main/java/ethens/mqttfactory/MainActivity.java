package ethens.mqttfactory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;



public class MainActivity extends AppCompatActivity {

  MqttHelper mqttHelper;
  Button button;
  TextView dataReceived;
  EditText editText,channel;
  @Override

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    dataReceived = (TextView) findViewById(R.id.dataReceived);
    button = (Button) findViewById(R.id.submitText);
    editText = (EditText)findViewById(R.id.editText);
    channel = (EditText) findViewById(R.id.channel);

    startMqtt();

  }

  private void startMqtt(){
    mqttHelper = new MqttHelper(getApplicationContext());
    mqttHelper.mqttAndroidClient.setCallback(new MqttCallbackExtended() {
      @Override
      public void connectComplete(boolean b, String s) {
        Log.w("Debug","Connected");
      }

      @Override
      public void connectionLost(Throwable throwable) {

      }

      @Override
      public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        Log.w("Debug",mqttMessage.toString());
        dataReceived.setText(mqttMessage.toString());
      }

      @Override
      public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

      }
    });
    button.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mqttHelper.sendMessageFromAndroid(editText.getText().toString(),channel.getText().toString());
      }
    });


  }


}
