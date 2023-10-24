package kitchenpos.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;
    private int numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        if (tableGroup != null) {
            tableGroup.addOrderTable(this);
        }
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

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
}
