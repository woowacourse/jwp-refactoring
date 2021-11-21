package kitchenpos.domain;

import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.exception.NotChangeOrderTableException;
import kitchenpos.exception.OrderStatusException;
import kitchenpos.exception.UnGroupNotCompletionException;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @OneToMany(mappedBy = "orderTable", fetch = FetchType.LAZY)
    private List<Order> orders;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Column
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(OrderTableBuilder orderTableBuilder) {
        this.id = orderTableBuilder.id;
        this.tableGroup = orderTableBuilder.tableGroup;
        this.numberOfGuests = NumberOfGuests.create(orderTableBuilder.numberOfGuests);
        this.orders = orderTableBuilder.orders;
        this.empty = orderTableBuilder.empty;
    }

    public void changeTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void changeEmpty(boolean empty) {

        checkOrderStatusByChangeEmpty();

        if (Objects.nonNull(tableGroup)) {
            throw new NotChangeOrderTableException("단체 지정에 속해있는 테이블은 변경할 수 없습니다.");
        }

        this.empty = empty;
    }

    private void checkOrderStatusByChangeEmpty() {
        this.orders.stream()
                .filter(Order::isNotCompletion)
                .findAny()
                .ifPresent(order -> {
                    throw new OrderStatusException("주문 상태가 요리 또는 식사 상태입니다.");
                });
    }

    public void changeGuests(int numberOfGuests) {

        if (empty) {
            throw new NotChangeOrderTableException("빈 테이블은 변경할 수 없습니다.");
        }

        this.numberOfGuests = NumberOfGuests.create(numberOfGuests);
    }

    public void unGroup() {
        checkOrderStatusByUnGroup();
        this.tableGroup = null;
        this.empty = false;
    }

    private void checkOrderStatusByUnGroup() {
        this.orders.stream()
                .filter(Order::isNotCompletion)
                .findAny()
                .ifPresent(order -> {
                    throw new UnGroupNotCompletionException();
                });
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
    }

    public boolean isEmpty() {
        return empty;
    }

    public static class OrderTableBuilder {

        private Long id;
        private TableGroup tableGroup;
        private int numberOfGuests;
        private List<Order> orders;
        private boolean empty;

        public OrderTableBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public OrderTableBuilder setTableGroup(TableGroup tableGroup) {
            this.tableGroup = tableGroup;
            return this;
        }

        public OrderTableBuilder setNumberOfGuests(int numberOfGuests) {
            this.numberOfGuests = numberOfGuests;
            return this;
        }

        public OrderTableBuilder setOrders(List<Order> orders) {
            this.orders = orders;
            return this;
        }

        public OrderTableBuilder setEmpty(boolean empty) {
            this.empty = empty;
            return this;
        }

        public OrderTable build() {
            return new OrderTable(this);
        }
    }
}
