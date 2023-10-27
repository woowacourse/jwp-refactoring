package kitchenpos.table.domain;

import javax.persistence.*;
import java.util.Objects;
import kitchenpos.table.exception.OrderTableException;
import kitchenpos.table.exception.TableGroupException;
import org.springframework.data.domain.AbstractAggregateRoot;

@Entity
public class OrderTable extends AbstractAggregateRoot<OrderTable> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long tableGroupId;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeEmpty(boolean empty) {
        if (empty) {
            validateTableGroupIsNull();
            this.numberOfGuests = 0;
        }

        this.empty = empty;
    }

    public void validateTableGroupIsNull() {
        if (Objects.nonNull(this.tableGroupId)) {
            throw new OrderTableException("테이블에 테이블 그룹이 존재합니다.");
        }
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new OrderTableException("방문한 손님 수가 유효하지 않습니다.");
        }

        if (this.empty) {
            throw new OrderTableException("테이블이 비어있습니다.");
        }

        this.numberOfGuests = numberOfGuests;
    }

    public void assignTableGroup(Long tableGroupId) {
        if (!empty || Objects.nonNull(this.tableGroupId)) {
                throw new TableGroupException("주문 테이블의 상태가 유효하지 않습니다.");
            }

        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    public void ungroup() {
        this.tableGroupId = null;
    }

    public boolean hasTableGroup() {
        return Objects.nonNull(tableGroupId);
    }

    public OrderTable publish() {
        return this.andEvent(new OrderTableChangeEvent(this));
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
