package com.example.leiriajeansamsi;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leiriajeansamsi.Modelo.Fatura;
import com.example.leiriajeansamsi.Modelo.LinhaCarrinho;
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
    private Spinner spinnerMetodoPagamento, spinnerMetodoExpedicao;
    private EditText etNumeroMBWay, etEmailPayPal;
    private LinearLayout containerMultibanco;
    private TextView tvValorMultibanco;
    private TextView tvTotalCompra;
    private float valorTotal = 0;
    private Button btnFinalizarCompra;
    private LinhasFaturasAdaptador adaptador;
    private List<LinhaFatura> linhasFaturas = new ArrayList<>();
    private List<MetodoPagamento> metodosPagamento = new ArrayList<>();
    private List<MetodoExpedicao> metodosExpedicao = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Inicializar todas as views
        inicializarComponentes();

        // Configurar RecyclerView
        configurarRecyclerView();

        // Carregar dados
        carregarDados();

        // Configurar listeners
        configurarListeners();
    }

    private void inicializarComponentes() {
        // Usar as variáveis de classe ao invés de criar novas locais
        recyclerLinhasCheckout = findViewById(R.id.recyclerLinhasCheckout);
        spinnerMetodoPagamento = findViewById(R.id.spinnerMetodoPagamento);
        spinnerMetodoExpedicao = findViewById(R.id.spinnerMetodoExpedicao);
        btnFinalizarCompra = findViewById(R.id.btnFinalizarCompra);
        tvTotalCompra = findViewById(R.id.tvTotalCompra);
        tvValorMultibanco = findViewById(R.id.tvValorMultibanco);
        containerMultibanco = findViewById(R.id.containerMultibanco);
        etNumeroMBWay = findViewById(R.id.etNumeroMBWay);
        etEmailPayPal = findViewById(R.id.etEmailPayPal);
    }

    private void configurarRecyclerView() {
        adaptador = new LinhasFaturasAdaptador(this, linhasFaturas);
        recyclerLinhasCheckout.setLayoutManager(new LinearLayoutManager(this));
        recyclerLinhasCheckout.setAdapter(adaptador);
    }

    private void carregarDados() {
        carregarLinhasFaturas();
        carregarMetodosPagamento(spinnerMetodoPagamento);
        carregarMetodosExpedicao(spinnerMetodoExpedicao);
        atualizarTotal();
    }

    private void configurarListeners() {
        btnFinalizarCompra.setOnClickListener(v -> validarEFinalizarCompra());

        spinnerMetodoPagamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!metodosPagamento.isEmpty()) {
                    atualizarCamposPagamento(metodosPagamento.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                esconderTodosCamposPagamento();
            }
        });

        spinnerMetodoExpedicao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                atualizarTotal();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                atualizarTotal();
            }
        });
    }



    private void atualizarCamposPagamento(MetodoPagamento metodoPagamento) {
        esconderTodosCamposPagamento();

        switch (metodoPagamento.getNome().toLowerCase()) {
            case "mbway":
                etNumeroMBWay.setVisibility(View.VISIBLE);
                break;
            case "paypal":
                etEmailPayPal.setVisibility(View.VISIBLE);
                break;
            case "multibanco":
                containerMultibanco.setVisibility(View.VISIBLE);
                atualizarDadosMultibanco();
                break;
        }
    }

    private void atualizarTotal() {
        float subtotal = 0;
        float totalIva = 0;

        for (LinhaFatura linha : linhasFaturas) {
            subtotal += linha.getSubTotal();
            totalIva += linha.getValorIva();
        }

        float custoExpedicao = 0;
        if (!metodosExpedicao.isEmpty() && spinnerMetodoExpedicao != null &&
                spinnerMetodoExpedicao.getSelectedItemPosition() >= 0) {
            MetodoExpedicao metodoSelecionado = metodosExpedicao.get(spinnerMetodoExpedicao.getSelectedItemPosition());
            custoExpedicao = metodoSelecionado.getCusto();
        }

        valorTotal = subtotal + custoExpedicao;

        tvTotalCompra.setText(String.format("Subtotal: %.2f€\nIVA: %.2f€\nExpedição: %.2f€\nTotal: %.2f€",
                subtotal, totalIva, custoExpedicao, valorTotal));

        if (containerMultibanco.getVisibility() == View.VISIBLE) {
            atualizarDadosMultibanco();
        }
    }



    private void atualizarDadosMultibanco() {
        tvValorMultibanco.setText(String.format("Valor a pagar: %.2f€", valorTotal));
    }

    private void validarEFinalizarCompra() {
        if (metodosPagamento.isEmpty() || metodosExpedicao.isEmpty()) {
            Toast.makeText(this, "Selecione métodos válidos", Toast.LENGTH_SHORT).show();
            return;
        }

        MetodoPagamento metodoPagamento = metodosPagamento.get(spinnerMetodoPagamento.getSelectedItemPosition());

        switch (metodoPagamento.getNome().toLowerCase()) {
            case "mbway":
                String numeroMBWay = etNumeroMBWay.getText().toString().trim();
                if (numeroMBWay.isEmpty()) {
                    etNumeroMBWay.setError("Insira o número de telefone");
                    return;
                }
                if (!numeroMBWay.matches("^9[1236][0-9]{7}$")) {
                    etNumeroMBWay.setError("Número de telefone inválido");
                    return;
                }
                break;

            case "paypal":
                String emailPayPal = etEmailPayPal.getText().toString().trim();
                if (emailPayPal.isEmpty()) {
                    etEmailPayPal.setError("Insira o email PayPal");
                    return;
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailPayPal).matches()) {
                    etEmailPayPal.setError("Email inválido");
                    return;
                }
                break;
        }

        // Se passou pelas validações, prossegue com a finalização
        finalizarCompra(spinnerMetodoPagamento, spinnerMetodoExpedicao);
    }

    private void esconderTodosCamposPagamento() {
        etNumeroMBWay.setVisibility(View.GONE);
        etEmailPayPal.setVisibility(View.GONE);
        containerMultibanco.setVisibility(View.GONE);
    }

    private void carregarLinhasFaturas() {
        linhasFaturas.clear();
        // Converte LinhaCarrinho para LinhaFatura
        for (LinhaCarrinho linhaCarrinho : SingletonProdutos.getInstance(this).getLinhaCarrinhos()) {
            // Calcular o valor do IVA corretamente
            float precoVenda = linhaCarrinho.getProduto().getPreco();
            float taxaIva = linhaCarrinho.getProduto().getIva() / 100f; // Converte percentagem para decimal
            float valorIva = precoVenda * taxaIva * linhaCarrinho.getQuantidade();
            float subTotal = (precoVenda * linhaCarrinho.getQuantidade()) + valorIva;

            LinhaFatura linhaFatura = new LinhaFatura(
                    0,                                     // id
                    0,                                     // faturaId
                    0,                                     // ivaId
                    linhaCarrinho.getProduto().getId(),    // produtoId
                    precoVenda,                            // precoVenda
                    valorIva,                              // valorIva (corrigido)
                    subTotal,                              // subTotal (corrigido)
                    linhaCarrinho.getQuantidade()          // quantidade
            );
            linhasFaturas.add(linhaFatura);
        }
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

        Log.d("CheckoutActivity", "A iniciar finalização da compra");
        Log.d("CheckoutActivity", "MetodoPagamentoID: " + metodoPagamentoId);
        Log.d("CheckoutActivity", "MetodoExpedicaoID: " + metodoExpedicaoId);

        SingletonProdutos.getInstance(this).criarFaturaAPI(this, metodoPagamentoId, metodoExpedicaoId,
                new FaturasListener() {
                    @Override
                    public void onRefreshListaFatura(ArrayList<Fatura> faturas) {
                        // Não utilizado aqui
                    }

                    @Override
                    public void onFaturaCriada(Fatura faturaCriada) {
                        if (faturaCriada != null && faturaCriada.getId() > 0) {
                            Log.d("CheckoutActivity", "Fatura criada com ID: " + faturaCriada.getId());
                            processarLinhasFatura(faturaCriada);
                        } else {
                            runOnUiThread(() -> {
                                Toast.makeText(CheckoutActivity.this,
                                        "Erro: Fatura criada inválida", Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                });
    }

    private void processarLinhasFatura(Fatura fatura) {
        if (fatura == null || fatura.getId() <= 0) {
            Toast.makeText(CheckoutActivity.this,
                    "Erro: ID da fatura inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        linhasFaturas.clear();
        // Converte LinhaCarrinho para LinhaFatura
        for (LinhaCarrinho linhaCarrinho : SingletonProdutos.getInstance(this).getLinhaCarrinhos()) {
            LinhaFatura linhaFatura = new LinhaFatura(
                    0,                                     // id
                    fatura.getId(),                        // faturaId
                    0,                                     // ivaId
                    linhaCarrinho.getProduto().getId(),    // produtoId
                    linhaCarrinho.getProduto().getPreco(), // precoVenda
                    linhaCarrinho.getProduto().getIva(),   // valorIva
                    linhaCarrinho.getSubTotal(),           // subTotal
                    linhaCarrinho.getQuantidade()          // quantidade
            );
            linhasFaturas.add(linhaFatura);
        }

        final int[] processadas = {0};
        final int total = linhasFaturas.size();

        Log.d("CheckoutActivity", "ID da Fatura: " + fatura.getId());
        Log.d("CheckoutActivity", "Total de linhas a processar: " + total);

        for (LinhaFatura linha : linhasFaturas) {
            Log.d("CheckoutActivity", "Criando linha com fatura_id: " + linha.getFaturaId());

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
