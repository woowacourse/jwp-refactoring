package kitchenpos.domain.ordertable;

import kitchenpos.domain.tablegroup.TableGroup;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;
    @NotNull
    @Embedded
    private OrderTableNumberOfGuests numberOfGuests;
    @NotNull
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(final TableGroup tableGroup, final OrderTableNumberOfGuests numberOfGuests, final boolean empty) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void updateEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void updateTableGroup(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void updateNumberOfGuests(final OrderTableNumberOfGuests orderTableNumberOfGuests) {
        this.numberOfGuests = orderTableNumberOfGuests;
    }

    public void validateCreateTableGroup() {
        if (!this.empty || Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException();
        }
    }

    public void validateChangeEmpty() {
        if (Objects.nonNull(tableGroup)) {
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

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public Long getTableGroupId() {
        if (Objects.isNull(tableGroup)) {
            return null;
        }
        return tableGroup.getId();
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
    }

    public boolean isEmpty() {
        return empty;
    }
}
