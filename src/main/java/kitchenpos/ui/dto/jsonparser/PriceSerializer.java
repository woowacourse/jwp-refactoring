package kitchenpos.ui.dto.jsonparser;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import kitchenpos.domain.Price;

public class PriceSerializer extends JsonSerializer<Price> {
    @Override
    public void serialize(final Price price, final JsonGenerator gen, final SerializerProvider serializerProvider) throws
        IOException {
        gen.writeNumber(price.getValue());
    }
}
