package com.cjstar.hugeimageload.hugelocalimageload;
/*
 * Copyright (C) 2015 CJstar, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.cjstar.hugeimageload.brz.ImageLoaderUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView mImgView;
    Button mBtnLoad;

    final String strFromResource = "load image from Resource";
    final String strFromSDCard = "load image from SDCard";
    //at first : you should copy #R.drawable.hugeimage to SDCard at the flow path
    String path = Environment.getExternalStorageDirectory() + "/hugeimage/IMG_2411.jpg";
    int width;
    int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImgView = (ImageView) findViewById(R.id.image);
        mBtnLoad = (Button) findViewById(R.id.button);
        mBtnLoad.setOnClickListener(this);
        mBtnLoad.setText(strFromSDCard);
        WindowManager wm = getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
        mImgView.setImageBitmap(ImageLoaderUtils.loadHugeBitmapFromDrawable(getResources(), R.drawable.hugeimage, height, width));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                if (mBtnLoad.getText().equals(strFromResource)) {
                    mImgView.setImageBitmap(ImageLoaderUtils.loadHugeBitmapFromDrawable(getResources(), R.drawable.hugeimage, height, width));
                    mBtnLoad.setText(strFromSDCard);
                } else {
                    mBtnLoad.setText(strFromResource);
                    mImgView.setImageBitmap(ImageLoaderUtils.loadHugeBitmapFromSDCard(path, height, width));
                }
                break;
            default:
                break;
        }
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
