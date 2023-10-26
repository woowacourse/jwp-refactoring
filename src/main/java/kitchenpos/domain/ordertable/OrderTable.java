package kitchenpos.domain.ordertable;

import kitchenpos.domain.tablegroup.TableGroup;

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
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_order_table_to_table_group"))
    private TableGroup tableGroup;
    private int numberOfGuests = 0;
    private boolean empty = true;

    public OrderTable() {
    }

    public OrderTable(Long id) {
        this.id = id;
    }

    public void assign(TableGroup tableGroup) {
        validateEmpty();
        validateNotGrouped();
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void ungroup() {
        this.tableGroup = null;
        this.empty = false;
    }

    public void empty() {
        validateNotGrouped();
        this.empty = true;
    }

    public void fill() {
        validateNotGrouped();
        this.empty = false;
    }

    public void fill(int numberOfGuests) {
        validateFull();
        validateNotNegative(numberOfGuests);
        this.empty = false;
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNotGrouped() {
        if (this.hasGroup()) {
            throw new IllegalArgumentException("그룹에 포함된 테이블입니다 tableId:" + id);
        }
    }

    private void validateFull() {
        if (isEmpty()) {
            throw new IllegalArgumentException("손님을 받기 전에 테이블을 먼저 채워주세요 tableId:" + id);
        }
    }

    private void validateEmpty() {
        if (isFull()) {
            throw new IllegalArgumentException("빈 테이블이 아닙니다 tableId:" + id);
        }
    }

    private void validateNotNegative(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님의 수는 양수여야 합니다");
        }
    }

    public boolean hasGroup() {
        return Objects.nonNull(tableGroup);
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isFull() {
        return !empty;
    }
}
