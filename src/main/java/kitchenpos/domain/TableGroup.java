package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class TableGroup {
    private static final int MIN_GROUP_SIZE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    private LocalDateTime createdDate;

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;

        validateCreation(orderTables);

        for (OrderTable table : orderTables) {
            table.belongsTo(this);
        }
    }

    public TableGroup(List<OrderTable> orderTables) {
        this(null, LocalDateTime.now(), orderTables);
    }

    public TableGroup() {
    }

    private void validateCreation(List<OrderTable> orderTables) {
        if (orderTables.size() < MIN_GROUP_SIZE) {
            throw new IllegalStateException("단체 지정할 테이블들이 부족합니다.");
        }

        for (final OrderTable orderTable : orderTables) {
            if (Objects.nonNull(orderTable.getTableGroup())) {
                throw new IllegalStateException("이미 단체 지정된 테이블입니다.");
            }

            if (!orderTable.isEmpty()) {
                throw new IllegalStateException("빈 테이블이 아닙니다.");
            }
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
