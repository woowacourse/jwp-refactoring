package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TableGroup {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables = new ArrayList<>();

    public TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables.addAll(orderTables);
    }

    public TableGroup(final Long id, final LocalDateTime createdDate) {
        this(id, createdDate, Collections.emptyList());
    }

    public TableGroup(final LocalDateTime createdDate) {
        this(null, createdDate, Collections.emptyList());
    }

    private TableGroup() {
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void addOrderTables(final List<OrderTable> orderTables) {
        this.orderTables.addAll(orderTables);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TableGroup tableGroup)) {
            return false;
        }
        return Objects.equals(id, tableGroup.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
