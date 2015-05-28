package com.example.administrador.myapplication.controllers.material;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.administrador.myapplication.R;
import com.example.administrador.myapplication.models.entities.ServiceOrder;
import com.example.administrador.myapplication.util.AppUtil;
import com.melnykov.fab.FloatingActionButton;

import org.apache.http.protocol.HTTP;

import java.util.List;

public class ServiceOrderListActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    public static final int REQUEST_CODE_ADD = 1;
    public static final int REQUEST_CODE_EDIT = 2;
    private RecyclerView mOrders;
    private ServiceOrderListAdapter mOrdersAdapter;
    private MenuItem mMenuArquivados,mMenuNaoArquivados;
    private FloatingActionButton mFabAdd;
    private static final boolean listaArquivada = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_order_list_material);

        mMenuArquivados = (MenuItem)findViewById(R.id.actionArquivados);
        mMenuNaoArquivados = (MenuItem)findViewById(R.id.actionNaoArquivados);

        mOrders = AppUtil.get(findViewById(R.id.listViewServiceOrders));
        mOrders.setHasFixedSize(true);
        mOrders.setLayoutManager(new LinearLayoutManager(this));

        mFabAdd = (FloatingActionButton) findViewById(R.id.fabAdd);

        mFabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ServiceOrderListActivity.this, ServiceOrderActivity.class), REQUEST_CODE_ADD);
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        this.atualizarLista("0");
    }

    protected void atualizarLista(String arquivados) {
        List<ServiceOrder> listaServiceOrders = ServiceOrder.getAll(arquivados);
        if (mOrdersAdapter == null) {
            mOrdersAdapter = new ServiceOrderListAdapter(listaServiceOrders);
            mOrders.setAdapter(mOrdersAdapter);
        } else {
            mOrdersAdapter.setItens(listaServiceOrders);
            mOrdersAdapter.notifyDataSetChanged();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_ADD) {
                Toast.makeText(this, data.getAction() + "\nSuccessful Add!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, data.getAction() + "\nSuccessful Edit!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onMenuItemClick(final MenuItem item) {
        final ServiceOrder serviceOrder = mOrdersAdapter.getSelectedItem();
        switch (item.getItemId()) {
            case R.id.actionCall:
                // Best Practices: http://stackoverflow.com/questions/4275678/how-to-make-phone-call-using-intent-in-android
                final Intent goToSOPhoneCall = new Intent(Intent.ACTION_DIAL);
                goToSOPhoneCall.setData(Uri.parse("tel:" + serviceOrder.getPhone()));
                startActivity(goToSOPhoneCall);
                return true;
            case R.id.actionEdit:
                Intent goToEdit = new Intent(ServiceOrderListActivity.this, ServiceOrderActivity.class);
                goToEdit.putExtra(ServiceOrderActivity.EXTRA_SERVICE_ORDER, serviceOrder);
                goToEdit.putExtra("EXTRA_BM", SystemClock.elapsedRealtime());
                startActivityForResult(goToEdit, REQUEST_CODE_EDIT);
                return true;
            case R.id.actionArquivar:
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.lbl_confirmar))
                        .setMessage(getString(R.string.alert_continue)
                        ).setPositiveButton(R.string.lbl_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(item.getTitle().toString() == getText(R.string.lbl_arquivar)){
                            serviceOrder.setActive(true);
                        }else{
                            serviceOrder.setActive(false);
                        }
                        serviceOrder.save();
                        Toast.makeText(ServiceOrderListActivity.this, getString(R.string.lbl_delete_success), Toast.LENGTH_LONG).show();

                        atualizarLista("0");
                    }
                })
                        .setNeutralButton(R.string.lbl_no, null)
                        .create().show();
                ;

                return true;

        }

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_service_order_list_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * @see <a href="http://developer.android.com/guide/components/intents-filters.html">Forcing an app chooser</a>
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionShare:
                // Create the text message with a string
                final Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, ServiceOrder.getAll("0").toString());
                sendIntent.setType(HTTP.PLAIN_TEXT_TYPE);

                // Create intent to show the chooser dialog
                final Intent chooser = Intent.createChooser(sendIntent, "Compartilhar");

                // Verify the original intent will resolve to at least one activity
                if (sendIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(chooser);
                }
                return true;
            case R.id.actionArquivados:
                atualizarLista("1");
                return true;
            case R.id.actionNaoArquivados:
                atualizarLista("0");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
