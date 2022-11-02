package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import kitchenpos.exception.GuestSizeException;
import kitchenpos.exception.TableGroupNotNullException;
import kitchenpos.exception.UnableToGroupingException;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private TableGroup tableGroup;

    private Integer numberOfGuests;

    private Boolean empty;

    protected OrderTable() {
    }

    public OrderTable(TableGroup tableGroup, Integer numberOfGuests, Boolean empty) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(Integer numberOfGuests, Boolean empty) {
        this.tableGroup = null;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void grouping(TableGroup tableGroup) {
        if (!empty || this.tableGroup != null) {
            throw new UnableToGroupingException();
        }
        this.tableGroup = tableGroup;
    }

    public void changeEmpty(boolean empty) {
        if (Objects.nonNull(tableGroup)) {
            throw new TableGroupNotNullException();
        }
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0 || empty.equals(true)) {
            throw new GuestSizeException();
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void ungrouping() {
        this.tableGroup = null;
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
