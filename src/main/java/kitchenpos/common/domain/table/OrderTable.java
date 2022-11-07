package kitchenpos.common.domain.table;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long tableGroupId;

    @Column
    private Integer numberOfGuests;

    @Column
    private Boolean empty;

    protected OrderTable() {
    }

    public OrderTable(Long id, Long tableGroupId, Integer numberOfGuests, Boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = Objects.requireNonNullElse(numberOfGuests, 0);
        this.empty = Objects.requireNonNullElse(empty, false);
    }

    public OrderTable(Integer numberOfGuests, Boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isGroupedOrNotEmpty() {
        return !isEmpty() || Objects.nonNull(tableGroupId);
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public void addTableGroupId(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    public void removeTableGroupId() {
        this.tableGroupId = null;
        this.empty = false;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0 || isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void changeEmpty(final boolean empty) {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException();
        }
        this.empty = empty;
    }
}
