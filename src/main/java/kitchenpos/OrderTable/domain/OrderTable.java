package kitchenpos.OrderTable.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    private int numberOfGuests;

    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(Long id) {
        this(id, null, 0, false);
    }

    public OrderTable(boolean empty) {
        this(null, null, 0, empty);
    }

    public OrderTable(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public void enrollTableGroup(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void changeEmptyStatus(boolean empty) {
        this.empty = empty;
    }

    public void releaseTableGroup() {
        this.tableGroupId = null;
    }

    public void enrollId(Long id) {
        this.id = id;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isBelongToTableGroup() {
        return Objects.nonNull(tableGroupId);
    }
}
