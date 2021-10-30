package kitchenpos.domain.ordertable;

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
import kitchenpos.domain.TableGroup;
import kitchenpos.exception.InvalidArgumentException;
import kitchenpos.exception.InvalidStateException;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;

    @Column(nullable = false)
    private Integer numberOfGuests;

    @Column(nullable = false)
    private Boolean empty;

    public OrderTable(Long id, TableGroup tableGroup, Integer numberOfGuests, Boolean empty) {
        validate(numberOfGuests, empty);
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(TableGroup tableGroup, Integer numberOfGuests, Boolean empty) {
        this(null, tableGroup, numberOfGuests, empty);
    }

    public OrderTable(Integer numberOfGuests, Boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    protected OrderTable() {
    }

    private void validate(Integer numberOfGuests, Boolean empty) {
        validateNumberOfGuests(numberOfGuests);
        validateEmptyIsNonNull(empty);
    }

    private void validateNumberOfGuests(Integer numberOfGuests) {
        validateNumberOfGuestsIsNonNull(numberOfGuests);
        validateNumberOfGuestsIsNotNegative(numberOfGuests);
    }

    private void validateNumberOfGuestsIsNonNull(Integer numberOfGuests) {
        if (Objects.isNull(numberOfGuests)) {
            throw new InvalidArgumentException("numberOfGuests 값은 null일 수 없습니다.");
        }
    }

    private void validateNumberOfGuestsIsNotNegative(Integer numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new InvalidArgumentException("numberOfGuests 값은 0보다 작을 수 없습니다.");
        }
    }

    private void validateEmptyIsNonNull(Boolean empty) {
        if (Objects.isNull(empty)) {
            throw new InvalidArgumentException("empty 값은 null일 수 없습니다.");
        }
    }

    public void validateTableGroupIsNull() {
        if (Objects.nonNull(tableGroup)) {
            throw new InvalidStateException("OrderTable은 TableGroup에 속해있지 않아야 합니다.");
        }
    }

    public void validateNotEmpty() {
        if (empty) {
            throw new InvalidStateException("OrderTable이 비어있습니다.");
        }
    }

    public void validateIsEmpty() {
        if (!empty) {
            throw new InvalidStateException("OrderTable이 비어있지 않습니다.");
        }
    }

    public void changeEmpty(Boolean newEmpty) {
        validateEmptyIsNonNull(newEmpty);
        this.empty = newEmpty;
    }

    public void changeNumberOfGuests(Integer newNumberOfGuests) {
        validateNumberOfGuests(newNumberOfGuests);
        numberOfGuests = newNumberOfGuests;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public Long getTableGroupId() {
        if (Objects.isNull(tableGroup)) {
            return null;
        }
        return tableGroup.getId();
    }

    public void assign(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void removeTableGroup() {
        tableGroup = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderTable that = (OrderTable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
