package com.aldiariq.avalancheeffect;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.aldiariq.avalancheeffect.algoritma.Blowfish;
import com.aldiariq.avalancheeffect.utils.FileUtils;

public class Dekripsi extends AppCompatActivity {

    private Button btnPilihfile, btnDekripsifile;
    private TextView txtLokasifile, txtWaktudekripsi, txtLokasihasildekrip;
    private EditText etPasswordblowfish;

    private static final int MY_REQUEST_CODE_PERMISSION = 1000;
    private static final int MY_RESULT_CODE_FILECHOOSER = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dekripsi);

        this.initView();

        this.btnPilihfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(Dekripsi.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Dekripsi.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    pilihFile();
                }else {
                    deteksiPermissionandroid();
                }
            }
        });

        this.btnDekripsifile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwordblowfish = etPasswordblowfish.getText().toString().trim();
                String fileinput = txtLokasifile.getText().toString().trim();
                String fileoutput = fileinput.replace(".enc", "");

                long waktumulai = System.currentTimeMillis();

                Blowfish algoritmaBlowfish = new Blowfish(passwordblowfish);
                algoritmaBlowfish.decrypt(fileinput, fileoutput);

                long waktuselesai = System.currentTimeMillis();

                txtWaktudekripsi.setText("Lama Proses : " + algoritmaBlowfish.hitunglamaProses(waktumulai, waktuselesai) + "ms");

                txtLokasihasildekrip.setText(fileoutput);

                algoritmaBlowfish.deleteFile(fileinput);
            }
        });
    }

    private void initView(){
        this.btnPilihfile = (Button) findViewById(R.id.btnPilihfiledekripsi);
        this.btnDekripsifile = (Button) findViewById(R.id.btnDekripsidekripsi);
        this.txtLokasifile = (TextView) findViewById(R.id.txtLokasifiledekripsi);
        this.txtWaktudekripsi = (TextView) findViewById(R.id.txtWaktudekripsi);
        this.txtLokasihasildekrip = (TextView) findViewById(R.id.txtLokasifilehasildekripsi);
        this.etPasswordblowfish = (EditText) findViewById(R.id.etPasswordblowfishdekripsi);
    }

    private void pilihFile() {
        String[] tipeFile = {"*/*"};

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
            int pemissionread = ActivityCompat.checkSelfPermission(Dekripsi.this,
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
            int permissionwrite = ActivityCompat.checkSelfPermission(Dekripsi.this,
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
                    Toast.makeText(Dekripsi.this, "Akses File Diizinkan", Toast.LENGTH_SHORT).show();
                }
                // Cancelled or denied.
                else {
                    Toast.makeText(Dekripsi.this, "Akses File Tidak Diizinkan", Toast.LENGTH_SHORT).show();
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
                            filePath = FileUtils.getPath(Dekripsi.this,fileUri);
                        } catch (Exception e) {
                            Toast.makeText(Dekripsi.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                        }
                        this.txtLokasifile.setText(filePath);
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}