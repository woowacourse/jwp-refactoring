package kitchenpos.ordertable.repository;

import java.util.List;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.persistence.OrderTableDataAccessor;
import kitchenpos.ordertable.persistence.dto.OrderTableDataDto;
import kitchenpos.ordertable.repository.converter.OrderTableConverter;
import kitchenpos.support.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public class OrderTableRepository extends
        BaseRepository<OrderTable, OrderTableDataDto, OrderTableDataAccessor, OrderTableConverter> {

    public OrderTableRepository(final OrderTableDataAccessor dataAccessor, final OrderTableConverter converter) {
        super(dataAccessor, converter);
    }

    public List<OrderTable> findAllByIdIn(final List<Long> ids) {
        return converter.dataToEntity(dataAccessor.findAllByIdIn(ids));
    }

    public List<OrderTable> findAllByTableGroupId(final Long tableGroupId) {
        return converter.dataToEntity(dataAccessor.findAllByTableGroupId(tableGroupId));
    }
}
