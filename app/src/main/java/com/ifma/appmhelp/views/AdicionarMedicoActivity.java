package com.ifma.appmhelp.views;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.ifma.appmhelp.R;
import com.ifma.appmhelp.enums.BundleKeys;
import com.ifma.appmhelp.models.Paciente;

public class AdicionarMedicoActivity extends AppCompatActivity {

    private ImageView imgQrCode;
    private Paciente paciente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_medico);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.carregaComponentes();
        this.exibeQRCode();

    }

    private void carregaComponentes(){
        this.imgQrCode = (ImageView) findViewById(R.id.imgQrCode);
        this.paciente = (Paciente) this.getIntent().getSerializableExtra(BundleKeys.PACIENTE.getValue());
    }

    private void exibeQRCode(){
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int altura  = metrics.heightPixels/2;
        int largura = metrics.widthPixels;

        try {
            BitMatrix bitMatrix = new QRCodeWriter().encode(this.paciente.toJson(), BarcodeFormat.QR_CODE,largura,altura);
            Bitmap bmp = Bitmap.createBitmap(largura, altura, Bitmap.Config.RGB_565);
            for (int i = 0; i < largura; i ++)
                for (int j = 0; j < altura; j ++)
                    bmp.setPixel(i, j, bitMatrix.get(i,j) ? Color.BLACK : Color.WHITE);

            imgQrCode.setImageBitmap(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

}
