package kitchenpos.domain;

import org.springframework.util.CollectionUtils;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

public class OrderTables {

    @OneToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            orphanRemoval = true, mappedBy = "tableGroup")
    private List<OrderTable> values;

    public OrderTables() {
    }

    public OrderTables(final List<OrderTable> values) {
        validateSize(values);
        this.values = values;
    }

    private void validateSize(final List<OrderTable> values) {
        if (CollectionUtils.isEmpty(values) || values.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderTable> getValues() {
        return values;
    }
}
