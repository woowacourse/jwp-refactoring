package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.repository.MenuProductRepository;
import kitchenpos.domain.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuProductRepository menuProductRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuProductRepository menuProductRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuProductRepository = menuProductRepository;
    }

    @Transactional
    public Menu create(final Menu menu) {
        validateMenuCreating(menu);

        menuRepository.save(menu);

        final MenuProducts menuProducts = menu.getMenuProducts();
        menuProducts.updateMenu(menu);

        menuProductRepository.saveAll(menuProducts.getValues());

        return menu;
    }

    private void validateMenuCreating(final Menu menu) {
        if (Objects.isNull(menu.getMenuGroup().getId())) {
            throw new IllegalArgumentException();
        }

        if (menu.isPriceGreaterThanTotalProductPrice()) {
            throw new IllegalArgumentException();
        }
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
