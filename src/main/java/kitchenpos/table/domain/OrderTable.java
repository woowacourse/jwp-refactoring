package kitchenpos.table.domain;

import java.util.Objects;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Embedded;
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

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    private OrderTable(
            final Long id,
            final TableGroup tableGroup,
            final NumberOfGuests numberOfGuests,
            final boolean empty
    ) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(
            final TableGroup tableGroup,
            final int numberOfGuests,
            final boolean empty
    ) {
        this(null, tableGroup, new NumberOfGuests(numberOfGuests), empty);
    }

    public void updateTableGroup(final TableGroup group) {
        this.tableGroup = group;
        this.empty = false;
    }

    public void updateNumberOfGuests(final int numberOfGuests) {
        validateEmptyForUpdateNumberOfGuests();
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    private void validateEmptyForUpdateNumberOfGuests() {
        if (this.empty) {
            throw new IllegalArgumentException("테이블은 비어있을 수 없습니다.");
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

    public Optional<Long> getTableGroupId() {
        if (tableGroup == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(tableGroup.getId());
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }
}
