package kitchenpos.ordertable.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Optional;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import kitchenpos.vo.NumberOfGuests;
import org.springframework.data.domain.AbstractAggregateRoot;

@Entity
public class OrderTable extends AbstractAggregateRoot<OrderTable> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long tableGroupId;

    @Embedded
    private NumberOfGuests numberOfGuests;

    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(Long tableGroupId, NumberOfGuests numberOfGuests, boolean empty) {
        this(null, tableGroupId, numberOfGuests, empty);
    }

    public OrderTable(Long id, Long tableGroupId, NumberOfGuests numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeEmpty(boolean empty) {
        if (tableGroupId != null) {
            throw new IllegalStateException("이미 속한 테이블 그룹이 있으면 주문 가능 상태를 변경할 수 없습니다.");
        }
        registerEvent(new TableEmptyChangedEvent(this, empty));
        this.empty = empty;
    }

    public void changeNumberOfGuests(NumberOfGuests numberOfGuests) {
        if (empty) {
            throw new IllegalStateException("주문 할 수 없는 상태일 땐 손님의 수를 변경할 수 없습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void group(Long tableGroupId) {
        if (this.tableGroupId != null) {
            throw new IllegalStateException("이미 속한 테이블 그룹이 존재합니다.");
        }
        this.tableGroupId = tableGroupId;
    }

    public void ungroup() {
        this.tableGroupId = null;
    }

    public Long id() {
        return id;
    }

    public Optional<Long> tableGroupId() {
        if (tableGroupId == null) {
            return Optional.empty();
        }
        return Optional.of(tableGroupId);
    }

    public NumberOfGuests numberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
