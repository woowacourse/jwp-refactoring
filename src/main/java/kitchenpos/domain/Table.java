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
        validate(numberOfGuests, empty);
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

    private void validate(int numberOfGuests, boolean empty) {
        if (empty && numberOfGuests != 0) {
            throw new IllegalArgumentException("빈 테이블의 손님 수가 " + numberOfGuests + "명일 수 없습니다.");
        }
    }

    @JsonIgnore
    public boolean isGrouped() {
        if (Objects.nonNull(tableGroupId) && empty) {
            throw new IllegalStateException("소속 그룹이 있는데 테이블이 비어있는 이상한 상황입니다.");
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

    public void changeEmpty(final boolean empty) {
        if (empty && isGrouped()) {
            throw new IllegalArgumentException("그룹에 속한 테이블을 빈 테이블로 바꿀 수 없습니다."
                + "해당 테이블은 그룹이 해제되면 자동으로 비워집니다.");
        }
        if (empty) {
            this.numberOfGuests = 0;
        }
        this.empty = empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("테이블에 착석한 손님 수가 0보다 작을 수 없습니다.");
        }
        if (isEmpty() && numberOfGuests != 0) {
            throw new IllegalStateException("빈 테이블에 손님이 앉을 수 없습니다.");
        }
        if (numberOfGuests == 0) {
            this.empty = true;
        }
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
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
}
