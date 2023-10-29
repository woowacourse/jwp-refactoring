package kitchenpos.ordertable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tableGroupId;
    @Column
    private int numberOfGuests;
    @Column
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(final Long tableGroupId,
                      final int numberOfGuests,
                      final boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        validateNumberOfGuest(numberOfGuests);
    }

    private void validateNumberOfGuest(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("방문한 손님 수는 0명 이상이어야 합니다.");
        }
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, numberOfGuests, empty);
    }

    public void changeNumberOfGuest(final int numberOfGuests) {
        validateNumberOfGuest(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty() {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException("테이블이 이미 단체 지정되었습니다. 빈 테이블로 변경할 수 없습니다.");
        }
        this.empty = true;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void setTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
