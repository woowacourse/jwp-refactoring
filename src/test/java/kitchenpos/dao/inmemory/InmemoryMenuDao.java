package kitchenpos.dao.inmemory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;

public class InmemoryMenuDao implements MenuDao {

    private final Map<Long, Menu> menus;
    private long idValue;

    public InmemoryMenuDao() {
        idValue = 0;
        menus = new LinkedHashMap<>();
    }

    @Override
    public Menu save(Menu entity) {
        long savedId = idValue;
        Menu menu = new Menu();
        menu.setId(savedId);
        menu.setName(entity.getName());
        menu.setMenuGroupId(entity.getMenuGroupId());
        menu.setPrice(entity.getPrice());
        menu.setMenuProducts(new ArrayList<>(entity.getMenuProducts()));
        menus.put(savedId, menu);
        idValue++;
        return menu;
    }

    @Override
    public Optional<Menu> findById(Long id) {
        return Optional.ofNullable(menus.get(id));
    }

    @Override
    public List<Menu> findAll() {
        return new ArrayList<>(menus.values());
    }

    @Override
    public long countByIdIn(List<Long> ids) {
        Set<Long> distinctIds = new HashSet<>(ids);
        return menus.keySet().stream().filter(distinctIds::contains).count();
    }
}
