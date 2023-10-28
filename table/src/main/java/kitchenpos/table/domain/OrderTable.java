package kitchenpos.table.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.table.domain.dto.vo.NumberOfGuests;

@Entity
@Table(name = "order_table")
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Column(name = "empty")
    private boolean empty;

    protected OrderTable() {
    }

    private OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isGrouped() {
        return tableGroupId != null;
    }

    public void group(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void ungroup() {
        tableGroupId = null;
        empty = false;
    }

    public void changeTableStatus(boolean empty) {
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
    }

    public static OrderTableBuilder builder() {
        return new OrderTableBuilder();
    }

    public static class OrderTableBuilder {

        private Long tableGroupId;
        private int numberOfGuests;
        private boolean empty;

        public OrderTableBuilder tableGroupId(Long tableGroupId) {
            this.tableGroupId = tableGroupId;
            return this;
        }

        public OrderTableBuilder numberOfGuests(int numberOfGuests) {
            this.numberOfGuests = numberOfGuests;
            return this;
        }

        public OrderTableBuilder empty(boolean empty) {
            this.empty = empty;
            return this;
        }

        public OrderTable build() {
            return new OrderTable(tableGroupId, numberOfGuests, empty);
        }
    }
}
