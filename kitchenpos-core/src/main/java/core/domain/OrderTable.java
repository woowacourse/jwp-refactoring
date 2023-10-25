package core.domain;

import core.vo.NumberOfGuest;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.util.Objects;

public class OrderTable extends AbstractAggregateRoot<OrderTable> {
    @Id
    private Long id;
    private Long tableGroupId;
    private NumberOfGuest numberOfGuests;
    private boolean empty;

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        this(null, tableGroupId, numberOfGuests, empty);
    }

    @PersistenceCreator
    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = new NumberOfGuest(numberOfGuests);
        this.empty = empty;
    }

    public void changeEmpty(final boolean empty, TableValidator tableValidator) {
        tableValidator.validate(this);
        validateGroup();
        this.empty = empty;
    }

    private void validateGroup() {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException("단체 지정된 테이블은 변경할 수 없습니다");
        }
    }

    public void changeTableGroup(Long tableGroupId) {
        if (!empty || Objects.nonNull(this.tableGroupId)) {
            throw new IllegalArgumentException("단체 지정은 빈 테이블만 가능합니다");
        }
        this.tableGroupId = tableGroupId;
    }

    public void ungroup(TableValidator tableValidator) {
        tableValidator.validate(this);
        this.tableGroupId = null;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validateEmpty();
        this.numberOfGuests = new NumberOfGuest(numberOfGuests);
    }

    private void validateEmpty() {
        if (this.empty) {
            throw new IllegalArgumentException("인원을 변경할 테이블은 빈 테이블일 수 없습니다");
        }
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getValue();
    }

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        OrderTable that = (OrderTable) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
