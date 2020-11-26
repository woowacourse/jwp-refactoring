package kitchenpos.domain.table;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private TableGroup tableGroup;

    private int numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님 수는 음수일 수 없습니다.");
        }
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable changeEmpty(boolean empty) {
        if (tableGroup != null) {
            throw new IllegalArgumentException("table group에 속해있는 테이블은 empty 수정이 불가합니다.");
        }
        return new OrderTable(this.id, null, this.numberOfGuests, empty);
    }

    public OrderTable group(TableGroup tableGroup) {
        Objects.requireNonNull(tableGroup);
        if (isNotGrouping()) {
            throw new IllegalArgumentException("이미 테이블 그룹에 속한 테이블이거나 비어있지 않은 테이블입니다.");
        }
        return new OrderTable(this.id, tableGroup, this.numberOfGuests, false);
    }

    private boolean isNotGrouping() {
        return !isEmpty() || tableGroup != null;
    }

    public OrderTable unGroup() {
        return new OrderTable(this.id, null, this.numberOfGuests, this.empty);
    }

    public OrderTable changeNumberOfGuests(int numberOfGuests) {
        if (isEmpty()) {
            throw new IllegalArgumentException("비어있는 테이블은 손님 수를 변경할 수 없습니다.");
        }
        return new OrderTable(this.id, this.tableGroup, numberOfGuests, this.empty);
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
