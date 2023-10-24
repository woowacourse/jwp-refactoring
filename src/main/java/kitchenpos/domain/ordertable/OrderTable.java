package kitchenpos.domain.ordertable;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private TableGroup tableGroup;

    @Column(name = "number_of_guests", nullable = false)
    private int numberOfGuests;

    @Column(name = "empty", nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final Long id, final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(final int numberOfGuests) {
        this(null, null, numberOfGuests, false);
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public void validateOrderTableHasTableGroupId() {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException("이미 테이블 그룹에 속한 주문 테이블입니다.");
        }
    }

    public void changeEmptyStatus() {
        this.empty = !empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateIsEmptyForChangeNumberOfGuests();
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validateIsEmptyForChangeNumberOfGuests() {
        if (empty) {
            throw new IllegalArgumentException("주문테이블이 비어있는 상태이면 손님 수를 변경할 수 없습니다.");
        }
    }

    private void validateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("변경할 손님 수는 음수이면 안됩니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setTableGroup(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }
}
