package com.stepbystep.bossapp.home;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import com.stepbystep.bossapp.DO.StoreAccount;
import com.stepbystep.bossapp.R;

public class AddMenuActivity extends AppCompatActivity {
    private TextInputEditText name, description, cost;
    private Button add , choose;
    private ImageView img;
    private Uri imgUri;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;
    private TextInputLayout nameTextInputLayout , cntTextInputLayout, costTextInputLayout;
    public static final int GALLERY_PICK = 1;

    DatabaseReference databaseReference;
    DatabaseReference foodRef;
    String truckId;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);

        ActionBar abar = getSupportActionBar();
        abar.hide();

        name = findViewById(R.id.editTextFoodName);
        description = findViewById(R.id.editTextFoodDescription);
        cost = findViewById(R.id.editTextFoodPrice);
        add = findViewById(R.id.btnAdd);
        choose = findViewById(R.id.btnChooseImg);
        img = findViewById(R.id.imgFood);
        nameTextInputLayout = findViewById(R.id.editTextFoodNameLayout);
        cntTextInputLayout = findViewById(R.id.editTextFoodDescriptionLayout);
        costTextInputLayout = findViewById(R.id.editTextFoodPriceLayout);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        mStorageRef = FirebaseStorage.getInstance().getReference("Menu");

        nameTextInputLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(name.getText().toString().trim().isEmpty())
                {
                    nameTextInputLayout.setErrorEnabled(true);
                    nameTextInputLayout.setError("메뉴 이름을 적어주세요");
                }
                else
                {
                    nameTextInputLayout.setErrorEnabled(false);
                }
            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (name.getText().toString().trim().isEmpty()) {
                    nameTextInputLayout.setErrorEnabled(true);
                    nameTextInputLayout.setError("메뉴 이름을 적어주세요");
                } else {
                    nameTextInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        cntTextInputLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(description.getText().toString().trim().isEmpty())
                {
                    cntTextInputLayout.setErrorEnabled(true);
                    cntTextInputLayout.setError("메뉴 설명을 적어주세요");
                }
                else
                {
                    cntTextInputLayout.setErrorEnabled(false);
                }
            }
        });

        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (description.getText().toString().trim().isEmpty()) {
                    cntTextInputLayout.setErrorEnabled(true);
                    cntTextInputLayout.setError("메뉴 설명을 적어주세요");
                } else {
                    cntTextInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        costTextInputLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(cost.getText().toString().trim().isEmpty())
                {
                    costTextInputLayout.setErrorEnabled(true);
                    costTextInputLayout.setError("가격을 적어주세요");
                }
                else
                {
                    costTextInputLayout.setErrorEnabled(false);
                }
            }
        });

        cost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (cost.getText().toString().trim().isEmpty()) {
                    costTextInputLayout.setErrorEnabled(true);
                    costTextInputLayout.setError("가격을 적어주세요");
                } else {
                    costTextInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUploadTask != null && mUploadTask.isInProgress())
                    Toast.makeText(AddMenuActivity.this, "업로드 중 입니다", Toast.LENGTH_SHORT).show();
                else if (name.getText().toString().isEmpty() || description.getText().toString().isEmpty() || cost.getText().toString().isEmpty() || imgUri == null){
                    Toast.makeText(AddMenuActivity.this, "입력되지 않은 곳이 있습니다", Toast.LENGTH_SHORT).show();
                }
                else{
                    uploadData();
                    finish();
                }

            }
        });
    }

    public void uploadData() {
        if (description.getText().toString().isEmpty() || cost.getText().toString().isEmpty() || imgUri == null || name.getText().toString().isEmpty()) {
            Toast.makeText(AddMenuActivity.this, "입력되지 않은 곳이 있습니다", Toast.LENGTH_SHORT).show();
        } else {
            uploadImage();
        }
    }

    public void uploadImage() {
        if (imgUri != null) {
            StorageReference fileReference = mStorageRef.child(name.getText().toString() + "." + getFileExtension(imgUri));
            mUploadTask = fileReference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful()) ;
                    Uri downloadUrl = urlTask.getResult();
                    Food food = new Food(cost.getText().toString().trim(),
                            name.getText().toString().trim(),
                            downloadUrl.toString(),
                            description.getText().toString().trim());
                    databaseReference = FirebaseDatabase.getInstance().getReference("BossApp");
                    databaseReference.child("StoreAccount").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()) {
                                StoreAccount storeAccount = snapshot.getValue(StoreAccount.class);
                                truckId = storeAccount.getTruckId();
                                foodRef = FirebaseDatabase.getInstance().getReference("FoodTruck").child("Food").child(truckId);
                                String foodKey = foodRef.push().getKey();
                                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("FoodTruck")
                                        .child("Food")
                                        .child(truckId);
                                myRef.child(foodKey).setValue(food).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(AddMenuActivity.this, "메뉴 등록 성공", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddMenuActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
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
