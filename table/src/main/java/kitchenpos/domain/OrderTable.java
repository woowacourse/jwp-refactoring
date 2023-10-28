package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.application.TableGroupValidator;
import kitchenpos.application.TableValidator;
import kitchenpos.domain.vo.NumberOfGuests;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Column(nullable = false, columnDefinition = "bit")
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public OrderTable(
            Long tableGroupId,
            int numberOfGuests,
            boolean empty,
            TableGroupValidator tableGroupValidator
    ) {
        tableGroupValidator.validateGroup(tableGroupId);
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public void changeNumberOfGuest(int numberOfGuests) {
        if (isEmpty()) {
            throw new IllegalArgumentException("비어있는 테이블 인원 수는 수정할 수 없습니다.");
        }
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public void changeEmpty(boolean empty, TableValidator tableValidator) {
        if (isGrouped()) {
            throw new IllegalArgumentException("그룹이 존재하는 테이블은 수정할 수 없습니다.");
        }
        tableValidator.validateEmpty(id);
        this.empty = empty;
    }

    public void group() {
        this.empty = false;
    }

    public void unGroup() {
        this.tableGroupId = null;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isGrouped() {
        return tableGroupId != null;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
    }
}
