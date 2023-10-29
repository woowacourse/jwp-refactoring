package kitchenpos.core.domain.ordertable;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import kitchenpos.core.domain.order.OrderValidator;

@Entity
public class OrderTable {

    private static final int NUMBER_OF_GUESTS_MIN = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(table = "table_group", name = "table_group_id",
            foreignKey = @ForeignKey(name = "fk_order_table_to_table_group"),
            nullable = false)
    private Long tableGroupId;
    @Column(nullable = false)
    private int numberOfGuests;
    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final Long id,
                      final Long tableGroupId,
                      final int numberOfGuests,
                      final boolean empty) {
        validateNumberOfGuests(numberOfGuests);
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public void validateToPlace() {
        if (isEmpty()) {
            throw new IllegalArgumentException("빈 테이블에는 주문을 등록할 수 없습니다.");
        }
    }

    public void group(final long tableGroupId) {
        validateAbleToGroup();
        this.empty = false;
        this.tableGroupId = tableGroupId;
    }

    private void validateAbleToGroup() {
        if (isNotEmpty()) {
            throw new IllegalArgumentException("이미 주문 상태인 테이블을 단체로 지정할 수 없습니다.");
        }
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException("이미 단체에 속한 테이블을 단체로 지정할 수 없습니다.");
        }
    }

    public void unGroup(final OrderValidator orderValidator) {
        validateAbleToUnGroup();
        orderValidator.validateHasAnyOrderInProgress(id);
        this.tableGroupId = null;
        this.empty = false;
    }

    private void validateAbleToUnGroup() {
        if (Objects.isNull(tableGroupId)) {
            throw new IllegalArgumentException("이미 개별 테이블이라 단체에서 분할할 수 없습니다.");
        }
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        if (isEmpty()) {
            throw new IllegalArgumentException("빈 테이블의 인원 수를 변경할 수 없습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < NUMBER_OF_GUESTS_MIN) {
            throw new IllegalArgumentException("테이블 당 인원 수는 최소 " + NUMBER_OF_GUESTS_MIN + "명입니다.");
        }
    }

    public void changeEmpty(final OrderValidator orderValidator, final boolean empty) {
        validateAbleToChangeEmpty();
        orderValidator.validateHasAnyOrderInProgress(id);
        this.empty = empty;
    }

    private void validateAbleToChangeEmpty() {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException("단체로 지정된 테이블을 비울 수 없습니다.");
        }
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isNotEmpty() {
        return !empty;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
