package kitchenpos.domain;

import kitchenpos.config.BaseEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import java.time.LocalDateTime;
import java.util.List;

@AttributeOverride(name = "id", column = @Column(name = "table_group_id"))
@Entity
public class TableGroup extends BaseEntity {
    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;
    private LocalDateTime createdDate;

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

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }
}
