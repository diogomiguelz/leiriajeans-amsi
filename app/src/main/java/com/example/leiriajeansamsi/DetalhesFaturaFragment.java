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
import com.example.leiriajeansamsi.Modelo.SingletonProdutos;
import com.example.leiriajeansamsi.adaptadores.LinhasFaturasAdaptador;
import com.example.leiriajeansamsi.listeners.FaturaListener;
import com.example.leiriajeansamsi.listeners.FaturasListener;
import com.example.leiriajeansamsi.listeners.LinhasFaturasListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
            tvDataFatura.setText("Carregando data...");
            tvEstadoFatura.setText("Carregando estado...");
            tvTotalFatura.setText("Carregando total...");

            Log.d("DetalhesFatura", "tvDataFatura inicializado: " + (tvDataFatura != null));
            Log.d("DetalhesFatura", "tvEstadoFatura inicializado: " + (tvEstadoFatura != null));
            Log.d("DetalhesFatura", "tvTotalFatura inicializado: " + (tvTotalFatura != null));

            recyclerLinhasCheckout.setLayoutManager(new LinearLayoutManager(getContext()));
        } catch (Exception e) {
            Log.e("DetalhesFatura", "Erro ao inicializar views: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void carregarDetalhesFatura() {
        Log.d("DetalhesFatura", "Carregando detalhes da fatura ID: " + faturaId);

        if (faturaId != -1) {
            // Primeiro, carregar as linhas da fatura
            SingletonProdutos.getInstance(getContext()).setLinhasFaturasListener(this);
            SingletonProdutos.getInstance(getContext()).getLinhasFaturasAPI(getContext(), faturaId, this);

            // Criar o listener para os detalhes da fatura
            FaturasListener faturasListener = new FaturasListener() {
                @Override
                public void onRefreshListaFatura(ArrayList<Fatura> faturas) {
                    if (faturas != null && !faturas.isEmpty() && isAdded()) {
                        Log.d("DetalhesFatura", "Fatura recebida: " + faturas.get(0).toString());
                        atualizarInterfaceFatura(faturas.get(0));
                    } else {
                        Log.e("DetalhesFatura", "Lista de faturas vazia ou null");
                    }
                }

                @Override
                public void onFaturaCriada(Fatura fatura) {
                    // Não necessário implementar
                }
            };

            // Depois, carregar os detalhes da fatura
            SingletonProdutos.getInstance(getContext()).getFaturasAPI(getContext(), faturaId, faturasListener);
        } else {
            Log.e("DetalhesFatura", "ID da fatura inválido");
        }
    }

    private void atualizarInterfaceFatura(Fatura fatura) {
        Log.d("DetalhesFatura", "Atualizando interface com fatura: " + fatura.toString());

        try {
            // Formatação da data
            if (fatura.getData() != null) {
                String dataFormatada = formatarData(fatura.getData());
                if (tvDataFatura != null) {
                    tvDataFatura.setText(dataFormatada);
                    Log.d("DetalhesFatura", "Data atualizada: " + dataFormatada);
                } else {
                    Log.e("DetalhesFatura", "tvDataFatura é null");
                }
            }

            // Formatação do estado
            if (fatura.getStatusPedido() != null) {
                String estadoFormatado = formatarEstado(fatura.getStatusPedido());
                if (tvEstadoFatura != null) {
                    tvEstadoFatura.setText(estadoFormatado);
                    Log.d("DetalhesFatura", "Estado atualizado: " + estadoFormatado);
                } else {
                    Log.e("DetalhesFatura", "tvEstadoFatura é null");
                }
            }

            // Formatação do total
            if (tvTotalFatura != null) {
                tvTotalFatura.setText(String.format(Locale.getDefault(), "Total: %.2f EUR", fatura.getValorTotal()));
                Log.d("DetalhesFatura", "Total atualizado: " + String.format(Locale.getDefault(), "%.2f EUR", fatura.getValorTotal()));
            } else {
                Log.e("DetalhesFatura", "tvTotalFatura é null");
            }

        } catch (Exception e) {
            Log.e("DetalhesFatura", "Erro ao atualizar interface: " + e.getMessage());
            e.printStackTrace();
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
    public void onRefreshListaLinhasFaturas(int faturaId, ArrayList<LinhaFatura> linhasFatura) {
        if (linhasFatura != null && isAdded()) {
            Log.d("DetalhesFatura", "Linhas da fatura carregadas: " + linhasFatura.size());
            adapter = new LinhasFaturasAdaptador(getContext(), linhasFatura);
            recyclerLinhasCheckout.setAdapter(adapter);
        }
    }

    @Override
    public void onLinhaFaturaCriada(LinhaFatura linhaFatura) {
        // Implementação necessária do método abstrato
    }


}