package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Column
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(Long id, int numberOfGuests, boolean empty) {
        this(id, null, numberOfGuests, empty);
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = NumberOfGuests.create(numberOfGuests);
        this.empty = empty;
    }

    public void group(TableGroup tableGroup) {
        if (this.isFull() || Objects.nonNull(this.tableGroup)) {
            throw new IllegalArgumentException("테이블에 손님이 있거나 다른 그룹에 속한 테이블은 그룹으로 지정할 수 없습니다.");
        }
        empty = false;
        this.tableGroup = tableGroup;
    }

    public void ungroup() {
        tableGroup = null;
        empty = false;
    }

    public void changeEmpty(boolean empty) {
        this.empty = empty;
    }

    public void changeNumberOfGuests(NumberOfGuests numberOfGuests) {
        if (isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isFull() {
        return !empty;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getValue();
    }
}
