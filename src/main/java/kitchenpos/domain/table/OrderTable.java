package kitchenpos.domain.table;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;

@Entity
public class OrderTable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = LAZY)
    private TableGroup tableGroup;

    private int numberOfGuests;

    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        this(null, tableGroup, numberOfGuests, empty);
    }

    public OrderTable(final Long id, final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        validateNotNull(tableGroup, empty);
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    private void validateNotNull(final TableGroup tableGroup, final boolean empty) {
        if (tableGroup != null) {
            throw new IllegalArgumentException(String.format("OrderTable을 생성할 때 tableGroup 이 있는 상태 일 수 없습니다. TableGroupId = %s", tableGroup.getId()));
        }

        if (!empty) {
            throw new IllegalArgumentException("OrderTable을 생성할 때 이미 테이블이 차 있는 상태일 수 없습니다.");
        }
    }

    public void ungroup() {
        if (tableGroup != null || !empty) {
            throw new IllegalStateException(String.format("OrderTable이 그룹을 해제할 수 없는 상태입니다. tableGroup = %s, empty = %s", tableGroup, empty));
        }

        changeGroup(null);
        changeEmpty(false);
    }

    public void changeGroup(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void changeEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (empty) {
            throw new IllegalStateException(String.format("비어 있는 상태에서 게스트 숫자를 변경할 수 없습니다. 주문 테이블 상태(empty) = %s, 변경하려는 게스트의 수 = %s", empty, numberOfGuests));
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderTable that = (OrderTable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
