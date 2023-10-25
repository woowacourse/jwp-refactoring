package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import kitchenpos.order.domain.OrderTable;
import suppoert.domain.BaseEntity;

@Entity
public class TableGroup extends BaseEntity {

    @Column(nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();
    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
