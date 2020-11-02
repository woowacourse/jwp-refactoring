package kitchenpos.table.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
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
    @Embedded
    private TableNumberOfGuests tableNumberOfGuests;

    @Column(nullable = false)
    @Embedded
    private TableEmpty tableEmpty;

    public Table() {
    }

    public Table(TableGroup tableGroup, int numberOfGuests, Boolean empty) {
        this(null, tableGroup, numberOfGuests, empty);
    }

    public Table(Long id, TableGroup tableGroup, int numberOfGuests, Boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.tableNumberOfGuests = new TableNumberOfGuests(numberOfGuests);
        this.tableEmpty = new TableEmpty(empty);
    }

    public TableNumberOfGuests getTableNumberOfGuests() {
        return tableNumberOfGuests;
    }

    public TableEmpty getTableEmpty() {
        return tableEmpty;
    }

    public void changeEmpty(boolean empty) {
        this.tableEmpty.changeEmpty(empty, hasGroup());
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        this.tableNumberOfGuests.changeGuest(numberOfGuests, tableEmpty.isEmpty());
    }

    public void changeTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void fill() {
        tableEmpty.fill();
    }

    public boolean existCustomer() {
        return tableEmpty.existCustomer();
    }

    public boolean isEmpty() {
        return tableEmpty.isEmpty();
    }

    public boolean hasGroup() {
        return Objects.nonNull(this.tableGroup);
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }
}
