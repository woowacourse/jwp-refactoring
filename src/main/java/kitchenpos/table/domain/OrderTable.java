package kitchenpos.table.domain;

import javax.persistence.*;
import java.util.Objects;

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

    public OrderTable(final Long id,
                      final TableGroup tableGroup,
                      final int numberOfGuests,
                      final boolean empty) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("테이블 당 인원 수는 음수가 될 수 없습니다.");
        }
        if (numberOfGuests == 0 && !empty) {
            throw new IllegalArgumentException("인원 수가 0 일때는 비지않은 테이블로 지정할 수 없습니다.");
        }
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(final int numberOfGuests,
                      final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public void group(final TableGroup tableGroup) {
        if (!empty) {
            throw new IllegalArgumentException("이미 주문 상태인 테이블을 단체로 지정할 수 없습니다.");
        }
        if (this.tableGroup != null) {
            throw new IllegalArgumentException("이미 단체에 속한 테이블을 단체로 지정할 수 없습니다.");
        }
        this.tableGroup = tableGroup;
    }


    public void unGroup() {
        if (tableGroup == null) {
            throw new IllegalArgumentException("테이블이 그룹에 이미 속해있지 않습니다.");
        }
        this.tableGroup = null;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("테이블 당 인원 수는 음수가 될 수 없습니다.");
        }
        if (empty) {
            throw new IllegalArgumentException("빈 테이블의 인원 수를 변경할 수 없습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void changeEmpty(final boolean empty) {
        if (empty) {
            if (tableGroup != null) {
                throw new IllegalArgumentException("단체로 지정된 테이블을 비울 수 없습니다.");
            }

            this.empty = true;
            this.numberOfGuests = 0;
            return;
        }
        this.empty = false;
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

    public boolean hasTableGroup() {
        return this.tableGroup != null;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderTable that = (OrderTable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
