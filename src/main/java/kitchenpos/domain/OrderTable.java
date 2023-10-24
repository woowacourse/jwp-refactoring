package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "order_table_table_group"))
    private TableGroup tableGroup;

    private int numberOfGuests;

    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(final Long id) {
        this.id = id;
    }

    public OrderTable(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public OrderTable(final boolean empty) {
        this.empty = empty;
    }

    public void groupBy(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public void changeEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void ungroup() {
        this.tableGroup = null;
        full();
    }

    public void full() {
        this.empty = false;
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
}
