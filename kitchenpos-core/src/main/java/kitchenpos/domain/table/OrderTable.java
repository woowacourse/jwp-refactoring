package kitchenpos.domain.table;

import static java.util.Objects.nonNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "table_group_id")
    private Long tableGroupId;

    private int numberOfGuests;

    private boolean empty;

    public OrderTable() {
    }


    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this(null, tableGroupId, numberOfGuests, empty);
    }

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void updateEmptyStatus(final boolean empty, final TableValidator tableValidator) {
        if (nonNull(tableGroupId)) {
            throw new IllegalArgumentException("그룹화 된 테이블은 못비워용");
        }
        tableValidator.validateOrderStatus(this.id);
        this.empty = empty;
    }

    public void updateNumberOfGuest(final int numberOfGuests) {
        if(numberOfGuests < 0) {
            throw new IllegalArgumentException("손님이 0명 이하에요");
        }

        if (empty) {
            throw new IllegalArgumentException("테이블이 비어있어요");
        }

        this.numberOfGuests = numberOfGuests;
    }


    public void group(final Long tableGroup) {
        if (!empty || nonNull(tableGroupId)) {
            throw new IllegalArgumentException("비어있지 않거나, 이미 그룹화된 테이블을 포함하고 있습니다.");
        }
        this.empty = false;
        this.tableGroupId = tableGroup;
    }

    public void ungroup(final TableValidator tableValidator) {
        tableValidator.validateOrderStatus(this.id);
        this.tableGroupId = null;
        this.empty = false;
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
}
