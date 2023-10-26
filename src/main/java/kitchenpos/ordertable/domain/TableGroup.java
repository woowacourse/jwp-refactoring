package kitchenpos.ordertable.domain;

import kitchenpos.ordertable.exception.InvalidTableGroupException;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
        this(null, null, orderTables);
    }
    
    public TableGroup(final Long id,
                      final LocalDateTime createdDate,
                      final List<OrderTable> orderTables) {
        validate(orderTables);
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }
    
    private void validate(final List<OrderTable> orderTables) {
        validateOrderTableSize(orderTables);
        validateIfDuplicatedTableExist(orderTables);
        validateIfOrderTableBelongsToOtherTableGroup(orderTables);
        validateIfOrderTableNotEmpty(orderTables);
    }
    
    private void validateOrderTableSize(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new InvalidTableGroupException("개별 테이블 수가 2개 미만이면 단체 테이블을 만들 수 없습니다");
        }
    }
    
    private void validateIfDuplicatedTableExist(List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                                                    .map(OrderTable::getId)
                                                    .collect(Collectors.toList());
        final Set<Long> notDuplicatedOrderTableIds = new HashSet<>(orderTableIds);
        if (notDuplicatedOrderTableIds.size() != orderTableIds.size()) {
            throw new InvalidTableGroupException("같은 테이블은 단체 테이블로 만들 수 없습니다");
        }
        
    }
    
    private void validateIfOrderTableBelongsToOtherTableGroup(List<OrderTable> orderTables) {
        boolean isOrderTableBelongsToOtherTableGroup = orderTables.stream()
                                                                  .anyMatch(orderTable -> Objects.nonNull(orderTable.getTableGroup()));
        if (isOrderTableBelongsToOtherTableGroup) {
            throw new InvalidTableGroupException("다른 테이블 그룹에 속한 테이블은 단체 테이블로 만들 수 없습니다");
        }
    }
    
    private void validateIfOrderTableNotEmpty(List<OrderTable> orderTables) {
        boolean isOrderTableNotEmpty = orderTables.stream()
                                                  .anyMatch(orderTable -> !orderTable.isEmpty());
        if (isOrderTableNotEmpty) {
            throw new InvalidTableGroupException("비어있지 않은 테이블은 단체 테이블으로 만들 수 없습니다");
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
