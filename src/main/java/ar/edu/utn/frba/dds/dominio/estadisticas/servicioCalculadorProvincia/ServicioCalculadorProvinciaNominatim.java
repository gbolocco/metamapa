package ar.edu.utn.frba.dds.dominio.estadisticas.servicioCalculadorProvincia;

import ar.edu.utn.frba.dds.dominio.hechos.Ubicacion;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ServicioCalculadorProvinciaNominatim implements ServicioCalculadorProvincia {

    @Override
    public String calcularProvincia(Ubicacion ubicacion) {
        try {
            double lat = ubicacion.getLatitud();
            double lon = ubicacion.getLongitud();

            String urlStr = String.format(
                    "https://nominatim.openstreetmap.org/reverse?lat=%f&lon=%f&format=json&accept-language=es",
                    lat, lon
            );

            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "DDS-ProvinciaLookup/1.0 (contacto@tuapp.com)");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            JSONObject json = new JSONObject(response.toString());
            JSONObject address = json.getJSONObject("address");

            // Algunas provincias pueden venir como "state" o "region"
            String provincia = null;
            if (address.has("state")) {
                provincia = address.getString("state");
            } else if (address.has("region")) {
                provincia = address.getString("region");
            }

            if (provincia == null) {
                System.err.println("Advertencia: No se pudo determinar la provincia para: " + lat + ", " + lon);
                return "Desconocida";
            }

            return provincia;

        } catch (Exception e) {
            throw new RuntimeException("Error consultando Nominatim: " + e.getMessage(), e);
        }
    }
}
