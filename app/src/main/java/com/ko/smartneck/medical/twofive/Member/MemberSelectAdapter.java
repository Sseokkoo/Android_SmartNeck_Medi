package com.ko.smartneck.medical.twofive.Member;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.ko.smartneck.medical.twofive.Main.MainActivity;
import com.ko.smartneck.medical.twofive.R;
import com.ko.smartneck.medical.twofive.util.Address;
import com.ko.smartneck.medical.twofive.util.HttpConnect;
import com.ko.smartneck.medical.twofive.util.Param;
import com.ko.smartneck.medical.twofive.util.User.Member;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.support.constraint.Constraints.TAG;
import static com.ko.smartneck.medical.twofive.GlobalApplication.userPreference;
import static com.ko.smartneck.medical.twofive.Member.MemberSelectActivity.admin;

public class MemberSelectAdapter extends RecyclerView.Adapter<MemberSelectAdapter.ViewHolder> {

    ArrayList<Member> users;
    Context context, activityContext;
    Handler handler;
    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    View view;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_phone, tv_lately, tv_birth;
        ImageView img_delete, img_edit , img_func_btn;
        View view;

        ViewHolder(final View view) {

                    super(view);

            // 뷰 객체에 대한 참조. (hold strong reference)
            this.view = view;
            tv_name = view.findViewById(R.id.tv_member_list_name);
            tv_phone = view.findViewById(R.id.tv_member_list_phone);
            tv_birth = view.findViewById(R.id.tv_member_list_birth);
            tv_lately = view.findViewById(R.id.tv_member_list_lately);
            img_delete = view.findViewById(R.id.img_member_list_delete);
            img_edit = view.findViewById(R.id.img_member_list_edit);
            img_func_btn = view.findViewById(R.id.img_member_select_item_function_btn);
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.d(TAG, "onClick: ");
////                        public User(int memberNo, String name, String birth, String gender, String phone, int age, String lately) {
//                    int position = getAdapterPosition();
//
//                    MemberSelectActivity.user = new Member(users.get(position).getMemberNo() , users.get(position).getAdmin() , users.get(position).getName(), users.get(position).getPhone(), users.get(position).getBirth(), users.get(position).getGender(), users.get(position).getCountry(), users.get(position).getLately());
//                    Intent intent = new Intent(view.getContext(), MainActivity.class);
//                   view.getContext().startActivity(intent);
//                }
//            });

        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    MemberSelectAdapter(ArrayList<Member> users, Context context, Context activityContext) {
        this.users = users;
        this.context = context;
        this.activityContext = activityContext;


    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.admin_member_list_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        handler = new Handler();

        return vh;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(MemberSelectAdapter.ViewHolder holder, final int position) {
//        String text = users.get(position) ;
//        holder.textView1.setText(text) ;

        String genderStr = "";
        if (users.get(position).getGender().equals("남자") || users.get(position).getGender().equals("male")) {
            genderStr = context.getString(R.string.member_select_gender_male);
        } else {
            genderStr = context.getString(R.string.member_select_gender_female);
        }
        holder.tv_name.setText(users.get(position).getName() + genderStr);
        String[] phoneSplit = {};
        String phone = "";
        if (users.get(position).getPhone().contains("-")) {
            phoneSplit = users.get(position).getPhone().split("-");
            holder.tv_phone.setText(phoneSplit[1]);
        } else {
            phone = users.get(position).getPhone();
            holder.tv_phone.setText(phone);
        }

        holder.tv_birth.setText(users.get(position).getBirth());
        holder.tv_lately.setText(
//                context.getString(R.string.last_visit) +
                users.get(position).getLately().split(" ")[0]);

        holder.img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MemberAddActivity.class);
                intent.putExtra("edit", true);
                intent.putExtra("name", users.get(position).getName());
                intent.putExtra("memberNo", users.get(position).getMemberNo());
                intent.putExtra("birth", users.get(position).getBirth());
                intent.putExtra("gender", users.get(position).getGender());
                intent.putExtra("phone", users.get(position).getPhone());
                intent.putExtra("position" , position);
                intent.putExtra("users" , users);
                Log.d(TAG ,"memberNo =>> " + users.get(position).getMemberNo() );
                Log.d(TAG ,"position =>> " + position );
                context.startActivity(intent);
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSelect(view, position);
            }
        });

        holder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSelect(view, position);
            }
        });

        holder.tv_lately.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSelect(view, position);
            }
        });

        holder.tv_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSelect(view, position);
            }
        });

        holder.tv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSelect(view, position);
            }
        });

        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                showAlertDialog(position);

            }
        });


        holder.img_func_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(context, v);
//Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.member_select_func, popup.getMenu());


                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.menu_member_select_delete:
                                showAlertDialog(position);

                                break;

                            case R.id.menu_member_select_edit:
                                Intent intent = new Intent(context, MemberAddActivity.class);
                                intent.putExtra("edit", true);
                                intent.putExtra("name", users.get(position).getName());
                                intent.putExtra("memberNo", users.get(position).getMemberNo());
                                intent.putExtra("birth", users.get(position).getBirth());
                                intent.putExtra("gender", users.get(position).getGender());
                                intent.putExtra("phone", users.get(position).getPhone());
                                intent.putExtra("users" , users);
                                intent.putExtra("position" , position);

                                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                                break;

                        }


                        return true;
                    }
                });


                popup.show();//showing popup menu


            }
        });

    }

    private void userSelect(View view, int position) {
        Log.d(TAG, "onClick: ");

        Intent intent = new Intent(view.getContext(), MainActivity.class);
        intent.putExtra("preset" , userPreference.getPreset(users.get(position).getAdmin() , users.get(position).getUid()));
        intent.putExtra("member" , users.get(position));
        Log.e(TAG, "userSelect: " + users.get(position) );

        view.getContext().startActivity(intent);




    }

    private void showAlertDialog(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activityContext);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.admin_member_delete, null);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView confirm = view.findViewById(R.id.member_delete_confirm);
        TextView cancel = view.findViewById(R.id.member_delete_cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                users.get(position).setDeleted(true);
                users.remove(position);

                userPreference.editMember(users);

                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdf = new SimpleDateFormat("y-M-d H:m:s");
                String[] dateSplit = sdf.format(date).split(" ")[0].split("-");
                String year = dateSplit[0];
                int imonth = Integer.valueOf(dateSplit[1]);
                int iday = Integer.valueOf(dateSplit[2]);
                String month = "";
                String day = "";
                if (imonth < 10){
                    month = "0" + imonth;
                }else{
                    month = imonth + "";
                }

                if (iday < 10){
                    day = "0" + iday;
                }else{
                    day = iday + "";
                }
                String delete_date = year + "-" + month + "-" + day;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpConnect httpConnect = new HttpConnect();
                        Param param = new Param();
                        param.add("uid" , users.get(position).getUid());
                        param.add("delete_date" , delete_date);
                        if (httpConnect.httpConnect(param.getValue() , new Address().getMemberDelete() , true) == 200){

                        }
                    }
                }).start();

                notifyDataSetChanged();
                dialog.dismiss();
            }
        });


//        dialog.setCancelable(false);
        dialog.show();
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return users.size();
    }
}
