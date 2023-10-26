package kitchenpos.ordertable.domain;

import kitchenpos.ordertable.exception.OrderTableException;
import kitchenpos.ordertable.exception.TableGroupException;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    @Embedded
    private GuestNumber numberOfGuests;

    private Boolean empty;

    protected OrderTable() {
    }

    public OrderTable(Integer numberOfGuests, Boolean empty) {
        this(null, numberOfGuests, empty);
    }

    public OrderTable(Long id, Integer numberOfGuests, Boolean empty) {
        this.id = id;
        this.tableGroupId = null;
        this.numberOfGuests = new GuestNumber(numberOfGuests);
        this.empty = empty;
    }

    public void changeTableGroupId(Long tableGroupId) {
        if (isNotEmpty()) {
            throw new TableGroupException("주문 테이블이 주문이 가능한 상태여서 테이블 그룹을 생성할 수 없습니다.");
        }
        if (existsTableGroup()) {
            throw new TableGroupException("주문 테이블이 이미 테이블 그룹에 속해 있어 테이블 그룹을 생성할 수 없습니다.");
        }
        empty = Boolean.FALSE;
        this.tableGroupId = tableGroupId;
    }

    public void changeEmpty(Boolean empty, OrderValidator orderValidator) {
        if (existsTableGroup()) {
            throw new OrderTableException("주문테이블이 테이블 그룹에 속해 상태를 변경할 수 없습니다.");
        }
        orderValidator.validateOrder(id);
        this.empty = empty;
    }

    private boolean existsTableGroup() {
        return Objects.nonNull(tableGroupId);
    }

    private boolean isNotEmpty() {
        return !this.empty;
    }

    public void changeNumberOfGuests(GuestNumber numberOfGuests) {
        if (empty) {
            throw new OrderTableException("주문테이블이 주문을 할 수 없는 상태라 게스트 수를 변경할 수 없습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void ungroup(OrderValidator orderValidator) {
        orderValidator.validateOrder(id);
        empty = false;
        tableGroupId = null;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
    }

    public Boolean getEmpty() {
        return empty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTable that = (OrderTable) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
