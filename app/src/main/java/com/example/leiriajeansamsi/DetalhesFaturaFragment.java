package com.example.leiriajeansamsi;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.leiriajeansamsi.Modelo.Fatura;
import com.example.leiriajeansamsi.Modelo.LinhaFatura;
import com.example.leiriajeansamsi.Modelo.Singleton;
import com.example.leiriajeansamsi.adaptadores.LinhasFaturasAdaptador;
import com.example.leiriajeansamsi.listeners.FaturasListener;
import com.example.leiriajeansamsi.listeners.LinhasFaturasListener;
import com.example.leiriajeansamsi.Modelo.MetodoExpedicao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class DetalhesFaturaFragment extends Fragment implements LinhasFaturasListener {
    private RecyclerView recyclerLinhasCheckout;
    private LinhasFaturasAdaptador adapter;
    private TextView tvDataFatura, tvEstadoFatura, tvTotalFatura;
    private int faturaId;
    private ImageButton btnVoltar;
    private List<MetodoExpedicao> metodosExpedicao = new ArrayList<>();
    private int metodoExpedicaoId; // Para armazenar o ID do método selecionado

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalhes_fatura, container, false);

        if (getArguments() != null) {
            faturaId = getArguments().getInt("FATURA_ID", -1);
        }

        if (getArguments() != null) {
            faturaId = getArguments().getInt("FATURA_ID", -1);
            Log.d("DetalhesFatura", "ID da fatura recebido: " + faturaId);
        } else {
            Log.e("DetalhesFatura", "Nenhum argumento recebido");
        }

        initViews(view);
        carregarDetalhesFatura();

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });


        return view;
    }

    private void initViews(View view) {
        try {
            recyclerLinhasCheckout = view.findViewById(R.id.recyclerLinhasCheckout);
            tvDataFatura = view.findViewById(R.id.tvDataFatura);
            tvEstadoFatura = view.findViewById(R.id.tvEstadoFatura);
            tvTotalFatura = view.findViewById(R.id.tvTotalFatura);
            btnVoltar = view.findViewById(R.id.btnVoltar);

            // Verificar se as views foram encontradas
            if (tvDataFatura == null) throw new IllegalStateException("tvDataFatura não encontrado");
            if (tvEstadoFatura == null) throw new IllegalStateException("tvEstadoFatura não encontrado");
            if (tvTotalFatura == null) throw new IllegalStateException("tvTotalFatura não encontrado");

            // Definir textos iniciais
            tvDataFatura.setText("A carregar data...");
            tvEstadoFatura.setText("A carregar estado...");
            tvTotalFatura.setText("A carregar total...");

            Log.d("DetalhesFatura", "tvDataFatura inicializar: " + (tvDataFatura != null));
            Log.d("DetalhesFatura", "tvEstadoFatura inicializar: " + (tvEstadoFatura != null));
            Log.d("DetalhesFatura", "tvTotalFatura inicializar: " + (tvTotalFatura != null));

            recyclerLinhasCheckout.setLayoutManager(new LinearLayoutManager(getContext()));
        } catch (Exception e) {
            Log.e("DetalhesFatura", "Erro ao inicializar views: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void carregarDetalhesFatura() {
        Log.d("DetalhesFatura", "Carregar detalhes da fatura ID: " + faturaId);

        if (faturaId != -1) {
            // Carregar as linhas da fatura
            Singleton.getInstance(getContext()).getLinhasFaturasAPI(getContext(), faturaId, this);

            // Carregar os detalhes da fatura
            Singleton.getInstance(getContext()).getFaturasAPI(getContext());

            // Atualizar o listener para receber as faturas
            Singleton.getInstance(getContext()).setFaturasListener(new FaturasListener() {
                @Override
                public void onRefreshListaFatura(ArrayList<Fatura> faturas) {
                    if (faturas != null && !faturas.isEmpty() && isAdded()) {
                        // Procurar a fatura específica pelo ID
                        for (Fatura fatura : faturas) {
                            if (fatura.getId() == faturaId) {
                                Log.d("DetalhesFatura", "Fatura encontrada: " + fatura.toString());
                                atualizarInterfaceFatura(fatura);
                                break;
                            }
                        }
                    } else {
                        Log.e("DetalhesFatura", "Lista de faturas vazia ou null");
                    }
                }

                @Override
                public void onFaturaCriada(Fatura fatura) {
                    // Não necessário implementar
                }
            });
        } else {
            Log.e("DetalhesFatura", "ID da fatura inválido");
        }
    }

    private void atualizarInterfaceFatura(Fatura fatura) {
        if (!isAdded() || getContext() == null) return;

        Log.d("DetalhesFatura", "Atualizando interface com fatura: " + fatura.toString());

        // Formatação da data
        if (fatura.getData() != null) {
            String dataFormatada = formatarData(fatura.getData());
            if (tvDataFatura != null) {
                tvDataFatura.setText(dataFormatada);
            }
        }

        // Formatação do estado
        if (fatura.getStatusPedido() != null) {
            String estadoFormatado = formatarEstado(fatura.getStatusPedido());
            if (tvEstadoFatura != null) {
                tvEstadoFatura.setText(estadoFormatado);
            }
        }

        metodoExpedicaoId = fatura.getMetodoExpedicaoId();
        atualizarTotais();
    }

    private void atualizarTotais() {
        if (tvTotalFatura == null || !isAdded()) return;

        if (adapter == null || adapter.getLinhasFaturas() == null || adapter.getLinhasFaturas().isEmpty()) {
            Log.d("DetalhesFatura", "Adapter ou linhas não disponíveis ainda");
            return;
        }

        // Declarar as variáveis como final para usar no lambda
        final float[] subtotal = {0};
        final float[] ivaTotal = {0};
        final float[] custoExpedicao = {0};

        // Calcular subtotal e IVA primeiro
        for (LinhaFatura linha : adapter.getLinhasFaturas()) {
            if (linha != null) {
                float precoUnitario = linha.getPrecoVenda();
                int quantidade = linha.getQuantidade();
                float valorIva = precoUnitario * (linha.getValorIva() / 100f) * quantidade;

                subtotal[0] += precoUnitario * quantidade;
                ivaTotal[0] += valorIva;
            }
        }

        // Carregar custo de expedição
        Singleton.getInstance(getContext()).getMetodosExpedicaoAPI(getContext(), metodos -> {
            if (metodos != null) {
                for (MetodoExpedicao metodo : metodos) {
                    if (metodo.getId() == metodoExpedicaoId) {
                        custoExpedicao[0] = metodo.getCusto();
                        break;
                    }
                }
            }

            float total = subtotal[0] + ivaTotal[0] + custoExpedicao[0];

            // Atualizar a interface na thread principal
            if (isAdded()) {
                requireActivity().runOnUiThread(() -> {
                    tvTotalFatura.setText(String.format(Locale.getDefault(),
                            "Preço unitário: %.2f€\n" +
                                    "IVA: %.2f€\n" +
                                    "Expedição: %.2f€\n" +
                                    "Total: %.2f€",
                            subtotal[0], ivaTotal[0], custoExpedicao[0], total));
                });
            }
        });
    }

    @Override
    public void onRefreshListaLinhasFaturas(int faturaId, ArrayList<LinhaFatura> linhasFatura) {
        if (faturaId == this.faturaId && linhasFatura != null && isAdded()) {
            Log.d("DetalhesFatura", "Recebidas " + linhasFatura.size() + " linhas para fatura " + faturaId);

            ArrayList<LinhaFatura> linhasUnicas = new ArrayList<>();
            for (LinhaFatura linha : linhasFatura) {
                boolean existe = false;
                for (LinhaFatura linhaUnica : linhasUnicas) {
                    if (linhaUnica.getProdutoId() == linha.getProdutoId()) {
                        existe = true;
                        break;
                    }
                }
                if (!existe) {
                    linhasUnicas.add(linha);
                }
            }

            if (adapter != null) {
                adapter.clear();
            }

            adapter = new LinhasFaturasAdaptador(getContext(), linhasUnicas);
            recyclerLinhasCheckout.setAdapter(adapter);

            // Atualiza os totais depois que as linhas foram carregadas
            atualizarTotais();
        }
    }


    private String formatarData(String data) {
        try {
            Log.d("DetalhesFatura", "Tentando formatar data: " + data);

            // Primeiro, tente o formato completo
            SimpleDateFormat formatoEntrada;
            if (data.contains(":")) {
                formatoEntrada = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            } else {
                // Se não tiver hora, use apenas a data
                formatoEntrada = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            }

            SimpleDateFormat formatoSaida = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            Date dataParseada = formatoEntrada.parse(data);
            if (dataParseada != null) {
                String resultado = "Data: " + formatoSaida.format(dataParseada);
                Log.d("DetalhesFatura", "Data formatada com sucesso: " + resultado);
                return resultado;
            }
        } catch (ParseException e) {
            Log.e("DetalhesFatura", "Erro ao formatar data: " + e.getMessage());
            e.printStackTrace();
        }
        return "Data: " + data;
    }

    private String formatarEstado(Fatura.StatusPedido statusPedido) {
        if (statusPedido == null) {
            return "Estado: Não definido";
        }

        String estadoFormatado = statusPedido.toString();
        estadoFormatado = estadoFormatado.substring(0, 1).toUpperCase() +
                estadoFormatado.substring(1).toLowerCase();

        // Adiciona cores diferentes baseadas no estado
        String cor;
        switch (statusPedido) {
            case pendente:
                cor = "#FFA500"; // Laranja
                break;
            case pago:
                cor = "#4CAF50"; // Verde
                break;
            case anulada:
                cor = "#FF0000"; // Vermelho
                break;
            default:
                cor = "#000000"; // Preto
        }

        tvEstadoFatura.setTextColor(Color.parseColor(cor));
        return "Estado: " + estadoFormatado;
    }





    @Override
    public void onLinhaFaturaCriada(LinhaFatura linhaFatura) {
        // Implementação necessária do método abstrato
    }


}