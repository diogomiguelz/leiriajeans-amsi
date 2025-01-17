package com.example.leiriajeansamsi.Modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FaturasDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "BDFaturas";
    public static final String TABLE_NAME = "Faturas";
    private static final String TAG = "FaturasDBHelper";

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

    public FaturasDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreateTableFaturas = "CREATE TABLE " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY, " +
                METODOPAGAMENTO_ID + " INTEGER, " +
                METODOEXPEDICAO_ID + " INTEGER, " +
                USERDATA_ID + " INTEGER, " +
                DATA + " TEXT, " +
                VALORTOTAL + " REAL, " +
                STATUSPEDIDO + " TEXT" +
                ");";
        db.execSQL(sqlCreateTableFaturas);
    }

    public boolean temFaturasOffline(int userId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_NAME +
                " WHERE " + USERDATA_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        try {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0) > 0;
            }
            return false;
        } finally {
            cursor.close();
        }
    }

    public void atualizarFaturas(ArrayList<Fatura> faturas, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            // Limpa faturas antigas
            db.delete(TABLE_NAME, USERDATA_ID + " = ?", new String[]{String.valueOf(userId)});

            for (Fatura fatura : faturas) {
                ContentValues values = new ContentValues();
                values.put(ID, fatura.getId());
                values.put(METODOPAGAMENTO_ID, fatura.getMetodoPagamentoId());
                values.put(METODOEXPEDICAO_ID, fatura.getMetodoExpedicaoId());
                values.put(USERDATA_ID, userId);
                values.put(DATA, fatura.getData());
                values.put(VALORTOTAL, fatura.getValorTotal());
                values.put(STATUSPEDIDO, fatura.getStatusPedido().toString().toLowerCase()); // Garante minúsculas

                db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    // Recebe a lista de faturas da base de dados para um utilizador específico
    public ArrayList<Fatura> getAllFaturas(int userId) {
        ArrayList<Fatura> faturas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + USERDATA_ID + " = ?";

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    try {
                        int id = cursor.getInt(cursor.getColumnIndexOrThrow(ID));
                        int metodoPagamentoId = cursor.getInt(cursor.getColumnIndexOrThrow(METODOPAGAMENTO_ID));
                        int metodoExpedicaoId = cursor.getInt(cursor.getColumnIndexOrThrow(METODOEXPEDICAO_ID));
                        String data = cursor.getString(cursor.getColumnIndexOrThrow(DATA));
                        int userDataId = cursor.getInt(cursor.getColumnIndexOrThrow(USERDATA_ID));
                        float valorTotal = cursor.getFloat(cursor.getColumnIndexOrThrow(VALORTOTAL));
                        String statusStr = cursor.getString(cursor.getColumnIndexOrThrow(STATUSPEDIDO));

                        // Log para debug do status
                        Log.d(TAG, "Status lido do BD: '" + statusStr + "'");

                        Fatura fatura = new Fatura();
                        fatura.setId(id);
                        fatura.setMetodoPagamentoId(metodoPagamentoId);
                        fatura.setMetodoExpedicaoId(metodoExpedicaoId);
                        fatura.setData(data);
                        fatura.setUserdata_id(userDataId);
                        fatura.setValorTotal(valorTotal);
                        fatura.setStatusPedidoFromString(statusStr); // Usa o método auxiliar

                        faturas.add(fatura);
                    } catch (Exception e) {
                        Log.e(TAG, "Erro ao ler fatura do cursor: " + e.getMessage());
                    }
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return faturas;
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


}
