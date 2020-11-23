package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import kitchenpos.ui.dto.jsonparser.OrderTablesDeserializer;
import kitchenpos.ui.dto.jsonparser.OrderTablesSerializer;
import lombok.Getter;

@Embeddable
@JsonSerialize(using = OrderTablesSerializer.class)
@JsonDeserialize(using = OrderTablesDeserializer.class)
@Getter
public class OrderTables {
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "table_group_id")
    private final List<OrderTable> orderTables;

    public OrderTables(final List<OrderTable> orderTables) {
        validate(orderTables);
        this.orderTables = orderTables;
    }

    public OrderTables() {
        orderTables = new ArrayList<>();
    }

    private void validate(final List<OrderTable> orderTables) {
        if (orderTables.isEmpty() || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public void changeEmptyStatus() {
        if (orderTables.stream()
            .anyMatch(orderTable -> !orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId()))) {
            throw new IllegalArgumentException();
        }
        for (OrderTable orderTable : orderTables) {
            orderTable.changeStatus(false);
        }
    }

    public List<Long> extractIds() {
        return orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
    }

    public void ungroup() {
        for (OrderTable orderTable : orderTables) {
            orderTable.changeStatus(false);
            orderTable.ungroup();
        }
    }
}
