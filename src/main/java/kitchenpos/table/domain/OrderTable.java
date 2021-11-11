package kitchenpos.table.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    private int numberOfGuests;

    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(Long id) {
        this.id = id;
    }

    public static OrderTable create(int numberOfGuests, boolean empty) {
        final OrderTable orderTable = new OrderTable();
        orderTable.numberOfGuests = numberOfGuests;
        orderTable.empty = empty;
        return orderTable;
    }

    public static OrderTable createBySingleId(Long id) {
        final OrderTable orderTable = new OrderTable();
        orderTable.id = id;
        return orderTable;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public Long getTableGroupId() {
        if(tableGroup == null) {
            return null;
        }
        return tableGroup.getId();
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0 || empty) {
            throw new IllegalArgumentException();
        }

        this.numberOfGuests = numberOfGuests;
    }

    public void changeEmpty(boolean empty) {
        this.empty = empty;
    }

//    public boolean isChangeable() {
//        return orders
//            .stream()
//            .filter(Order::unableUngroup)
//            .findAny()
//            .isPresent();
//    }
}
