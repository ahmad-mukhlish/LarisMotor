package com.yayanheryanto.larismotor.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yayanheryanto.larismotor.R;
import com.yayanheryanto.larismotor.model.Motor;
import com.yayanheryanto.larismotor.model.Transaksi;
import com.yayanheryanto.larismotor.retrofit.ApiClient;
import com.yayanheryanto.larismotor.retrofit.ApiInterface;
import com.yayanheryanto.larismotor.view.owner.EditMotorActivity;
import com.yayanheryanto.larismotor.view.owner.MotorActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import de.codecrafters.tableview.TableDataAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yayanheryanto.larismotor.config.config.DATA_MOTOR;
import static com.yayanheryanto.larismotor.config.config.ID_USER;
import static com.yayanheryanto.larismotor.config.config.MY_PREFERENCES;
import static com.yayanheryanto.larismotor.helper.HelperClass.formatter;
import static com.yayanheryanto.larismotor.helper.HelperClass.getFirstName;

public class LaporanAdapter extends TableDataAdapter<Transaksi> {

    private Double persentaseMokas;

    public LaporanAdapter(Context context, List<Transaksi> data, Double persentaseMokas) {
        super(context, data);
        this.persentaseMokas = persentaseMokas;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final Transaksi transaksi = getRowData(rowIndex);
        View renderedView = null;
        switch (columnIndex) {
            case 0:
                renderedView = renderNomor(transaksi);
                break;
            case 1:
                renderedView = renderTanggal(transaksi);
                break;
            case 2:
                renderedView = renderNoMesin(transaksi);
                break;
            case 3:
                renderedView = renderPembeli(transaksi);
                break;
            case 4:
                renderedView = renderSales(transaksi);
                break;
            case 5:
                renderedView = renderKondisi(transaksi);
                break;
            case 6:
                renderedView = renderDp(transaksi);
                break;
            case 7:
                renderedView = renderHargaTerjual(transaksi);
                break;
            case 8:
                renderedView = renderHJM(transaksi);
                break;
            case 9:
                renderedView = renderSubsidi(transaksi);
                break;
            case 10:
                renderedView = renderMediator(transaksi);
                break;
            case 11:
                renderedView = renderNetto(transaksi);
                break;
            case 12:
                renderedView = renderPersentaseMokas(transaksi);
                break;
            case 13:
                renderedView = renderEdit(transaksi);
                renderedView.setBackgroundColor(getContext().getColor(R.color.colorPrimary));
                renderedView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getMotor(transaksi);
                    }
                });
                break;



        }

        return renderedView;
    }

    private View renderNomor(final Transaksi transaksi) {
        return renderString(transaksi.getNomor() + "");
    }


    private View renderTanggal(final Transaksi transaksi) {

        String hasil = "";

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", new Locale("ID"));
        SimpleDateFormat sqlformat = new SimpleDateFormat("yyyy-MM-dd", new Locale("EN"));
        try {
            hasil = df.format(sqlformat.parse(transaksi.getTanggal()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return renderString(hasil);
    }

    private View renderNoMesin(final Transaksi transaksi) {
        String nopol = transaksi.getNopol();
        String hasil = nopol;

        if (hasil == null)
            hasil = transaksi.getNosin();

        return renderString(hasil);

    }

    private View renderPembeli(final Transaksi transaksi) {
        return renderString(transaksi.getPembeli());
    }


    private View renderSales(final Transaksi transaksi) {
        return renderString(getFirstName(transaksi.getSales()));
    }

    private View renderKondisi(final Transaksi transaksi) {

        String hasil = "baru";

        if (transaksi.getKondisi().equals("0"))
            hasil = "bekas";

        return renderString(hasil);
    }

    private View renderDp(final Transaksi transaksi) {
        String hasil = "kredit";

        if (transaksi.getDp() == null)
            hasil = "cash";

        return renderString(hasil);
    }

    private View renderHargaTerjual(final Transaksi transaksi) {

        String hasil = "";

        //bila cash maka munculkan terjual
        if (transaksi.getDp() == null) {
            hasil = transaksi.getTerjual();
        } else {

            //motor baru
            if (!transaksi.getKondisi().equals("0")) {

                hasil = transaksi.getDp();

            } else {

                int pencairan = 0;
                if (transaksi.getPencairanLeasing() == null) {
                    pencairan = 0;
                } else {
                    pencairan = Integer.parseInt(transaksi.getPencairanLeasing());
                }



                hasil = (Integer.parseInt(transaksi.getDp()) + pencairan) + "";

            }

        }
        return renderString(formatter(hasil));
    }

    private View renderHJM(final Transaksi transaksi) {

        String hasil = "-";

        if (transaksi.getHjm() != null) {
            hasil = formatter(transaksi.getHjm() + "");
        }


        return renderString(hasil);
    }


    private View renderSubsidi(final Transaksi transaksi) {

        String hasil = "";

        //bila cash maka hasilnya tidak ada
        if (transaksi.getDp() == null) {
            hasil = "-";
        } else {

            //motor baru
            if (!transaksi.getKondisi().equals("0")) {

                hasil = formatter(transaksi.getSubsidi());

            } else {

                hasil = "-";

            }

        }
        return renderString(hasil);
    }


    private View renderMediator(final Transaksi transaksi) {

        int mediator = 0;
        if (transaksi.getMediator() == null) {
            mediator = 0;
        } else {
            mediator = Integer.parseInt(transaksi.getMediator());
        }
        return renderString(formatter(mediator + ""));
    }


    private View renderNetto(final Transaksi transaksi) {

        String hasil = "";

        //bila cash terjual - mediator
        if (transaksi.getDp() == null) {
            int mediator = 0;
            if (transaksi.getMediator() == null) {
                mediator = 0;
            } else {
                mediator = Integer.parseInt(transaksi.getMediator());
            }

            int terjual = 0;
            if (transaksi.getTerjual() == null) {
                terjual = 0;
            } else {
                terjual = Integer.parseInt(transaksi.getTerjual());
            }

            hasil = (terjual - mediator) + "";
        } else {

            //motor baru
            if (!transaksi.getKondisi().equals("0")) {

                int mediator = 0;
                if (transaksi.getMediator() == null) {
                    mediator = 0;
                } else {
                    mediator = Integer.parseInt(transaksi.getMediator());
                }

                hasil = (Integer.parseInt(transaksi.getDp()) -
                        Integer.parseInt(transaksi.getSubsidi()) - mediator) + "";

            } else {

                int pencairan = 0;
                if (transaksi.getPencairanLeasing() == null) {
                    pencairan = 0;
                } else {
                    pencairan = Integer.parseInt(transaksi.getPencairanLeasing());
                }

                int mediator = 0;
                if (transaksi.getMediator() == null) {
                    mediator = 0;
                } else {
                    mediator = Integer.parseInt(transaksi.getMediator());
                }


                hasil = (Integer.parseInt(transaksi.getDp()) +
                        pencairan - mediator) + "";

            }

        }
        return renderString(formatter(hasil));
    }


    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(15);
        textView.setTextColor(Color.BLACK);
        return textView;
    }

    private View renderStringPutih(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(15);
        textView.setTextColor(Color.WHITE);
        return textView;
    }


    private View renderPersentaseMokas(final Transaksi transaksi) {

        String hasil = "";

        //bila cash terjual - mediator
        if (transaksi.getDp() == null) {
            int mediator = 0;
            if (transaksi.getMediator() == null) {
                mediator = 0;
            } else {
                mediator = Integer.parseInt(transaksi.getMediator());
            }

            int terjual = 0;
            if (transaksi.getTerjual() == null) {
                terjual = 0;
            } else {
                terjual = Integer.parseInt(transaksi.getTerjual());
            }

            hasil = (terjual - mediator) + "";
            Log.v("naha", hasil);
        } else {

            //motor baru
            if (!transaksi.getKondisi().equals("0")) {

                int mediator = 0;
                if (transaksi.getMediator() == null) {
                    mediator = 0;
                } else {
                    mediator = Integer.parseInt(transaksi.getMediator());
                }

                hasil = (Integer.parseInt(transaksi.getDp()) -
                        Integer.parseInt(transaksi.getSubsidi()) - mediator) + "";

            } else {

                int pencairan = 0;
                if (transaksi.getPencairanLeasing() == null) {
                    pencairan = 0;
                } else {
                    pencairan = Integer.parseInt(transaksi.getPencairanLeasing());
                }

                int mediator = 0;
                if (transaksi.getMediator() == null) {
                    mediator = 0;
                } else {
                    mediator = Integer.parseInt(transaksi.getMediator());
                }


                hasil = (Integer.parseInt(transaksi.getDp()) +
                        pencairan - mediator) + "";

            }


        }

        Integer persentaseMokas;

        persentaseMokas = Integer.parseInt(hasil) - Integer.parseInt(transaksi.getHjm());
        hasil = (persentaseMokas * this.persentaseMokas / 100) + "";

        Log.v("nahaa", this.persentaseMokas + "");
        Log.v("nahaaa", formatter(hasil));
        Log.v("nahaaaa", persentaseMokas + "");


        if (transaksi.getKondisi().equalsIgnoreCase("1") || persentaseMokas < 0) {
            hasil = "-";
        } else {
            hasil = formatter(hasil);
        }
        return renderString(hasil);
    }

    private View renderEdit(final Transaksi transaksi) {
        return renderStringPutih("Edit Data");
    }

    private void getMotor(final Transaksi transaksi) {
        SharedPreferences pref = getContext().getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        String id = pref.getString(ID_USER, "");
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Motor>> call = apiInterface.getMotor(id);
        call.enqueue(new Callback<List<Motor>>() {
            @Override
            public void onResponse(Call<List<Motor>> call, Response<List<Motor>> response) {
                List<Motor> list = response.body();
                for (Motor motor : list) {

                    if (motor.getNoMesin().equalsIgnoreCase(transaksi.getNosin()))
                    {
                        Intent intent = new Intent(getContext(), EditMotorActivity.class);
                        intent.putExtra(DATA_MOTOR, motor);
                        intent.putExtra("ada", false);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(intent);
                    }

                }


            }

            @Override
            public void onFailure(Call<List<Motor>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
