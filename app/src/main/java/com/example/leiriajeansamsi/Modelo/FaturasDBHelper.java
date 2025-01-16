package com.example.leiriajeansamsi.Modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FaturasDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "BDFaturas";
    public static final String TABLE_NAME = "Faturas";

    private static final int DB_VERSION = 1;

    private SQLiteDatabase db;

    // Colunas da tabela
    private static final String ID = "id";
    private static final String METODOPAGAMENTO_ID = "metodopagamento";
    private static final String METODOEXPEDICAO_ID = "metodoexpedicao";
    private static final String USERDATA_ID = "userdata_id";
    private static final String DATA= "data";
    private static final String VALORTOTAL = "valortotal";
    private static final String STATUSPEDIDO = "statuspedido";

    public FaturasDBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreateTableFaturas = "CREATE TABLE " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                METODOPAGAMENTO_ID + " INTEGER, " +
                METODOEXPEDICAO_ID + " INTEGER, " +
                USERDATA_ID + " INTEGER, " +
                DATA + " TEXT, " +
                VALORTOTAL + " FLOAT, " +
                STATUSPEDIDO + " TEXT" +
                ");";
        db.execSQL(sqlCreateTableFaturas);
    }

    public Fatura adicionarFaturaBD(Fatura fatura) {
        ContentValues values = new ContentValues();

        // Adiciona os valores da fatura à base de dados
        values.put(METODOPAGAMENTO_ID, fatura.getMetodopagamento());
        values.put(METODOEXPEDICAO_ID, fatura.getMetodoexpedicao());
        values.put(USERDATA_ID, fatura.getUserdata_id());
        values.put(DATA, fatura.getData());
        values.put(VALORTOTAL, fatura.getValorTotal());
        values.put(STATUSPEDIDO, fatura.getStatuspedido());

        // ADD Fatura à base de dados
        db.insert(TABLE_NAME, null, values);

        return fatura; // Retorna o objeto da fatura inserida
    }

    // Recebe a lista de faturas da base de dados para um utilizador específico
    public List<Fatura> getAllFaturas(int utilizadorId) {
        ArrayList<Fatura> faturasList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        String selection = USERDATA_ID + " = ?";
        String[] selectionArgs = {String.valueOf(utilizadorId)};
        Cursor cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(ID));
                int metodoPagamento = cursor.getInt(cursor.getColumnIndexOrThrow(METODOPAGAMENTO_ID));
                int metodoExpedicao = cursor.getInt(cursor.getColumnIndexOrThrow(METODOEXPEDICAO_ID));
                String data = cursor.getString(cursor.getColumnIndexOrThrow(DATA));
                float valorTotal = cursor.getFloat(cursor.getColumnIndexOrThrow(VALORTOTAL));
                String statusPedido = cursor.getString(cursor.getColumnIndexOrThrow(STATUSPEDIDO));

                // Cria uma nova fatura com os valores obtidos
                Fatura fatura = new Fatura(id, metodoPagamento, metodoExpedicao, utilizadorId, data, valorTotal, statusPedido);
                faturasList.add(fatura);
            }

            cursor.close();
        }

        db.close();
        return faturasList;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


}
