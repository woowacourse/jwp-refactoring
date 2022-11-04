package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;

public interface TableDomainService {

    OrderTable changeEmpty(Long orderTableId, Boolean empty);
}
