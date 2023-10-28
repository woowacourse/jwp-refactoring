package kitchenpos.domain.table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Optional;

@Entity
public class OrderTable {
    public static final String NUMBER_OF_GUESTS_IS_BELOW_ZERO_ERROR_MESSAGE = "손님 수는 0보다 작을 수 없습니다.";
    public static final String CHANGE_UNORDERABLE_TABLE_NUMBER_OF_TABLE_ERROR_MESSAGE = "주문 불가능한 테이블의 손님 수는 변경할 수 없습니다.";
    public static final String CHANGE_UNORDERABLE_TABLE_WHEN_IN_TABLE_GROUP_ERROR_MESSAGE = "그룹 지정된 테이블은 주문 불가능 상태로 변경할 수 없습니다.";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = true, name = "table_group_id")
    private Long tableGroupId;
    @NotNull
    private int numberOfGuests;
    @NotNull
    private boolean orderable;

    protected OrderTable() {
    }

    private OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean orderable) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
        this.orderable = orderable;
    }

    public boolean isGrouped() {
        return Objects.nonNull(this.tableGroupId);
    }

    private void validateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException(NUMBER_OF_GUESTS_IS_BELOW_ZERO_ERROR_MESSAGE);
        }
    }

    public static OrderTable of(final int numberOfGuests) {
        return new OrderTable(null, null, numberOfGuests, false);
    }

    public static OrderTable of(final Long tableGroup) {
        return new OrderTable(null, tableGroup, 0, false);
    }

    public static OrderTable of(final Long tableGroupId, final int numberOfGuests) {
        return new OrderTable(null, tableGroupId, numberOfGuests, true);
    }

    public static OrderTable of(final int numberOfGuests, final boolean orderable) {
        return new OrderTable(null, null, numberOfGuests, orderable);
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (!this.orderable) {
            throw new IllegalArgumentException(CHANGE_UNORDERABLE_TABLE_NUMBER_OF_TABLE_ERROR_MESSAGE);
        }
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public void setUnOrderable() {
        if (Objects.nonNull(this.tableGroupId)) {
            throw new IllegalArgumentException(CHANGE_UNORDERABLE_TABLE_WHEN_IN_TABLE_GROUP_ERROR_MESSAGE);
        }
        setOrderable(false);
    }

    public void setTableGroupId(final Long tableGroup) {
        this.tableGroupId = tableGroup;
    }

    public void setOrderable(final boolean orderable) {
        this.orderable = orderable;
    }

    public Long getId() {
        return id;
    }

    public Optional<Long> getTableGroupId() {
        return Optional.ofNullable(tableGroupId);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isOrderable() {
        return orderable;
    }
}
