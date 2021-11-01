package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class TableGroup {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @CreatedDate
    private LocalDateTime createdDate;
    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    public TableGroup() {
    }

    public TableGroup(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroup(final Long id,
                      final List<OrderTable> orderTables) {
        this.id = id;
        this.orderTables = orderTables;
    }

    public void changeOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
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
