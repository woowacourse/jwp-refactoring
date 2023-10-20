package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.exception.MenuException.NotExistsMenuGroupException;
import kitchenpos.domain.exception.MenuException.NotExistsProductException;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(final MenuRepository menuRepository,
                       final MenuGroupRepository menuGroupRepository,
                       final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(final Menu menu) {
        final List<MenuProduct> menuProducts = menu.getMenuProducts();
        validateCreate(menu.getMenuGroupId(), menuProducts);
        return menuRepository.save(menu);
    }

    private void validateCreate(final Long menuGroupId, List<MenuProduct> menuProducts) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new NotExistsMenuGroupException(menuGroupId);
        }

        long countOfProducts = productRepository.countByIdIn(menuProducts.stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toList()));

        if (countOfProducts != menuProducts.size()) {
            throw new NotExistsProductException();
        }
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
