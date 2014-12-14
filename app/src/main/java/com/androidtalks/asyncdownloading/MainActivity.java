package com.androidtalks.asyncdownloading;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.androidtalks.asyncdownloading.adapters.GridAdapter;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Log que tiene ION
        Ion.getDefault(this).configure().setLogging("ion-android-talks", Log.DEBUG);

        // Vista para mostrar los resultados de la busqueda
        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        GridView gridView = (GridView) findViewById(R.id.results);
        gridView.setNumColumns(cols);

        final GridAdapter gridAdapter = new GridAdapter(this);
        gridView.setAdapter(gridAdapter);


        //  Campo para ingresar la busqueda de imagenes
        final EditText searchEdit = (EditText)findViewById(R.id.search_text);


        //Obtenemos el texto por default en el editText
        getResultFromSearch(searchEdit,gridAdapter);

        // Botón para empezar la busqueda de imagenes
        Button searchButton = (Button)findViewById(R.id.search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getResultFromSearch(searchEdit,gridAdapter);
            }
        });

    }




    private void getResultFromSearch(EditText searchEditView, final GridAdapter gridAdapter){

        if(gridAdapter.getCount() > 0)
            gridAdapter.clear();

        hideKeyboard(searchEditView);
        // Iniciamos ION para hacer peticiones http
        // El resultado lo obtendremos como un Json Object
        // ION utiliza Gson

        Ion.with(MainActivity.this)
                .load(String.format("https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=%s&start=%d&rsz=8&imgsz=medium", Uri.encode(searchEditView.getText().toString()),gridAdapter.getCount()))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {

                            if (e != null)
                                throw e;
                            // Parseamos los datos que obtenemos de la busqueda y los mostramos
                            JsonArray results = result.getAsJsonObject("responseData").getAsJsonArray("results");
                            for (int i = 0; i < results.size(); i++) {
                                gridAdapter.add(results.get(i).getAsJsonObject().get("url").getAsString());
                            }
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });

    }


    //Metodo para ocultar el teclado después de presionar el botón de busqueda
    private void hideKeyboard(EditText searchEdit){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchEdit.getWindowToken(), 0);
    }

}
