package com.example.leiriajeansamsi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.osmdroid.api.IMapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.tileprovider.tilesource.XYTileSource;

public class SobreFragment extends Fragment {

    private TextView textInformacoes;
    private MapView mapView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        org.osmdroid.config.Configuration.getInstance().setUserAgentValue(getActivity().getPackageName());

        View view = inflater.inflate(R.layout.fragment_sobre, container, false);
        super.onViewCreated(view, savedInstanceState);
        // Altera o título da ActionBar
        if (getActivity() != null) {
            getActivity().setTitle("Sobre Nós");
        }
        // Referenciar os elementos do layout
        textInformacoes = view.findViewById(R.id.text_informacoes);

        // Inicializar o MapView do OSMDroid
        mapView = view.findViewById(R.id.mapa);
        mapView.setBuiltInZoomControls(true);  // Permite o controle de zoom
        mapView.setMultiTouchControls(true);   // Permite zoom multi-toque

        // Configurar o mapa com o estilo CartoDB positron (estilo claro)
        mapView.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK);


        // Configurar o zoom e o centro do mapa
        IMapController mapController = mapView.getController();
        mapController.setZoom(15);  // Ajusta o zoom do mapa
        GeoPoint startPoint = new GeoPoint(39.7476, -8.8055);  // Latitude e longitude do local
        mapController.setCenter(startPoint);

        // Adicionar marcador no mapa
        Marker marker = new Marker(mapView);
        marker.setPosition(startPoint);
        marker.setTitle("Politécnico de Leiria");
        mapView.getOverlays().add(marker);

        // Informações da empresa
        String informacoes =
                "Fundada em 2024, a nossa loja nasceu com a missão de trazer estilo, conforto e qualidade para todos os nossos clientes. Inspirados pelas últimas tendências da moda e comprometidos com a sustentabilidade, oferecemos uma seleção de roupas cuidadosamente escolhidas para refletir as necessidades e preferências de cada pessoa. \n\n\n"+
                "Endereço: Politécnico de Leiria\n" +
                "Telefone: +351 123 456 789\n" +
                "Email: contato@leiriajeans.pt";
        textInformacoes.setText(informacoes);

        return view;
    }
}
