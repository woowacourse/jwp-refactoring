package kitchenpos.fakedao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.MenuProduct;

public class MenuProductFakeDao implements MenuProductDao {

    private Long autoIncrementId = 0L;
    private final Map<Long, MenuProduct> repository = new HashMap<>();

    @Override
    public MenuProduct save(MenuProduct entity) {
        repository.putIfAbsent(++autoIncrementId, entity);
        entity.setSeq(autoIncrementId);
        return entity;
    }

    @Override
    public Optional<MenuProduct> findById(Long id) {
        return Optional.ofNullable(repository.get(id));
    }

    @Override
    public List<MenuProduct> findAll() {
        return new ArrayList<>(repository.values());
    }

    @Override
    public List<MenuProduct> findAllByMenuId(Long menuId) {
        return repository.values()
                .stream()
                .filter(menuProduct -> menuProduct.getMenuId().equals(menuId))
                .collect(Collectors.toList());
    }
}
