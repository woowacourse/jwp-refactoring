package kitchenpos.order.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class OrderTable {

    @GeneratedValue
    @Id
    private Long id;

    @ManyToOne
    private TableGroup tableGroup;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmptyStatus(final boolean empty) {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException();
        }
        this.empty = empty;
    }

    public boolean hasGroup() {
        return Objects.nonNull(tableGroup);
    }

    public void group(TableGroup tableGroup) {
        if(!isEmpty() || hasGroup()){
            throw new IllegalArgumentException();
        }
        this.tableGroup = tableGroup;
    }

    public void ungroup() {
        this.tableGroup = null;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (isEmpty() || numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
        this.numberOfGuests = numberOfGuests;
    }
}
