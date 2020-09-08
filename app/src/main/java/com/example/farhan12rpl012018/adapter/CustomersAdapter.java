package com.example.farhan12rpl012018.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farhan12rpl012018.ListCustomerActivity;
import com.example.farhan12rpl012018.R;
import com.example.farhan12rpl012018.model.CustomersModel;

import java.util.List;

public class CustomersAdapter extends RecyclerView.Adapter<CustomersAdapter.CustomersViewHolder> {

    private Context mContext;
    private List<CustomersModel> mData;

    public CustomersAdapter(Context mContext, List<CustomersModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }
    @NonNull
    @Override
    public CustomersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.customers_recycler, parent, false);
        CustomersViewHolder holder = new CustomersViewHolder(v);
        mContext = parent.getContext();
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull CustomersViewHolder holder, int position) {

        holder.profile.setImageResource(mData.get(position).getProfile());
        holder.name.setText(mData.get(position).getName());
        holder.location.setText(mData.get(position).getLocation());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class CustomersViewHolder extends RecyclerView.ViewHolder {

        private ImageView profile;
        private TextView name, location;
        private Button btnedit, btnDelete, btncancel, btnaccept;
        private EditText txtusername,txtnohp,txtnoktp,txtalamat;

        public CustomersViewHolder( View view) {
            super(view);
            profile = (ImageView) view.findViewById(R.id.img_profile);
            name = (TextView)view.findViewById(R.id.txt_name);
            location = (TextView)view.findViewById(R.id.txt_location);
            btnedit = view.findViewById(R.id.btn_edit);
            btnedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    View dlgView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_edit_customer, null);
                    final Dialog dialog = new Dialog(v.getContext(), android.R.style.Theme_Material_Dialog);

                    dialog.setContentView(dlgView);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                    btncancel = dialog.findViewById(R.id.btn_delete);
                    btnaccept = dialog.findViewById(R.id.btn_edits);

                    txtusername = dialog.findViewById(R.id.txtusername);
                    txtnohp = dialog.findViewById(R.id.txtnomerhp);
                    txtnoktp = dialog.findViewById(R.id.txtnoktp);
                    txtalamat = dialog.findViewById(R.id.txtalamat);

                    txtusername.setText(mData.get(getAdapterPosition()).getName());
                    txtnohp.setText(mData.get(getAdapterPosition()).getNohp());
                    txtnoktp.setText(mData.get(getAdapterPosition()).getNoktp());
                    txtalamat.setText(mData.get(getAdapterPosition()).getLocation());

                    btncancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    btnaccept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String username = txtusername.getText().toString();
                            String nohp = txtnohp.getText().toString();
                            String noktp = txtnoktp.getText().toString();
                            String alamat = txtalamat.getText().toString();
                            String u_id = mData.get(getAdapterPosition()).getId();
                            if (mContext instanceof ListCustomerActivity) {
                                ((ListCustomerActivity)mContext).editData(u_id, username, nohp, noktp, alamat);
                            }
                        }
                    });
                }
            });

            btnDelete = view.findViewById(R.id.btn_delete);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View dlgView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_delete_customer, null);
                    final Dialog dialog = new Dialog(v.getContext(), android.R.style.Theme_Material_Dialog);

                    dialog.setContentView(dlgView);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();

                    btnaccept = dialog.findViewById(R.id.btn_yes);
                    btncancel = dialog.findViewById(R.id.btn_no);

                    btnaccept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String u_id = mData.get(getAdapterPosition()).getId();
                            if (mContext instanceof ListCustomerActivity) {
                                ((ListCustomerActivity)mContext).deleteData(u_id);
                            }
                        }
                    });

                    btncancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            });
        }
    }
}