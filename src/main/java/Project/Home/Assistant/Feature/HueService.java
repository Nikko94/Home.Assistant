package Project.Home.Assistant.Feature;

import Project.Home.Assistant.Feature.Security.HueBridgeConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Service
public class HueService {

    private static final Logger log = LoggerFactory.getLogger(HueService.class);

    @Autowired
    private HueBridgeConfig hueBridgeConfig;

    @Autowired
    private WebClientConfig webClientConfig;

    // Om du inte redan har en ObjectMapper som bean kan du instansiera en här
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Uppdaterar lampans tillstånd via Hue API v2 (lokalt).
     * URL: https://<bridge-ip>/clip/v2/resource/light/<lampId>
     * Payload ex:
     * {
     *   "on": { "on": true },
     *   "dimming": { "brightness": 50.0 },
     *   "color": { "xy": { "x": 0.4605, "y": 0.2255 } }
     * }
     * Autentisering via header "hue-application-key"
     *
     * @param lampId Identifieraren (v2‑ID) för lampan.
     * @param request DTO med egenskaper att uppdatera.
     */
    public Mono<String> updateLampState(String lampId, LampStateRequest request) throws Exception {
        String url = "https://" + hueBridgeConfig.getIp() + "/clip/v2/resource/light/" + lampId;

        // Bygg payload enligt v2-strukturen
        Map<String, Object> payload = new HashMap<>();
        if (request.getOn() != null) {
            Map<String, Object> onObj = new HashMap<>();
            onObj.put("on", request.getOn());
            payload.put("on", onObj);
        }
        if (request.getBrightness() != null) {
            Map<String, Object> dimmingObj = new HashMap<>();
            dimmingObj.put("brightness", request.getBrightness());
            payload.put("dimming", dimmingObj);
        }
        Map<String, Object> colorPayload = new HashMap<>();
        if (request.getMirek() != null) {
            colorPayload.put("ct", request.getMirek());
        }
        if (request.getX() != null && request.getY() != null) {
            Map<String, Double> xyObj = new HashMap<>();
            xyObj.put("x", request.getX());
            xyObj.put("y", request.getY());
            colorPayload.put("xy", xyObj);
        }
        if (!colorPayload.isEmpty()) {
            payload.put("color", colorPayload);
        }

        // Logga payloaden som JSON
        try {
            String payloadJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(payload);
            log.info("Skickar payload: \n{}", payloadJson);
        } catch (Exception e) {
            log.error("Kunde inte serialisera payload", e);
        }

        return webClientConfig.webClient()
                .put()
                .uri(url)
                .header("Content-Type", "application/json")
                .header("hue-application-key", hueBridgeConfig.getUsername())
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(error -> {
                    log.error("Fel vid anropet", error);
                    return Mono.error(new RuntimeException("Anropet misslyckades", error));
                });
    }
}
