package kitchenpos.ordertable.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.tablegroup.domain.TableGroup;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tableGroupId;

    private int numberOfGuests;
    private boolean empty;

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable() {
    }

    public void belongsTo(Long tableGroupId) {
        if (!isEmpty()) {
            throw new IllegalStateException("빈 테이블이 아닙니다.");
        }
        if (isGrouped()) {
            throw new IllegalStateException("이미 단체 지정된 테이블입니다.");
        }
        this.tableGroupId = tableGroupId;
    }

    public void ungroup() {
        if (this.tableGroupId == null) {
            throw new IllegalStateException("아직 단체 지정되지 않은 테이블입니다.");
        }
        this.tableGroupId = null;
    }

    public boolean isGrouped() {
        return Objects.nonNull(tableGroupId);
    }

    public void changeEmpty(boolean empty) {
        if (isGrouped()) {
            throw new IllegalStateException("이미 단체 지정된 테이블입니다.");
        }
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("입력값이 유효하지 않습니다.");
        }
        validateNotEmpty();
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNotEmpty() {
        if (isEmpty()) {
            throw new IllegalStateException("빈 테이블입니다.");
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
