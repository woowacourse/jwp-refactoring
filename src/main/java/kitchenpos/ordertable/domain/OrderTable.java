package kitchenpos.ordertable.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.vo.NumberOfGuests;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long tableGroupId;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    private OrderTable(NumberOfGuests numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    private OrderTable(
            Long id,
            Long tableGroupId,
            NumberOfGuests numberOfGuests,
            boolean empty
    ) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(
            int numberOfGuests,
            boolean empty
    ) {
        return new OrderTable(
                NumberOfGuests.from(numberOfGuests),
                empty
        );
    }

    public void registerTableGroup(Long tableGroupId) {
        if (!isEmpty()) {
            throw new IllegalArgumentException("주문이 가능한 주문 테이블은 테이블 그룹에 포함될 수 없습니다.");
        }

        if (isAlreadyContainsTableGroup()) {
            throw new IllegalArgumentException("이미 테이블 그룹에 포함된 주문 테이블은 테이블 그룹에 포함될 수 없습니다.");
        }

        this.empty = false;
        this.tableGroupId = tableGroupId;
    }

    public void breakupTableGroup() {
        empty = false;
        tableGroupId = null;
    }

    public boolean isAlreadyContainsTableGroup() {
        return Objects.nonNull(tableGroupId);
    }

    public void changeEmpty(boolean empty) {
        if (isAlreadyContainsTableGroup()) {
            throw new IllegalArgumentException("테이블 그룹에 이미 속해있는 경우 주문 가능 상태를 바꿀 수 없습니다.");
        }

        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (isEmpty()) {
            throw new IllegalArgumentException();
        }

        this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
    }

    public boolean isEmpty() {
        return empty;
    }

}
