package kitchenpos.ordertable.domain;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "TABLE_GROUP_ID", foreignKey = @ForeignKey(name = "FK_ORDER_TABLE_TABLE_GROUP"))
    private TableGroup tableGroup;

    private int numberOfGuests;

    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        validate();
    }

    private void validate() {
        if (this.empty && this.numberOfGuests > 0) {
            throw new IllegalArgumentException(
                    String.format("%d명 : 1명 이상의 손님과 함께 빈 테이블로 등록할 수 없습니다.", this.numberOfGuests));
        }
    }

    public void changeEmpty(boolean empty, boolean isCookingOrMeal) {
        if (Objects.nonNull(this.tableGroup)) {
            throw new IllegalArgumentException("단체 지정된 주문 테이블은 빈 테이블 설정 또는 해지할 수 없습니다.");
        }

        if (isCookingOrMeal) {
            throw new IllegalArgumentException("주문 상태가 조리 또는 식사인 주문 테이블은 빈 테이블 설정 또는 해지할 수 없습니다.");
        }

        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException(String.format("%d명 : 방문한 손님 수가 0명 미만이면 입력할 수 없습니다.", numberOfGuests));
        }

        if (this.empty) {
            throw new IllegalArgumentException("빈 테이블은 방문한 손님 수를 입력할 수 없습니다.");
        }

        this.numberOfGuests = numberOfGuests;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public Long getIdOfTableGroup() {
        if (Objects.isNull(this.tableGroup)) {
            return null;
        }
        return this.tableGroup.getId();
    }

    public void setTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }
}
