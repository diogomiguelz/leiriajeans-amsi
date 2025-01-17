package com.example.leiriajeansamsi;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;

import com.example.leiriajeansamsi.Modelo.SingletonProdutos;
import com.example.leiriajeansamsi.Modelo.Utilizador;
import com.example.leiriajeansamsi.listeners.UtilizadorDataListener;

public class PerfilFragment extends Fragment {

    private View view;
    private TextView tvUsername, tvNome, tvEmail, tvTelefone, tvNIF, tvRua, tvLocalidade, tvCodigoPostal;
    private Button btnEditarPerfil;
    private String username, nome, email, telefone, nif, rua, localidade, codigoPostal;
    private Utilizador utilizador, utilizadorData;

    private SingletonProdutos singleton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_perfil, container, false);

        singleton = SingletonProdutos.getInstance(getContext());

        // usar o listener para aguardar os dados
        singleton.getUserDataAPI(getContext(), new UtilizadorDataListener() {
            @Override
            public void onGetUtilizadorData(Utilizador utilizadorData) {
                // Quando os dados são recebidos, atualize a UI
                if (utilizadorData != null) {
                    PerfilFragment.this.utilizadorData = utilizadorData;
                    updateUI();
                }
            }
        });

        // Find TextView objects by their IDs
        //tvUsername = view.findViewById(R.id.tvUsername);
        tvNome = view.findViewById(R.id.tvNomeProprio);
        //tvEmail = view.findViewById(R.id.tvEmail);
        tvTelefone = view.findViewById(R.id.tvTelefone);
        tvNIF = view.findViewById(R.id.tvNif);
        tvRua = view.findViewById(R.id.tvRua);
        tvLocalidade = view.findViewById(R.id.tvLocalidade);
        tvCodigoPostal = view.findViewById(R.id.tvCodPostal);
        btnEditarPerfil = view.findViewById(R.id.btnEditarPerfil);

        return view;
    }

    // Metodo para atualizar os dados na interface
    private void updateUI() {
        if (utilizadorData != null) {
            //username = utilizadorData.getUsername();
            nome = utilizadorData.getNome();
            //email = utilizadorData.getEmail();
            telefone = utilizadorData.getTelefone();
            nif = utilizadorData.getNif();
            rua = utilizadorData.getRua();
            localidade = utilizadorData.getLocalidade();
            codigoPostal = utilizadorData.getCodpostal();
        }

        Log.d("O TELEFONE DO UTILIZADOR É", telefone);
        Log.d("O NIF DO UTILIZADOR É", nif);
        Log.d("A RUA DO UTILIZADOR É", rua);
        Log.d("A LOCALIDADE DO UTILIZADOR É", localidade);
        Log.d("O CODIGO POSTAL DO UTILIZADOR É", codigoPostal);


        // Atualize os TextViews
        //if (tvUsername != null) {
        //    tvUsername.setText(username != null ? username : "por definir");
        //}
        if (tvNome != null) {
            tvNome.setText(nome != null ? nome : "por definir");
        }
        //if (tvEmail != null) {
        //    tvEmail.setText(email != null ? email : "por definir");
        //}
        if (tvTelefone != null) {
            tvTelefone.setText(telefone != null ? telefone : "por definir");
        }
        if (tvNIF != null) {
            tvNIF.setText(nif != null ? nif : "por definir");
        }
        if (tvRua != null) {
            tvRua.setText(rua != null ? rua : "por definir");
        }
        if (tvLocalidade != null) {
            tvLocalidade.setText(localidade != null ? localidade : "por definir");
        }
        if (tvCodigoPostal != null) {
            tvCodigoPostal.setText(codigoPostal != null ? codigoPostal : "por definir");
        }

        // Configura o botão para editar o perfil
        btnEditarPerfil.setOnClickListener(v -> {
            // Inicia a transação para substituir o fragmento atual pelo PerfilEditFragment
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            // Substitui o fragmento atual pelo PerfilEditFragment
            transaction.replace(R.id.fragment_container, new PerfilEditFragment()); // R.id.fragment_container é o ID do contêiner onde os fragments são adicionados
            // Adiciona à pilha de fragmentos (opcional, caso você queira voltar para o PerfilFragment)
            transaction.addToBackStack(null);
            // Executa a transação
            transaction.commit();
        });
    }
}

