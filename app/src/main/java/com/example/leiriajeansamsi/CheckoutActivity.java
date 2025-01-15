package com.example.leiriajeansamsi;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leiriajeansamsi.Modelo.Fatura;
import com.example.leiriajeansamsi.Modelo.LinhaFatura;
import com.example.leiriajeansamsi.Modelo.MetodoExpedicao;
import com.example.leiriajeansamsi.Modelo.MetodoPagamento;
import com.example.leiriajeansamsi.Modelo.SingletonProdutos;
import com.example.leiriajeansamsi.adaptadores.LinhasFaturasAdaptador;
import com.example.leiriajeansamsi.listeners.FaturasListener;
import com.example.leiriajeansamsi.listeners.LinhasFaturasListener;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    private RecyclerView recyclerLinhasCheckout;
    private Button btnFinalizarCompra;
    private LinhasFaturasAdaptador adaptador;
    private List<LinhaFatura> linhasFaturas = new ArrayList<>();
    private List<MetodoPagamento> metodosPagamento = new ArrayList<>();
    private List<MetodoExpedicao> metodosExpedicao = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        recyclerLinhasCheckout = findViewById(R.id.recyclerLinhasCheckout);
        btnFinalizarCompra = findViewById(R.id.btnFinalizarCompra);
        Spinner spinnerMetodoPagamento = findViewById(R.id.spinnerMetodoPagamento);
        Spinner spinnerMetodoExpedicao = findViewById(R.id.spinnerMetodoExpedicao);

        adaptador = new LinhasFaturasAdaptador(this, linhasFaturas);
        recyclerLinhasCheckout.setLayoutManager(new LinearLayoutManager(this));
        recyclerLinhasCheckout.setAdapter(adaptador);

        carregarLinhasFaturas();
        carregarMetodosPagamento(spinnerMetodoPagamento);
        carregarMetodosExpedicao(spinnerMetodoExpedicao);

        btnFinalizarCompra.setOnClickListener(v -> finalizarCompra(spinnerMetodoPagamento, spinnerMetodoExpedicao));
    }

    private void carregarLinhasFaturas() {
        linhasFaturas.clear();
        linhasFaturas.addAll(SingletonProdutos.getInstance(this).convertCarrinhoParaLinhasFaturas());
        adaptador.notifyDataSetChanged();
    }

    private void carregarMetodosPagamento(Spinner spinner) {
        SingletonProdutos.getInstance(this).getMetodosPagamentoAPI(this, metodos -> {
            if (metodos != null) {
                metodosPagamento.clear();
                metodosPagamento.addAll(metodos);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, extractNomesMetodosPagamento(metodos));
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            } else {
                Toast.makeText(this, "Erro ao carregar métodos de pagamento", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void carregarMetodosExpedicao(Spinner spinner) {
        SingletonProdutos.getInstance(this).getMetodosExpedicaoAPI(this, metodos -> {
            if (metodos != null) {
                metodosExpedicao.clear();
                metodosExpedicao.addAll(metodos);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, extractNomesMetodosExpedicao(metodos));
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            } else {
                Toast.makeText(this, "Erro ao carregar métodos de expedição", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void finalizarCompra(Spinner spinnerMetodoPagamento, Spinner spinnerMetodoExpedicao) {
        if (metodosPagamento.isEmpty() || metodosExpedicao.isEmpty()) {
            Toast.makeText(this, "Selecione métodos válidos", Toast.LENGTH_SHORT).show();
            return;
        }

        int metodoPagamentoId = metodosPagamento.get(spinnerMetodoPagamento.getSelectedItemPosition()).getId();
        int metodoExpedicaoId = metodosExpedicao.get(spinnerMetodoExpedicao.getSelectedItemPosition()).getId();
        int userId = SingletonProdutos.getInstance(this).getUserId(this);

        Log.d("CheckoutActivity", "A iniciar finalização da compra");
        Log.d("CheckoutActivity", "UserID: " + userId);
        Log.d("CheckoutActivity", "MetodoPagamentoID: " + metodoPagamentoId);
        Log.d("CheckoutActivity", "MetodoExpedicaoID: " + metodoExpedicaoId);

        SingletonProdutos.getInstance(this).criarFaturaAPI(this, userId, metodoPagamentoId, metodoExpedicaoId,
                new FaturasListener() {
                    @Override
                    public void onRefreshListaFatura(ArrayList<Fatura> faturas) {
                        // Não utilizado aqui
                    }

                    @Override
                    public void onFaturaCriada(Fatura faturaCriada) {
                        if (faturaCriada != null && faturaCriada.getId() > 0) {
                            processarLinhasFatura(faturaCriada);
                        } else {
                            Toast.makeText(CheckoutActivity.this,
                                    "Erro: Fatura criada inválida", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void processarLinhasFatura(Fatura fatura) {
        List<LinhaFatura> linhas = SingletonProdutos.getInstance(this).convertCarrinhoParaLinhasFaturas();
        final int[] processadas = {0};
        final int total = linhas.size();

        for (LinhaFatura linha : linhas) {
            linha.setFaturaId(fatura.getId());
            SingletonProdutos.getInstance(this).criarLinhaFaturaAPI(this, linha,
                    new LinhasFaturasListener() {
                        @Override
                        public void onLinhaFaturaCriada(LinhaFatura linhaCriada) {
                            processadas[0]++;
                            Log.d("CheckoutActivity", "Linha de fatura criada (" + processadas[0] + "/" + total + ")");

                            if (processadas[0] == total) {
                                runOnUiThread(() -> {
                                    Toast.makeText(CheckoutActivity.this,
                                            "Compra finalizada com sucesso!", Toast.LENGTH_LONG).show();
                                    finish();
                                });
                            }
                        }

                        @Override
                        public void onRefreshListaLinhasFaturas(int faturaId, ArrayList<LinhaFatura> linhas) {
                            // Não utilizado aqui
                        }
                    });
        }
    }



    private List<String> extractNomesMetodosPagamento(List<MetodoPagamento> metodosPagamento) {
        List<String> nomes = new ArrayList<>();
        for (MetodoPagamento metodo : metodosPagamento) {
            nomes.add(metodo.getNome());
        }
        return nomes;
    }

    private List<String> extractNomesMetodosExpedicao(List<MetodoExpedicao> metodosExpedicao) {
        List<String> nomes = new ArrayList<>();
        for (MetodoExpedicao metodo : metodosExpedicao) {
            nomes.add(metodo.getNome());
        }
        return nomes;
    }
}
