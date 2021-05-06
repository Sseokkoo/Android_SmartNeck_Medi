package com.ko.smartneck.medical.twofive.Member;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ko.smartneck.medical.twofive.GlobalApplication;
import com.ko.smartneck.medical.twofive.Main.BleConnectActivity;
import com.ko.smartneck.medical.twofive.R;
import com.ko.smartneck.medical.twofive.WebViewActivity;
import com.ko.smartneck.medical.twofive.util.Commend;
import com.ko.smartneck.medical.twofive.util.User.Admin;
import com.ko.smartneck.medical.twofive.util.User.Member;
import com.ko.smartneck.medical.twofive.Member.MemberAddActivity;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.ko.smartneck.medical.twofive.util.Constants.TAG;

public class MemberSelectActivity extends AppCompatActivity {
    //    ListView memberList;
    ImageView img_add;
    TextView tv_title, tv_sign_out_btn;
    ArrayList<Member> users, tmpUsers;
    RecyclerView memberList;
    MemberSelectAdapter adapter;
    Handler handler;
    EditText edt_search;
    Button btn_date_sort, btn_name_sort, btn_gender_sort;
    public static Admin admin;

    boolean isDateSort = true;
    boolean isNameSort = true;
    boolean isGenderSort = true;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_member_select_activity);
        init();


    }

    @Override
    protected void onResume() {
        super.onResume();
        BleConnectActivity.setMessage(new Commend().sendGoExercise((byte)0 , (byte)0));
        if (admin.getAccount() != null){
        getMemberList();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        edt_search.setText("");
    }

    private void init() {
        handler = new Handler();
        users = new ArrayList<>();
        tmpUsers = new ArrayList<>();

        btn_date_sort = findViewById(R.id.btn_member_select_date_sort);
        btn_name_sort = findViewById(R.id.btn_member_select_name_sort);
        btn_gender_sort = findViewById(R.id.btn_member_select_gender_sort);

        btn_date_sort.setBackgroundColor(Color.parseColor("#cc1b17"));
        btn_name_sort.setBackgroundColor(Color.parseColor("#ffffff"));
        btn_gender_sort.setBackgroundColor(Color.parseColor("#ffffff"));
        btn_date_sort.setTextColor(Color.parseColor("#ffffff"));
        btn_name_sort.setTextColor(Color.parseColor("#cc1b17"));
        btn_gender_sort.setTextColor(Color.parseColor("#cc1b17"));

        tv_title = findViewById(R.id.tv_member_select_title);
        img_add = findViewById(R.id.img_member_select_add);
        memberList = findViewById(R.id.recyclerview_member_select);
        edt_search = findViewById(R.id.edt_member_select_search);
        tv_sign_out_btn = findViewById(R.id.tv_member_select_sign_out_btn);


        final InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edt_search.getWindowToken(), 0);

        memberList.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));


        String type = "";
        String select = getString(R.string.word_select);


        type = getString(R.string.word_patient);


        tv_title.setText(type + " " + select);
        setListener();


        // TODO: 2020-03-17 테스트 후 삭제
//        tv_title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext() , ProtocolTestActivity.class);
//                startActivity(intent);
//            }
//        });
    }


// Collections.sort 로 comparator 를 주어서 데이터를 정렬 시킨다.
//Collections.sort(myArrayData ,myComparator);


    private void setListener() {

        btn_date_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_date_sort.setBackgroundColor(Color.parseColor("#cc1b17"));
                btn_name_sort.setBackgroundColor(Color.parseColor("#ffffff"));
                btn_gender_sort.setBackgroundColor(Color.parseColor("#ffffff"));
                btn_date_sort.setTextColor(Color.parseColor("#ffffff"));
                btn_name_sort.setTextColor(Color.parseColor("#cc1b17"));
                btn_gender_sort.setTextColor(Color.parseColor("#cc1b17"));
                if (isDateSort) {
                    //날짜 내림차
                    Comparator<Member> userSort = new Comparator<Member>() {
                        private final Collator collator = Collator.getInstance();

                        @Override
                        public int compare(Member o1, Member o2) {
                            return collator.compare(o1.getLately(), o2.getLately());
                        }
                    };
                    Collections.sort(users, userSort);


                    adapter.notifyDataSetChanged();
                    isDateSort = false;
                } else {
                    //날짜 오름차
                    Comparator<Member> userSort = new Comparator<Member>() {
                        private final Collator collator = Collator.getInstance();

                        @Override
                        public int compare(Member o1, Member o2) {
                            return collator.compare(o1.getLately(), o2.getLately());
                        }
                    };
                    Collections.sort(users, userSort);
                    Collections.reverse(users);


                    adapter.notifyDataSetChanged();
                    isDateSort = true;
                }


            }
        });

        btn_name_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_date_sort.setBackgroundColor(Color.parseColor("#ffffff"));
                btn_name_sort.setBackgroundColor(Color.parseColor("#cc1b17"));
                btn_gender_sort.setBackgroundColor(Color.parseColor("#ffffff"));
                btn_date_sort.setTextColor(Color.parseColor("#cc1b17"));
                btn_name_sort.setTextColor(Color.parseColor("#ffffff"));
                btn_gender_sort.setTextColor(Color.parseColor("#cc1b17"));
                if (isNameSort) {
                    //날짜 내림차
                    Comparator<Member> userSort = new Comparator<Member>() {
                        private final Collator collator = Collator.getInstance();

                        @Override
                        public int compare(Member o1, Member o2) {
                            return collator.compare(o1.getName(), o2.getName());
                        }
                    };
                    Collections.sort(users, userSort);


                    adapter.notifyDataSetChanged();
                    isNameSort = false;
                } else {
                    //날짜 오름차
                    Comparator<Member> userSort = new Comparator<Member>() {
                        private final Collator collator = Collator.getInstance();

                        @Override
                        public int compare(Member o1, Member o2) {
                            return collator.compare(o1.getName(), o2.getName());
                        }
                    };
                    Collections.sort(users, userSort);
                    Collections.reverse(users);


                    adapter.notifyDataSetChanged();
                    isNameSort = true;
                }


            }
        });

        btn_gender_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_date_sort.setBackgroundColor(Color.parseColor("#ffffff"));
                btn_name_sort.setBackgroundColor(Color.parseColor("#ffffff"));
                btn_gender_sort.setBackgroundColor(Color.parseColor("#cc1b17"));
                btn_date_sort.setTextColor(Color.parseColor("#cc1b17"));
                btn_name_sort.setTextColor(Color.parseColor("#cc1b17"));
                btn_gender_sort.setTextColor(Color.parseColor("#ffffff"));

                if (isGenderSort) {
                    //성별 내림차
                    Comparator<Member> userSort = new Comparator<Member>() {
                        private final Collator collator = Collator.getInstance();

                        @Override
                        public int compare(Member o1, Member o2) {
                            return collator.compare(o1.getGender(), o2.getGender());
                        }
                    };
                    Collections.sort(users, userSort);


                    adapter.notifyDataSetChanged();
                    isGenderSort = false;
                } else {
                    //성별 오름차
                    Comparator<Member> userSort = new Comparator<Member>() {
                        private final Collator collator = Collator.getInstance();

                        @Override
                        public int compare(Member o1, Member o2) {
                            return collator.compare(o1.getGender(), o2.getGender());
                        }
                    };
                    Collections.sort(users, userSort);
                    Collections.reverse(users);


                    adapter.notifyDataSetChanged();
                    isGenderSort = true;
                }
            }
        });

        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MemberAddActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //앱종료
        tv_sign_out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getApplicationContext() , WebViewActivity.class);
                startActivity(intent);


//                Admin.setAutologinState(MemberSelectActivity.this , false);
//                Intent intent = new Intent(getApplicationContext() , LoginActivity.class);
//                startActivity(intent);
//                finish();


//                moveTaskToBack(true);
//                bleActivity.finish();
//                finish();

//                android.os.Process.killProcess(android.os.Process.myPid());
//                System.exit(0);
            }
        });
        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                users.clear();
                for (int i = 0; i < tmpUsers.size(); i++) {
                    if (tmpUsers.get(i).getName().contains(s.toString()) || tmpUsers.get(i).getPhone().contains(s.toString())) {

                        users.add(new Member(tmpUsers.get(i).getMemberNo() , tmpUsers.get(i).getAdmin() , tmpUsers.get(i).getUid(), tmpUsers.get(i).getName(), tmpUsers.get(i).getPhone() ,tmpUsers.get(i).getBirth() ,  tmpUsers.get(i).getGender(),"ko"  ,  tmpUsers.get(i).getLately() , tmpUsers.get(i).getDeleted()));
                    }
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        handler.post(new Runnable() {
                            @Override
                            public void run() {


                                adapter.notifyDataSetChanged();


                            }
                        });

                    }
                }).start();
            }
        });

    }

    Context context;

    private void getMemberList() {
        context = this;
        users = GlobalApplication.userPreference.getMember(admin.getAccount());
        tmpUsers = GlobalApplication.userPreference.getMember(admin.getAccount());
        Collections.reverse(users);
        Collections.reverse(tmpUsers);
        adapter = new MemberSelectAdapter(users, getApplicationContext(), context);
        memberList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Log.d(TAG, "getMemberList: run" + adapter.getItemCount());
        Log.d(TAG, "admin account -> " + admin.getAccount());

        for (int i = 0; i < users.size(); i++){
            Log.d(TAG, "getMemberList: " + users.get(i).toString());

        }


    }
}
