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
import com.example.leiriajeansamsi.utils.CarrinhoJsonParser;
import com.example.leiriajeansamsi.utils.FaturasJsonParser;
import com.example.leiriajeansamsi.listeners.SignupListener;
import com.example.leiriajeansamsi.listeners.UtilizadorDataListener;
import com.example.leiriajeansamsi.utils.LinhaCarrinhoJsonParser;
import com.example.leiriajeansamsi.utils.LinhasFaturasJsonParser;
import com.example.leiriajeansamsi.utils.LoginJsonParser;
import com.example.leiriajeansamsi.utils.MetodosExpedicaoJsonParser;
import com.example.leiriajeansamsi.utils.MetodosPagamentoJsonParser;
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
    private int userdata_id = -1;

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

    public int getUserdataId() {
        return userdata_id;
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
            if (loginListener != null) loginListener.onUpdateLogin(null);
            return;
        }

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("username", username);
            jsonParams.put("password", password);
        } catch (JSONException e) {
            if (loginListener != null) loginListener.onUpdateLogin(null);
            return;
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, mUrlAPILogin(context), jsonParams,
                response -> {
                    utilizador = LoginJsonParser.parserJsonLogin(response);
                    if (utilizador != null && utilizador.getAuth_key() != null) {
                        saveUserId(context, utilizador.getId());
                        saveUserToken(context, utilizador.getAuth_key(), utilizador.getUsername());
                        saveUsername(context, utilizador.getUsername());
                        getUserDataAPI(context, utilizadorDataListener);
                    }
                    if (loginListener != null) loginListener.onUpdateLogin(utilizador);
                },
                error -> {
                    Toast.makeText(context, "Credenciais incorretas", Toast.LENGTH_SHORT).show();
                    if (loginListener != null) loginListener.onUpdateLogin(null);
                });

        volleyQueue.add(req);
    }

    public void getUserDataAPI(Context context, final UtilizadorDataListener listener) {
        String username = getUsername(context);
        String accessToken = getUserToken(context);

        if (username == null || accessToken == null) {
            Log.e("DEBUG_API", "Username ou token não encontrados");
            return;
        }

        String url = "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/user/dados?username=" + username + "&access-token=" + accessToken;

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject userForm = response.getJSONObject("userForm");
                        userdata_id = userForm.getInt("id");
                        utilizadorData = LoginJsonParser.parserJsonGetUtilizadorData(userForm);

                        if (utilizadorData != null) {
                            if (listener != null) listener.onGetUtilizadorData(utilizadorData);
                            getCarrinhoAPI(context);
                        }
                    } catch (JSONException e) {
                        Log.e("DEBUG_API", "Erro no parsing JSON: " + e.getMessage());
                    }
                },
                error -> Log.e("DEBUG_API", "Erro na requisição: " + error.getMessage()));

        volleyQueue.add(req);
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
                    Toast.makeText(context, "Registo realizado com sucesso!", Toast.LENGTH_SHORT).show();

                    // Redireciona para o login
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    if (context instanceof Activity) {
                        ((Activity) context).finish();
                    }
                },
                error -> {
                    Toast.makeText(context, "Erro ao registar utilizador", Toast.LENGTH_SHORT).show();
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

    //region carrinho
    public void getCarrinhoAPI(Context context) {
        if (userdata_id <= 0) {
            return;
        }

        String url = mUrlGetCarrinhoAPI(context);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        carrinho = CarrinhoJsonParser.criarCarrinho(response);
                        if (carrinho != null) {
                            carrinhoId = carrinho.getId();
                            getLinhasCarrinhoAPI(context);
                        }
                    } catch (JSONException e) {
                        Log.e("CarrinhoAPI", "Erro no parsing: " + e.getMessage());
                    }
                },
                error -> {
                    // Se o carrinho não existir (erro 404) ou outro erro, criar um novo
                    criarCarrinhoAPI(context);
                });

        volleyQueue.add(req);
    }

    public void criarCarrinhoAPI(Context context) {
        if (userdata_id <= 0) {
            Log.e("CarrinhoAPI", "userdata_id inválido");
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, mUrlPostCarrinhoAPI(context),
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        carrinho = CarrinhoJsonParser.criarCarrinho(jsonResponse);
                        if (carrinho != null) {
                            carrinhoId = carrinho.getId();
                            getLinhasCarrinhoAPI(context);
                        }
                    } catch (JSONException e) {
                        Log.e("CarrinhoAPI", "Erro ao processar resposta: " + e.getMessage());
                    }
                },
                error -> Log.e("CarrinhoAPI", "Erro ao criar carrinho: " + error.getMessage())) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userdata_id", String.valueOf(userdata_id));
                params.put("total", "0");
                params.put("ivatotal", "0");
                params.put("data", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                params.put("estado", "1");
                return params;
            }
        };

        volleyQueue.add(request);
    }
    //endregion

    //region LinhasCarrinho
    public void getLinhasCarrinhoAPI(Context context) {
        if (carrinhoId <= 0) {
            Log.e("LinhasCarrinhoAPI", "carrinhoId inválido: " + carrinhoId);
            return;
        }
        String url = mUrlGetLinhasCarrinhoAPI(context, carrinhoId);

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response != null) {
                            linhaCarrinhos = LinhaCarrinhoJsonParser.parserJsonLinhaCarrinho(response, context);
                            if (linhasCarrinhoListener != null) {
                                linhasCarrinhoListener.onRefreshListaLinhasCarrinhos(linhaCarrinhos);
                            }
                        } else {
                            Log.e("LinhasCarrinhoAPI", "Resposta nula do servidor");
                        }
                    } catch (Exception e) {
                        Log.e("LinhasCarrinhoAPI", "Erro ao processar resposta: " + e.getMessage());
                    }
                },
                error -> {
                    Log.e("LinhasCarrinhoAPI", "Erro na requisição: " + error.getMessage());
                    if (error.networkResponse != null) {
                        Log.e("LinhasCarrinhoAPI", "Código de erro: " + error.networkResponse.statusCode);
                    }
                });

        volleyQueue.add(req);
    }

    public void criarLinhaCarrinhoAPI(Context context, Produto produto, int quantidade) {
        if (carrinhoId <= 0) {
            Log.e("LinhasCarrinhoAPI", "ID do carrinho inválido");
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, mUrlAPIPostLinhaCarrinho(context),
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        LinhaCarrinhoJsonParser.criarLinhaCarrinho(jsonResponse, context);
                        getLinhasCarrinhoAPI(context);
                    } catch (JSONException e) {
                        Log.e("LinhasCarrinhoAPI", "Erro ao processar linha: " + e.getMessage());
                    }
                },
                error -> Toast.makeText(context, "Erro ao adicionar ao carrinho", Toast.LENGTH_SHORT).show()) {
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


    public void atualizarLinhaCarrinhoAPI(Context context, int linhaId, int novaQuantidade, Consumer<List<LinhaCarrinho>> callback) {
        StringRequest request = new StringRequest(Request.Method.PUT, mUrlUpdateLinha(linhaId, context),
                response -> {
                    try {
                        getLinhasCarrinhoAPI(context);
                        if (callback != null) {
                            callback.accept(linhaCarrinhos);
                        }
                    } catch (Exception e) {
                        Log.e("LinhasCarrinhoAPI", "Erro: " + e.getMessage());
                    }
                },
                error -> Toast.makeText(context, "Erro ao atualizar quantidade", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("quantidade", String.valueOf(novaQuantidade));
                return params;
            }
        };
        volleyQueue.add(request);
    }

    public void removerLinhaCarrinhoAPI(Context context, int linhaId, Consumer<List<LinhaCarrinho>> callback) {
        StringRequest request = new StringRequest(Request.Method.DELETE, urlDeleteLinhaCarrinho(linhaId, context),
                response -> {
                    getLinhasCarrinhoAPI(context); // Corrigido para usar userdata_id
                    if (callback != null) {
                        callback.accept(linhaCarrinhos);
                    }
                },
                error -> Toast.makeText(context, "Erro ao remover item", Toast.LENGTH_SHORT).show());
        volleyQueue.add(request);
    }

    public void aumentarQuantidade(Context context, LinhaCarrinho linha, Consumer<List<LinhaCarrinho>> callback) {
        atualizarLinhaCarrinhoAPI(context, linha.getId(), linha.getQuantidade() + 1, callback);
    }

    public void diminuirQuantidade(Context context, LinhaCarrinho linha, Consumer<List<LinhaCarrinho>> callback) {
        if (linha.getQuantidade() > 1) {
            atualizarLinhaCarrinhoAPI(context, linha.getId(), linha.getQuantidade() - 1, callback);
        }
    }
//endregion

    //region Faturas
    public void getFaturasAPI(Context context) {
        if (userdata_id <= 0) {
            Log.e("FaturasAPI", "userdata_id inválido: " + userdata_id);
            if (faturasListener != null) {
                faturasListener.onRefreshListaFatura(new ArrayList<>());
            }
            return;
        }

        String url = mUrlGetFaturasAPI(context);
        Log.d("FaturasAPI", "URL da requisição: " + url);

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    Log.d("FaturasAPI", "Resposta completa: " + response.toString());
                    ArrayList<Fatura> faturas = FaturasJsonParser.parserJsonFaturas(response);

                    // Guardar as faturas na base de dados local
                    FaturasDBHelper dbHelper = new FaturasDBHelper(context);
                    dbHelper.atualizarFaturas(faturas != null ? faturas : new ArrayList<>(), userdata_id);

                    if (faturasListener != null) {
                        faturasListener.onRefreshListaFatura(faturas != null ? faturas : new ArrayList<>());
                    }
                },
                error -> {
                    Log.e("FaturasAPI", "Erro na requisição: " + error.getMessage());
                    // Notifica o listener com lista vazia em caso de erro
                    if (faturasListener != null) {
                        faturasListener.onRefreshListaFatura(new ArrayList<>());
                    }
                });

        volleyQueue.add(req);
    }

    public void criarFaturaAPI(Context context, int metodoPagamentoId, int metodoExpedicaoId, FaturasListener listener) {
        if (userdata_id <= 0) {
            Log.e("FaturasAPI", "userdata_id inválido");
            return;
        }

        float valorTotal = 0;
        for (LinhaCarrinho linha : linhaCarrinhos) {
            valorTotal += linha.getSubTotal();
        }

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("userdata_id", userdata_id);
            jsonParams.put("metodopagamento_id", metodoPagamentoId);
            jsonParams.put("metodoexpedicao_id", metodoExpedicaoId);
            jsonParams.put("statuspedido", "pago");
            jsonParams.put("valorTotal", valorTotal);
        } catch (JSONException e) {
            Log.e("FaturasAPI", "Erro ao criar JSON: " + e.getMessage());
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, mUrlPostFaturaAPI(context), jsonParams,
                response -> {
                    try {
                        Fatura fatura = FaturasJsonParser.criarFatura(response);
                        if (fatura != null && fatura.getId() > 0) {
                            // Adiciona a nova fatura à base de dados local
                            FaturasDBHelper dbHelper = new FaturasDBHelper(context);
                            ArrayList<Fatura> faturasAtuais = dbHelper.getAllFaturas(userdata_id);
                            faturasAtuais.add(fatura);
                            dbHelper.atualizarFaturas(faturasAtuais, userdata_id);
                            
                            // Remove todas as linhas do carrinho
                            for (LinhaCarrinho linha : linhaCarrinhos) {
                                removerLinhaCarrinhoAPI(context, linha.getId(), null);
                            }
                            
                            if (listener != null) {
                                listener.onFaturaCriada(fatura);
                            }
                            
                            Toast.makeText(context, "Fatura criada com sucesso!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Erro ao criar fatura", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e("FaturasAPI", "Erro ao processar fatura: " + e.getMessage());
                        Toast.makeText(context, "Erro ao processar fatura", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("FaturasAPI", "Erro na requisição: " + error.getMessage());
                    Toast.makeText(context, "Erro ao criar fatura", Toast.LENGTH_SHORT).show();
                });

        volleyQueue.add(request);
    }
    //endregion

    //region LinhasFaturas
    public void getLinhasFaturasAPI(Context context, int faturaId, LinhasFaturasListener listener) {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, mUrlGetLinhasFaturas(faturaId, context), null,
                response -> {
                    ArrayList<LinhaFatura> linhasFaturas = LinhasFaturasJsonParser.parserJsonLinhasFaturas(response, faturaId);
                    if (listener != null) {
                        listener.onRefreshListaLinhasFaturas(faturaId, linhasFaturas);
                    }
                },
                error -> Toast.makeText(context, "Erro ao obter linhas da fatura", Toast.LENGTH_SHORT).show());

        volleyQueue.add(request);
    }


    public void criarLinhaFaturaAPI(Context context, LinhaFatura linhaFatura, LinhasFaturasListener listener) {
        StringRequest request = new StringRequest(Request.Method.POST, mUrlPostLinhaFaturaAPI(context),
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        LinhaFatura linha = LinhasFaturasJsonParser.criarLinhaFatura(jsonResponse.getJSONObject("created"));
                        if (listener != null) {
                            listener.onLinhaFaturaCriada(linha);
                        }
                    } catch (JSONException e) {
                        Log.e("LinhasFaturasAPI", "Erro ao processar linha: " + e.getMessage());
                    }
                },
                error -> Toast.makeText(context, "Erro ao criar linha da fatura", Toast.LENGTH_SHORT).show()) {
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
        };
        volleyQueue.add(request);
    }
//endregion

    //region Métodos Pagamento e Expedição
    public void getMetodosPagamentoAPI(Context context, Consumer<List<MetodoPagamento>> callback) {
        String url = "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/metodos-pagamentos?access-token=" + getUserToken(context);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    List<MetodoPagamento> metodosPagamento = MetodosPagamentoJsonParser.parserJsonMetodosPagamento(response);
                    callback.accept(metodosPagamento);
                },
                error -> Log.e("MetodosPagamentoAPI", "Erro ao buscar métodos: " + error.getMessage()));

        volleyQueue.add(request);
    }

    public void getMetodosExpedicaoAPI(Context context, Consumer<List<MetodoExpedicao>> callback) {
        String url = "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/metodos-expedicoes?access-token=" + getUserToken(context);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    List<MetodoExpedicao> metodosExpedicao = MetodosExpedicaoJsonParser.parserJsonMetodosExpedicao(response);
                    callback.accept(metodosExpedicao);
                },
                error -> Log.e("MetodosExpedicaoAPI", "Erro ao buscar métodos: " + error.getMessage()));

        volleyQueue.add(request);
    }

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
        return getBaseUrl(context) + "carrinho" + "/carrinho?access-token=" + getUserToken(context) + "&user_id=" + getUserdataId();
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
    private String mUrlGetFaturasAPI(Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/faturas" + "/faturas?access-token=" + getUserToken(context) + "&id=" + getUserdataId();
    }

    // API Faturas: Criar nova fatura
    private String mUrlPostFaturaAPI(Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/faturas/criarfatura?access-token=" + getUserToken(context);
    }

    // API Faturas: Obter todas as faturas associadas ao utilizador
    private String mUrlGetAllFaturasAPI(Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/faturas/faturas?access-token=" + getUserToken(context) + "&id=" + getUserdataId();
    }


    //endregion

    //region LINHAS FATURAS

    // API Linhas de Fatura
    private String mUrlGetLinhasFaturas(int fatura_id, Context context) {
        return "http://" + getApiIP(context) +
                "/leiriajeans/backend/web/api/linhas-faturas/dados?" +
                "access-token=" + getUserToken(context) +
                "&fatura_id=" + fatura_id;
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
