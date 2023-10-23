package kitchenpos.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @JoinColumn(name = "table_group_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private TableGroup tableGroup;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(final TableGroup tableGroup,
                      final int numberOfGuests,
                      final boolean empty) {
        this(null, tableGroup, numberOfGuests, empty);
    }

    public OrderTable(final Long id,
                      final TableGroup tableGroup,
                      final int numberOfGuests,
                      final boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateNumberOfGuestsOverZero();
        validateActive();
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNumberOfGuestsOverZero() {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validateActive() {
        if (empty) {
            throw new IllegalArgumentException();
        }
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderTable that = (OrderTable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OrderTable{" +
                "id=" + id +
                ", tableGroupId=" + tableGroup.getId() +
                ", numberOfGuests=" + numberOfGuests +
                ", empty=" + empty +
                '}';
    }
}
