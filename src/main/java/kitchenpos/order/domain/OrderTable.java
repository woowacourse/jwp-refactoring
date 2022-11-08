package kitchenpos.order.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.Hibernate;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void group(Long tableGroupId, OrderTableValidator orderTableValidator) {
        orderTableValidator.validateGroup(this);
        this.tableGroupId = tableGroupId;
        empty = false;
    }

    public void ungroup() {
        tableGroupId = null;
        empty = false;
    }

    public void changeEmpty(boolean empty, OrderTableValidator orderTableValidator) {
        orderTableValidator.validateChangeEmpty(this);
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests, OrderTableValidator orderTableValidator) {
        orderTableValidator.validateChangeNumberOfGuests(numberOfGuests,this);
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        OrderTable orderTable = (OrderTable)o;
        return id != null && Objects.equals(id, orderTable.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
