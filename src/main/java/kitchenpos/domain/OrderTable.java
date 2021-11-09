package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tableGroupId;
    @Column(nullable = false)
    private int numberOfGuests;
    @Column(nullable = false)
    private boolean empty;

    public OrderTable() {
    }

    private OrderTable(Builder builder) {
        this.id = builder.id;
        this.tableGroupId = builder.tableGroupId;
        this.numberOfGuests = builder.numberOfGuests;
        this.empty = builder.empty;
    }

    public void check() {
        if (!empty || Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public void update(Long tableGroupId, boolean empty) {
        this.tableGroupId = tableGroupId;
        this.empty = empty;
    }

    public void checkTableGroupId() {
        if (Objects.nonNull(this.tableGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    public void checkValidity() {
        if (empty) {
            throw new IllegalArgumentException();
        }
    }

    public static class Builder {
        private Long id;
        private Long tableGroupId;
        private int numberOfGuests;
        private boolean empty;

        private Builder() {
        }

        public Builder of(OrderTable orderTable) {
            this.id = orderTable.id;
            this.tableGroupId = orderTable.tableGroupId;
            this.numberOfGuests = orderTable.numberOfGuests;
            this.empty = orderTable.empty;
            return this;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder tableGroupId(Long tableGroupId) {
            this.tableGroupId = tableGroupId;
            return this;
        }

        public Builder numberOfGuests(int numberOfGuests) {
            this.numberOfGuests = numberOfGuests;
            return this;
        }

        public Builder empty(boolean empty) {
            this.empty = empty;
            return this;
        }

        public OrderTable build() {
            return new OrderTable(this);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void updateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void updateEmpty(final boolean empty) {
        this.empty = empty;
    }
}
