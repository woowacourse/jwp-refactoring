package kitchenpos.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.MERGE;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup", cascade = MERGE)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    public OrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public boolean hasSizeOf(final int size) {
        return orderTables.size() == size;
    }

    public void validateCanGroupAndChangeToOccupied() {
        validateAllCanGroup();
        orderTables.forEach(OrderTable::changeToOccupied);
    }

    private void validateAllCanGroup() {
        final long validatedCount = orderTables.stream()
                .filter(OrderTable::canGroup)
                .count();
        if (validatedCount != orderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    public void validateSizeAndUngroup(final int size) {
        validateSize(size);
        orderTables.forEach(OrderTable::ungroup);
    }

    private void validateSize(final int size) {
        if (orderTables.size() != size) {
            throw new IllegalArgumentException();
        }
    }

    public List<Long> getIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toUnmodifiableList());
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
