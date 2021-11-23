package kitchenpos.common.support;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.math.BigDecimal;
import kitchenpos.common.Price;

public class PriceDeserializer extends JsonDeserializer<Price> {

    @Override
    public Price deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return new Price(BigDecimal.valueOf(p.getIntValue()));
    }
}
