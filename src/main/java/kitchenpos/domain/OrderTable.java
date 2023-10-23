package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_table")
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

    public OrderTable(
            final TableGroup tableGroup,
            final int numberOfGuests,
            final boolean empty
    ) {
        this(null, tableGroup, numberOfGuests, empty);
    }

    public OrderTable(
            final Long id,
            final TableGroup tableGroup,
            final int numberOfGuests,
            final boolean empty
    ) {
        validateNumberOfGuests(numberOfGuests);
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    private void validateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("[ERROR] 손님의 수가 음수입니다.");
        }
    }

    public void changeEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void updateNumberOfGuests(final int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        validateTableIsEmpty();
        this.numberOfGuests = numberOfGuests;
    }

    private void validateTableIsEmpty() {
        if (this.empty) {
            throw new IllegalArgumentException("[ERROR] 주문 테이블이 비어있습니다.");
        }
    }

    public void group(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void ungroup() {
        this.tableGroup = null;
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
}
