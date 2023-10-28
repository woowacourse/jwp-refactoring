package kitchenpos.table.domain.implementation;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.util.List;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table_group.application.OrderTableDtoReader;
import kitchenpos.table_group.application.dto.OrderTableDtoInTableGroup;
import org.springframework.stereotype.Component;

@Component
public class OrderTableDtoReaderImpl implements OrderTableDtoReader {

    private final OrderTableRepository repository;

    public OrderTableDtoReaderImpl(final OrderTableRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<OrderTableDtoInTableGroup> readTablesByTableGroupId(final Long tableGroupId) {
        return repository.findAllByTableGroupId(tableGroupId)
            .stream()
            .map(orderTable ->
                new OrderTableDtoInTableGroup(
                    orderTable.getId(),
                    orderTable.getTableGroupId(),
                    orderTable.getNumberOfGuests(),
                    orderTable.isEmpty()
                )
            )
            .collect(toUnmodifiableList());
    }
}
