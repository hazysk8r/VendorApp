package com.stepbystep.bossapp.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.stepbystep.bossapp.DO.Food;
import com.stepbystep.bossapp.R;

public class EditMenuActivity extends AppCompatActivity {
    private TextInputEditText name, description, price;
    private Button edit, choose;
    private ImageView img;
    private Uri imgUri;
    private String SelectedName, SelectedImagePath, selectedKey;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;
    private byte[] SelectedImageBytes;
    public static final int GALLERY_PICK = 1;
    String truckId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu);

        ActionBar abar = getSupportActionBar();
        abar.hide();

        name = findViewById(R.id.editTextProductNameEdit);
        edit = findViewById(R.id.btnAddEdit);
        choose = findViewById(R.id.btnChooseImgEdit);
        img = findViewById(R.id.imgProductEdit);
        price = findViewById(R.id.editTextProductPriceEdit);
        description = findViewById(R.id.editTextProductCntEdit);

        Bundle b = getIntent().getExtras();
        name.setText(b.getString("name"));
        selectedKey = b.getString("foodkey");
        SelectedName = b.getString("name");
        description.setText(b.getString("description"));
        Picasso.get().load(b.getString("img")).fit().centerCrop().into(img);
        price.setText(b.getString("price"));
        imgUri = Uri.parse(b.getString("img"));
        SelectedImagePath = b.getString("img");
        truckId = b.getString("truck");

        mStorageRef = FirebaseStorage.getInstance().getReference("Menu");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("FoodTruck").child("Food");

        StorageReference selectedImgRef = FirebaseStorage.getInstance().getReferenceFromUrl(SelectedImagePath);
        final long ONE_MEGABYTE = 1024 * 1024 * 10; //다운로드할 최대 바이트 수
        selectedImgRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override //이전에 선택한 사진이 들어있는 폴더에서 선택했던 사진 데이터 반환
            public void onSuccess(byte[] bytes) {
                SelectedImageBytes = bytes;
            }
        });

        choose.setOnClickListener(new View.OnClickListener() {
            @Override //새로운 사진 선택
            public void onClick(View view) {
                openImage();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override //내용 모두 편집이후 업로드
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress())
                    Toast.makeText(EditMenuActivity.this, "업로드 중 입니다.", Toast.LENGTH_SHORT).show();
                else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(EditMenuActivity.this).setTitle("확인절차").setMessage("저장하시겠습니까?").setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteImage();
                            deleteData();
                            uploadData();
                            finish();
                        }
                    }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert);
                    dialog.show();
                }
            }
        });
    }

    public void deleteData() {
        DatabaseReference ref = mDatabaseRef.child(truckId);
        ref.orderByChild("name").equalTo(SelectedName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    dataSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void deleteImage() {
        if (imgUri.toString().equals(SelectedImagePath)) {
            StorageReference nameRef = mStorageRef.child(SelectedName);
            nameRef.delete();
        }else
        {
            StorageReference jpgRef = mStorageRef.child(SelectedName + ".jpg");
            jpgRef.delete();
        }
    }

    public void uploadData() {
        if (name.getText().toString().isEmpty() || description.getText().toString().isEmpty() || price.getText().toString().isEmpty() || imgUri == null) {
            Toast.makeText(EditMenuActivity.this, "입력되지 않은 곳이 있습니다", Toast.LENGTH_SHORT).show();
        } else {
            uploadImage();
        }
    }

    public void uploadImage() {
        if (imgUri.toString().equals(SelectedImagePath)) {
            StorageReference fileReference = mStorageRef.child(name.getText().toString());
            mUploadTask = fileReference.putBytes(SelectedImageBytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful()) ;
                    Uri downloadUrl = urlTask.getResult();
                    Food food = new Food(price.getText().toString().trim(), name.getText().toString().trim(), downloadUrl.toString(), description.getText().toString().trim());
                    DatabaseReference truckDataRef = mDatabaseRef.child(truckId);
                    truckDataRef.child(selectedKey).setValue(food);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditMenuActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else if (imgUri != null) {
            StorageReference fileReference = mStorageRef.child(name.getText().toString() + "." + getFileExtension(imgUri));
            mUploadTask = fileReference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful()) ;
                    Uri downloadUrl = urlTask.getResult();
                    Food food = new Food(price.getText().toString().trim(),
                            name.getText().toString().trim(),
                            downloadUrl.toString(),
                            description.getText().toString().trim());
                    DatabaseReference FoodRef = FirebaseDatabase.getInstance().getReference("FoodTruck")
                            .child("Food")
                            .child(truckId)
                            .child(selectedKey);
                    FoodRef.setValue(food);
                    Toast.makeText(EditMenuActivity.this, "메뉴 수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditMenuActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void openImage() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, GALLERY_PICK);
    }

    public String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == Activity.RESULT_OK && data.getData() != null && data != null) {
            imgUri = data.getData();
            try {
                Picasso.get().load(imgUri).fit().centerCrop().into(img);
            } catch (Exception e) {
                Log.e(this.toString(), e.getMessage().toString());
            }

        }
    }
}
