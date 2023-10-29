package kitchenpos.table.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tableGroupId;
    @NotNull
    @Embedded
    private OrderTableNumberOfGuests numberOfGuests;
    @NotNull
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final Long tableGroupId, final OrderTableNumberOfGuests numberOfGuests, final boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void updateEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void updateTableGroup(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void updateNumberOfGuests(final OrderTableNumberOfGuests orderTableNumberOfGuests) {
        this.numberOfGuests = orderTableNumberOfGuests;
    }

    public void validateCreateTableGroup() {
        if (!this.empty || Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    public void validateChangeEmpty() {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    public void validateIsEmpty() {
        if (empty) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        if (Objects.isNull(tableGroupId)) {
            return null;
        }
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
    }

    public boolean isEmpty() {
        return empty;
    }
}
