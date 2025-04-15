package Project.Home.Assistant.Feature;

import lombok.Data;

@Data
public class LampStateRequest {

    // Sätt exempelvärde på on/off (exempel: true = slå på, false = stänga av)
    private Boolean on;

    // Ljusstyrka i procent (exempelvis 0.0 till 100.0)
    private Double brightness;

    // Färgtemperatur angivet i mirek. Se till att värdet hamnar inom lampans giltiga intervall
    private Integer mirek;

    // XY-färgkoordinater (båda ska vara satta för att uppdatera färgen)
    private Double x;
    private Double y;
}
