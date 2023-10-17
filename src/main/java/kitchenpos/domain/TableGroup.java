package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNullElseGet;

public class TableGroup {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    public TableGroup(final Long id, final LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = requireNonNullElseGet(createdDate, LocalDateTime::now);
    }

    public TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = requireNonNullElseGet(createdDate, LocalDateTime::now);
        validate(orderTables);
        this.orderTables = orderTables;
    }

    private void validate(final List<OrderTable> orderTables) {
        if (isNull(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("테이블 그룹은 2개 이상의 테이블로 구성되어야 합니다.");
        }
        for (final OrderTable savedOrderTable : orderTables) {
            validate(savedOrderTable);
        }
    }

    private void validate(final OrderTable savedOrderTable) {
        if (!savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블만 테이블 그룹으로 만들 수 있습니다.");
        }
        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException("다른 테이블 그룹에 포함되어있습니다. table id: " + savedOrderTable.getId());
        }
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
        if (Objects.isNull(createdDate)) {
            throw new IllegalArgumentException("생성일자를 null로 설정할 수 없습니다.");
        }
        this.createdDate = createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

}
