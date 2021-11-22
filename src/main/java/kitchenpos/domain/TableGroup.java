package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.LAZY)
    private List<OrderTable> orderTables = new ArrayList<>();

    public TableGroup() {
        this(null, LocalDateTime.now());
    }

    public TableGroup(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public void add(List<OrderTable> sources) {
        this.orderTables = sources;
        for (OrderTable source : sources) {
            source.belongsTo(this);
        }
    }

    public void remove(List<OrderTable> sources) {
        this.orderTables = new ArrayList<>();
        for (OrderTable source : sources) {
            source.notBelongsTo();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TableGroup)) {
            return false;
        }
        TableGroup tableGroup = (TableGroup) o;

        return getId() != null ? getId().equals(tableGroup.getId()) : tableGroup.getId() == null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
