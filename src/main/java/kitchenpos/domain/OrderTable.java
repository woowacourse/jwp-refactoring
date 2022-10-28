package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.domain.validator.OrderTableValidator;
import kitchenpos.exception.badrequest.OrderTableNegativeNumberOfGuestsException;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint(20)")
    private Long id;
    @Column(name = "table_group_id", columnDefinition = "bigint(20)")
    private Long tableGroupId;
    @Column(name = "number_of_guests", nullable = false, columnDefinition = "int(11)")
    private int numberOfGuests;
    @Column(name = "empty", nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;

        if (numberOfGuests < 0) {
            throw new OrderTableNegativeNumberOfGuestsException(numberOfGuests);
        }
    }

    public OrderTable(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable changeEmpty(final OrderTableValidator validator, final boolean empty) {
        validator.validateChangeEmpty(this, this.id);
        this.empty = empty;
        return this;
    }

    public OrderTable changeNumberOfGuests(final OrderTableValidator validator, final int numberOfGuests) {
        validator.validateChangeNumberOfGuests(this, numberOfGuests);
        this.numberOfGuests = numberOfGuests;
        return this;
    }

    public OrderTable group(final OrderTableValidator validator, final Long tableGroupId) {
        validator.validateGroup(this);
        this.empty = false;
        this.tableGroupId = tableGroupId;
        return this;
    }

    public void unGroup(final OrderTableValidator validator) {
        validator.validateUnGroup(this);
        this.tableGroupId = null;
    }

    public boolean alreadyInGroup() {
        return Objects.nonNull(this.tableGroupId);
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
