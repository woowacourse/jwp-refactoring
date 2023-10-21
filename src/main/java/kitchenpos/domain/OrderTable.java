package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderTable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "table_group_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private TableGroup tableGroup;

    @Column(name = "number_of_guests")
    private int numberOfGuests;

    @Column(name = "empty")
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(final TableGroup tableGroup,
                      final int numberOfGuests,
                      final boolean empty
    ) {
        this(null, tableGroup, numberOfGuests, empty);
    }

    public OrderTable(final Long id,
                      final TableGroup tableGroup,
                      final int numberOfGuests,
                      final boolean empty
    ) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
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

    public void setId(final Long id) {
        this.id = id;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
}
