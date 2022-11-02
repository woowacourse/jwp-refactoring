package kitchenpos.domain.tablegroup;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "table_group_id", nullable = false, updatable = false)
    private List<OrderTableRef> orderTableRefs = new ArrayList<>();

    protected TableGroup() {
    }

    public TableGroup(LocalDateTime createdDate, List<Long> orderTableIds) {
        this(null, createdDate, orderTableIds.stream().map(OrderTableRef::new).collect(Collectors.toList()));
    }

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTableRef> orderTableRefs) {
        validateOrderTableIdsSize(orderTableRefs);
        this.id = id;
        this.createdDate = createdDate;
        this.orderTableRefs = orderTableRefs;
    }

    private void validateOrderTableIdsSize(List<OrderTableRef> orderTableRefs) {
        if (CollectionUtils.isEmpty(orderTableRefs) || orderTableRefs.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public void ungroup() {
        this.orderTableRefs.clear();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableRef> getOrderTableRefs() {
        return orderTableRefs;
    }
}
