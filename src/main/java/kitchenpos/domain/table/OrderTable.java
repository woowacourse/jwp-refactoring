package kitchenpos.domain.table;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "order_table_table_group"))
    private TableGroup tableGroup;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "number_of_guests"))
    private NumberOfGuests numberOfGuests;

    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public void groupBy(final TableGroup tableGroup) {
        if (isNotEmpty() || this.tableGroup != null) {
            throw new IllegalArgumentException("테이블을 그룹화하려면 테이블이 비어있고 그룹화되어있지 않아야 합니다.");
        }

        this.tableGroup = tableGroup;
        full();
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (isEmpty()) {
            throw new IllegalArgumentException("테이블이 비어있으면 손님 수를 변경할 수 없습니다.");
        }

        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public void changeEmpty(final boolean empty) {
        if (tableGroup != null) {
            throw new IllegalArgumentException("그룹화된 테이블의 상태를 변경할 수 없습니다.");
        }

        this.empty = empty;
    }

    public void ungroup() {
        this.tableGroup = null;
        full();
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

    public void full() {
        this.empty = false;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public NumberOfGuests getNumberOfGuests() {
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
