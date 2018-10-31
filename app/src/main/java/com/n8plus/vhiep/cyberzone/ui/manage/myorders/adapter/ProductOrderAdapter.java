package com.n8plus.vhiep.cyberzone.ui.manage.myorders.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPickerListener;
import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.PurchaseItem;
import com.n8plus.vhiep.cyberzone.ui.cart.CartActivity;
import com.n8plus.vhiep.cyberzone.ui.cart.CartContract;
import com.n8plus.vhiep.cyberzone.ui.product.adapter.ProductVerticalAdapter;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class ProductOrderAdapter extends BaseAdapter {
    private Context context;
    private int resource;
    private List<PurchaseItem> purchaseItemList;

    public ProductOrderAdapter(Context context, int resource, List<PurchaseItem> purchaseItemList) {
        this.context = context;
        this.resource = resource;
        this.purchaseItemList = purchaseItemList;
    }

    public class ViewHolder {
        public ImageView img_order_product, img_cart_product;
        public TextView txt_order_productName, txt_order_productPrice, txt_order_productBasicPrice, txt_order_discount, txt_order_quantity;
        public TextView txt_cart_productName, txt_cart_productPrice, txt_cart_productBasicPrice, txt_cart_discount;
        public LinearLayout lnr_removeFromCart;
        public ScrollableNumberPicker snp_quantity_product;
    }

    @Override
    public int getCount() {
        return purchaseItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return purchaseItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(resource, null);
            holder = new ViewHolder();
            if (resource == R.layout.row_product_order) {
                holder.img_order_product = (ImageView) convertView.findViewById(R.id.img_order_product);
                holder.txt_order_productName = (TextView) convertView.findViewById(R.id.txt_order_productName);
                holder.txt_order_productPrice = (TextView) convertView.findViewById(R.id.txt_order_productPrice);
                holder.txt_order_productBasicPrice = (TextView) convertView.findViewById(R.id.txt_order_productBasicPrice);
                holder.txt_order_discount = (TextView) convertView.findViewById(R.id.txt_order_discount);
                holder.txt_order_quantity = (TextView) convertView.findViewById(R.id.txt_order_quantity);
            } else {
                holder.img_cart_product = (ImageView) convertView.findViewById(R.id.img_cart_product);
                holder.txt_cart_productName = (TextView) convertView.findViewById(R.id.txt_cart_productName);
                holder.txt_cart_productPrice = (TextView) convertView.findViewById(R.id.txt_cart_productPrice);
                holder.txt_cart_productBasicPrice = (TextView) convertView.findViewById(R.id.txt_cart_productBasicPrice);
                holder.txt_cart_discount = (TextView) convertView.findViewById(R.id.txt_cart_discount);
                holder.lnr_removeFromCart = (LinearLayout) convertView.findViewById(R.id.lnr_removeFromCart);
                holder.snp_quantity_product = (ScrollableNumberPicker) convertView.findViewById(R.id.snp_product_quantity);
            }

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = purchaseItemList.get(position).getProduct();
        DecimalFormat df = new DecimalFormat("#.000");

        if (resource == R.layout.row_product_order) {
            if (product.getImageDefault() != null) {
                Picasso.get().load(product.getImageDefault()).into(holder.img_order_product);
            }

            holder.txt_order_productName.setText(product.getProductName());
            if (product.getSaleOff() != null && product.getSaleOff().getDiscount() > 0) {
                int basicPrice = Integer.valueOf(product.getPrice());
                int discount = product.getSaleOff().getDiscount();
                int salePrice = basicPrice - (basicPrice * discount / 100);

                holder.txt_order_productBasicPrice.setText(basicPrice > 1000 ? df.format(Product.convertToPrice(String.valueOf(basicPrice))).replace(",", ".") : String.valueOf(basicPrice));
                holder.txt_order_productPrice.setText(salePrice > 1000 ? df.format(Product.convertToPrice(String.valueOf(salePrice))).replace(",", ".") : String.valueOf(salePrice));

                holder.txt_order_discount.setText(String.valueOf(product.getSaleOff().getDiscount()));
                convertView.findViewById(R.id.layout_order_discount).setVisibility(View.VISIBLE);
            } else {
                holder.txt_order_productPrice.setText(Integer.valueOf(product.getPrice()) > 1000 ? df.format(Product.convertToPrice(product.getPrice())).replace(",", ".") : product.getPrice());
                convertView.findViewById(R.id.layout_order_discount).setVisibility(View.INVISIBLE);
            }
            holder.txt_order_quantity.setText(String.valueOf(purchaseItemList.get(position).getQuantity()));
        } else {
            if (product.getImageList() != null && product.getImageList().size() > 0) {
                Picasso.get().load(product.getImageList().get(0).getImageURL()).into(holder.img_cart_product);
            }
            holder.txt_cart_productName.setText(product.getProductName());
            if (product.getSaleOff() != null && product.getSaleOff().getDiscount() > 0) {
                int basicPrice = Integer.valueOf(product.getPrice());
                int discount = product.getSaleOff().getDiscount();
                int salePrice = basicPrice - (basicPrice * discount / 100);

                holder.txt_cart_productBasicPrice.setText(basicPrice > 1000 ? df.format(Product.convertToPrice(String.valueOf(basicPrice))).replace(",", ".") : String.valueOf(basicPrice));
                holder.txt_cart_productPrice.setText(salePrice > 1000 ? df.format(Product.convertToPrice(String.valueOf(salePrice))).replace(",", ".") : String.valueOf(salePrice));
                holder.txt_cart_discount.setText(String.valueOf(product.getSaleOff().getDiscount()));

                convertView.findViewById(R.id.layout_cart_discount).setVisibility(View.VISIBLE);
            } else {
                holder.txt_cart_productPrice.setText(Integer.valueOf(product.getPrice()) > 1000 ? df.format(Product.convertToPrice(product.getPrice())).replace(",", ".") : product.getPrice());
                convertView.findViewById(R.id.layout_cart_discount).setVisibility(View.INVISIBLE);
            }
            holder.snp_quantity_product.setValue(purchaseItemList.get(position).getQuantity());
            holder.snp_quantity_product.setMaxValue(purchaseItemList.get(position).getProduct().getQuantity());

            holder.snp_quantity_product.setListener(new ScrollableNumberPickerListener() {
                @Override
                public void onNumberPicked(int value) {
                    purchaseItemList.get(position).setQuantity(value);
                    notifyDataSetChanged();
                    if (context instanceof CartActivity) {
                        ((CartActivity) context).updateCart();
                    }
                }
            });

            holder.lnr_removeFromCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Xác nhận");
                    builder.setMessage("Bạn muốn xóa sản phẩm khỏi giỏ?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Chấp nhận", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            purchaseItemList.remove(position);
                            notifyDataSetChanged();
                            if (context instanceof CartActivity) {
                                ((CartActivity) context).updateCart();
                            }
                            dialogInterface.dismiss();
                        }
                    });
                    builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
        }
        return convertView;
    }
}
