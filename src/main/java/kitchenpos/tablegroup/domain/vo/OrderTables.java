package kitchenpos.tablegroup.domain.vo;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.ordertable.domain.OrderTable;
import org.hibernate.annotations.BatchSize;

@Embeddable
public class OrderTables {
    @BatchSize(size = 10)
    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<OrderTable> orderTableItems;
}
