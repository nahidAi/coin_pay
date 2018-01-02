package com.tset.buycoinproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tset.buycoinproject.util.IabHelper;
import com.tset.buycoinproject.util.IabResult;
import com.tset.buycoinproject.util.Inventory;
import com.tset.buycoinproject.util.Purchase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        IabHelper.OnIabSetupFinishedListener, IabHelper.QueryInventoryFinishedListener,
        IabHelper.OnIabPurchaseFinishedListener, IabHelper.OnConsumeFinishedListener {
    private ImageView img_show_coin;
    private TextView txt_show_coin;
    private Button btn_buy_coin;
    private CardView card_1;
    private CardView card_2;
    private CardView card_3;
    private CardView card_4;
    private CardView card_5;
    private final String RsaKey = "MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwCXkffj+kyxrE/LkCbojqByEPX+NnnNNYasZadmodbyCYLsoXdrLkjp8uxCFlhqSG+0oBKNM4+YXTrrjUfF5TmqKdm6NQn/uPCftE2vTAQSy7ZNXHi4UGMitgO2nqlcN0N+lYT2fD2iQGuGIPwdTYa1xbmaJdkD9SGhwRNpw4vFRlEWX+nSKl32DbBaR4c2CWiwnew3XfWgqGoVsOXH7zAOoCPT0sd1k8P3fag/pSUCAwEAAQ==";
    private IabHelper iabHelper;
    private SharedPreferences coinSharedPraferencec;
    private TextView txtShowText2;
    private TextView txtShowText3;
    private TextView txtShowText4;
    private TextView txtShowText5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iabHelper = new IabHelper(this, RsaKey);
        iabHelper.startSetup(this);
        coinSharedPraferencec = getSharedPreferences("coins_shp", MODE_PRIVATE);
        setupViews();

    }

    private void setupViews() {
        img_show_coin = (ImageView) findViewById(R.id.imageView_coin_show);
        txt_show_coin = (TextView) findViewById(R.id.txt_coin_show);
        txtShowText2 = (TextView) findViewById(R.id.txt_show_text_2);
        txtShowText3 = (TextView) findViewById(R.id.txt_show_text_3);
        txtShowText4 = (TextView) findViewById(R.id.txt_show_text_4);
        txtShowText5 = (TextView) findViewById(R.id.txt_show_text_5);

        updateCoinsUi();

        btn_buy_coin = (Button) findViewById(R.id.btn_buy_coin);
        btn_buy_coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog();
                dialog.setProductSelected(new Dialog.ProductSelected() {
                    @Override
                    public void productselected(int productId) {
                        switch (productId) {
                            case Dialog.twenty_coin:
                                if (iabHelper != null) iabHelper.flagEndAsync();
                                iabHelper.launchPurchaseFlow(MainActivity.this, "20_coin", 101, MainActivity.this);
                                break;
                            case Dialog.thirty_coin:
                                if (iabHelper != null) iabHelper.flagEndAsync();
                                iabHelper.launchPurchaseFlow(MainActivity.this, "coin_30", 101, MainActivity.this);
                                break;
                            case Dialog.forty_coin:
                                if (iabHelper != null) iabHelper.flagEndAsync();
                                iabHelper.launchPurchaseFlow(MainActivity.this, "coin_40", 101, MainActivity.this);
                                break;
                        }
                    }
                });
                dialog.show(getFragmentManager(), null);


            }
        });
        card_1 = (CardView) findViewById(R.id.card_1);
        card_1.setOnClickListener(this);
        card_2 = (CardView) findViewById(R.id.card_2);
        card_2.setOnClickListener(this);
        if (hasPurchase(card_2.getId())) {
            findViewById(R.id.txt_show_text_2).setVisibility(View.GONE);
        }

        card_3 = (CardView) findViewById(R.id.card_3);
        card_3.setOnClickListener(this);
        if (hasPurchase(card_3.getId())) {
            findViewById(R.id.txt_show_text_3).setVisibility(View.GONE);
        }
        card_4 = (CardView) findViewById(R.id.card_4);
        card_4.setOnClickListener(this);
        if (hasPurchase(card_4.getId())) {
            findViewById(R.id.txt_show_text_4).setVisibility(View.GONE);
        }
        card_5 = (CardView) findViewById(R.id.card_5);
        card_5.setOnClickListener(this);
        if (hasPurchase(card_5.getId())) {
            findViewById(R.id.txt_show_text_5).setVisibility(View.GONE);
        }
    }

    private void openNextPage(int id) {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    @Override
    public void onClick(final View view) {
        // اگر کاربر این آیتم را خریداری کرده است مستقیم به صفحه بعد میرود
        if (hasPurchase(view.getId())) {
            openNextPage(view.getId());
            return;
        }
        switch (view.getId()) {
            case (R.id.card_1):
                openNextPage(view.getId());
                break;

            case (R.id.card_2):
                if (getCoinsCount() >= 10) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setView(R.layout.confirmation_dialog);
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    Button btnDialogYes = (Button) dialog.findViewById(R.id.btn_dialog_yes);
                    btnDialogYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view1) {
                            saveCoin(getCoinsCount() - 10);
                            savePurchase(view.getId());
                            updateCoinsUi();
                            txtShowText2.setVisibility(View.GONE);
                            openNextPage(view.getId());
                            dialog.cancel();


                        }
                    });
                    Button btnDialogCancel = (Button) dialog.findViewById(R.id.btn_dialog_cancel);
                    btnDialogCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();

                        }
                    });

                } else {
                    Toast.makeText(this, " سکه موجود نیست ", Toast.LENGTH_SHORT).show();
                    Dialog dialog = new Dialog();
                    dialog.setProductSelected(new Dialog.ProductSelected() {
                        @Override
                        public void productselected(int productId) {
                            switch (productId) {
                                case Dialog.twenty_coin:
                                    if (iabHelper != null) iabHelper.flagEndAsync();
                                    iabHelper.launchPurchaseFlow(MainActivity.this, "20_coin", 101, MainActivity.this);
                                    break;
                                case Dialog.thirty_coin:
                                    if (iabHelper != null) iabHelper.flagEndAsync();
                                    iabHelper.launchPurchaseFlow(MainActivity.this, "coin_30", 101, MainActivity.this);
                                    break;
                                case Dialog.forty_coin:
                                    if (iabHelper != null) iabHelper.flagEndAsync();
                                    iabHelper.launchPurchaseFlow(MainActivity.this, "coin_40", 101, MainActivity.this);
                                    break;
                            }
                        }
                    });
                    dialog.show(getFragmentManager(), null);
                }


                break;
            case (R.id.card_3):
                if (getCoinsCount() >= 10) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setView(R.layout.confirmation_dialog);
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    Button btnDialogYes = (Button) dialog.findViewById(R.id.btn_dialog_yes);
                    btnDialogYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view1) {
                            saveCoin(getCoinsCount() - 10);
                             savePurchase(view.getId());
                            updateCoinsUi();
                            txtShowText3.setVisibility(View.GONE);
                             openNextPage(view.getId());
                            dialog.cancel();


                        }
                    });
                    Button btnDialogCancel = (Button) dialog.findViewById(R.id.btn_dialog_cancel);
                    btnDialogCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();

                        }
                    });

                } else {
                    Toast.makeText(this, " سکه موجود نیست ", Toast.LENGTH_SHORT).show();
                    Dialog dialog = new Dialog();
                    dialog.setProductSelected(new Dialog.ProductSelected() {
                        @Override
                        public void productselected(int productId) {
                            switch (productId) {
                                case Dialog.twenty_coin:
                                    if (iabHelper != null) iabHelper.flagEndAsync();
                                    iabHelper.launchPurchaseFlow(MainActivity.this, "20_coin", 101, MainActivity.this);
                                    break;
                                case Dialog.thirty_coin:
                                    if (iabHelper != null) iabHelper.flagEndAsync();
                                    iabHelper.launchPurchaseFlow(MainActivity.this, "coin_30", 101, MainActivity.this);
                                    break;
                                case Dialog.forty_coin:
                                    if (iabHelper != null) iabHelper.flagEndAsync();
                                    iabHelper.launchPurchaseFlow(MainActivity.this, "coin_40", 101, MainActivity.this);
                                    break;
                            }
                        }
                    });
                    dialog.show(getFragmentManager(), null);
                }

                break;
            case (R.id.card_4):
                if (getCoinsCount() >= 10) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setView(R.layout.confirmation_dialog);
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    Button btnDialogYes = (Button) dialog.findViewById(R.id.btn_dialog_yes);
                    btnDialogYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view1) {
                            saveCoin(getCoinsCount() - 10);
                             savePurchase(view.getId());
                            updateCoinsUi();
                            txtShowText4.setVisibility(View.GONE);
                            openNextPage(view.getId());
                            dialog.cancel();


                        }
                    });
                    Button btnDialogCancel = (Button) dialog.findViewById(R.id.btn_dialog_cancel);
                    btnDialogCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();

                        }
                    });

                } else {
                    Toast.makeText(this, " سکه موجود نیست ", Toast.LENGTH_SHORT).show();
                    Dialog dialog = new Dialog();
                    dialog.setProductSelected(new Dialog.ProductSelected() {
                        @Override
                        public void productselected(int productId) {
                            switch (productId) {
                                case Dialog.twenty_coin:
                                    if (iabHelper != null) iabHelper.flagEndAsync();
                                    iabHelper.launchPurchaseFlow(MainActivity.this, "20_coin", 101, MainActivity.this);
                                    break;
                                case Dialog.thirty_coin:
                                    if (iabHelper != null) iabHelper.flagEndAsync();
                                    iabHelper.launchPurchaseFlow(MainActivity.this, "coin_30", 101, MainActivity.this);
                                    break;
                                case Dialog.forty_coin:
                                    if (iabHelper != null) iabHelper.flagEndAsync();
                                    iabHelper.launchPurchaseFlow(MainActivity.this, "coin_40", 101, MainActivity.this);
                                    break;
                            }
                        }
                    });
                    dialog.show(getFragmentManager(), null);
                }

                break;
            case (R.id.card_5):
                if (getCoinsCount() >= 10) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setView(R.layout.confirmation_dialog);
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    Button btnDialogYes = (Button) dialog.findViewById(R.id.btn_dialog_yes);
                    btnDialogYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view1) {
                            saveCoin(getCoinsCount() - 10);
                             savePurchase(view.getId());
                            updateCoinsUi();
                            txtShowText5.setVisibility(View.GONE);
                          openNextPage(view.getId());
                            dialog.cancel();


                        }
                    });
                    Button btnDialogCancel = (Button) dialog.findViewById(R.id.btn_dialog_cancel);
                    btnDialogCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();

                        }
                    });

                } else {
                    Toast.makeText(this, " سکه موجود نیست ", Toast.LENGTH_SHORT).show();
                    Dialog dialog = new Dialog();
                    dialog.setProductSelected(new Dialog.ProductSelected() {
                        @Override
                        public void productselected(int productId) {
                            switch (productId) {
                                case Dialog.twenty_coin:
                                    if (iabHelper != null) iabHelper.flagEndAsync();
                                    iabHelper.launchPurchaseFlow(MainActivity.this, "20_coin", 101, MainActivity.this);
                                    break;
                                case Dialog.thirty_coin:
                                    if (iabHelper != null) iabHelper.flagEndAsync();
                                    iabHelper.launchPurchaseFlow(MainActivity.this, "coin_30", 101, MainActivity.this);
                                    break;
                                case Dialog.forty_coin:
                                    if (iabHelper != null) iabHelper.flagEndAsync();
                                    iabHelper.launchPurchaseFlow(MainActivity.this, "coin_40", 101, MainActivity.this);
                                    break;
                            }
                        }
                    });
                    dialog.show(getFragmentManager(), null);
                }

                break;


        }

    }

    @Override
    public void onIabSetupFinished(IabResult result) {
        iabHelper.queryInventoryAsync(this);

    }

    @Override
    public void onIabPurchaseFinished(IabResult result, Purchase info) {
        if (result.isSuccess() && info != null) {
            iabHelper.consumeAsync(info, this);
        }

    }

    @Override
    public void onQueryInventoryFinished(IabResult result, Inventory inv) {

    }

    public void saveCoin(int coins) {
        SharedPreferences.Editor editor = coinSharedPraferencec.edit();
        editor.putInt("coins", coins);
        editor.apply();


    }

    public int getCoinsCount() {
        return coinSharedPraferencec.getInt("coins", 30);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101) {
            iabHelper.handleActivityResult(requestCode, resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);

        }

    }

    public void updateCoinsUi() {
        String coinCount = String.valueOf(getCoinsCount());
        txt_show_coin.setText("تعداد سکه ها: " + coinCount);


    }

    @Override
    public void onConsumeFinished(Purchase purchase, IabResult result) {
        if (result.isSuccess() && purchase != null) {
            if (purchase.getSku().equalsIgnoreCase("20_coin")) {
                saveCoin(getCoinsCount() + 20);

            } else if (purchase.getSku().equalsIgnoreCase("coin_30")) {
                saveCoin(getCoinsCount() + 30);
            } else if (purchase.getSku().equalsIgnoreCase("coin_40")) {
                saveCoin(getCoinsCount() + 40);
            }
            updateCoinsUi();

        }
    }

    public void savePurchase(int productId) {
        SharedPreferences.Editor editor = coinSharedPraferencec.edit();
        editor.putBoolean("purchase" + productId, true);
        editor.apply();

    }

    public boolean hasPurchase(int productId) {
        return coinSharedPraferencec.getBoolean("purchase" + productId, false);

    }


}
