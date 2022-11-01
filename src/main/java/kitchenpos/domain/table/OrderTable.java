package kitchenpos.domain.table;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "order_table")
@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

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
        if (Objects.nonNull(tableGroup)) {
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
        return Objects.nonNull(tableGroup);
    }

    public void joinTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void leaveTableGroup() {
        this.tableGroup = null;
        this.empty = true;
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

    public boolean isNotEmpty() {
        return !empty;
    }
}
