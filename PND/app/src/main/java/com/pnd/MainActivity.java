package com.pnd;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * Created by Dan Lee Jo on 11/15/15.
 */
public class MainActivity extends Activity implements View.OnClickListener {
    EditText mEditText;
    Button mButton;

    private int generateQRDimensions() {
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;

        return smallerDimension;
    }

    /************
     * Overrides
     ************/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mButton = (Button) findViewById(R.id.generateQRButton);
        mButton.setOnClickListener(this);

        mEditText = (EditText) findViewById(R.id.urlEditText);
    }

    /**
     * Activity onClick
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.generateQRButton:
                if (mEditText == null) {
                    return;
                }
                String url = mEditText.getText().toString();
                int qrDimensions = generateQRDimensions();

                try {
                    //Encode with a QR Code image
                    QRCodeWriter writer = new QRCodeWriter();
                    BitMatrix bm = writer.encode(url, BarcodeFormat.QR_CODE, qrDimensions, qrDimensions);
                    Bitmap bitmap = Bitmap.createBitmap(qrDimensions, qrDimensions, Bitmap.Config.ARGB_8888);
                    for (int i = 0; i < qrDimensions; i++) {
                        for (int j = 0; j < qrDimensions; j++) {
                            bitmap.setPixel(i, j, bm.get(i, j) ? Color.BLACK : Color.WHITE);
                        }
                    }

                    ImageView myImage = (ImageView) findViewById(R.id.imageView);
                    myImage.setImageBitmap(bitmap);

                } catch (WriterException e) {
                    e.printStackTrace();
                }

                break;
        }
    }
}
