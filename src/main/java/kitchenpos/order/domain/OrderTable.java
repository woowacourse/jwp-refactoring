package kitchenpos.order.domain;

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
        orderTableValidator.validateChangeEmpty(this);
        this.empty = empty;
    }

    public void changeNumberOfGuests(OrderTableValidator orderTableValidator, int numberOfGuests) {
        orderTableValidator.validateChangeNumberOfGuests(this, numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public void group(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void ungroup() {
        this.tableGroupId = null;
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
