package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.CollectionUtils;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tableGroup", cascade = CascadeType.PERSIST)
    private List<OrderTable> orderTables;

    public TableGroup() {
        this(null, new ArrayList<>());
    }

    public TableGroup(Long id, List<OrderTable> orderTables) {
        this.id = id;
        this.orderTables = orderTables;
    }

    public void updateOrderTables(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("TableGroup에 속한 OrderTable은 최소 2개 이상이어야합니다.");
        }
        this.orderTables = orderTables;
    }

    public void ungroup() {
        for (OrderTable orderTable : orderTables) {
            orderTable.ungroup();
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
