package kitchenpos.table.domain;

import javax.persistence.*;
import java.util.Objects;
import kitchenpos.table.exception.OrderTableException;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void validateTableGroupIsNull() {
        if (Objects.nonNull(this.tableGroup)) {
            throw new OrderTableException("테이블에 테이블 그룹이 존재합니다.");
        }
    }

    public void changeEmpty(boolean empty) {
        if (empty) {
            validateTableGroupIsNull();
            this.numberOfGuests = 0;
        }

        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new OrderTableException("방문한 손님 수가 유효하지 않습니다.");
        }

        if (this.empty) {
            throw new OrderTableException("테이블이 비어있습니다.");
        }

        this.numberOfGuests = numberOfGuests;
    }

    public void assignTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void ungroup() {
        this.tableGroup = null;
    }

    public boolean hasTableGroup() {
        return Objects.nonNull(tableGroup);
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
