package kitchenpos.menu.domain;

import java.util.List;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {

    private static final int MIN_ORDER_TABLES_SIZE = 2;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> elements;

    protected OrderTables() {
    }

    public OrderTables(final List<OrderTable> elements) {
        validateToConstruct(elements);
        this.elements = elements;
    }

    private void validateToConstruct(final List<OrderTable> orderTables) {
        validateOrderTablesSize(orderTables);
        for (final OrderTable orderTable : orderTables) {
            validateOrderTable(orderTable);
        }
    }

    private void validateOrderTable(final OrderTable orderTable) {
        if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
            throw new IllegalArgumentException(
                String.format(
                    "빈 상태가 아니거나 이미 그룹에 존재하는 테이블로는 그룹을 만들 수 없습니다.(id: %d)",
                    orderTable.getId()
                )
            );
        }
    }

    private void validateOrderTablesSize(final List<OrderTable> orderTables) {
        if (orderTables.size() < MIN_ORDER_TABLES_SIZE) {
            throw new IllegalArgumentException(
                String.format(
                    "주문 테이블은 최소 %d개 이상이어야 합니다.(현재 개수: %d개)",
                    MIN_ORDER_TABLES_SIZE,
                    orderTables.size()
                )
            );
        }
    }

    public void setTableGroup(final TableGroup tableGroup) {
        for (final OrderTable element : elements) {
            element.changeEmpty(false);
            element.changeTableGroup(tableGroup);
        }
    }

    public void ungroup() {
        for (final OrderTable element : elements) {
            element.changeTableGroup(null);
            element.changeEmpty(false);
        }
    }

    public List<OrderTable> getElements() {
        return elements;
    }
}
