package kitchenpos.table.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private TableGroup tableGroup;

    private int numberOfGuests;

    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        this(null, tableGroup, numberOfGuests, empty);
    }

    public OrderTable(final Long id, final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void updateNumberOfGuests(final int numberOfGuests) {
        if (empty) {
            throw new IllegalArgumentException("빈 테이블에는 손님이 있을 수 없습니다.");
        }
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님 수는 0보다 작을 수 없습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void updateStatus(final boolean empty) {
        this.empty = empty;
    }

    public void update(final TableGroup tableGroup, final boolean empty) {
        this.tableGroup = tableGroup;
        this.empty = empty;
    }

    public void unGroup() {
        this.tableGroup = null;
        this.empty = true;
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
