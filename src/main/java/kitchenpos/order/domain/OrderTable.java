package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
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

    public OrderTable(Long id) {
        this(id, null, 0, true);
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        setTableGroup(tableGroup);
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    private void setTableGroup(TableGroup tableGroup) {
        if (Objects.nonNull(this.tableGroup)) {
            this.tableGroup.getOrderTables().remove(this);
        }
        this.tableGroup = tableGroup;
        if (Objects.nonNull(tableGroup)) {
            tableGroup.getOrderTables().add(this);
        }
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(boolean empty) {
        if (Objects.equals(this.empty, empty)) {
            throw new IllegalArgumentException("변경하려는 테이블 비어있음의 유무가 이미 일치합니다.");
        }
        this.empty = empty;
    }

    public void addNumberOfGuests(int numberOfGuests) {
        if (isEmpty()) {
            throw new IllegalArgumentException("비어있는 테이블의 손님 수를 변경할 수 없습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void addTableGroup(TableGroup tableGroup) {
        if (Objects.nonNull(this.tableGroup)) {
            throw new IllegalArgumentException("tableGroup이 이미 등록되어있습니다.");
        }
        setTableGroup(tableGroup);
        changeEmpty(false);
    }

    public Long getTableGroupId() {
        if (Objects.isNull(tableGroup)) {
            return null;
        }
        return tableGroup.getId();
    }

    public void ungroup() {
        this.tableGroup.getOrderTables().remove(this);
        this.tableGroup = null;
        this.empty = false;
    }
}
