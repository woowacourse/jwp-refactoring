package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    public static OrderTableBuilder builder() {
        return new OrderTableBuilder();
    }

    public OrderTableBuilder toBuilder() {
        return new OrderTableBuilder(id, tableGroupId, numberOfGuests, empty);
    }

    public void changeStatus(boolean empty) {
        this.empty = empty;
    }

    public void ungroup() {
        this.tableGroupId = null;
    }

    public static class OrderTableBuilder {
        private Long id;
        private Long tableGroupId;
        private int numberOfGuests;
        private boolean empty;

        public OrderTableBuilder() {
        }

        public OrderTableBuilder(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
            this.id = id;
            this.tableGroupId = tableGroupId;
            this.numberOfGuests = numberOfGuests;
            this.empty = empty;
        }

        public OrderTableBuilder id(Long id) {
            this.id = id;
            return this;
        }

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
            return new OrderTable(id, tableGroupId, numberOfGuests, empty);
        }
    }
}
