package com.example.leiriajeansamsi.Modelo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

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
import java.util.Map;

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
import com.example.leiriajeansamsi.utils.LinhaCarrinhoJsonParser;
import com.example.leiriajeansamsi.listeners.SignupListener;
import com.example.leiriajeansamsi.listeners.UtilizadorDataListener;
import com.example.leiriajeansamsi.utils.LinhasFaturasJsonParser;
import com.example.leiriajeansamsi.utils.LoginJsonParser;
import com.example.leiriajeansamsi.utils.ProdutoJsonParser;

public class    SingletonProdutos {

    // Constantes para endpoints
    private static final String CARRINHO_API = "carrinho";
    private static final String CRIAR_CARRINHO_API = "carrinho/criar";
    private static final String LINHAS_CARRINHO_API = "linhas-carrinhos";
    private int carrinhoId = -1;

    public ArrayList<Produto> produtos = new ArrayList<>();


    public ArrayList<Fatura> faturas = new ArrayList<>();
    public ArrayList<LinhaFatura> linhasFaturas = new ArrayList<>();
    public ArrayList<LinhaCarrinho> linhaCarrinhos = new ArrayList<>();
    public Carrinho carrinho;
    public LinhaCarrinho linhaCarrinho;
    public Utilizador utilizador, utilizadorData;

    private static volatile SingletonProdutos instance = null;
    private static int user_id;
    private static int carrinho_id;

    /*private ProdutoBDHelper produtosBD=null;*/

    private static RequestQueue volleyQueue = null;
    private LoginListener loginListener;
    private ProfileUpdateListener profileUpdateListener;
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
    private Utilizador loggedInUser;

    private List<LinhaCarrinho> linhasCarrinho; // Lista de produtos no carrinho

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
        getAllProdutosAPI(context); // Chama o método para buscar produtos
    }

    private void inicializarProdutos() {
        // Exemplo de adição de produtos
        produtos.add(new Produto(1, 23, 100, "Produto 1", "Descrição do Produto 1", "Categoria 1", "url_imagem_1", "Cor 1", 10.0f)); // ID 1
        produtos.add(new Produto(2, 23, 200, "Produto 2", "Descrição do Produto 2", "Categoria 2", "url_imagem_2", "Cor 2", 20.0f)); // ID 2
        produtos.add(new Produto(8, 23, 150, "Produto 8", "Descrição do Produto 8", "Categoria 8", "url_imagem_8", "Cor 8", 30.0f)); // ID 8
        produtos.add(new Produto(9, 23, 250, "Produto 9", "Descrição do Produto 9", "Categoria 9", "url_imagem_9", "Cor 9", 40.0f)); // ID 9
    }

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

    public Produto getProduto(int id) {
        for (Produto produto : produtos) {
            if (produto.getId() == id)
                return produto;
        }
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
        return linhasCarrinho;
    }

    //endregion

    //region PEDIDOS API CRUD
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
                    String errorMessage = "Ocorreu um erro ao carregar os dados.";
                    if (error != null && error.getMessage() != null) {
                        errorMessage = error.getMessage();
                    }
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                }
                });
            volleyQueue.add(req);
        }
    }


    public void getCarrinhoAPI(Context context) {
        carrinhoId = getLastCarrinhoId(context); // Recupera o carrinho guardado localmente

        if (carrinhoId > 0) {
            Log.d("CarrinhoDebug", "Carrinho já associado ao utilizador: " + carrinhoId);
            getLinhasCarrinhoAPI(context); // Carrega as linhas do carrinho
        } else {
            Log.d("CarrinhoDebug", "Carrinho não encontrado. A criar novo carrinho...");
            criarCarrinhoAPI(context); // Cria um novo carrinho se não houver nenhum associado
        }
    }


    private void criarCarrinhoAPI(Context context) {
        int userId = getUserId(context);
        String url = getBaseUrl(context) + CRIAR_CARRINHO_API + "?access-token=" + getUserToken(context);

        Log.d("CarrinhoDebug", "Criando carrinho. URL: " + url);

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
            // Armazena o produto para adicionar depois que o carrinho for criado
            produtoParaAdicionar = produto;
            quantidadeParaAdicionar = quantidade;
            getCarrinhoAPI(context);
            return;
        }

        String url = getBaseUrl(context) + "linhas-carrinhos/postlinhacarrinho?access-token=" + getUserToken(context);

        Log.d("CarrinhoDebug", "Adicionando linha ao carrinho. URL: " + url);

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
                        ArrayList<LinhaCarrinho> linhas = LinhaCarrinhoJsonParser.parserJsonLinhaCarrinho(response, context);
                        linhaCarrinhos = linhas;

                        if (linhasCarrinhoListener != null) {
                            linhasCarrinhoListener.onRefreshListaLinhasCarrinhos(linhaCarrinhos);
                        }
                    } catch (Exception e) {
                        Log.e("CarrinhoDebug", "Erro ao processar linhas do carrinho: " + e.getMessage());
                    }
                },
                error -> Log.e("CarrinhoDebug", "Erro ao obter linhas do carrinho: " + error.getMessage()));

        volleyQueue.add(request);
    }



    private String mUrlAPILinhasCarrinho(Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/linhascarrinhos";
    }

    public void atualizarLinhaCarrinhoAPI(Context context, LinhaCarrinho linha, LinhaCarrinhoListener listener) {
        StringRequest req = new StringRequest(Request.Method.PUT,
                mUrlUpdateLinha(linha.getId(), context), // Usando o endpoint correto
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getBoolean("success")) {
                            Toast.makeText(context, "Carrinho atualizado", Toast.LENGTH_SHORT).show();
                            if (listener != null) listener.onItemUpdate();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(context, "Erro ao atualizar carrinho", Toast.LENGTH_SHORT).show()) {
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

    public void removerLinhaCarrinhoAPI(Context context, int linhaId, LinhaCarrinhoListener listener) {
        StringRequest req = new StringRequest(Request.Method.DELETE,
                urlDeleteLinha(linhaId, context), // Usando o endpoint correto
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getBoolean("success")) {
                            Toast.makeText(context, "Item removido do carrinho", Toast.LENGTH_SHORT).show();
                            if (listener != null) listener.onItemUpdate();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(context, "Erro ao remover item", Toast.LENGTH_SHORT).show());
        volleyQueue.add(req);
    }

    public void deleteLinhaCarrinhoAPI(final Context context, final LinhaCarrinho linhaCarrinho) {
        if (!ProdutoJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, "Não tem ligação à internet", Toast.LENGTH_SHORT).show();
        } else {
            StringRequest req = new StringRequest(Request.Method.DELETE, urlDeleteLinha(linhaCarrinho.getId(), context), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (linhaCarrinhoListener != null) {
                        Log.d("LINHAS CARRINHO LISTENER", linhaCarrinhoListener.toString());
                        linhaCarrinhoListener.onItemUpdate();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String response = new String(error.networkResponse.data);
                    Log.e("VolleyError", "Error Response: " + response);
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            volleyQueue.add(req);
        }
    }

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
        } else {
            int utilizadorID = getUserId(context); // Fetch user ID from SharedPreferences
            String username = getUsername(context); // Método fictício para obter o username
            String accessToken = getUserToken(context);

            if (username != null && accessToken != null) {
                String url = "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/user/dados?username=" + username + "&access-token=" + accessToken;

                JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Extrair o objeto "user" que contém os dados
                            JSONObject user = response.getJSONObject("user");
                            JSONObject userForm = response.getJSONObject("userForm");

                            String username = user.getString("username");
                            String email = user.getString("email");
                            String rua = userForm.getString("rua");  // Corrigido para pegar de "userForm"
                            String codpostal = userForm.getString("codpostal");
                            String localidade = userForm.getString("localidade");
                            String nif = userForm.getString("nif");
                            String telefone = userForm.getString("telefone");
                            String nomeUtilizador = userForm.getString("nome");

                            // Criar o objeto de dados do usuário com os valores recebidos
                            Utilizador utilizadorData = new Utilizador();  // Agora funciona com o construtor sem parâmetros
                            utilizadorData.setUsername(username);
                            utilizadorData.setEmail(email);
                            utilizadorData.setRua(rua);
                            utilizadorData.setCodpostal(codpostal);
                            utilizadorData.setLocalidade(localidade);
                            utilizadorData.setNif(nif);
                            utilizadorData.setTelefone(telefone);
                            utilizadorData.setNome(nomeUtilizador);



                            // Notificar o listener de que os dados do utilizador foram recebidos
                            if (listener != null) {
                                listener.onGetUtilizadorData(utilizadorData);
                            }

                        } catch (JSONException e) {
                            Log.e("getUserDataAPI", "Erro ao fazer parse: " + e.getMessage());
                            Toast.makeText(context, "Erro ao processar os dados do usuário", Toast.LENGTH_SHORT).show();
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("getUserDataAPI", "Erro ao acessar o servidor: " + error.getMessage());
                        Toast.makeText(context, "Erro ao acessar o servidor: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                // Adiciona a requisição à fila
                volleyQueue.add(req);
            } else {
                Toast.makeText(context, "Username ou Access Token não encontrados", Toast.LENGTH_SHORT).show();
            }
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
        } else {
            JSONObject jsonParams = new JSONObject();
            try {
                jsonParams.put("username", username);
                jsonParams.put("password", password);
                jsonParams.put("email", email);
                jsonParams.put("rua", rua);
                jsonParams.put("codpostal", codpostal);
                jsonParams.put("localidade", localidade);
                jsonParams.put("nif", nif);
                jsonParams.put("telefone", telefone);
                jsonParams.put("nome", nomeUtilizador);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, mUrlAPISignup(context), jsonParams, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    utilizador = LoginJsonParser.parserJsonLogin(response);

                    // Atualizar o utilizador logado no SingletonProdutos
                    loggedInUser = utilizador;

                    // Guardar ID e token do utilizador no SharedPreferences
                    saveUserId(context, utilizador.getId());
                    saveUserToken(context, utilizador.getAuth_key(), utilizador.getUsername());
                    saveUsername(context, utilizador.getUsername());

                    // Notificar o listener de sucesso no signup
                    if (signupListener != null) {
                        signupListener.onUpdateSignup(utilizador);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Erro durante o signup", Toast.LENGTH_SHORT).show();
                    Log.e("Signup", "Erro: " + error.getMessage());
                }
            });

            volleyQueue.add(req);
        }
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


    public void updateProfileAPI(final String rua, final String codPostal, final String localidade, final String nif, final String telefone, final String nomeUtilizador, final Context context) {
        if (!ProdutoJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, "Não tem ligação à internet", Toast.LENGTH_SHORT).show();
        } else {
            JSONObject jsonParams = new JSONObject();
            try {
                jsonParams.put("telefone", telefone);
                jsonParams.put("nif", nif);
                jsonParams.put("nome", nomeUtilizador);
                jsonParams.put("rua", rua);
                jsonParams.put("localidade", localidade);
                jsonParams.put("codigopostal", codPostal);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, urlPostAPIPerfilDados(context), jsonParams, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    // Handle the response from the server after updating the profile
                    // For example, you may parse the response JSON and update the UI or perform additional actions

                    // Notify listeners or update UI as needed
                    if (profileUpdateListener != null) {
                        profileUpdateListener.onProfileUpdated(response.toString());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Handle error response
                    Toast.makeText(context, "Error durante a atualização do perfil", Toast.LENGTH_SHORT).show();
                }
            });

            volleyQueue.add(req);
        }
    }

    public void adicionarFaturaAPI(final Context context) {
        if (!ProdutoJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, "Não tem ligação à internet", Toast.LENGTH_SHORT).show();
        } else {
            StringRequest req = new StringRequest(Request.Method.POST, mUrlApiPostFatura(context), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if (faturasListener != null) {
                        Log.d("POST FATURAS", response);
                        faturasListener.onRefreshListaFatura(faturas);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    String response = new String(error.networkResponse.data);
                    Log.e("VolleyError", "Error Response: " + response);
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("user_id", getUserId(context) + "");


                    return params;
                }
            };
            volleyQueue.add(req);
        }

    }


    public void getFaturasAPI(final Context context) {
        if (!ProdutoJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, "Não tem ligação à internet", Toast.LENGTH_SHORT).show();
            if (faturasListener != null) {

                faturasListener.onRefreshListaFatura(faturas);
            }
        } else {
            JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, mUrlGetFaturas(context), null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    faturas = FaturasJsonParser.parserJsonFaturas(response);
                    //getLinhasFaturasAPI(context, faturas.get(0));
                    Log.d("Faturas", faturas.toString());
                    if (faturasListener != null) {
                        Log.d("GET FATURAS", response.toString());
                        faturasListener.onRefreshListaFatura(faturas);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("GET FATURAS error", error.getMessage());
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
            );
            volleyQueue.add(req);

        }
    }

    public void getLinhasFaturasAPI(final Context context, Fatura fatura) {
        if (!ProdutoJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, "Não tem ligação à internet", Toast.LENGTH_SHORT).show();
        } else {
            JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, mUrlGetLinhasFaturas(fatura.getId(), context), null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    linhasFaturas = LinhasFaturasJsonParser.parserJsonLinhasFaturas(response);
                    Log.d("Fatuas", linhasFaturas.toString());
                    if (linhasFaturasListener != null) {
                        Log.d("GET LINHAS FATURAS", response.toString());
                        linhasFaturasListener.onRefreshListaLinhasFaturas(fatura);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            volleyQueue.add(req);
        }
    }

    // Método para definir o carrinho
    public void setCarrinho(Carrinho novoCarrinho) {
        this.carrinho = novoCarrinho;
    }

    // Método para obter um produto pelo ID
    public Produto getProdutoById(int id) {
        Log.d("SingletonProdutos", "Buscando produto com ID: " + id);
        for (Produto produto : produtos) {
            Log.d("SingletonProdutos", "Produto encontrado: " + produto.getId());
            if (produto.getId() == id) {
                return produto;
            }
        }
        Log.e("SingletonProdutos", "Produto com ID " + id + " não encontrado.");
        return null; // Devolve null se o produto não for encontrado
    }

    // Método para adicionar um produto ao carrinho
    public void adicionarProdutoAoCarrinho(LinhaCarrinho linhaCarrinho) {
        linhasCarrinho.add(linhaCarrinho);
    }

    //endregion

    //region URLS API
    // API Produtos
    public String mUrlAPIProdutos(Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/produtos/produtos?access-token=" + getUserToken(context);
    }

    private String urlPostAPIPerfilDados(Context context) {

        String username = getUsername(context);
        String accessToken = getUserToken(context);

        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/user/dados?username=" + username + "&access-token=" + accessToken;
    }

    // API Delete Linha
    private String urlDeleteLinha(int id, Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/linhas-carrinhos/deletelinhacarrinho/"
                + "?access-token=" + getUserToken(context) + "&id=" + id;
    }


    // API Dados do Utilizador
    private String mUrlAPIUserData(Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/users/" + getUserId(context) + "?access-token=" + getUserToken(context);
    }

    // API Faturas
    private String mUrlGetFaturas(Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/faturas/" + getUserId(context) + "/faturas?access-token=" + getUserToken(context);
    }

    // API Linhas de Fatura
    private String mUrlGetLinhasFaturas(int fatura_id, Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/faturas/" + fatura_id + "/faturasdados?access-token=" + getUserToken(context);
    }

    // API Update Linha
    private String mUrlUpdateLinha(int id, Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/linhas-carrinhos/updatelinhacarrinho/"
                + "?access-token=" + getUserToken(context) + "&id=" + id;
    }


    // API Carrinho Dados
    private String mUrlGetLinhasCarrinhoAPI(Context context, int carrinhoId) {
        return getBaseUrl(context) + LINHAS_CARRINHO_API + "/dados?access-token="
               + getUserToken(context) + "&carrinho_id=" + carrinhoId;
    }

    // API Carrinho do Utilizador
    

    // API Post Linha no Carrinho
    private String mUrlAPIPostLinhaCarrinho(Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/linhascarrinhos/postlinhacarrinho?access-token=" + getUserToken(context);
    }

    // API Post Fatura
    private String mUrlApiPostFatura(Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/faturas/criarfatura?access-token=" + getUserToken(context);
    }

    // API Post Carrinho


    // API Login
    private String mUrlAPILogin(Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/auth/login";
    }

    // API Signup
    public String mUrlAPISignup(Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/auth/signup";
    }

    // Adicionar URLs para os novos endpoints
    private String mUrlAPIUpdateLinhaCarrinho(Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/linhascarrinhos/atualizar";
    }

    public String mUrlAPIDeleteLinhaCarrinho(Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/linhascarrinhos/deletar";
    }

    //endregion

    public void verificarECriarCarrinho(Context context, Produto produto, int quantidade) {
        Log.d("CarrinhoDebug", "Verificando carrinho para utilizador: " + getUserId(context));
        Log.d("CarrinhoDebug", "URL: " + mUrlGetCarrinhoAPI(context));

        StringRequest req = new StringRequest(Request.Method.GET,
                mUrlGetCarrinhoAPI(context),
                response -> {
                    Log.d("CarrinhoDebug", "Resposta verificação: " + response);
                    try {
                        JSONObject jsonCarrinho = new JSONObject(response);
                        adicionarLinhaCarrinhoAPI(context, produto, quantidade);
                    } catch (JSONException e) {
                        Log.e("CarrinhoDebug", "Erro ao parser resposta: " + e.getMessage());
                        criarNovoCarrinho(context, produto, quantidade);
                    }
                },
                error -> {
                    Log.e("CarrinhoDebug", "Erro ao verificar carrinho: " + error.toString());
                    if (error instanceof com.android.volley.ClientError) {
                        // Se for 404, criamos um novo carrinho
                        criarNovoCarrinho(context, produto, quantidade);
                    } else {
                        Toast.makeText(context, "Erro ao verificar carrinho", Toast.LENGTH_SHORT).show();
                    }
                });
        volleyQueue.add(req);
    }

    private void criarNovoCarrinho(Context context, Produto produto, int quantidade) {
        Log.d("CarrinhoDebug", "Criando novo carrinho");
        Log.d("CarrinhoDebug", "URL: " + mUrlPostCarrinhoAPI(context));

        StringRequest req = new StringRequest(Request.Method.POST,
                mUrlPostCarrinhoAPI(context),
                response -> {
                    Log.d("CarrinhoDebug", "Resposta criação: " + response);
                    try {
                        JSONObject resp = new JSONObject(response);
                        if (!resp.has("errors")) {
                            // Guardar o ID do carrinho criado
                            saveLastCarrinhoId(context, resp.getInt("id"));
                            Toast.makeText(context, "Carrinho criado com sucesso", Toast.LENGTH_SHORT).show();
                            adicionarLinhaCarrinhoAPI(context, produto, quantidade);
                        } else {
                            Log.e("CarrinhoDebug", "Erro nos dados: " + resp.toString());
                            Toast.makeText(context, "Erro ao criar carrinho", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e("CarrinhoDebug", "Erro ao criar carrinho: " + e.getMessage());
                        Toast.makeText(context, "Erro ao criar carrinho", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("CarrinhoDebug", "Erro ao criar carrinho: " + error.toString());
                    Toast.makeText(context, "Erro ao criar carrinho", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userdata_id", String.valueOf(getUserId(context)));
                
                // Adicionando campos obrigatórios
                params.put("total", "0"); // Valor inicial zero
                params.put("ivatotal", "0"); // Valor inicial zero
                
                // Data atual
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                params.put("data", sdf.format(new Date()));
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

    private String mUrlPostCarrinhoAPI(Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/carrinho/criar?access-token=" + getUserToken(context);
    }

    private String mUrlGetCarrinhoAPI(Context context) {
        return getBaseUrl(context) + CARRINHO_API + "/carrinho?access-token=" 
               + getUserToken(context) + "&user_id=" + getUserId(context);
    }

    // Método para guardar o ID do último carrinho criado
    private void saveLastCarrinhoId(Context context, int carrinhoId) {
        SharedPreferences prefs = context.getSharedPreferences("CarrinhoPrefs", Context.MODE_PRIVATE);
        prefs.edit().putInt("last_carrinho_id", carrinhoId).apply();
    }

    // Método para obter o ID do último carrinho
    private int getLastCarrinhoId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("CarrinhoPrefs", Context.MODE_PRIVATE);
        return prefs.getInt("last_carrinho_id", -1);
    }

    private String getBaseUrl(Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/";
    }

    // Variáveis para armazenar temporariamente o produto a ser adicionado
    private Produto produtoParaAdicionar;
    private int quantidadeParaAdicionar;

}
