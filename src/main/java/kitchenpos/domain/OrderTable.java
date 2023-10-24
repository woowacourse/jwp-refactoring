package kitchenpos.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @NotNull
    private Integer numberOfGuests;

    @NotNull
    private Boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final Long id, final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    private OrderTable(final int numberOfGuests, final boolean empty) {
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable create(final int numberOfGuests, final boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    private void validateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("방문한 손님 수는 0명 이상이어야 합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void group(final TableGroup tableGroup) {
        if (!this.empty) {
            throw new IllegalArgumentException("주문 테이블이 비어있지 않은 경우 그룹화할 수 없습니다.");
        }

        if (Objects.nonNull(this.tableGroup)) {
            throw new IllegalArgumentException("이미 그룹 지정된 테이블은 그룹화할 수 없습니다.");
        }

        this.empty = false;
        this.tableGroup = tableGroup;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void setTableGroup(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (this.empty) {
            throw new IllegalArgumentException("빈 테이블의 방문한 손님 수는 변경할 수 없습니다.");
        }
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(final boolean empty) {
        if (Objects.nonNull(this.tableGroup)) {
            throw new IllegalArgumentException("그룹 지정된 테이블은 빈 테이블로 변경할 수 없습니다.");
        }
        this.empty = empty;
    }

    public void ungroup() {
        if (Objects.isNull(this.tableGroup)) {
            throw new IllegalArgumentException("그룹 지정되지 않은 테이블은 그룹을 해제할 수 없습니다.");
        }
        this.tableGroup = null;
        this.empty = false;
    }
}
