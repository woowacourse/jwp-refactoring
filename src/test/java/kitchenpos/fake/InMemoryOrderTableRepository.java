package kitchenpos.fake;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class InMemoryOrderTableRepository implements OrderTableRepository {

    private final Map<Long, OrderTable> map = new HashMap<>();
    private final AtomicLong id = new AtomicLong(0);

    @Override
    public OrderTable save(OrderTable entity) {
        if (Objects.isNull(entity.getId())) {
            long id = this.id.getAndIncrement();
            OrderTable orderTable = new OrderTable(id, entity.getTableGroup(), entity.getNumberOfGuests(), entity.isEmpty());
            map.put(id, orderTable);
            return orderTable;
        }
        update(entity);
        return entity;
    }

    private void update(OrderTable entity) {
        map.put(entity.getId(), entity);
    }

    @Override
    public Optional<OrderTable> findById(Long id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public List<OrderTable> findAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public List<OrderTable> findAllByIdIn(List<Long> ids) {
        return ids.stream()
                .map(map::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(Long tableGroupId) {
        return map.values().stream()
                .filter(it -> it.getTableGroup().getId().equals(tableGroupId))
                .collect(Collectors.toList());
    }
}
