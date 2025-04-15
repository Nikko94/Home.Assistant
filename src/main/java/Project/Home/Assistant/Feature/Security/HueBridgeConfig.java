package Project.Home.Assistant.Feature.Security;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "hue.bridge")
public class HueBridgeConfig {
    private String ip;
    private String username;
    private String clientId;
    private String clientSecret;
    private String accessToken;
    private String refreshToken;
}
