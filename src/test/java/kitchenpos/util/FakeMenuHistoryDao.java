package kitchenpos.util;

import kitchenpos.dao.MenuHistoryDao;
import kitchenpos.domain.history.MenuHistory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class FakeMenuHistoryDao implements MenuHistoryDao {

    private Long id = 0L;
    private final Map<Long, MenuHistory> repository = new HashMap<>();

    @Override
    public MenuHistory save(MenuHistory entity) {
        if (entity.getId() == null) {
            MenuHistory addEntity = new MenuHistory(++id, entity.getMenuId(), entity.getPriceAtTime(), entity.getNameAtTime(), entity.getCreateDate());
            repository.put(addEntity.getId(), addEntity);
            return addEntity;
        }
        return repository.computeIfAbsent(entity.getId(), (id) -> entity);
    }

    @Override
    public List<MenuHistory> findAllByDateAndMenuId(Long menuId, LocalDateTime searchTime) {
        return repository.values().stream()
                .filter(each -> Objects.equals(each.getMenuId(), menuId)
                        && each.getCreateDate().isBefore(searchTime) || each.getCreateDate().isEqual(searchTime))
                .sorted()
                .collect(Collectors.toUnmodifiableList());
    }
}
