package com.tset.buycoinproject;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

/**
 * Created by USER on 20/12/2017.
 */

public class Dialog extends DialogFragment implements View.OnClickListener{
    private Button coin_20;
    private Button coin_30;
    private Button coin_40;
    public static  final  int twenty_coin = 0;
    public static final int thirty_coin = 1;
    public static final int forty_coin =2;
    private ProductSelected productSelected;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog, null);
        builder.setView(view);
        coin_20 = (Button)view.findViewById(R.id.btn_dialog_twenty);
        coin_20.setTag(twenty_coin);
        coin_20.setOnClickListener(this);
        coin_30 = (Button)view.findViewById(R.id.btn_dialog_thirty);
        coin_30.setTag(thirty_coin);
        coin_30.setOnClickListener(this);
        coin_40 = (Button)view.findViewById(R.id.btn_dialog_forty);
        coin_40.setTag(forty_coin);
        coin_40.setOnClickListener(this);
        return builder.create();
    }

    @Override
    public void onClick(View view) {
        if (productSelected!=null){
            productSelected.productselected((Integer) view.getTag());
            dismiss();
        }

    }

    public interface ProductSelected{
        void  productselected(int productId);
    }
    public void setProductSelected (ProductSelected productSelected){
        this.productSelected = productSelected;
    }
}
