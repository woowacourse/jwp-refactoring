package kitchenpos.domain.table;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static java.util.Objects.nonNull;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
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
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("테이블 인원수는 0명 이상이어야 합니다.");
        }
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void setTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (empty) {
            throw new IllegalArgumentException("빈 테이블의 인원수는 변경할 수 없습니다.");
        }
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("테이블 인원수는 0명 이상이어야 합니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(final boolean empty) {
        if (empty && nonNull(tableGroupId)) {
            throw new IllegalArgumentException("그룹이 지정된 테이블은 empty로 설정할 수 없습니다.");
        }
        this.empty = empty;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderTable that = (OrderTable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void ungroup() {
        this.tableGroupId = null;
        this.empty = false;
    }

}
