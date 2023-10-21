package kitchenpos.domain;

import java.util.Objects;
import kitchenpos.domain.ordertable.NumberOfGuests;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;

public class OrderTable {
    @Id
    private Long id;
    private Long tableGroupId;
    private NumberOfGuests numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final Long id, final Long tableGroupId, final NumberOfGuests numberOfGuests,
                      final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(final NumberOfGuests numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    @Embedded.Nullable
    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public void changeNumberOfGuests(final NumberOfGuests numberOfGuests) {
        if (isEmpty()) {
            throw new IllegalArgumentException();
        }

        this.numberOfGuests = numberOfGuests;
    }

    public void changeEmptyStatus(final boolean empty) {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException();
        }

        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
