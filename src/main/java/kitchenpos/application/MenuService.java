package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
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
    public Menu create(CreateMenuRequest createMenuRequest) {
        Menu menu = createMenuRequest.getMenu();
        MenuProducts menuProducts = new MenuProducts(createMenuRequest.getMenuProducts());

        if (!menuGroupRepository.existsById(menu.getMenuGroup().getId())) {
            throw new IllegalArgumentException();
        }

        List<Long> productIds = menuProducts.getProductIds();
        if (productRepository.countByIdIn(productIds) != menuProducts.getSize()) {
            throw new IllegalArgumentException();
        }

        BigDecimal sum = menuProducts.getSum();
        if (isGreaterThan(menu.getPrice(), sum)) {
            throw new IllegalArgumentException();
        }

        Menu savedMenu = menuRepository.save(menu);
        menuProducts.updateMenu(savedMenu);
        menuProductRepository.saveAll(menuProducts.getMenuProducts());
        return savedMenu;
    }

    private boolean isGreaterThan(BigDecimal number1, BigDecimal number2) {
        return number1.compareTo(number2) > 0;
    }

    public List<Menu> findAll() {
        return menuRepository.findAll();
    }
}
