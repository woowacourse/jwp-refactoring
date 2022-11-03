package kitchenpos.domain.table;

import java.util.Collections;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderTables {

    private static final int MINIMUM_NUMBER_OF_TABLE = 2;

    @OneToMany
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> value;

    protected OrderTables() {
    }

    public OrderTables(final List<OrderTable> value) {
        validateOrderTableSizeIsValid(value);
        this.value = value;
    }

    private void validateOrderTableSizeIsValid(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MINIMUM_NUMBER_OF_TABLE) {
            throw new IllegalArgumentException(String.format("테이블의 수가 2개 이상이어야 합니다. [%s]", orderTables.size()));
        }
    }

    public List<OrderTable> getValue() {
        return Collections.unmodifiableList(value);
    }
}
