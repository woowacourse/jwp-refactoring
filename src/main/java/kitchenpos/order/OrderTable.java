package kitchenpos.order;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    @Embedded
    private NumberOfGuests numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public void changeEmpty(boolean empty) {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException("테이블그룹에 속한 테이블의 상태를 변경할 수 없습니다.");
        }

        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (empty) {
            throw new IllegalArgumentException("비어있는 테이블의 인원을 변경할 수 없습니다.");
        }
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public void group(Long tableGroupId) {
        if (!empty) {
            throw new IllegalArgumentException("비어있는 테이블만 주문그룹이 될 수 있습니다.");
        }

        if (Objects.nonNull(this.tableGroupId)) {
            throw new IllegalArgumentException("그룹에 속하지 않은 테이블만 주문그룹이 될 수 있습니다.");
        }
        this.empty = false;
        this.tableGroupId = tableGroupId;
    }

    public void unGroup() {
        this.empty = true;
        this.tableGroupId = null;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getValue();
    }

    public boolean isEmpty() {
        return empty;
    }
}
