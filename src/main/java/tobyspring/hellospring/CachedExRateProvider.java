package tobyspring.hellospring;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CachedExRateProvider implements ExRateProvider{
    private final ExRateProvider target;

    private BigDecimal cachedExRate;
    private LocalDateTime cacheExpriryTime;

    public CachedExRateProvider(ExRateProvider target) {
        this.target = target;
    }

    @Override
    public BigDecimal getExRate(String currency) throws IOException {
        if (cachedExRate == null || cacheExpriryTime.isBefore(LocalDateTime.now())) {
            cachedExRate = this.target.getExRate(currency);
            cacheExpriryTime = LocalDateTime.now().plusSeconds(3);

            System.out.println("Cache Updated");
        }

        return cachedExRate;
    }
}
