package kitchenpos.ordertable.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.tablegroup.domain.TableGroup;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    @ManyToOne(fetch = FetchType.LAZY)
    private TableGroup tableGroup;

    @Column(name = "number_of_guests", nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    private OrderTable(final Long id, final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeEmpty(final boolean empty) {
        if (Objects.nonNull(this.tableGroup)) {
            throw new IllegalArgumentException("[ERROR] 테이블 그룹이 null이 아닙니다.");
        }

        this.empty = empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("[ERROR] 손님 수는 0 미만일 수 없습니다.");
        }

        if (this.empty) {
            throw new IllegalArgumentException("[ERROR] 주문 테이블이 비어있어서 손님 수를 조정할 수 없습니다.");
        }

        this.numberOfGuests = numberOfGuests;
    }

    public void ungroup() {
        this.tableGroup = null;
        this.empty = false;
    }

    public void validateNotEmptyOrNonNullTableGroup() {
        if (!this.empty || Objects.nonNull(this.tableGroup)) {
            throw new IllegalArgumentException("[ERROR] 비어있지 않거나 테이블 그룹을 가진 주문 테이블이 존재합니다.");
        }
    }

    public void group(final TableGroup tableGroup) {
        this.empty = false;
        this.tableGroup = tableGroup;
    }

    public Long id() {
        return id;
    }

    public TableGroup tableGroup() {
        return tableGroup;
    }

    public int numberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
