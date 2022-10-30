package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "order_table")
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    @Column(name = "number_of_guests", nullable = false)
    private int numberOfGuests;

    @Column(name = "empty", nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void ungroup() {
        this.tableGroupId = null;
        this.empty = false;
    }

    public void changeEmpty(final boolean empty) {
        if (inTableGroup()) {
            throw new IllegalArgumentException("단체 지정된 주문 테이블은 비활성화할 수 없습니다.");
        }
        this.empty = empty;
    }

    public void changeNumberOfGuest(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님 수는 0명 미만일 수 없습니다.");
        }
        if (empty) {
            throw new IllegalArgumentException("비활성화된 주문 테이블의 손님 수는 변경할 수 없습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    private boolean inTableGroup() {
        return Objects.nonNull(tableGroupId);
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

    public static class Builder {

        private Long id;
        private Long tableGroupId;
        private int numberOfGuests;
        private boolean empty;

        public Builder id(final Long id) {
            this.id = id;
            return this;
        }

        public Builder tableGroupId(final Long tableGroupId) {
            this.tableGroupId = tableGroupId;
            return this;
        }

        public Builder numberOfGuests(final int numberOfGuests) {
            this.numberOfGuests = numberOfGuests;
            return this;
        }

        public Builder empty(final boolean empty) {
            this.empty = empty;
            return this;
        }

        public OrderTable build() {
            return new OrderTable(id, tableGroupId, numberOfGuests, empty);
        }
    }
}
