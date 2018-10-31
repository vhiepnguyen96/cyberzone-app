package com.n8plus.vhiep.cyberzone.ui.manage.registersale.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.RegisterSale;
import com.n8plus.vhiep.cyberzone.ui.product.adapter.RecyclerProductAdapter;

import java.util.Calendar;
import java.util.List;

public class RegisterSaleAdapter extends RecyclerView.Adapter<RegisterSaleAdapter.ViewHolder> {
    List<RegisterSale> registerSales;

    public RegisterSaleAdapter(List<RegisterSale> registerSales) {
        this.registerSales = registerSales;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView id, date, state, nameStore, location, nameCustomer, phone, email, username, password, showPasword;
        private ItemClickListener itemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.tv_register_sale_id);
            date = (TextView) itemView.findViewById(R.id.tv_register_date);
            state = (TextView) itemView.findViewById(R.id.tv_register_state);
            nameStore = (TextView) itemView.findViewById(R.id.tv_register_name_store);
            location = (TextView) itemView.findViewById(R.id.tv_register_location);
            nameCustomer = (TextView) itemView.findViewById(R.id.tv_customer_name);
            phone = (TextView) itemView.findViewById(R.id.tv_store_phone);
            email = (TextView) itemView.findViewById(R.id.tv_store_email);
            username = (TextView) itemView.findViewById(R.id.tv_store_account);
            password = (TextView) itemView.findViewById(R.id.tv_password);
            showPasword = (TextView) itemView.findViewById(R.id.tv_show_password);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }


        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), true);
            return true;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_register_sale, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        RegisterSale registerSale = registerSales.get(position);
        holder.id.setText(registerSale.getId());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(registerSale.getRegisteredDate());
        String date = calendar.get(Calendar.DATE) + " Thg " + (calendar.get(Calendar.MONTH) + 1) + " " + calendar.get(Calendar.YEAR);
        holder.date.setText(date);

        holder.state.setText(registerSale.getApprove() == null ? "Đang chờ phê duyệt" : registerSale.getApprove() ? "Đã được phê duyệt" : "Đơn bị từ chối");
        holder.nameStore.setText(registerSale.getStoreName());
        holder.location.setText(registerSale.getAddress());
        holder.nameCustomer.setText(registerSale.getCustomer().getName());
        holder.phone.setText(registerSale.getPhoneNumber());
        holder.email.setText(registerSale.getEmail());
        holder.username.setText(registerSale.getUsername());
        holder.password.setText(registerSale.getPassword());

        holder.showPasword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        holder.password.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case MotionEvent.ACTION_UP:
                        holder.password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return registerSales.size();
    }

    public interface ItemClickListener {
        void onClick(View view, int position, boolean isLongClick);
    }


}
