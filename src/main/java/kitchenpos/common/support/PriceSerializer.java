package kitchenpos.common.support;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import kitchenpos.common.Price;

public class PriceSerializer extends JsonSerializer<Price> {

    @Override
    public void serialize(Price price, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNumber(price.getPrice());
    }
}
