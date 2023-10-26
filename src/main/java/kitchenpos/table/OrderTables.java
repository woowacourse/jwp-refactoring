package kitchenpos.table;

import kitchenpos.tablegroup.TableGroup;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class OrderTables {

    @JoinColumn(name = "table_group_id")
    @OneToMany(fetch = FetchType.EAGER)
    private List<OrderTable> collection;

    public OrderTables() {
    }

    public OrderTables(List<OrderTable> collection) {
        this.collection = collection;
    }

    public TableGroup group() {
        validateOrderTables(collection);
        for (OrderTable orderTable : collection) {
            orderTable.makeFull();
            orderTable.makeGrouped();
        }
        return new TableGroup(collection);
    }

    public void validateOrderTables(List<OrderTable> collection) {
        if (collection == null || collection.size() < 2) {
            throw new IllegalArgumentException("테이블 그룹은 2개 이상의 테이블로 구성되어야 합니다.");
        }
        if (collection.stream().anyMatch(OrderTable::isNotEmpty)) {
            throw new IllegalArgumentException("비어있지 않은 테이블은 그룹으로 지정할 수 없습니다.");
        }
        if (collection.stream().anyMatch(OrderTable::hasTableGroup)) {
            throw new IllegalArgumentException("이미 그룹으로 지정된 테이블은 그룹으로 지정할 수 없습니다.");
        }
    }

    public void unGroup() {
        for (OrderTable orderTable : collection) {
            orderTable.unGroup();
        }
        collection.clear();
    }

    public List<OrderTable> getCollection() {
        return new ArrayList<>(collection);
    }

    public List<Long> getOrderTableIds() {
        return collection.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}
