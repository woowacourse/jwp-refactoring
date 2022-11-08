package kitchenpos.table.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "order_table")
@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    @Column(name = "number_of_guests")
    private int numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        validateMinimunGuest(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeEmptyStatus(boolean status) {
        validateNotBelongToTableGroup();
        this.empty = status;
    }

    private void validateNotBelongToTableGroup() {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException("이미 테이블 그룹이 형성된 테이블입니다.");
        }
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validateOrderTableIsNotEmpty();
        validateMinimunGuest(numberOfGuests);

        this.numberOfGuests = numberOfGuests;
    }

    private void validateOrderTableIsNotEmpty() {
        if (empty) {
            throw new IllegalArgumentException("주문 테이블이 비어있습니다.");
        }
    }

    private void validateMinimunGuest(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("테이블에는 0명 이상의 손님이 앉을 수 있습니다.");
        }
    }

    public boolean isAlreadyGrouped() {
        return Objects.nonNull(tableGroupId);
    }

    public void joinTableGroup(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    public void leaveTableGroup() {
        this.tableGroupId = null;
        this.empty = false;
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

    public boolean isNotEmpty() {
        return !empty;
    }
}
