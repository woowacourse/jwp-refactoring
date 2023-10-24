package kitchenpos.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

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
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(final Long id, final TableGroup tableGroup, final OrderTableNumberOfGuests numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(final Long id, final Long tableGroup, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroup = new TableGroup();
        this.numberOfGuests = new OrderTableNumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public OrderTable(final TableGroup tableGroup, final OrderTableNumberOfGuests numberOfGuests, final boolean empty) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeNotEmpty() {
        this.empty = false;
    }

    public void updateTableGroup(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void changeEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void changeNumberOfGuests(final OrderTableNumberOfGuests orderTableNumberOfGuests) {
        this.numberOfGuests = orderTableNumberOfGuests;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
    }

    public boolean isEmpty() {
        return empty;
    }
}
