package table.domain;

import static javax.persistence.GenerationType.IDENTITY;

import exception.EmptyTableException;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import value.NumberOfGuests;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long tableGroupId;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Column(nullable = false)
    private Boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final NumberOfGuests numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
        this.empty = false;
    }

    public void changeNumberOfGuests(final NumberOfGuests numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public void changeEmpty(final Boolean empty) {
        this.empty = empty;
    }

    public void group(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void unGroup() {
        this.tableGroupId = null;
    }


    public void validateEmptyAndGroup() {
        if (!empty || Objects.nonNull(tableGroupId)) {
            throw new EmptyTableException("비어있지 않거나 테이블 그룹이 형성된 테이블은 테이블을 형성할 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public int getNumberOfGuestsValue() {
        return numberOfGuests.getNumberOfGuests();
    }

    public boolean isEmpty() {
        return empty;
    }
}
