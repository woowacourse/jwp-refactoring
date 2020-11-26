package kitchenpos.table.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;

public class OrderTablesSerializer extends JsonSerializer<OrderTables> {
    @Override
    public void serialize(OrderTables value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartArray();
        for (OrderTable orderTable : value.getOrderTables()) {
            gen.writeStartObject();
            gen.writeObjectField("id", orderTable.getId());
            gen.writeObjectField("tableGroupId", orderTable.getTableGroupId());
            gen.writeObjectField("numberOfGuests", orderTable.getNumberOfGuests());
            gen.writeObjectField("empty", orderTable.isEmpty());
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }
}
