package kitchenpos.util;

import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.MenuProduct;

import java.util.*;
import java.util.stream.Collectors;

public class FakeMenuProductDao implements MenuProductDao {

    private Long id = 0L;
    private final Map<Long, MenuProduct> repository = new HashMap<>();

    @Override
    public MenuProduct save(MenuProduct entity) {
        if (entity.getSeq() == null) {
            entity.setSeq(++id);
            repository.put(entity.getSeq(), entity);
            return entity;
        }
        return repository.computeIfAbsent(entity.getSeq(), (id) -> entity);
    }

    @Override
    public Optional<MenuProduct> findById(Long id) {
        if (repository.containsKey(id)) {
            return Optional.of(repository.get(id));
        }
        return Optional.empty();
    }

    @Override
    public List<MenuProduct> findAll() {
        return repository.values()
                .stream()
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<MenuProduct> findAllByMenuId(Long menuId) {
        return repository.values()
                .stream()
                .filter(each -> Objects.equals(each.getMenuId(), menuId))
                .collect(Collectors.toUnmodifiableList());
    }
}
