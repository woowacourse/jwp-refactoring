package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.exception.KitchenException;

@Entity
public class OrderTable {

    public static OrderTable EMPTY_TABLE = new OrderTable(0, true);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "order_table")
    private final List<Order> orders = new ArrayList<>();

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        validate();
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    private void validate() {
        if(numberOfGuests > 0 && this.empty) {
            throw new KitchenException("1명 이상의 손님과 함께 빈 테이블로 등록할 수 없습니다.");
        }
    }

    public void changeEmpty(boolean empty) {
        if (Objects.nonNull(tableGroup)) {
            throw new KitchenException("테이블이 속한 단체가 존재합니다.");
        }
        if(!hasCookingOrMeal()) {
            throw new KitchenException("식사가 완료되지 않은 테이블입니다.");
        }
        this.empty = empty;
    }

    private boolean hasCookingOrMeal() {
        return orders.stream().allMatch(Order::hasCookingOrMeal);
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new KitchenException("손님 수는 0명 이상이어야 합니다.");
        }

        if (isEmpty()) {
            throw new KitchenException("손님이 있는 테이블은 비어있을 수 없습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void setTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }
}
