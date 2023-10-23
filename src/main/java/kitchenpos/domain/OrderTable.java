package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Column;
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
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(final TableGroup tableGroup,
                                final int numberOfGuests,
                                final boolean empty) {
        return new OrderTable(null, tableGroup, numberOfGuests, empty);
    }

    public void updateTableGroup(final TableGroup group) {
        this.tableGroup = group;
        this.empty = false;
    }

    public void updateNumberOfGuests(final int numberOfGuests) {
        validateEmptyForUpdateNumberOfGuests();
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validateEmptyForUpdateNumberOfGuests() {
        if (this.empty) {
            throw new IllegalArgumentException("테이블은 비어있을 수 없습니다.");
        }
    }

    private void validateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("인원 수는 음수일 수 없습니다.");
        }
    }

    public void updateEmpty(final boolean empty) {
        validateTableGroup();

        this.empty = empty;
    }

    private void validateTableGroup() {
        if (Objects.nonNull(this.tableGroup)) {
            throw new IllegalArgumentException("이미 속해있는 table group이 있습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        if (tableGroup == null) {
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

    public TableGroup getTableGroup() {
        return tableGroup;
    }
}
