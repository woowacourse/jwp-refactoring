package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    private TableGroup tableGroup;
    private int numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, numberOfGuests, empty);
    }

    public void changeTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void ungroup() {
        this.tableGroup = null;
        this.empty = false;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (isEmpty() || numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void changeEmpty(boolean status) {
        if (existsTableGroup()) {
            throw new IllegalArgumentException();
        }
        this.empty = status;
    }

    public boolean existsTableGroup() {
        return Objects.nonNull(this.tableGroup);
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
}
