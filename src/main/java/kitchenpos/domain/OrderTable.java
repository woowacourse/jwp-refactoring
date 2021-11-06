package kitchenpos.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private TableGroup tableGroup;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(Integer numberOfGuest, Boolean empty) {
        this(null, null, numberOfGuest, empty);
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void checkNotGrouped() {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException("테이블 그룹이 존재합니다.");
        }
    }

    public void changeEmpty(Boolean empty) {
        this.empty = empty;
    }

    public void changeNumberOfGuests(Integer numberOfGuest) {
        validateEmpty();
        validateNumberOfGuests();
        this.numberOfGuests = numberOfGuest;
    }

    private void validateEmpty() {
        if (empty) {
            throw new IllegalArgumentException("빈 테이블 입니다.");
        }
    }

    private void validateNumberOfGuests() {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("변경할 수 없는 손님수 입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroup.getId();
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
