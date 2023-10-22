package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.exception.CannotChangeEmptyTableNumberOfGuestsException;
import kitchenpos.exception.OrderTableCannotBeGroupedException;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @Column(name = "number_of_guests")
    private int numberOfGuests;
    private boolean empty;

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    protected OrderTable() {
    }

    public boolean isEmpty() {
        return empty;
    }

    public Long getId() {
        return id;
    }

    public void addToTableGroup(TableGroup tableGroup) {
        validateCanBeAddedToNewTableGroup();
        this.tableGroup = tableGroup;
    }

    private void validateCanBeAddedToNewTableGroup() {
        if (isEmpty() || Objects.nonNull(this.tableGroup)) {
            throw new OrderTableCannotBeGroupedException();
        }
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (isEmpty()) {
            throw new CannotChangeEmptyTableNumberOfGuestsException();
        }
        this.numberOfGuests = numberOfGuests;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }
}
