package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        this(null, tableGroupId, numberOfGuests, empty);
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeEmpty(boolean empty) {
        validateTableGroupNull();
        this.empty = empty;
    }

    private void validateTableGroupNull() {
        if (this.tableGroupId != null) {
            throw new IllegalArgumentException("주문 테이블의 테이블 그룹이 없어야 합니다. : " + tableGroupId);
        }
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validatePositiveNumber(numberOfGuests);
        validateNotEmpty();
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNotEmpty() {
        if (empty) {
            throw new IllegalArgumentException("주문 테이블이 비어있기 때문에 손님의 수를 변경할 수 없습니다");
        }
    }

    private void validatePositiveNumber(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님의 수를 음수로 변경할 수 없습니다 : " + numberOfGuests);
        }
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setTableGroupId(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }
}
