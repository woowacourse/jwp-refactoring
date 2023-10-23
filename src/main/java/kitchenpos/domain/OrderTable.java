package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class OrderTable {

    private static final int MIN_NUMBER_OF_GUESTS = 0;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @NotNull
    private Integer numberOfGuests;

    @NotNull
    private Boolean empty;

    protected OrderTable() {
    }

    private OrderTable(
            Long id,
            TableGroup tableGroup,
            Integer numberOfGuests,
            Boolean empty
    ) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable createWithoutTableGroup(
            Integer numberOfGuests,
            Boolean empty
    ) {
        validateNumberOfGuests(numberOfGuests);
        validateNumberEmpty(empty);

        return new OrderTable(null, null, numberOfGuests, empty);
    }

    private static void validateNumberOfGuests(Integer numberOfGuests) {
        if (numberOfGuests < MIN_NUMBER_OF_GUESTS) {
            throw new IllegalArgumentException("테이블에 방문한 손님 수는 0 이상이어야 합니다.");
        }
    }

    private static void validateNumberEmpty(Boolean empty) {
        if (empty == null) {
            throw new NullPointerException("empty는 null일 수 없습니다.");
        }
    }

    public void changeNumberOfGuests(Integer numberOfGuests) {
        validateChangeableNumberOfGuests(numberOfGuests);

        this.numberOfGuests = numberOfGuests;
    }

    private void validateChangeableNumberOfGuests(Integer numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);

        if (Boolean.TRUE.equals(isEmpty())) {
            throw new IllegalArgumentException("주문을 할 수 없는 상태이므로, 방문 손님 수를 변경할 수 없습니다.");
        }
    }

    public void changeEmpty(Boolean empty) {
        validateChangeableEmpty();

        this.empty = empty;
    }

    private void validateChangeableEmpty() {
        if (isGrouped()) {
            throw new IllegalArgumentException("다른 그룹에 속해있으므로, 주문 상태를 변경할 수 없습니다.");
        }

    }

    public void group(TableGroup tableGroup) {
        if (isGrouped()) {
            throw new IllegalArgumentException("이미 다른 그룹에 속한 테이블입니다.");
        }

        this.tableGroup = tableGroup;
        this.empty = Boolean.FALSE;
    }

    public void ungroup() {
        this.tableGroup = null;
        this.empty = Boolean.FALSE;
    }

    public boolean isGrouped() {
        return tableGroup != null;
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

    public Boolean isEmpty() {
        return empty;
    }

    public Long getTableGroupIdOrNull() {
        if (isGrouped()) {
            return tableGroup.getId();
        }

        return null;
    }

}
