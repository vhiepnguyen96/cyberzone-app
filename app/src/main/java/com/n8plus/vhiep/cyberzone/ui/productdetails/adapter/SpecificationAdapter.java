package com.n8plus.vhiep.cyberzone.ui.productdetails.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.n8plus.vhiep.cyberzone.data.model.Specification;
import com.n8plus.vhiep.cyberzone.R;

import java.util.List;

public class SpecificationAdapter extends RecyclerView.Adapter<SpecificationAdapter.MyViewHolder> {

    private List<Specification> specifications;

    public SpecificationAdapter(List<Specification> specifications) {
        this.specifications = specifications;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_titleSpecification, txt_valueSpecification;

        public MyViewHolder(View itemView) {
            super(itemView);
            txt_titleSpecification = (TextView) itemView.findViewById(R.id.txt_titleSpecification);
            txt_valueSpecification = (TextView) itemView.findViewById(R.id.txt_valueSpecification);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_specification, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Specification specification = specifications.get(position);
        holder.txt_titleSpecification.setText(specification.getTitle());
        holder.txt_valueSpecification.setText(specification.getValue());
    }

    @Override
    public int getItemCount() {
        return specifications.size();
    }


//    private Context context;
//    private int resource;
//    private List<Specification> arrSpecification;
//
//    public SpecificationAdapter(Context context, int resource, List<Specification> arrSpecification) {
//        this.context = context;
//        this.resource = resource;
//        this.arrSpecification = arrSpecification;
//    }
//
//    public class ViewHolder {
//        TextView txt_titleSpecification, txt_valueSpecification;
//    }
//
//    @Override
//    public int getCount() {
//        return arrSpecification.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return arrSpecification.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder viewHolder;
//        if (convertView == null) {
//            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = layoutInflater.inflate(resource, null);
//            viewHolder = new ViewHolder();
//            viewHolder.txt_titleSpecification = (TextView) convertView.findViewById(R.id.txt_titleSpecification);
//            viewHolder.txt_valueSpecification = (TextView) convertView.findViewById(R.id.txt_valueSpecification);
//
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//
//       Specification specification = arrSpecification.get(position);
//        viewHolder.txt_titleSpecification.setText(specification.getName());
//        viewHolder.txt_valueSpecification.setText(specification.getValue());
//
//        return convertView;
//    }
}
