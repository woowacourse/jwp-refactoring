package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.dto.CreateMenuRequest;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductRepository menuProductRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(final CreateMenuRequest createMenuRequest) {
        Menu menu = createMenuRequest.getMenu();
        MenuProducts menuProducts = new MenuProducts(createMenuRequest.getMenuProducts());

        if (!menuGroupRepository.existsById(menu.getMenuGroup().getId())) {
            throw new IllegalArgumentException();
        }

        for (MenuProduct menuProduct : menuProducts.getMenuProducts()) {
            Objects.requireNonNull(menuProduct.getProduct().getId());
            productRepository.findById(menuProduct.getProduct().getId())
                    .orElseThrow(IllegalArgumentException::new);
        }

        BigDecimal sum = menuProducts.getSum();
        if (isGraterThan(menu.getPrice(), sum)) {
            throw new IllegalArgumentException();
        }

        final Menu savedMenu = menuRepository.save(menu);
        menuProducts.updateMenu(savedMenu);
        menuProductRepository.saveAll(menuProducts.getMenuProducts());
        return savedMenu;
    }

    private boolean isGraterThan(BigDecimal number1, BigDecimal number2) {
        return number1.compareTo(number2) > 0;
    }

    public List<Menu> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus;
    }
}
