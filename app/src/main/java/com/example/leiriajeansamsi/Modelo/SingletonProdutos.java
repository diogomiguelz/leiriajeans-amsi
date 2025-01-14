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

import java.util.ArrayList;
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

public class SingletonProdutos {

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
    private LinhasCarrinhosListener linhasCarrinhosListener;
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

    public void setLinhasCarrinhosListener(LinhasCarrinhosListener linhasCarrinhosListener) {
        this.linhasCarrinhosListener = linhasCarrinhosListener;
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
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            volleyQueue.add(req);
        }
    }


    public void getCarrinhoAPI(final Context context) {
        if (!ProdutoJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, "Não tem ligação à internet", Toast.LENGTH_SHORT).show();

        } else {

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, mUrlGetCarrinho(context), null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    // Add this line to log the response
                    // converter json em livros
                    carrinho = CarrinhoJsonParser.parserJsonCarrinho(response);

                    Log.d("API_Response CARRINHO", response.toString());
                    Log.d("Carrinho", carrinho.toString());

                    // informar a vista
                    if (carrinhoListener != null) {

                        Log.d("CARRINHO LISTENER", carrinhoListener.toString());
                        carrinhoListener.onRefreshListaCarrinho(carrinho);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("API_Response CARRINHO", error.toString());
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            volleyQueue.add(req);
        }
    }

    public void adicionarCarrinhoAPI(final Context context) {
        if (!ProdutoJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, "Não tem ligação à internet", Toast.LENGTH_SHORT).show();
        } else {
            StringRequest req = new StringRequest(Request.Method.POST, mUrlApiPostCarrinho(context), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    //  Toast.makeText(context, "Erro ao adicionar carrinho", Toast.LENGTH_SHORT).show();
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


    public void getLinhasCarrinhosAPI(final Context context, Carrinho carrinho) {
        if (!ProdutoJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, "Não tem ligação à internet", Toast.LENGTH_SHORT).show();


            if (linhasCarrinhosListener != null)
                linhasCarrinhosListener.onRefreshListaLinhasCarrinhos(linhaCarrinhos);
        } else {

            JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, mUrlGetLinhasCarrinho(carrinho.getId(), context), null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    // Add this line to log the response
                    // converter json em livros
                    linhaCarrinhos = LinhaCarrinhoJsonParser.parserJsonLinhaCarrinho(response, context);
                    Log.d("API_Response", response.toString());
                    Log.d("LinhaCarrinho", linhaCarrinhos.toString());

                    // informar a vista
                    if (linhasCarrinhosListener != null) {

                        Log.d("LINHAS CARRINHO LISTENER", linhasCarrinhosListener.toString());
                        linhasCarrinhosListener.onRefreshListaLinhasCarrinhos(linhaCarrinhos);
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

    public void updateLinhaCarrinhoAPI(final Context context, final LinhaCarrinho linhaCarrinho) {
        if (!ProdutoJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, "Não tem ligação à internet", Toast.LENGTH_SHORT).show();
        } else {
            StringRequest req = new StringRequest(Request.Method.PUT, mUrlUpdateLinha(linhaCarrinho.getId(), context), new Response.Listener<String>() {
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
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("quantidade", linhaCarrinho.getQuantidade() + "");

                    return params;
                }
            };
            volleyQueue.add(req);
        }
    }


    public void adicionarLinhaCarrinhoAPI(final Context context, Produto produto, Carrinho carrinho) {
        if (carrinho == null) {
            Toast.makeText(context, "Carrinho não disponível", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtenha o user_id do utilizador logado
        int userId = getUserId(context); // Supondo que você tenha um método para obter o ID do usuário logado

        // Inicialize ivatotal e total como 0
        float ivatotal = 0.0f;
        float total = 0.0f;

        // Crie um novo objeto LinhaCarrinho com os valores necessários
        LinhaCarrinho novaLinha = new LinhaCarrinho(0, 1, carrinho.getId(), produto, ivatotal, produto.getPreco());

        // Agora você pode fazer a requisição para adicionar a linha ao carrinho
        // Aqui você deve implementar a lógica para enviar a nova linha para a API
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

    public void getUserDataAPI(Context context) {
        if (!ProdutoJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, "Não tem ligação à internet", Toast.LENGTH_SHORT).show();
        } else {
            int utilizadorID = getUserId(context); // Fetch user ID from SharedPreferences
            JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, mUrlAPIUserData(context), null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject item = response.getJSONObject(i);
                            utilizadorData = LoginJsonParser.parserJsonGetUtilizadorData(item);

                            if (utilizadorDataListener != null) {
                                utilizadorDataListener.onGetUtilizadorData(utilizadorData);
                            }

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Erro ao acessar o servidor: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            volleyQueue.add(req);
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

    public void signupAPI(final String username, final String password, final String email, final Context context) {
        if (!ProdutoJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, "Não tem ligação à internet", Toast.LENGTH_SHORT).show();
        } else {
            JSONObject jsonParams = new JSONObject();
            try {
                jsonParams.put("username", username);
                jsonParams.put("password", password);
                jsonParams.put("email", email);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, mUrlAPISignup(context), jsonParams, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    utilizador = LoginJsonParser.parserJsonLogin(response);

                    // Update the loggedInUser in SingletonProdutos with the new user data
                    loggedInUser = utilizador;

                    // Save the user's ID and token to SharedPreferences
                    saveUserId(context, utilizador.getId());
                    saveUserToken(context, utilizador.getAuth_key(), utilizador.getUsername());

                    // Perform additional actions as needed
                    if (signupListener != null) {
                        signupListener.onUpdateSignup(utilizador);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Error durante o signup", Toast.LENGTH_SHORT).show();
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


    public void updateProfileAPI(final String primeironome, final String apelido, final String telemovel, final String nif, final String genero, final String dtaNascimento, final String rua, final String localidade, final String codigoPostal, final Context context) {
        if (!ProdutoJsonParser.isConnectionInternet(context)) {
            Toast.makeText(context, "Não tem ligação à internet", Toast.LENGTH_SHORT).show();
        } else {
            JSONObject jsonParams = new JSONObject();
            try {
                jsonParams.put("primeironome", primeironome);
                jsonParams.put("apelido", apelido);
                jsonParams.put("telefone", telemovel);
                jsonParams.put("nif", nif);
                jsonParams.put("genero", genero);
                jsonParams.put("dtanasc", dtaNascimento);
                jsonParams.put("rua", rua);
                jsonParams.put("localidade", localidade);
                jsonParams.put("codigopostal", codigoPostal);
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

    public void criarCarrinhoAPI(final Context context) {
        String url = mUrlApiPostCarrinho(context); // URL para criar o carrinho

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        // Verifique se o campo "id" está presente na resposta
                        if (jsonResponse.has("id")) {
                            int carrinhoId = jsonResponse.getInt("id"); // Obtenha o ID do carrinho
                            int userId = getUserId(context); // Supondo que você tenha um método para obter o ID do usuário
                            Carrinho novoCarrinho = new Carrinho(carrinhoId, userId, 0, 0, 0);
                            setCarrinho(novoCarrinho); // Armazena o novo carrinho
                            Toast.makeText(context, "Carrinho criado com sucesso! ID: " + carrinhoId, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Erro: ID do carrinho não retornado.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Erro ao processar a resposta: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Erro ao criar carrinho: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userdata_id", "1"); // Adicione o ID do usuário aqui
                params.put("total", "0");
                params.put("ivatotal", "0");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
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
        return null; // Retorna null se o produto não for encontrado
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

    // API Perfil Dados
    private String urlPostAPIPerfilDados(Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/users/" + getUserId(context) + "/dados?access-token=" + getUserToken(context);
    }

    // API Delete Linha
    private String urlDeleteLinha(int id, Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/linhascarrinhos/" + id + "/delete?access-token=" + getUserToken(context);
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
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/linhascarrinhos/" + id + "/update?access-token=" + getUserToken(context);
    }

    // API Carrinho Dados
    private String mUrlGetLinhasCarrinho(int carrinho_id, Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/linhascarrinhos/" + carrinho_id + "/dados?access-token=" + getUserToken(context);
    }

    // API Carrinho do Utilizador
    private String mUrlGetCarrinho(Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/carrinho/" + getUserId(context) + "/carrinho?access-token=" + getUserToken(context);
    }

    // API Post Linha no Carrinho
    private String mUrlAPIPostLinhaCarrinho(Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/linhascarrinhos/criar?access-token=" + getUserToken(context);
    }

    // API Post Fatura
    private String mUrlApiPostFatura(Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/faturas/criarfatura?access-token=" + getUserToken(context);
    }

    // API Post Carrinho
    private String mUrlApiPostCarrinho(Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/carrinho/criar?access-token=" + getUserToken(context);
    }

    // API Login
    private String mUrlAPILogin(Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/auth/login";
    }

    // API Signup
    private String mUrlAPISignup(Context context) {
        return "http://" + getApiIP(context) + "/leiriajeans/backend/web/api/auth/signup";
    }


//endregion


}
