package kitchenpos.table.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.order.domain.OrderAble;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number_of_guests", nullable = false)
    private int numberOfGuests;

    @Column(name = "empty", columnDefinition = "BIT(1)")
    private boolean empty;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    protected OrderTable() {
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public static OrderTable empty(int numberOfGuests) {
        return new OrderTable(numberOfGuests, true);
    }

    public void changeEmpty(OrderAble orderAble) {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException("이미 단체지정이 되어있습니다.");
        }
        this.empty = orderAble.isEmptyTable();
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validate(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validate(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("방문한 손님 수는 0 이상이어야 합니다.");
        }
        if (empty) {
            throw new IllegalArgumentException("빈 테이블입니다.");
        }
    }

    public void unGroup() {
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
}
