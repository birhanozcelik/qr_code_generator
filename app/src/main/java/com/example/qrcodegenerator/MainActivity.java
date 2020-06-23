package com.example.qrcodegenerator;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    String gelenIsim,gelenSoy,gelenTc = "";
    @BindView(R.id.isim)
    EditText isim;
    @BindView(R.id.soy_isim)
    EditText soy_isim;

    @BindView(R.id.tc_kimlik_no)
    EditText tc_kimlik_no;

    @BindView(R.id.imageView)
    ImageView qrImage;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedpreferences = getPreferences(Context.MODE_PRIVATE);
        String imageS = sharedpreferences.getString("imagePreferance", "");
        gelenIsim = sharedpreferences.getString("isim","");
        gelenSoy = sharedpreferences.getString("soy_isim", "");
        gelenTc = sharedpreferences.getString("tc","");

        System.out.println("IMAGEEEE SSSSSSSS "+imageS);

        ButterKnife.bind(this);
        if (!gelenIsim.equals("")){
            isim.setText(gelenIsim);
        }
        if (!gelenSoy.equals("")){
            soy_isim.setText(gelenSoy);
        }
        if(!gelenTc.equals("")){
            tc_kimlik_no.setText(gelenTc);
        }
        if(!imageS.equals("")) {
            Bitmap imageB;
            imageB = decodeToBase64(imageS);
            qrImage.setImageBitmap(imageB);
            qrImage.setVisibility(View.VISIBLE);
        }

    }

    private void qr_uret(){

        SharedPreferences.Editor editor = sharedpreferences.edit();
        String name = "Isim : "+isim.getText().toString();
        String soy_isim1 = "Soy isim : "+soy_isim.getText().toString();
        String tc = "TC : "+tc_kimlik_no.getText().toString();
        editor.putString("isim",name);
        editor.putString("soy_isim",soy_isim1);
        editor.putString("tc",tc);
        editor.commit();
        StringBuilder textGonder = new StringBuilder();
        textGonder.append(name+" | "+soy_isim1+" | "+tc);
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try{
            BitMatrix bitMatrix = multiFormatWriter.encode(textGonder.toString(), BarcodeFormat.QR_CODE,600,600);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

            editor.putString("imagePreferance", encodeToBase64(bitmap));
            editor.commit();

            qrImage.setImageBitmap(bitmap);
            qrImage.setVisibility(View.VISIBLE);
        }catch (WriterException e){
            e.printStackTrace();
        }
    }
    public static String encodeToBase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        //Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }
    public static Bitmap decodeToBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    @OnClick(R.id.generate_button)
    public void onViewClicked(){
        qr_uret();
    }
}