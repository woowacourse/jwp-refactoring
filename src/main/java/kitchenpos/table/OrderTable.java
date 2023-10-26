package kitchenpos.table;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Table(name = "order_table")
@Entity
public class OrderTable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private Long tableGroupId;

    private int numberOfGuests;

    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.id = null;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeEmpty(boolean empty) {
        if (hasTableGroup()) {
            throw new IllegalArgumentException("그룹된 테이블을 비울 수 없습니다.");
        }
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests <= 0) {
            throw new IllegalArgumentException();
        }
        if (this.empty) {
            throw new IllegalArgumentException();
        }

        this.numberOfGuests = numberOfGuests;
    }

    public boolean hasTableGroup() {
        return this.tableGroupId != null;
    }

    public boolean hasNoTableGroup() {
        return this.tableGroupId == null;
    }

    public void group(Long tableGroupId) {
        this.empty = false;
        this.tableGroupId = tableGroupId;
    }

    public void unGroup() {
        this.tableGroupId = null;
        this.empty = true;
    }

    public boolean isNotEmpty() {
        return !this.empty;
    }

    public void makeFull() {
        this.empty = false;
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderTable)) {
            return false;
        }
        OrderTable orderTable = (OrderTable) o;
        return id.equals(orderTable.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
