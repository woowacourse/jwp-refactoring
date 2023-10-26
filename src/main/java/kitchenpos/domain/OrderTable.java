package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.springframework.data.domain.AbstractAggregateRoot;

@Entity
public class OrderTable extends AbstractAggregateRoot<OrderTable> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    private NumberOfGuests numberOfGuests;
    private boolean empty;

    public OrderTable() {
        this(null, null, 0, true);
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this(null, tableGroupId, numberOfGuests, empty);
    }

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public void unbindGroup() {
        registerEvent(new OrderTableUpdateEvent(id));
        this.empty = false;
        this.tableGroupId = null;
    }

    public void changeEmpty(final boolean empty) {
        if (tableGroupId != null) {
            throw new IllegalArgumentException("테이블 그룹 아이디가 null이 아닙니다.");
        }
        registerEvent(new OrderTableUpdateEvent(id));
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getValue();
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderTable)) {
            return false;
        }
        final OrderTable that = (OrderTable) o;
        return empty == that.empty && Objects.equals(id, that.id) && Objects.equals(numberOfGuests,
                that.numberOfGuests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numberOfGuests, empty);
    }
}
