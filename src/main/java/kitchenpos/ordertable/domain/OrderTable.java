package kitchenpos.ordertable.domain;

import kitchenpos.ordertable.exception.InvalidOrderTableChangeEmptyException;
import kitchenpos.ordertable.exception.InvalidOrderTableChangeNumberOfGuestsException;
import kitchenpos.ordertable.exception.InvalidOrderTableException;
import kitchenpos.tablegroup.domain.TableGroup;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {
    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;
    
    private int numberOfGuests;
    
    private boolean empty;
    
    public OrderTable() {
    }
    
    public OrderTable(final int numberOfGuests,
                      final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }
    
    public OrderTable(final TableGroup tableGroup,
                      final int numberOfGuests,
                      final boolean empty) {
        this(null, tableGroup, numberOfGuests, empty);
    }
    
    public OrderTable(final Long id,
                      final TableGroup tableGroup,
                      final int numberOfGuests,
                      final boolean empty) {
        validate(numberOfGuests);
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }
    
    private void validate(final int numberOfGuests) {
        if(numberOfGuests<0) {
            throw new InvalidOrderTableException("테이블의 고객 수가 0명 미만일 수 없습니다");
        }
    }
    
    public void changeEmpty(final OrderStatusValidator orderStatusValidator, final boolean empty) {
        orderStatusValidator.validateOrderStatusNotCompleted(this.id);
        if (Objects.nonNull(this.tableGroup)) {
            throw new InvalidOrderTableChangeEmptyException("단체 테이블이면 테이블의 상태를 변경할 수 없습니다");
        }
        
        this.empty = empty;
    }
    
    public void changeNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new InvalidOrderTableChangeNumberOfGuestsException("바꾸려는 손님의 수가 0명 미만이면 테이블 손님 수를 변경할 수 없습니다");
        }
        if(this.isEmpty()) {
            throw new InvalidOrderTableChangeNumberOfGuestsException("빈 테이블의 손님 수를 변경할 수 없습니다");
        }
        this.numberOfGuests = numberOfGuests;
    }
    
    public void detachFromTableGroup() {
        this.tableGroup = null;
        this.empty = false;
    }
    
    public void setTableGroup(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
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
