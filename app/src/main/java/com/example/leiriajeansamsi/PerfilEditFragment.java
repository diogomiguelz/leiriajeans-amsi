package com.example.leiriajeansamsi;

import android.os.Bundle;
import android.util.Log;
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

        // Usando o listener para aguardar os dados
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


    // Método para salvar as alterações realizadas pelo usuário
    public void onClickGuardar() {
        if (getContext() != null) {
            singleton.updateProfileAPI(
                    etRua.getText().toString(),
                    etCodPostal.getText().toString(),
                    etLocalidade.getText().toString(),
                    etNif.getText().toString(),
                    etTelefone.getText().toString(),
                    etNomeUtilizador.getText().toString(),
                    getContext()
            );

            // Atualizar os dados do usuário
            singleton.getUserDataAPI(getContext(), new UtilizadorDataListener() {
                @Override
                public void onGetUtilizadorData(Utilizador utilizadorData) {
                    Log.d("PerfilEditFragment", "Dados do utilizador atualizados: " + utilizadorData.getNome());
                    Log.d("PerfilEditFragment", "Dados a enviar para o servidor: " + etRua.getText().toString() + ", " + etCodPostal.getText().toString() + ", " + etLocalidade.getText().toString() + ", " + etNif.getText().toString() + ", " + etTelefone.getText().toString() + ", " + etNomeUtilizador.getText().toString() + ", " + getContext());

                }
            });


            Toast.makeText(getContext(), "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para cancelar as alterações e voltar à tela anterior
    public void onClickCancelar() {
        if (getActivity() != null) {
            // Fecha o fragmento e retorna para a tela anterior
            getActivity().onBackPressed();
        }
    }
}