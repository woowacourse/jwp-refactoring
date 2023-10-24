package kitchenpos.table.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import kitchenpos.value.NumberOfGuests;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long tableGroupId;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Column(nullable = false)
    private Boolean empty;

    protected OrderTable() {
    }

    private OrderTable(Builder builder) {
        this.id = builder.id;
        this.tableGroupId = builder.tableGroupId;
        this.numberOfGuests = builder.numberOfGuests;
        this.empty = builder.empty;
    }

    public void group(final Long tableGroupId){
        this.tableGroupId = tableGroupId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public int getNumberOfGuestsValue(){
        return numberOfGuests.getNumberOfGuests();
    }

    public boolean isEmpty() {
        return empty;
    }

    public static class Builder {
        private Long id;
        private Long tableGroupId;
        private NumberOfGuests numberOfGuests;
        private boolean empty;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder tableGroup(Long tableGroupId) {
            this.tableGroupId = tableGroupId;
            return this;
        }

        public Builder numberOfGuests(NumberOfGuests numberOfGuests) {
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
}
