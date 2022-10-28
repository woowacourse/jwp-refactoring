package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.dto.MenuProductRequest;
import kitchenpos.application.dto.MenuRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuService(MenuRepository menuRepository, ProductRepository productRepository, MenuGroupRepository menuGroupRepository) {
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public Menu create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                        .orElseThrow(IllegalArgumentException::new);

        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProductRequest menuProductRequest : menuRequest.getMenuProducts()) {
            Product product = productRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            MenuProduct menuProduct = new MenuProduct(product, menuProductRequest.getQuantity());
            savedMenuProducts.add(menuProduct);
        }

        return menuRepository.save(
                new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup, savedMenuProducts)
        );
    }

    public List<Menu> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus;
    }
}
