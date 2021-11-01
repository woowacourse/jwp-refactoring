package kitchenpos.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    private TableGroup tableGroup;

    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {

    }

    public OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this(null, tableGroup, numberOfGuests, empty);
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void addTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void fillTable() {
        this.empty = false;
    }

    public void changeEmpty(boolean empty) {
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

    public void ungroup() {
        this.tableGroup = null;
        this.empty = true;
    }
}
