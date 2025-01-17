package com.example.leiriajeansamsi.Modelo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import com.example.leiriajeansamsi.LoginActivity;
import com.example.leiriajeansamsi.listeners.CarrinhoListener;
import com.example.leiriajeansamsi.listeners.FaturaListener;
import com.example.leiriajeansamsi.listeners.FaturasListener;
import com.example.leiriajeansamsi.listeners.LinhaCarrinhoListener;
import com.example.leiriajeansamsi.listeners.LinhasCarrinhosListener;


import com.example.leiriajeansamsi.listeners.LinhasFaturasListener;
import com.example.leiriajeansamsi.listeners.LoginListener;
import com.example.leiriajeansamsi.listeners.ProdutoListener;
import com.example.leiriajeansamsi.listeners.ProdutosListener;
import com.example.leiriajeansamsi.listeners.ProfileUpdateListener;
import com.example.leiriajeansamsi.utils.FaturasJsonParser;
import com.example.leiriajeansamsi.listeners.SignupListener;
import com.example.leiriajeansamsi.listeners.UtilizadorDataListener;
import com.example.leiriajeansamsi.utils.LinhasFaturasJsonParser;
import com.example.leiriajeansamsi.utils.LoginJsonParser;
import com.example.leiriajeansamsi.utils.ProdutoJsonParser;

public class SingletonProdutos {

    //region DECLARAÇOES

    private int carrinhoId = -1;
    public ArrayList<Produto> produtos = new ArrayList<>();
    public ArrayList<Fatura> faturas = new ArrayList<>();
    public ArrayList<LinhaFatura> linhasFaturas = new ArrayList<>();
    public ArrayList<LinhaCarrinho> linhaCarrinhos = new ArrayList<>();
    public Carrinho carrinho;
    public Utilizador utilizador, utilizadorData;
    private static volatile SingletonProdutos instance = null;

    private static RequestQueue volleyQueue = null;
    private LoginListener loginListener;
    private UtilizadorDataListener utilizadorDataListener;
    private SignupListener signupListener;
    private ProdutosListener produtosListener;
    private ProdutoListener produtoListener;
    private CarrinhoListener carrinhoListener;
    private LinhasCarrinhosListener linhasCarrinhoListener;
    private LinhaCarrinhoListener linhaCarrinhoListener;
    private FaturasListener faturasListener;
    private FaturaListener faturaListener;
    private LinhasFaturasListener linhasFaturasListener;
    private List<LinhaCarrinho> linhasCarrinho; // Lista de produtos no carrinho
    //endregion

    //region singleton
    public static synchronized SingletonProdutos getInstance(Context context) {
        if (instance == null) {
            synchronized (SingletonProdutos.class) {
                if (instance == null) {
                    instance = new SingletonProdutos(context);
                    volleyQueue = Volley.newRequestQueue(context);
                }
            }
        }
        return instance;
    }

    private SingletonProdutos(Context context) {
        produtos = new ArrayList<>();
        volleyQueue = Volley.newRequestQueue(context); // Inicializa a RequestQueue aqui
        getAllProdutosAPI(context); // Chama o metodo para buscar produtos
    }
    //endregion

    //region GETTERS AND SETTERS

    public String getApiIP(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("api_url", Context.MODE_PRIVATE);
        return preferences.getString("API", null);
    }
    public ArrayList<Produto> getProdutos() {
        return produtos;
    }

    public ArrayList<Fatura> getFaturas() {
        return faturas;
    }

    public ArrayList<Produto> getFilteredProdutos(String query) {
        ArrayList<Produto> filteredProdutos = new ArrayList<>();
        for (Produto produto : produtos) {
            if (produto.getNome() != null && produto.getNome().toLowerCase().trim().contains(query.toLowerCase())) {
                filteredProdutos.add(produto);
            }
        }
        return filteredProdutos;
    }

    public Carrinho getCarrinho() {
        return this.carrinho;
    }

    public Utilizador getUtilizador() {
        return utilizador;
    }

    public Utilizador getUtilizadorData() {
        return utilizadorData;
    }

    public ArrayList<LinhaCarrinho> getLinhaCarrinhos() {
        return linhaCarrinhos;
    }

    public ArrayList<LinhaFatura> getLinhasFaturas() {
        return linhasFaturas;
    }

    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    public void setSignupListener(SignupListener signupListener) {
        this.signupListener = signupListener;
    }

    public void setUtilizadorDataListener(UtilizadorDataListener utilizadorDataListener) {
        this.utilizadorDataListener = utilizadorDataListener;
    }

    public void setProdutosListener(ProdutosListener produtosListener) {
        this.produtosListener = produtosListener;
    }

    public void setLinhasCarrinhoListener(LinhasCarrinhosListener listener) {
        this.linhasCarrinhoListener = listener;
    }

    public void setLinhaCarrinhoListener(LinhaCarrinhoListener linhaCarrinhoListener) {
        this.linhaCarrinhoListener = linhaCarrinhoListener;
    }

    public void setCarrinhoListener(CarrinhoListener carrinhoListener) {
        this.carrinhoListener = carrinhoListener;
    }

    public void setProdutoListener(ProdutoListener produtoListener) {
        this.produtoListener = produtoListener;
    }

    public void setFaturasListener(FaturasListener faturaListener) {
        this.faturasListener = faturaListener;
    }

    public void setFaturaListener(FaturaListener faturaListener) {
        this.faturaListener = faturaListener;
    }

    public void setLinhasFaturasListener(LinhasFaturasListener linhasFaturasListener) {
        this.linhasFaturasListener = linhasFaturasListener;
    }

    public Produto getProdutoById(int id) {
        Log.d("SingletonProdutos", "A procurar o produto com ID: " + id);
        for (Produto produto : produtos) {
            if (produto.getId() == id) {
                return produto;
            }
        }
        Log.e("SingletonProdutos", "Produto com ID " + id + " não encontrado.");
        return null;
    }

    public Fatura getFaturaById(int id) {
        for (Fatura fatura : faturas) {
            if (fatura.getId() == id)
                return fatura;
        }
        return null;
    }

    public LinhaCarrinho getLinhaCarrinho(int id) {
        for (LinhaCarrinho linhaCarrinho : linhaCarrinhos) {
            if (linhaCarrinho.getId() == id)
                return linhaCarrinho;
        }
        return null;
    }

    public List<LinhaCarrinho> getLinhasCarrinho() {
        if (linhasCarrinho == null) {
            linhasCarrinho = new ArrayList<>();
        }
        return linhasCarrinho;
    }


    //endregion

    //region PEDIDOS API CRUD

    //region user

    public int getUserId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return preferences.getInt("user_id", 0); // 0 is the default value if the user ID is not found
    }

    public String getUsername(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return preferences.getString("username", "");
    }

    public String getUserToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return preferences.getString("user_token", null);
    }

    public void loginAPI(final String username, final String password, final Context context) {
        if (!ProdutoJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, "Não tem ligação à internet", Toast.LENGTH_SHORT).show();
            if (loginListener != null) {
                loginListener.onUpdateLogin(null);
            }
            return;
        }

        String mUrlAPI = mUrlAPILogin(context);
        Log.d("LoginAPI", "URL: " + mUrlAPI);

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("username", username);
            jsonParams.put("password", password);
            Log.d("LoginAPI", "Dados enviados: " + jsonParams.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            if (loginListener != null) {
                loginListener.onUpdateLogin(null);
            }
            return;
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, mUrlAPI, jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("LoginAPI", "Resposta: " + response.toString());
                        try {
                            utilizador = LoginJsonParser.parserJsonLogin(response);
                            if (utilizador != null && utilizador.getAuth_key() != null) {
                                saveUserId(context, utilizador.getId());
                                saveUserToken(context, utilizador.getAuth_key(), utilizador.getUsername());
                                saveUsername(context, utilizador.getUsername());

                                if (loginListener != null) {
                                    loginListener.onUpdateLogin(utilizador);
                                }
                            } else {
                                if (loginListener != null) {
                                    loginListener.onUpdateLogin(null);
                                }
                            }
                        } catch (Exception e) {
                            Log.e("LoginAPI", "Erro: " + e.getMessage());
                            if (loginListener != null) {
                                loginListener.onUpdateLogin(null);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String mensagemErro = "Credenciais incorretas";
                        Toast.makeText(context, mensagemErro, Toast.LENGTH_SHORT).show();
                        if (loginListener != null) {
                            loginListener.onUpdateLogin(null);
                        }
                    }
                });

        volleyQueue.add(req);
    }

    public void getUserDataAPI(Context context, final UtilizadorDataListener listener) {
        if (!ProdutoJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, "Não tem ligação à internet", Toast.LENGTH_SHORT).show();
            return;
        }

        String username = getUsername(context);
        String accessToken = getUserToken(context);

        // Log dos dados de autenticação
        Log.d("DEBUG_API", "Username: " + username);
        Log.d("DEBUG_API", "Token: " + accessToken);

        if (username != null && accessToken != null) {
            String url = "http://172.22.21.212/leiriajeans/backend/web/api/user/dados?username=" + username + "&access-token=" + accessToken;
            Log.d("DEBUG_API", "URL da requisição: " + url);

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                // Log da resposta completa
                                Log.d("DEBUG_API", "Resposta completa: " + response.toString());

                                JSONObject user = response.getJSONObject("user");
                                JSONObject userForm = response.getJSONObject("userForm");

                                // Log do userForm antes do parsing
                                Log.d("DEBUG_API", "UserForm recebido: " + userForm.toString());

                                utilizadorData = LoginJsonParser.parserJsonGetUtilizadorData(userForm);

                                // Log após o parsing
                                if (utilizadorData != null) {
                                    Log.d("DEBUG_API", "Dados após parsing:" +
                                            "\nNome: " + utilizadorData.getNome() +
                                            "\nTelefone: " + utilizadorData.getTelefone() +
                                            "\nNIF: " + utilizadorData.getNif() +
                                            "\nRua: " + utilizadorData.getRua() +
                                            "\nLocalidade: " + utilizadorData.getLocalidade() +
                                            "\nCódigo Postal: " + utilizadorData.getCodpostal());

                                    if (listener != null) {
                                        listener.onGetUtilizadorData(utilizadorData);
                                    }
                                } else {
                                    Log.e("DEBUG_API", "utilizadorData é nulo após parsing");
                                    Toast.makeText(context, "Erro ao processar dados do utilizador", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                Log.e("DEBUG_API", "Erro no parsing JSON: " + e.getMessage());
                                Log.e("DEBUG_API", "Stack trace: ", e);
                                Toast.makeText(context, "Erro ao processar dados do servidor", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Melhor tratamento de erro
                            String errorMsg = "";
                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                try {
                                    String errorStr = new String(error.networkResponse.data, "UTF-8");
                                    Log.e("DEBUG_API", "Erro detalhado: " + errorStr);
                                    errorMsg = errorStr;
                                } catch (Exception e) {
                                    Log.e("DEBUG_API", "Erro ao ler resposta de erro", e);
                                    errorMsg = "Erro desconhecido";
                                }
                            } else {
                                errorMsg = error.getMessage() != null ? error.getMessage() : "Erro na conexão";
                            }

                            Log.e("DEBUG_API", "Erro ao acessar o servidor: " + errorMsg);
                            Toast.makeText(context, "Erro ao acessar o servidor: " + errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    });

            // Adicionar timeout maior
            req.setRetryPolicy(new DefaultRetryPolicy(
                    30000, // 30 segundos de timeout
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

            volleyQueue.add(req);
        } else {
            Log.e("DEBUG_API", "Username ou token nulos. Username: " + username + ", Token: " + accessToken);
            Toast.makeText(context, "Username ou Access Token não encontrados", Toast.LENGTH_SHORT).show();
        }
    }





    public void saveUserToken(Context context, String token, String username) {
        SharedPreferences preferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_token", token);
        editor.putString("username", username);
        editor.apply();
    }

    public void saveUserId(Context context, int userId) {
        SharedPreferences preferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("user_id", userId);
        editor.apply();
    }

    public void saveUsername(Context context, String username) {
        SharedPreferences preferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.apply();
    }

    public void signupAPI(
            final String username,
            final String password,
            final String email,
            final String rua,
            final String codpostal,
            final String localidade,
            final String nif,
            final String telefone,
            final String nomeUtilizador,
            final Context context) {

        if (!ProdutoJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, "Não tem ligação à internet", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject jsonParams = new JSONObject();
        try {
            // Dados para ambas as tabelas em um único JSON
            jsonParams.put("username", username);
            jsonParams.put("password", password);
            jsonParams.put("email", email);
            jsonParams.put("nome", nomeUtilizador);
            jsonParams.put("rua", rua);
            jsonParams.put("codpostal", codpostal);
            jsonParams.put("localidade", localidade);
            jsonParams.put("nif", nif);
            jsonParams.put("telefone", telefone);
        } catch (JSONException e) {
            Log.e("SignupAPI", "Erro ao criar JSON: " + e.getMessage());
            return;
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, mUrlAPISignup(context), jsonParams,
                response -> {
                    Log.d("SignupAPI", "Resposta: " + response.toString());
                    Toast.makeText(context, "Registro realizado com sucesso!", Toast.LENGTH_SHORT).show();

                    // Redireciona para o login
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    if (context instanceof Activity) {
                        ((Activity) context).finish();
                    }
                },
                error -> {
                    Toast.makeText(context, "Erro ao registrar utilizador", Toast.LENGTH_SHORT).show();
                    Log.e("SignupAPI", "Erro: " + error.getMessage());
                });

        volleyQueue.add(req);
    }


    public void logout(Context context) {
        // Clear the SharedPreferences data
        SharedPreferences preferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        utilizador = null;
        utilizadorData = null;

        // Other logout-related actions can be added here if needed

        // Redirect the user to the login screen or perform any necessary actions
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        ((Activity) context).finish(); // Finish the current activity to prevent going back to it
    }


    // Função para atualizar o perfil do utilizador via API
    public void updateProfileAPI(final String nome, final String codPostal, final String localidade,
                                 final String rua, final String nif, final String telefone,
                                 final Context context) {
        if (!ProdutoJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, "Não tem ligação à internet", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/user/update-dados?access-token=" + getUserToken(context);
        Log.d("DEBUG_UPDATE", "URL de atualização: " + url);

        StringRequest req = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("DEBUG_UPDATE", "Resposta do servidor: " + response);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.getBoolean("success")) {
                            Toast.makeText(context, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                            // Recarregar dados
                            getUserDataAPI(context, utilizadorDataListener);
                        } else {
                            Toast.makeText(context, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Erro ao processar resposta do servidor", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    String errorMsg = "";
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        try {
                            errorMsg = new String(error.networkResponse.data, "UTF-8");
                            Log.e("DEBUG_UPDATE", "Erro detalhado: " + errorMsg);
                        } catch (Exception e) {
                            errorMsg = error.getMessage();
                        }
                    }
                    Log.e("DEBUG_UPDATE", "Erro na atualização: " + errorMsg);
                    Toast.makeText(context, "Erro ao atualizar perfil: " + errorMsg, Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(getUserId(context)));
                params.put("nome", nome);
                params.put("codpostal", codPostal);
                params.put("localidade", localidade);
                params.put("rua", rua);
                params.put("nif", nif);
                params.put("telefone", telefone);

                Log.d("DEBUG_UPDATE", "Parâmetros enviados: " + params.toString());
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
        };

        volleyQueue.add(req);
    }



    //endregion

    //region produtos
    public void getAllProdutosAPI(final Context context) {
        if (!ProdutoJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, "Não tem ligação à internet", Toast.LENGTH_SHORT).show();
            if (produtosListener != null) {
                produtosListener.onRefreshListaProdutos(produtos);
            }
        } else {
            JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, mUrlAPIProdutos(context), null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            produtos = ProdutoJsonParser.parserJsonProdutos(response);
                            // Informar a vista
                            if (produtosListener != null) {
                                produtosListener.onRefreshListaProdutos(produtos);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "GetAllProdutos", Toast.LENGTH_SHORT).show();
                }
            });
            volleyQueue.add(req);
        }
    }
    //endregion

    //region carrinho e linhacarrinho

    public void getCarrinhoAPI(Context context) {
        carrinhoId = getLastCarrinhoId(context); // Recupera o carrinho guardado localmente
        if (carrinhoId > 0) {
            Log.d("CarrinhoDebug", "Carrinho já associado ao utilizador: " + carrinhoId);
            getLinhasCarrinhoAPI(context); // Carrega as linhas do carrinho
        } else {
            Log.d("CarrinhoDebug", "Carrinho não associado ao utilizador. A tentar verificar na API...");
            verificarCarrinho(context, null, 0); // Verifica ou cria novo carrinho
        }
    }

    private void criarCarrinhoAPI(Context context) {
        int userId = getUserId(context);
        String url = getBaseUrl(context) + "carrinho/criar?access-token=" + getUserToken(context);

        Log.d("CarrinhoDebug", "A criar carrinho. URL: " + url);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        carrinhoId = jsonResponse.getInt("id"); // Guarda o ID devolvido pelo backend
                        Log.d("CarrinhoDebug", "Novo carrinho criado. ID: " + carrinhoId);
                        saveLastCarrinhoId(context, carrinhoId); // Persiste o ID do carrinho
                        getLinhasCarrinhoAPI(context); // Carrega as linhas do carrinho
                    } catch (JSONException e) {
                        Log.e("CarrinhoDebug", "Erro ao processar resposta de criação: " + e.getMessage());
                    }
                },
                error -> Log.e("CarrinhoDebug", "Erro ao criar carrinho: " + error.getMessage())) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userdata_id", String.valueOf(userId));
                params.put("total", "0"); // Valor inicial
                params.put("ivatotal", "0"); // Valor inicial
                return params;
            }
        };

        volleyQueue.add(request);
    }

    public void adicionarLinhaCarrinhoAPI(Context context, Produto produto, int quantidade) {
        if (carrinhoId == -1) {
            // Se não existe carrinho, cria um e depois adiciona o produto
            verificarCarrinho(context, produto, quantidade);
            return;
        }

        String url = mUrlAPIPostLinhaCarrinho(context);
        Log.d("CarrinhoDebug", "A adicionar linha ao carrinho. URL: " + url);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("CarrinhoDebug", "Linha adicionada com sucesso: " + response);
                },
                error -> {
                    Log.e("CarrinhoDebug", "Erro ao adicionar linha: " + error.getMessage());
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("carrinho_id", String.valueOf(carrinhoId));
                params.put("produto_id", String.valueOf(produto.getId()));
                params.put("quantidade", String.valueOf(quantidade));
                return params;
            }
        };

        volleyQueue.add(request);
    }

    public void getLinhasCarrinhoAPI(Context context) {
        if (carrinhoId <= 0) {
            Log.e("CarrinhoDebug", "Carrinho não associado ao utilizador.");
            return;
        }

        String url = mUrlGetLinhasCarrinhoAPI(context, carrinhoId);
        Log.d("CarrinhoDebug", "A obter as linhas do carrinho. URL: " + url);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        ArrayList<LinhaCarrinho> linhas = new ArrayList<>();

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject linhaObj = response.getJSONObject(i);

                            int id = linhaObj.getInt("id");
                            int quantidade = linhaObj.getInt("quantidade");
                            float precoVenda = (float) linhaObj.getDouble("precoVenda");
                            float valorIva = (float) linhaObj.getDouble("valorIva");
                            int carrinhoId = linhaObj.getInt("carrinho_id");
                            int produtoId = linhaObj.getInt("produto_id");

                            Produto produto = getProdutoById(produtoId);
                            if (produto != null) {
                                LinhaCarrinho linha = new LinhaCarrinho(id, quantidade, carrinhoId, produto, valorIva, precoVenda);
                                linhas.add(linha);
                            } else {
                                Log.e("CarrinhoDebug", "Produto não encontrado para ID: " + produtoId);
                            }
                        }

                        linhaCarrinhos = linhas;

                        if (linhasCarrinhoListener != null) {
                            linhasCarrinhoListener.onRefreshListaLinhasCarrinhos(linhaCarrinhos);
                        }
                    } catch (JSONException e) {
                        Log.e("CarrinhoDebug", "Erro ao processar linhas do carrinho: " + e.getMessage());
                    }
                },
                error -> Log.e("CarrinhoDebug", "Erro ao obter linhas do carrinho: " + error.getMessage()));

        volleyQueue.add(request);
    }


    public void atualizarLinhaCarrinhoAPI(Context context, LinhaCarrinho linha, LinhaCarrinhoListener listener) {
        String url = mUrlUpdateLinha(linha.getId(), context);

        StringRequest req = new StringRequest(Request.Method.PUT, url,
                response -> {
                    try {
                        Log.d("AtualizarLinhaCarrinhoAPI", "Resposta do servidor: " + response);
                        JSONObject jsonObject = new JSONObject(response);

                        // Verifica se a resposta contém os campos esperados
                        if (jsonObject.has("id") && jsonObject.has("quantidade")) {
                            // Atualização foi bem-sucedida
                            Toast.makeText(context, "Carrinho atualizado com sucesso", Toast.LENGTH_SHORT).show();

                            // Notifica o listener para atualizar a interface
                            if (listener != null) listener.onItemUpdate();
                        } else {
                            // Caso os campos esperados não estejam presentes
                            Toast.makeText(context, "Erro ao atualizar carrinho: resposta inesperada", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Erro ao processar resposta do servidor", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Lida com erros na requisição
                    Toast.makeText(context, "Erro ao atualizar carrinho: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("SingletonProdutos", "Erro ao atualizar linha: " + error.getMessage());
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("quantidade", String.valueOf(linha.getQuantidade()));
                params.put("precoVenda", String.valueOf(linha.getPrecoVenda()));
                params.put("valorIva", String.valueOf(linha.getValorIva()));
                params.put("subTotal", String.valueOf(linha.getSubTotal()));
                return params;
            }
        };

        volleyQueue.add(req);
    }

    public void removerLinhaCarrinhoAPI(Context context, int id, LinhasCarrinhosListener listener) {
        String url = urlDeleteLinhaCarrinho(id, context);

        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.optBoolean("success", false);

                        if (success) {
                            Log.d("SingletonProdutos", "Produto eliminado com sucesso do carrinho.");
                            if (listener != null) {
                                listener.onRefreshListaLinhasCarrinhos(null); // Informe que uma linha foi removida
                            }
                        } else {
                            Log.e("SingletonProdutos", "Erro ao remover item do carrinho.");
                        }
                    } catch (JSONException e) {
                        Log.e("SingletonProdutos", "Erro ao processar a resposta da API: " + e.getMessage());
                    }
                },
                error -> Log.e("SingletonProdutos", "Erro na requisição: " + (error.getMessage() != null ? error.getMessage() : "Erro desconhecido"))
        );

        Volley.newRequestQueue(context).add(request);
    }

    // metodo para definir o carrinho
    public void setCarrinho(Carrinho novoCarrinho) {
        this.carrinho = novoCarrinho;
    }

    public void verificarCarrinho(Context context, Produto produto, int quantidade) {
        Log.d("CarrinhoDebug", "A iniciar verificação de carrinho");
        String url = "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/user/dados?username=" +
                getUsername(context) + "&access-token=" + getUserToken(context);

        JsonObjectRequest userDataRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response.toString());
                        JSONObject userForm = jsonResponse.optJSONObject("userForm");

                        if (userForm != null && !userForm.isNull("id")) {
                            int userdataId = userForm.getInt("id");
                            Log.d("CarrinhoDebug", "UserData ID obtido: " + userdataId);

                            // Endpoint correto para verificar carrinho
                            String carrinhoUrl = getBaseUrl(context) + "carrinho/carrinho?user_id=" +
                                    userdataId + "&access-token=" + getUserToken(context);

                            StringRequest carrinhoRequest = new StringRequest(Request.Method.GET, carrinhoUrl,
                                    carrinhoResponse -> {
                                        try {
                                            JSONObject jsonCarrinho = new JSONObject(carrinhoResponse);
                                            carrinhoId = jsonCarrinho.getInt("id");
                                            saveLastCarrinhoId(context, carrinhoId);
                                            Log.d("CarrinhoDebug", "Carrinho existente encontrado. ID: " + carrinhoId);

                                            if (produto != null) {
                                                adicionarLinhaCarrinhoAPI(context, produto, quantidade);
                                            } else {
                                                getLinhasCarrinhoAPI(context);
                                            }
                                        } catch (JSONException e) {
                                            criarCarrinho(context, userdataId, produto, quantidade);
                                        }
                                    },
                                    error -> {
                                        if (error.networkResponse != null && error.networkResponse.statusCode == 404) {
                                            criarCarrinho(context, userdataId, produto, quantidade);
                                        } else {
                                            Toast.makeText(context, "Erro ao verificar carrinho",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            volleyQueue.add(carrinhoRequest);
                        } else {
                            Toast.makeText(context, "Erro: Dados do utilizador não encontrados",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e("CarrinhoDebug", "Erro ao processar dados do utilizador: " + e.getMessage());
                    }
                },
                error -> Log.e("CarrinhoDebug", "Erro ao buscar dados do utilizador: " + error.getMessage()));

        volleyQueue.add(userDataRequest);
    }

    private void criarCarrinho(Context context, int userdataId, Produto produto, int quantidade) {
        Log.d("CarrinhoDebug", "A criar novo carrinho");

        StringRequest req = new StringRequest(Request.Method.POST,
                mUrlPostCarrinhoAPI(context),
                response -> {
                    Log.d("CarrinhoDebug", "Resposta criação: " + response);
                    try {
                        JSONObject resp = new JSONObject(response);
                        if (!resp.has("errors")) {
                            carrinhoId = resp.getInt("id");
                            saveLastCarrinhoId(context, carrinhoId);
                            if (produto != null) {
                                adicionarLinhaCarrinhoAPI(context, produto, quantidade);
                            }
                        } else {
                            Toast.makeText(context, "Erro ao criar carrinho", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e("CarrinhoDebug", "Erro ao criar carrinho: " + e.getMessage());
                    }
                },
                error -> {
                    Log.e("CarrinhoDebug", "Erro ao criar carrinho: " + error.toString());
                    Toast.makeText(context, "Erro ao criar carrinho", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userdata_id", String.valueOf(userdataId));
                params.put("total", "0");
                params.put("ivatotal", "0");
                params.put("data", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                params.put("estado", "1");
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };

        volleyQueue.add(req);
    }




    // Metodo para guardar o ID do último carrinho criado
    private void saveLastCarrinhoId(Context context, int carrinhoId) {
        SharedPreferences prefs = context.getSharedPreferences("CarrinhoPrefs", Context.MODE_PRIVATE);
        prefs.edit().putInt("last_carrinho_id", carrinhoId).apply();
    }




    // Metodo para obter o ID do último carrinho
    private int getLastCarrinhoId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("CarrinhoPrefs", Context.MODE_PRIVATE);
        return prefs.getInt("last_carrinho_id", -1);
    }

    //endregion

    //region fatura e linhafatura

    // Metodo para criar uma fatura
    public void criarFaturaAPI(Context context, int userId, int metodoPagamentoId, int metodoExpedicaoId, FaturasListener listener) {
        String urlUserData = "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/user/dados?username=" +
                getUsername(context) + "&access-token=" + getUserToken(context);

        JsonObjectRequest userDataRequest = new JsonObjectRequest(Request.Method.GET, urlUserData, null,
                response -> {
                    try {
                        JSONObject userForm = response.optJSONObject("userForm");
                        if (userForm == null || userForm.isNull("id")) {
                            Toast.makeText(context, "Erro: Dados do utilizador não encontrados", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int userdataId = userForm.getInt("id");
                        String carrinhoUrl = "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/carrinho/carrinho?access-token=" +
                                getUserToken(context) + "&user_id=" + userdataId;

                        JsonObjectRequest carrinhoRequest = new JsonObjectRequest(Request.Method.GET, carrinhoUrl, null,
                                carrinhoResponse -> {
                                    try {
                                        int carrinhoId = carrinhoResponse.getInt("id");
                                        String url = getBaseUrl(context) + "faturas/criarfatura?access-token=" + getUserToken(context);
                                        final float valorTotalFinal = calcularValorTotal();

                                        JSONObject jsonBody = new JSONObject();
                                        jsonBody.put("userdata_id", userdataId);
                                        jsonBody.put("carrinho_id", carrinhoId);
                                        jsonBody.put("metodopagamento_id", metodoPagamentoId);
                                        jsonBody.put("metodoexpedicao_id", metodoExpedicaoId);
                                        jsonBody.put("data", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
                                        jsonBody.put("valorTotal", String.format(Locale.US, "%.2f", valorTotalFinal));
                                        jsonBody.put("statuspedido", "pago");

                                        JsonObjectRequest faturaRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                                                faturaResponse -> {
                                                    try {
                                                        if (faturaResponse.has("success") && faturaResponse.getBoolean("success")) {
                                                            JSONObject data = faturaResponse.getJSONObject("data");
                                                            Fatura faturaCriada = new Fatura();
                                                            faturaCriada.setId(data.getInt("id"));
                                                            faturaCriada.setMetodoPagamentoId(metodoPagamentoId);
                                                            faturaCriada.setMetodoExpedicaoId(metodoExpedicaoId);
                                                            faturaCriada.setData(data.getString("data"));
                                                            faturaCriada.setValorTotal(Float.parseFloat(data.getString("valorTotal")));
                                                            faturaCriada.setStatusPedido(Fatura.StatusPedido.valueOf(data.getString("statuspedido")));

                                                            // Após criar a fatura, criar as linhas da fatura
                                                            List<LinhaFatura> linhasFatura = convertCarrinhoParaLinhasFaturas();
                                                            for (LinhaFatura linha : linhasFatura) {
                                                                linha.setFaturaId(faturaCriada.getId());
                                                                criarLinhaFaturaAPI(context, linha, new LinhasFaturasListener() {
                                                                    @Override
                                                                    public void onLinhaFaturaCriada(LinhaFatura linhaFatura) {
                                                                        // Linha criada com sucesso
                                                                    }

                                                                    @Override
                                                                    public void onRefreshListaLinhasFaturas(int faturaId, ArrayList<LinhaFatura> linhasFaturas) {
                                                                        // Atualização da lista não necessária aqui
                                                                    }
                                                                });
                                                            }

                                                            // Limpar o carrinho após criar todas as linhas
                                                            limparCarrinhoAposFatura(context);

                                                            if (listener != null) {
                                                                listener.onFaturaCriada(faturaCriada);
                                                            }
                                                            Toast.makeText(context, "Fatura criada com sucesso!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } catch (JSONException e) {
                                                        Log.e("FaturaAPI", "Erro ao processar resposta da fatura: " + e.getMessage());
                                                        handleError(context, new VolleyError(e.getMessage()), "Erro ao processar fatura");
                                                    }
                                                },
                                                error -> handleError(context, error, "Erro ao criar fatura"));

                                        volleyQueue.add(faturaRequest);

                                    } catch (JSONException e) {
                                        Log.e("FaturaAPI", "Erro ao processar dados do carrinho: " + e.getMessage());
                                        handleError(context, new VolleyError(e.getMessage()), "Erro ao processar carrinho");
                                    }
                                },
                                error -> handleError(context, error, "Carrinho não encontrado"));

                        volleyQueue.add(carrinhoRequest);

                    } catch (JSONException e) {
                        Log.e("FaturaAPI", "Erro ao processar dados do utilizador: " + e.getMessage());
                        handleError(context, new VolleyError(e.getMessage()), "Erro ao processar dados do utilizador");
                    }
                },
                error -> handleError(context, error, "Erro ao buscar dados do utilizador"));

        volleyQueue.add(userDataRequest);
    }

    private void limparCarrinhoAposFatura(Context context) {
        if (carrinhoId <= 0 || linhaCarrinhos.isEmpty()) {
            return;
        }

        // Cria uma cópia da lista para evitar ConcurrentModificationException
        ArrayList<LinhaCarrinho> linhasParaDeletar = new ArrayList<>(linhaCarrinhos);
        final int totalLinhas = linhasParaDeletar.size();
        final AtomicInteger linhasDeletadas = new AtomicInteger(0);

        for (LinhaCarrinho linha : linhasParaDeletar) {
            String url = urlDeleteLinhaCarrinho(linha.getId(), context);

            StringRequest request = new StringRequest(Request.Method.DELETE, url,
                    response -> {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse.optBoolean("success", false)) {
                                int deletadas = linhasDeletadas.incrementAndGet();

                                // Se todas as linhas foram deletadas
                                if (deletadas == totalLinhas) {
                                    // Limpa as listas locais
                                    linhaCarrinhos.clear();
                                    carrinho = null;
                                    carrinhoId = -1;

                                    // Limpa o ID do carrinho nas SharedPreferences
                                    SharedPreferences prefs = context.getSharedPreferences("CarrinhoPrefs", Context.MODE_PRIVATE);
                                    prefs.edit().remove("last_carrinho_id").apply();

                                    // Notifica os listeners
                                    if (linhasCarrinhoListener != null) {
                                        linhasCarrinhoListener.onRefreshListaLinhasCarrinhos(new ArrayList<>());
                                    }
                                    if (carrinhoListener != null) {
                                        carrinhoListener.onCarrinhoUpdated(null);
                                    }

                                    Toast.makeText(context, "Carrinho limpo com sucesso", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            Log.e("LimparCarrinho", "Erro ao processar resposta: " + e.getMessage());
                        }
                    },
                    error -> {
                        Log.e("LimparCarrinho", "Erro ao deletar linha: " + error.getMessage());
                        Toast.makeText(context, "Erro ao limpar carrinho", Toast.LENGTH_SHORT).show();
                    });

            request.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            volleyQueue.add(request);
        }
    }


    private void handleError(Context context, VolleyError error, String defaultMessage) {
        if (error.networkResponse != null && error.networkResponse.data != null) {
            try {
                String errorResponse = new String(error.networkResponse.data, "UTF-8");
                Log.e("FaturaAPI", "Erro detalhado: " + errorResponse);
                Toast.makeText(context, defaultMessage + ": " + errorResponse, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Log.e("FaturaAPI", "Erro ao ler resposta de erro: " + e.getMessage());
                Toast.makeText(context, defaultMessage, Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("FaturaAPI", "Erro na requisição: " + error.toString());
            Toast.makeText(context, "Erro de conexão", Toast.LENGTH_SHORT).show();
        }
    }


    public void getFaturasAPI(Context context, int userId, FaturasListener listener) {
        // Primeiro buscar o userdata_id
        String urlUserData = "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/user/dados?username=" + getUsername(context) + "&access-token=" + getUserToken(context);

        JsonObjectRequest userDataRequest = new JsonObjectRequest(Request.Method.GET, urlUserData, null,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response.toString());
                        JSONObject userForm = jsonResponse.optJSONObject("userForm");

                        if (userForm != null && !userForm.isNull("id")) {
                            int userdataId = userForm.getInt("id");
                            // Agora buscar as faturas com o userdata_id correto
                            String url = getBaseUrl(context) + "faturas/" + userdataId + "/faturas?access-token=" + getUserToken(context);

                            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                                    faturasResponse -> {
                                        ArrayList<Fatura> faturas = FaturasJsonParser.parserJsonFaturas(faturasResponse);
                                        if (listener != null) {
                                            listener.onRefreshListaFatura(faturas);
                                        }
                                    },
                                    error -> {
                                        Log.e("FaturaAPI", "Erro ao obter faturas: " + error.getMessage());
                                        Toast.makeText(context, "Erro ao carregar faturas", Toast.LENGTH_SHORT).show();
                                    });

                            volleyQueue.add(request);
                        } else {
                            Log.e("FaturaAPI", "UserForm não encontrado");
                            Toast.makeText(context, "Erro: Dados do utilizador não encontrados", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e("FaturaAPI", "Erro ao processar dados do utilizador: " + e.getMessage());
                    }
                },
                error -> Log.e("FaturaAPI", "Erro ao buscar dados do utilizador: " + error.getMessage()));

        volleyQueue.add(userDataRequest);
    }


    // Metodo para criar uma linha de fatura
    public void criarLinhaFaturaAPI(final Context context, final LinhaFatura linhaFatura, final LinhasFaturasListener listener) {
        String url = getBaseUrl(context) + "linhas-faturas/criarlinhafatura?access-token=" + getUserToken(context);

        Log.d("LinhaFaturaAPI", "A criar linha de fatura. URL: " + url);
        Log.d("LinhaFaturaAPI", "Fatura ID: " + linhaFatura.getFaturaId());

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("LinhaFaturaAPI", "Resposta: " + response);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.has("success") && jsonResponse.getBoolean("success")) {
                            if (listener != null) {
                                listener.onLinhaFaturaCriada(linhaFatura);
                            }
                        }
                    } catch (JSONException e) {
                        Log.e("LinhaFaturaAPI", "Erro ao processar resposta: " + e.getMessage());
                    }
                },
                error -> {
                    Log.e("LinhaFaturaAPI", "Erro ao criar linha de fatura: " + error.getMessage());
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("fatura_id", String.valueOf(linhaFatura.getFaturaId()));
                params.put("produto_id", String.valueOf(linhaFatura.getProdutoId()));
                params.put("quantidade", String.valueOf(linhaFatura.getQuantidade()));
                params.put("precoVenda", String.format(Locale.US, "%.2f", linhaFatura.getPrecoVenda()));
                params.put("valorIva", String.format(Locale.US, "%.2f", linhaFatura.getValorIva()));
                params.put("subTotal", String.format(Locale.US, "%.2f", linhaFatura.getSubTotal()));
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
        };

        volleyQueue.add(request);
    }

    // Metodo para buscar as linhas de uma fatura
    public void getLinhasFaturasAPI(final Context context, final int faturaId, final LinhasFaturasListener listener) {
        String url = "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/linhas-faturas/dados?access-token="
                + getUserToken(context) + "&fatura_id=" + faturaId;

        Log.d("LinhaFaturaAPI", "Obtendo linhas da fatura. URL: " + url);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    Log.d("LinhaFaturaAPI", "Resposta recebida: " + response.toString());
                    linhasFaturas = LinhasFaturasJsonParser.parserJsonLinhasFaturas(response);
                    if (listener != null) {
                        listener.onRefreshListaLinhasFaturas(faturaId, linhasFaturas);
                    }
                },
                error -> {
                    String errorMsg = "";
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        try {
                            errorMsg = new String(error.networkResponse.data, "UTF-8");
                        } catch (Exception e) {
                            errorMsg = error.getMessage();
                        }
                    }
                    Log.e("LinhaFaturaAPI", "Erro ao obter linhas da fatura: " + errorMsg);
                });

        volleyQueue.add(request);
    }

    public List<LinhaFatura> convertCarrinhoParaLinhasFaturas() {
        List<LinhaFatura> linhasFaturas = new ArrayList<>();
        for (LinhaCarrinho linhaCarrinho : linhaCarrinhos) {
            LinhaFatura linhaFatura = new LinhaFatura(
                    0,  // ID temporário
                    0,  // ID da fatura
                    linhaCarrinho.getProduto().getIva(),
                    linhaCarrinho.getProduto().getId(),
                    linhaCarrinho.getProduto().getPreco(),
                    linhaCarrinho.getValorIva(),
                    linhaCarrinho.getSubTotal(),
                    linhaCarrinho.getQuantidade()
            );
            linhasFaturas.add(linhaFatura);
        }
        return linhasFaturas;
    }

    // Metodo auxiliar para calcular o valor total
    private float calcularValorTotal() {
        float total = 0;
        for (LinhaCarrinho linha : linhaCarrinhos) {
            total += linha.getSubTotal();
        }
        return total;
    }
    //endregion

    //region metodos pagamento e metodos expedicao

    // Metodo para obter os métodos de pagamento da API
    public void getMetodosPagamentoAPI(Context context, Consumer<List<MetodoPagamento>> callback) {
        String url = "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/metodos-pagamentos?access-token=" + getUserToken(context);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    List<MetodoPagamento> metodosPagamento = new ArrayList<>();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonMetodo = response.getJSONObject(i);
                            MetodoPagamento metodo = new MetodoPagamento();
                            metodo.setId(jsonMetodo.getInt("id"));
                            metodo.setNome(jsonMetodo.getString("nome"));
                            metodosPagamento.add(metodo);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    callback.accept(metodosPagamento);
                },
                error -> Log.e("MetodosPagamentoAPI", "Erro ao buscar métodos: " + error.getMessage())
        );

        volleyQueue.add(request);
    }

    // Metodo para obter os métodos de expedição da API
    public void getMetodosExpedicaoAPI(Context context, Consumer<List<MetodoExpedicao>> callback) {
        String url = "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/metodos-expedicoes?access-token=" + getUserToken(context);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    List<MetodoExpedicao> metodosExpedicao = new ArrayList<>();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonMetodo = response.getJSONObject(i);
                            MetodoExpedicao metodo = new MetodoExpedicao();
                            metodo.setId(jsonMetodo.getInt("id"));
                            metodo.setNome(jsonMetodo.getString("nome"));
                            metodo.setCusto((float) jsonMetodo.getDouble("custo"));
                            metodo.setPrazoEntrega(jsonMetodo.getInt("prazo_entrega"));
                            metodo.setDescricao(jsonMetodo.optString("descricao", ""));
                            metodosExpedicao.add(metodo);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    callback.accept(metodosExpedicao);
                },
                error -> Log.e("MetodosExpedicaoAPI", "Erro ao buscar métodos: " + error.getMessage())
        );

        volleyQueue.add(request);
    }

    //endregion

    //endregion

    //region URLS API

    //region WEBSITE URL
    private String getBaseUrl(Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/";
    }
    //endregion

    //region API PRODUTOS

    public String mUrlAPIProdutos(Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/produtos/produtos?access-token=" + getUserToken(context);
    }

    //endregion

    //region API USER

    private String urlPostAPIPerfilDados(Context context) {

        String username = getUsername(context);
        String accessToken = getUserToken(context);

        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/user/dados?username=" + username + "&access-token=" + accessToken;
    }

    // API Dados do Utilizador
    private String mUrlAPIUserData(Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/users/" + getUserId(context) + "?access-token=" + getUserToken(context);
    }

    // API Login
    private String mUrlAPILogin(Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/auth/login";
    }

    // API Signup
    public String mUrlAPISignup(Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/auth/signup";
    }

    //endregion

    //region CARRINHOS

    private String mUrlPostCarrinhoAPI(Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/carrinho/criar?access-token=" + getUserToken(context);
    }


    // A URL segue o padrão: /carrinho/{id}/carrinho
    private String mUrlGetCarrinhoAPI(Context context) {
        return getBaseUrl(context) + "carrinho/" + getUserId(context) + "/carrinho?access-token=" + getUserToken(context);
    }


    //endregion

    //region LINHAS CARRINHOS

    // API Delete Linha
    private String urlDeleteLinhaCarrinho(int id, Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/linhas-carrinhos/deletelinhacarrinho"
                + "?access-token=" + getUserToken(context) + "&id=" + id;
    }

    // API Update Linha
    private String mUrlUpdateLinha(int id, Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/linhas-carrinhos/updatelinhacarrinho/"
                + "?access-token=" + getUserToken(context) + "&id=" + id;
    }

    // API Carrinho Dados
    private String mUrlGetLinhasCarrinhoAPI(Context context, int carrinhoId) {
        return getBaseUrl(context) + "linhas-carrinhos/dados?access-token="
                + getUserToken(context) + "&carrinho_id=" + carrinhoId;
    }

    // API Post Linha no Carrinho
    private String mUrlAPIPostLinhaCarrinho(Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/linhas-carrinhos/postlinhacarrinho?access-token=" + getUserToken(context);
    }

    //endregion

    //region FATURAS

    // API Faturas
    private String mUrlGetFaturas(Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/faturas/" + getUserId(context) + "/faturas?access-token=" + getUserToken(context);
    }

    // API Faturas: Criar nova fatura
    private String mUrlPostFaturaAPI(Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/faturas/criarfatura?access-token=" + getUserToken(context);
    }

    // API Faturas: Obter todas as faturas associadas ao utilizador
    private String mUrlGetFaturasAPI(Context context) {
        return getBaseUrl(context) + "faturas/{userdata_id}/faturas?access-token=" + getUserToken(context);
    }

    //endregion

    //region LINHAS FATURAS

    // API Linhas de Fatura
    private String mUrlGetLinhasFaturas(int fatura_id, Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/faturas/" + fatura_id + "/faturasdados?access-token=" + getUserToken(context);
    }

    // API Linhas de Faturas: Criar linha de fatura
    private String mUrlPostLinhaFaturaAPI(Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/linhas-faturas/criarlinhafatura?access-token=" + getUserToken(context);
    }




    // API Linhas de Faturas: Obter linhas de uma fatura específica
    private String mUrlGetLinhasFaturasAPI(int faturaId, Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/linhas-faturas/dados?access-token=" + getUserToken(context) + "&fatura_id=" + faturaId;
    }

    //endregion

    //endregion
}
