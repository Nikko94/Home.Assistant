package Project.Home.Assistant.Feature;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/lights")
public class LightController {

    @Autowired
    private HueService hueService;

    /**
     * Uppdaterar tillståndet för en specifik lampa.
     * Exempelanrop:
     * PUT /api/lights/{lampId}?remote=false
     * Med en JSON-body:
     * {
     *    "on": true,
     *    "brightness": 75.0,
     *    "mirek": 400,
     *    "x": 0.5,
     *    "y": 0.45
     * }
     *
     * @param lampId identifierare för lampan
     * @param remote om true används fjärrstyrning (dock i v2-exemplet ignoreras remote)
     * @param request JSON-payload med önskade ändringar
     * @return ResponseEntity med status
     */
    @PutMapping("/{lampId}")
    public Mono<ResponseEntity<String>> updateLampState(
            @PathVariable String lampId,
            @RequestParam(defaultValue = "false") boolean remote,
            @RequestBody LampStateRequest request) throws Exception {
        return hueService.updateLampState(lampId, request)
                .map(result -> ResponseEntity.ok("Lampans status uppdaterad: " + result))
                .onErrorResume(error -> Mono.just(ResponseEntity.status(500)
                        .body("Fel vid uppdatering: " + error.getMessage())));
    }

}
