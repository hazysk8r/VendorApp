package com.stepbystep.bossapp.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.stepbystep.bossapp.DO.AdminFood;
import com.stepbystep.bossapp.R;

import java.util.ArrayList;


public class MenuManageFragment extends Fragment {

    private View view;
    String truckId;

    private RecyclerView MenuRecycler;
    private MenuAdapter menuAdapter;
    private FloatingActionButton floatingActionButton;
    private ArrayList<AdminFood> foods;
    private DatabaseReference databaseReference;
    private ProgressBar bar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu_manage,container,false);

        bar = view.findViewById(R.id.foodProgressBar);

        MenuRecycler = (RecyclerView)view.findViewById(R.id.foodsRecycler);
        floatingActionButton = (FloatingActionButton)view.findViewById(R.id.foodsFloatingBtnId);

        truckId = getArguments().getString("truck");
        databaseReference = FirebaseDatabase.getInstance().getReference("FoodTruck").child("Food").child(truckId);
        foods = new ArrayList<>();

        String key = databaseReference.push().getKey();

        menuAdapter = new MenuAdapter(getActivity(), foods);
        MenuRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        MenuRecycler.setAdapter(menuAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foods.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()) //메뉴
                {
                    foods.add(new AdminFood(snapshot1.getKey(),
                            snapshot1.child("name").getValue(String.class),
                            snapshot1.child("content").getValue(String.class),
                            snapshot1.child("image").getValue(String.class) ,
                            snapshot1.child("cost").getValue(String.class)));
                }
                menuAdapter.notifyDataSetChanged();
                bar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "에러 발생", Toast.LENGTH_LONG).show();
                bar.setVisibility(View.INVISIBLE);
            }
        });

        menuAdapter.setOnItemClickListener(new MenuAdapter.onItemClickListener() { //클릭 시 메뉴편집 화면으로
            @Override
            public void onItemClick(int pos) {
                Intent i = new Intent(getActivity() , EditMenuActivity.class);
                Bundle b = new Bundle();
                b.putString("foodkey", foods.get(pos).getfKey());
                b.putString("img" , foods.get(pos).getImage());
                b.putString("name" , foods.get(pos).getName());
                b.putString("description" , foods.get(pos).getContent());
                b.putString("price" , foods.get(pos).getCost());
                b.putString("truck", truckId);
                i.putExtras(b);
                startActivity(i);
            }
        });

        menuAdapter.setOnLongClickListener(new MenuAdapter.onLongClickListener() { //길게 클릭 시 메뉴 삭제 절차
            @Override
            public void onItemLongClick(int pos) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity()).setTitle("확인절차").setMessage("정말 삭제하시겠습니까?!").setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference reference = databaseReference.child(foods.get(pos).getfKey());
                        reference.removeValue();
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference("Menu").child(foods.get(pos).getName() + ".jpg");
                        storageReference.delete();
                        StorageReference storageReference1 = FirebaseStorage.getInstance().getReference("Menu").child(foods.get(pos).getName());
                        storageReference1.delete();
                    }
                }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setIcon(android.R.drawable.ic_dialog_alert);
                dialog.show();
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() { //버튼 클릭 시 메뉴 추가 화면으로
            @Override
            public void onClick(View view) {
                //메뉴 등록 버튼
                startActivity(new Intent(getActivity(),AddMenuActivity.class));

            }
        });


        return view;
    }

}