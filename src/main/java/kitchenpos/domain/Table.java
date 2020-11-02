package kitchenpos.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Objects;

public class Table {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    private Table() {
    }

    public Table(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Table(Long tableGroupId, int numberOfGuests, boolean empty) {
        this(null, tableGroupId, numberOfGuests, empty);
    }

    public Table(int numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    @JsonIgnore
    public boolean isGrouped() {
        if (Objects.nonNull(tableGroupId) && empty) {
            throw new IllegalStateException();
        }
        return Objects.nonNull(tableGroupId);
    }

    public void putInGroup(Long tableGroupId) {
        if (Objects.nonNull(this.tableGroupId)) {
            throw new IllegalStateException("이미 그룹에 속해있는 테이블입니다.");
        }
        if (!this.empty) {
            throw new IllegalStateException("테이블이 비어있지 않습니다.");
        }
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    public void excludeFromGroup() {
        tableGroupId = null;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }
}
