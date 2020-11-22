package kitchenpos.domain.table;

import kitchenpos.util.ValidateUtil;

import javax.persistence.*;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;
    private NumberOfGuests numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(NumberOfGuests numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(NumberOfGuests numberOfGuests, boolean empty) {
        ValidateUtil.validateNonNull(numberOfGuests);

        return new OrderTable(numberOfGuests, empty);
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void setTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(NumberOfGuests numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(Boolean empty) {
        ValidateUtil.validateNonNull(empty);

        this.empty = empty;
    }
}
