package com.plantrip.api.exchange;

import java.math.BigDecimal;
import java.util.Map;

public record FrankfurterResponse(Map<String, BigDecimal> rates) {
}
