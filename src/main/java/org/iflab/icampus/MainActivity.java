package org.iflab.icampus;

import android.support.v7.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import org.iflab.icampus.oauth.AuthorizationCodeHandle;
import org.iflab.icampus.oauth.GetAccessToken;


public class MainActivity extends ActionBarActivity {

    private static final int GET_AUTHO_RIZATIONCODE = 1;//OAuth认证的requestCode
    private Button login;
    private Button logout;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = (Button) findViewById(R.id.button);
        logout = (Button) findViewById(R.id.button2);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OAuthActivity.class);
                startActivityForResult(intent, GET_AUTHO_RIZATIONCODE);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,PersonalActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GET_AUTHO_RIZATIONCODE:
                if (resultCode == RESULT_OK) {
                    String authorizationCode = data.getStringExtra("result");
                    System.out.println("authorizationCode:   " + authorizationCode);
                    AuthorizationCodeHandle.saveAuthorizationCode(MainActivity.this, authorizationCode);
                    GetAccessToken.getAccessToken(MainActivity.this, authorizationCode);
                } else {
                    Toast.makeText(MainActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}