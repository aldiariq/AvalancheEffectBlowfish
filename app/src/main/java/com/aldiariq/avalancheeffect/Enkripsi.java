package com.aldiariq.avalancheeffect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aldiariq.avalancheeffect.algoritma.Blowfish;
import com.aldiariq.avalancheeffect.utils.FileUtils;

public class Enkripsi extends AppCompatActivity {

    private Button btnPilihfile, btnEnkripsifile;
    private TextView txtLokasifile, txtHasilavalanche, txtLokasihasilenkrip;
    private EditText etPasswordblowfish;

    private static final int MY_REQUEST_CODE_PERMISSION = 1000;
    private static final int MY_RESULT_CODE_FILECHOOSER = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enkripsi);

        this.initView();

        this.btnPilihfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(Enkripsi.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Enkripsi.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    pilihFile();
                }else {
                    deteksiPermissionandroid();
                }
            }
        });

        this.btnEnkripsifile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwordblowfish = etPasswordblowfish.getText().toString().trim();
                String lokasifileinput = txtLokasifile.getText().toString().trim();
                String lokasifileoutput = lokasifileinput + ".enc";

                Blowfish algoritmaBlowfish = new Blowfish(passwordblowfish);
                algoritmaBlowfish.encrypt(lokasifileinput, lokasifileoutput);

                txtLokasihasilenkrip.setText(lokasifileoutput);

                Intent halamanUtama = new Intent(Enkripsi.this, MainActivity.class);
                startActivity(halamanUtama);
            }
        });
    }

    private void initView(){
        this.btnPilihfile = (Button) findViewById(R.id.btnPilihfileenkripsi);
        this.btnEnkripsifile = (Button) findViewById(R.id.btnEnkripsienkripsi);
        this.txtLokasifile = (TextView) findViewById(R.id.txtLokasifileenkripsi);
        this.txtHasilavalanche = (TextView) findViewById(R.id.txtHasilavalancheenkripsi);
        this.txtLokasihasilenkrip = (TextView) findViewById(R.id.txtLokasifilehasilenkripsi);
        this.etPasswordblowfish = (EditText) findViewById(R.id.etPasswordblowfishenkripsi);
    }

    private void pilihFile() {
        String[] tipeFile =
                {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                        "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation",
                        "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                        "text/plain",
                        "application/pdf"};

        Intent intentPilihfile = new Intent(Intent.ACTION_GET_CONTENT);
        intentPilihfile.addCategory(Intent.CATEGORY_OPENABLE);
        intentPilihfile.setType("*/*");
        intentPilihfile.putExtra(Intent.EXTRA_MIME_TYPES, tipeFile);
        startActivityForResult(Intent.createChooser(intentPilihfile,"Pilih File"), MY_RESULT_CODE_FILECHOOSER);
    }

    private void deteksiPermissionandroid() {
        // With Android Level >= 23, you have to ask the user
        // for permission to access External Storage.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) { // Level 23

            // Check if we have read storage permission
            int pemissionread = ActivityCompat.checkSelfPermission(Enkripsi.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);

            if (pemissionread != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                this.requestPermissions(
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_REQUEST_CODE_PERMISSION
                );
                return;
            }

            //check if we have write storage permission
            int permissionwrite = ActivityCompat.checkSelfPermission(Enkripsi.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permissionwrite != PackageManager.PERMISSION_GRANTED){
                this.requestPermissions(
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_REQUEST_CODE_PERMISSION
                );
                return;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_REQUEST_CODE_PERMISSION: {

                // Note: If request is cancelled, the result arrays are empty.
                // Permissions granted (CALL_PHONE).
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(Enkripsi.this, "Akses File Diizinkan", Toast.LENGTH_SHORT).show();
                }
                // Cancelled or denied.
                else {
                    Toast.makeText(Enkripsi.this, "Akses File Tidak Diizinkan", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MY_RESULT_CODE_FILECHOOSER:
                if (resultCode == Activity.RESULT_OK ) {
                    if(data != null)  {
                        Uri fileUri = data.getData();

                        String filePath = null;
                        try {
                            filePath = FileUtils.getPath(Enkripsi.this,fileUri);
                        } catch (Exception e) {
                            Toast.makeText(Enkripsi.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                        }
                        this.txtLokasifile.setText(filePath);
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}