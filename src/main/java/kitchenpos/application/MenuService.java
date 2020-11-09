package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Menu create(final Menu menu) {
        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        final List<MenuProduct> menuProducts = menu.getMenuProducts();

        Price sum = new Price(BigDecimal.ZERO);
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.calculatePrice(menuProduct.getQuantity()));
        }

        if (menu.isExpensive(sum)) {
            throw new IllegalArgumentException();
        }

        final Menu savedMenu = menuRepository.save(menu);

        final Long menuId = savedMenu.getId();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            final MenuProduct newMenuProduct = menuProduct.toBuilder()
                .menuId(menuId)
                .build();
            savedMenuProducts.add(menuProductRepository.save(newMenuProduct));
        }
        return savedMenu.toBuilder()
            .menuProducts(savedMenuProducts)
            .build();
    }

    public List<Menu> list() {
        final List<Menu> menus = menuRepository.findAll();
        final List<Menu> result = new ArrayList<>();

        for (final Menu menu : menus) {
            final Menu newMenu = menu.toBuilder()
                .menuProducts(menuProductRepository.findAllByMenuId(menu.getId()))
                .build();
            result.add(newMenu);
        }

        return result;
    }
}
