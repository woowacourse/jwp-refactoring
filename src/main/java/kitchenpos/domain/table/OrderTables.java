package kitchenpos.domain.table;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

@Embeddable
public class OrderTables {

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
        }
        return new TableGroup(null, collection);
    }

    public void validateOrderTables(List<OrderTable> collection) {
        if (collection == null || collection.size() < 2) {
            throw new IllegalArgumentException();
        }
        if (collection.stream().anyMatch(OrderTable::isNotEmpty)) {
            throw new IllegalArgumentException();
        }
        if (collection.stream().anyMatch(OrderTable::hasTableGroup)) {
            throw new IllegalArgumentException();
        }
    }

    public void unGroup() {
        if (collection.stream()
                .anyMatch(OrderTable::hasCookingOrMealOrder)) {
            throw new IllegalArgumentException();
        }
        if (collection.stream()
                .anyMatch(OrderTable::hasNoTableGroup)) {
            throw new IllegalArgumentException();
        }
        for (OrderTable orderTable : collection) {
            orderTable.unGroup();
        }
    }

    public List<OrderTable> getCollection() {
        return collection;
    }
}
