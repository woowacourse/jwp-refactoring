package kitchenpos.domain;

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

    protected OrderTable() {
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님 수는 음수일 수 없습니다.");
        }
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable changeEmpty(boolean empty) {
        return new OrderTable(this.id, this.tableGroup, this.numberOfGuests, empty);
    }

    public OrderTable changeTableGroup(TableGroup tableGroup) {
        return new OrderTable(this.id, tableGroup, this.numberOfGuests, this.empty);
    }

    public OrderTable changeNumberOfGuests(int numberOfGuests) {
        if (this.empty) {
            throw new IllegalArgumentException("비어있는 테이블은 손님 수를 변경할 수 없습니다.");
        }
        return new OrderTable(this.id, this.tableGroup, numberOfGuests, this.empty);
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
