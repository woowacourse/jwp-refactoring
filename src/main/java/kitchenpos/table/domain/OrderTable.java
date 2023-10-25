package kitchenpos.table.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import static java.util.Objects.nonNull;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public void changeEmpty(final boolean empty) {
        if (nonNull(tableGroupId)) {
            throw new IllegalStateException("그룹 지정된 테이블은 빈 테이블 여부를 바꿀 수 없습니다.");
        }

        this.empty = empty;
    }

    public void groupBy(final Long tableGroupId) {
        if (!empty || nonNull(this.tableGroupId)) {
            throw new IllegalArgumentException("주문 테이블이 빈 테이블이 아니거나 이미 그룹 지정된 테이블입니다.");
        }

        empty = false;
        this.tableGroupId = tableGroupId;
    }

    public void ungroup() {
        tableGroupId = null;
        empty = false;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("방문자 수는 음수일 수 없습니다.");
        }
        if (empty) {
            throw new IllegalStateException("빈 테이블의 방문자 수를 바꿀 수 없습니다.");
        }

        this.numberOfGuests = numberOfGuests;
    }

    public void validateNotEmpty() {
        if (empty) {
            throw new IllegalStateException("빈 주문 테이블입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
