package kitchenpos.ordertable.domain;

import kitchenpos.ordertable.application.OrderStatusValidator;
import kitchenpos.ordertable.exception.InvalidOrderTableChangeEmptyException;
import kitchenpos.ordertable.exception.InvalidOrderTableChangeNumberOfGuestsException;
import kitchenpos.ordertable.exception.InvalidOrderTableException;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {
    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    
    @Column(name = "table_group_id")
    private Long tableGroupId;
    
    private int numberOfGuests;
    
    private boolean empty;
    
    protected OrderTable() {
    }
    
    public OrderTable(final int numberOfGuests,
                      final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }
    
    public OrderTable(final Long tableGroupId,
                      final int numberOfGuests,
                      final boolean empty) {
        this(null, tableGroupId, numberOfGuests, empty);
    }
    
    public OrderTable(final Long id,
                      final Long tableGroupId,
                      final int numberOfGuests,
                      final boolean empty) {
        validate(numberOfGuests);
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }
    
    private void validate(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new InvalidOrderTableException("테이블의 고객 수가 0명 미만일 수 없습니다");
        }
    }
    
    public void changeEmpty(final OrderStatusValidator orderStatusValidator, final boolean empty) {
        orderStatusValidator.validateOrderStatusNotCompleted(this.id);
        if (Objects.nonNull(this.tableGroupId)) {
            throw new InvalidOrderTableChangeEmptyException("단체 테이블이면 테이블의 상태를 변경할 수 없습니다");
        }
        
        this.empty = empty;
    }
    
    public void changeNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new InvalidOrderTableChangeNumberOfGuestsException("바꾸려는 손님의 수가 0명 미만이면 테이블 손님 수를 변경할 수 없습니다");
        }
        if (this.isEmpty()) {
            throw new InvalidOrderTableChangeNumberOfGuestsException("빈 테이블의 손님 수를 변경할 수 없습니다");
        }
        this.numberOfGuests = numberOfGuests;
    }
    
    public void detachFromTableGroup() {
        this.tableGroupId = null;
        this.empty = false;
    }
    
    public void setTableGroup(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
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
}
