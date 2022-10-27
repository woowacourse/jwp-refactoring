package kitchenpos.domain;

import java.util.Objects;

import javax.persistence.Entity;
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

    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    private int numberOfGuests;

    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void updateTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void changeEmpty(boolean empty) {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException("그룹으로 묶인 테이블은 비울 수 없습니다.");
        }
        this.empty = empty;
    }

    public void unGroup() {
        this.tableGroup = null;
    }

    public void updateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님의 수는 0 미만일 수 없습니다.");
        }
        this.numberOfGuests = numberOfGuests;
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
