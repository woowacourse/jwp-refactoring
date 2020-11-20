package kitchenpos.ui.dto.jsonparser;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;

public class OrderTablesDeserializer extends JsonDeserializer<OrderTables> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public OrderTables deserialize(JsonParser p, DeserializationContext ctxt) throws
        IOException, JsonProcessingException {
        ObjectCodec objectCodec = p.getCodec();
        JsonNode jsonNode = objectCodec.readTree(p);

        final List<OrderTable> orderTables = objectMapper.readValue(jsonNode.toString(),
            new TypeReference<List<OrderTable>>() {
            });

        return new OrderTables(orderTables);
    }
}
