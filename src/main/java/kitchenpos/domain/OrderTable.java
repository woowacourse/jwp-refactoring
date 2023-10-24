package kitchenpos.domain;

import java.util.Objects;
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
    @JoinColumn
    private TableGroup tableGroup;

    private Integer numberOfGuests;

    private Boolean empty;

    public OrderTable() {
    }

    private OrderTable(final Long id, final TableGroup tableGroup, final Integer numberOfGuests, final Boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public void joinTableGroup(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void unjoinTableGroup() {
        this.tableGroup = null;
        this.empty = false;
    }

    public void changeNumberOfGuests(final Integer numberOfGuests) {
        if (empty || numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        this.numberOfGuests = numberOfGuests;
    }

    public void changeEmpty(final Boolean empty) {
        if (Objects.nonNull(this.tableGroup)) {
            throw new IllegalArgumentException();
        }
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
}
