package kitchenpos.table.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@javax.persistence.Table(name = "order_table")
public class Table {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private TableGroup tableGroup;

    @Column(nullable = false)
    private Integer numberOfGuests;

    @Column(nullable = false)
    private Boolean empty;

    public Table() {
    }

    public Table(TableGroup tableGroup, int numberOfGuests, Boolean empty) {
        this(null, tableGroup, numberOfGuests, empty);
    }

    public Table(Long id, TableGroup tableGroup, int numberOfGuests, Boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
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

    public boolean getEmpty() {
        return empty;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(boolean empty) {
        if (hasGroup()) {
            throw new IllegalArgumentException();
        }
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        if (isEmpty()) {
            throw new IllegalArgumentException();
        }

        this.numberOfGuests = numberOfGuests;
    }

    public void changeTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public boolean hasGroup() {
        return Objects.nonNull(this.tableGroup);
    }

    public void fill() {
        this.empty = false;
    }
}
