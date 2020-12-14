package kitchenpos.table.domain;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import kitchenpos.tablegroup.domain.TableGroup;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    private int numberOfGuests;

    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(final Long id, final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public void existTableGroupId() {
        if (tableGroup != null) {
            throw new IllegalArgumentException("테이블 그룹이 존재합니다.");
        }
    }

    private void validateByNotEmptyOrNonNullTableGroup() {
        if (!empty || Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException("비어있거나 테이블그룹이 존재합니다.");
        }
    }

    private void validateByNumberOfGuests(final int numberOfGuests) {
        if (empty) {
            throw new IllegalArgumentException("테이블이 비어있는 상태에서는 인원을 변경할 수 없습니다.");
        }
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님 수가 0보다 작을수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateByNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public void setUpGroupTable(final TableGroup tableGroup) {
        validateByNotEmptyOrNonNullTableGroup();
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void setUpUnGroupTable() {
        this.tableGroup = null;
        this.empty = true;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(final boolean empty) {
        this.empty = empty;
    }
}
