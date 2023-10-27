package kitchenpos.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id", nullable = true)
    private TableGroup tableGroup;

    private int numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void setEmpty(boolean empty, OrderTableValidator orderTableValidator) {
        orderTableValidator.validateChangeEmpty(this);
        this.empty = empty;
    }

    public void grouping(TableGroup tableGroup) {
        if (!isEmpty() || Objects.nonNull(this.tableGroup)) {
            throw new IllegalArgumentException();
        }
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void ungroup(OrderTableValidator orderTableValidator) {
        orderTableValidator.validateUngroup(this, "조리 혹은 식사중 상태의 테이블이 포함되어 있어 그룹을 해제할 수 없습니다.");
        this.tableGroup = null;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new OrderTableException("손님 수는 0명 이상이어야 합니다.");
        }
        if (isEmpty()) {
            throw new OrderTableException("비어있지 않은 테이블의 경우 손님 수를 변경할 수 없습니다.");
        }
        this.numberOfGuests = numberOfGuests;
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
