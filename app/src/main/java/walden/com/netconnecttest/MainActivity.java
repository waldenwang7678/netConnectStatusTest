package walden.com.netconnecttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private netBrocastReceiver receiver;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initReceiver();
    }

    private void initView() {
        tv = (TextView) findViewById(R.id.tv);
    }

    private void initReceiver() {
        receiver = new netBrocastReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, filter);
    }

    class netBrocastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (!mobNetInfo.isConnected()) {
                if (!wifiNetInfo.isConnected()) {
                    tv.setVisibility(View.VISIBLE);
                    tv.setText("移动网络和wifi都没连上");
                } else {
                    tv.setText("wifi连上");
                    tv.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "wifi连上", Toast.LENGTH_SHORT).show();
                }
            } else if (mobNetInfo.isConnected()) {
                tv.setVisibility(View.GONE);
                tv.setText("移动网络已连上");
                Toast.makeText(MainActivity.this, "移动网络已连上", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }
}
