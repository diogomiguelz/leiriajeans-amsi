package com.example.leiriajeansamsi;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.leiriajeansamsi.Modelo.SingletonProdutos;
import com.example.leiriajeansamsi.Modelo.Utilizador;
import com.example.leiriajeansamsi.listeners.UtilizadorDataListener;

public class PerfilEditFragment extends Fragment {

    private View view;
    private EditText etUsername, etEmail, etRua, etCodPostal, etLocalidade, etNif, etTelefone, etNomeUtilizador;
    private Button btnGuardarPerfil;
    private String username, email, rua, codpostal, localidade, nif, telefone, nomeUtilizador;
    private Utilizador utilizador, utilizadorData;
    private SingletonProdutos singleton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_edit_perfil, container, false);

        // Inicializar os EditText
        etUsername = view.findViewById(R.id.etUsername);
        etNomeUtilizador = view.findViewById(R.id.etNomeProprio);
        etRua = view.findViewById(R.id.etRua);
        etCodPostal = view.findViewById(R.id.etCodPostal);
        etLocalidade = view.findViewById(R.id.etLocalidade);
        etNif = view.findViewById(R.id.etNif);
        etTelefone = view.findViewById(R.id.etTelefone);

        Button btnGuardarPerfil = view.findViewById(R.id.btnGuardarPerfil);
        Button btnCancelarPerfil = view.findViewById(R.id.btnCancelarPerfil);
        // Configurar listener do botão Cancelar
        btnCancelarPerfil.setOnClickListener(v -> onClickCancelar());
        btnGuardarPerfil.setOnClickListener(v -> onClickGuardar());

        singleton = SingletonProdutos.getInstance(getContext());

        // usar o listener para aguardar os dados
        singleton.getUserDataAPI(getContext(), new UtilizadorDataListener() {
            @Override
            public void onGetUtilizadorData(Utilizador utilizadorData) {
                // Quando os dados são recebidos, atualize a UI
                if (utilizadorData != null) {
                    PerfilEditFragment.this.utilizadorData = utilizadorData;
                    updateUI();  // Agora, a UI é atualizada quando os dados chegam
                } else {
                    // Caso não receba dados válidos
                    Toast.makeText(getContext(), "Erro: Dados do utilizador não carregados", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void updateUI() {
        if (utilizadorData != null) {
            // Preenche os campos com os dados recebidos
            //if (etUsername != null) etUsername.setText(utilizadorData.getUsername());
            if (etNomeUtilizador != null) etNomeUtilizador.setText(utilizadorData.getNome());
            //if (etEmail != null) etEmail.setText(utilizadorData.getEmail());
            if (etRua != null) etRua.setText(utilizadorData.getRua());
            if (etCodPostal != null) etCodPostal.setText(utilizadorData.getCodpostal());
            if (etLocalidade != null) etLocalidade.setText(utilizadorData.getLocalidade());
            if (etNif != null) etNif.setText(utilizadorData.getNif());
            if (etTelefone != null) etTelefone.setText(utilizadorData.getTelefone());
        } else {
            Toast.makeText(getContext(), "Erro: Dados do utilizador não carregados", Toast.LENGTH_SHORT).show();
        }
    }


    // Metodo para guardar as alterações realizadas pelo utilizador
    public void onClickGuardar() {
        if (getContext() != null) {
            String nomeValue = etNomeUtilizador.getText().toString().trim();
            String ruaValue = etRua.getText().toString().trim();
            String codPostalValue = etCodPostal.getText().toString().trim();
            String localidadeValue = etLocalidade.getText().toString().trim();
            String nifValue = etNif.getText().toString().trim();
            String telefoneValue = etTelefone.getText().toString().trim();

            singleton.updateProfileAPI(
                    nomeValue,
                    codPostalValue,
                    localidadeValue,
                    ruaValue,
                    nifValue,
                    telefoneValue,
                    getContext()
            );

            // wait for 1 second before going back
            new Handler().postDelayed(() -> {
                if (getActivity() != null) {

                    PerfilFragment perfilFragment = new PerfilFragment();

                    // start the transaction
                    FragmentTransaction transaction = getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction();

                    // Replace the current fragment with the PerfilFragment
                    transaction.replace(R.id.fragment_container, perfilFragment);

                    // Remover o PerfilEditFragment
                    transaction.commit();

                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }, 1000); // 1 second
        }
    }


    // Metodo para cancelar as alterações e voltar à tela anterior
    public void onClickCancelar() {
        if (getActivity() != null) {
            // Fecha o fragmento e retorna para a tela anterior
            getActivity().onBackPressed();
        }
    }
}