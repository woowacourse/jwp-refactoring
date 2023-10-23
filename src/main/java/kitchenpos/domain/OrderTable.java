package kitchenpos.domain;

import kitchenpos.domain.exception.InvalidOrderTableChangeEmptyException;
import kitchenpos.domain.exception.InvalidOrderTableChangeNumberOfGuestsException;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

import static kitchenpos.domain.OrderStatus.COMPLETION;

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
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }
    
    public void changeEmpty(final List<Order> orders, final boolean empty) {
        if (Objects.nonNull(this.tableGroup)) {
            throw new InvalidOrderTableChangeEmptyException("단체 테이블이면 테이블의 상태를 변경할 수 없습니다");
        }
        boolean isOrderInProgress = orders.stream()
                          .anyMatch(order -> order.getOrderStatus() != COMPLETION);
        if(isOrderInProgress) {
            throw new InvalidOrderTableChangeEmptyException("테이블에 속하는 주문의 상태가 COOKING 또는 MEAL이라면 테이블의 상태를 변경할 수 없습니다");
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
