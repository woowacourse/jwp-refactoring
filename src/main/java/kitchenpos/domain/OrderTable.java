package kitchenpos.domain;

import java.util.Objects;

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

    private int numberOfGuests;

    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void groupBy(TableGroup tableGroup) {
        if (Objects.isNull(tableGroup)) {
            throw new IllegalArgumentException("존재하지 않는 테이블 그룹입니다.");
        }
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void ungroup() {
        this.tableGroup = null;
        this.empty = true;
    }

    public void changeEmpty(boolean empty) {
        if (Objects.nonNull(this.tableGroup)) {
            throw new IllegalArgumentException("그룹 지정된 테이블은 빈 테이블 설정 및 해지를 할 수 없습니다.");
        }

        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("변경할 인원수는 0명 이상이어야 합니다.");
        }

        if (this.isEmpty()) {
            throw new IllegalArgumentException("비어있는 테이블입니다.");
        }

        this.numberOfGuests = numberOfGuests;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public Long getIdOfTableGroup() {
        if (Objects.isNull(tableGroup)) {
            return null;
        }
        return tableGroup.getId();
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
