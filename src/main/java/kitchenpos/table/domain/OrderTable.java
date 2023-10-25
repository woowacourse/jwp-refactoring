package kitchenpos.table.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
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

    @JoinColumn(name = "table_group_id", nullable = true, foreignKey = @ForeignKey(name = "fk_order_table_to_table_group"))
    @ManyToOne(fetch = FetchType.LAZY)
    private TableGroup tableGroup;

    @Column(name = "number_of_guests", nullable = false)
    private int numberOfGuests;

    @Column(name = "empty", nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    protected OrderTable(final TableGroup tableGroup,
                         final int numberOfGuests,
                         final boolean empty
    ) {
        this(null, tableGroup, numberOfGuests, empty);
    }

    protected OrderTable(final Long id,
                         final TableGroup tableGroup,
                         final int numberOfGuests,
                         final boolean empty
    ) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable withoutTableGroup(final int numberOfGuests, final boolean empty) {
        return new OrderTable(null, numberOfGuests, empty);
    }

    public boolean isGrouped() {
        return tableGroup != null;
    }

    public void changeOrderTableEmpty(final boolean isEmpty) {
        if (isEmpty && tableGroup != null) {
            throw new IllegalArgumentException("단체 지정이 되어 있으므로 주문 테이블 상태를 비어있게 할 수 없습니다.");
        }

        this.empty = isEmpty;
    }

    public void assignTableGroup(final TableGroup requestTableGroup) {
        if (tableGroup != null) {
            throw new IllegalArgumentException("이미 단체 지정이 되어있는 주문 테이블을 새롭게 단체 지정할 수 없습니다.");
        }
        tableGroup = requestTableGroup;
    }

    public void deassignTableGroup() {
        this.tableGroup = null;
        this.empty = false;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (empty) {
            throw new IllegalArgumentException("주문 테이블이 비어있는 상태일 경우 손님 수를 변경할 수 없습니다.");
        }
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("변경할 손님 수는 음이 아닌 정수이어야 합니다.");
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
