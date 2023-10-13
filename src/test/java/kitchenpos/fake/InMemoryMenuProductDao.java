package kitchenpos.fake;

import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.MenuProduct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class InMemoryMenuProductDao implements MenuProductDao {

    private final Map<Long, MenuProduct> map = new HashMap<>();
    private final AtomicLong seq = new AtomicLong();

    @Override
    public MenuProduct save(MenuProduct entity) {
        long seq = this.seq.getAndIncrement();
        entity.setSeq(seq);
        map.put(seq, entity);
        return entity;
    }

    @Override
    public Optional<MenuProduct> findById(Long id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public List<MenuProduct> findAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public List<MenuProduct> findAllByMenuId(Long menuId) {
        return map.values().stream()
                .filter(it -> Objects.nonNull(it.getMenuId()))
                .filter(it -> it.getMenuId().equals(menuId))
                .collect(Collectors.toList());
    }

}
