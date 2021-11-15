package kitchenpos.order.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class OrderTable {

    @GeneratedValue
    @Id
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeEmptyStatus(OrderTableValidator orderTableValidator, boolean empty) {
        orderTableValidator.validateChangeEmpty(this, empty);
        this.empty = empty;
    }

    public void group(Long tableGroupId) {
        OrderTableValidator.validateGroup(this, tableGroupId);
        this.tableGroupId = tableGroupId;
    }

    public void ungroup() {
        OrderTableValidator.validateUngroup(this);
        this.tableGroupId = null;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        OrderTableValidator.validateChangeNumberOfGuests(this, numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public boolean isEmpty() {
        return empty;
    }
}
