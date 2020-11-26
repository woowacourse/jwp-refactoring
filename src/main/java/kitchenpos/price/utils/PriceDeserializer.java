package kitchenpos.price.utils;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import kitchenpos.price.domain.Price;

public class PriceDeserializer extends JsonDeserializer<Price> {
    @Override
    public Price deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {
        return new Price(new BigDecimal(parser.getText()));
    }
}
