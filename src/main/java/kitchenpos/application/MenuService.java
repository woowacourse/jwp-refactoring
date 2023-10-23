package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import kitchenpos.dao.JpaMenuGroupRepository;
import kitchenpos.dao.JpaMenuProductRepository;
import kitchenpos.dao.JpaMenuRepository;
import kitchenpos.dao.JpaProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final JpaMenuRepository jpaMenuRepository;
    private final JpaMenuGroupRepository jpaMenuGroupRepository;
    private final JpaMenuProductRepository jpaMenuProductRepository;
    private final JpaProductRepository jpaProductRepository;

    public MenuService(
            final JpaMenuRepository jpaMenuRepository,
            final JpaMenuGroupRepository jpaMenuGroupRepository,
            final JpaMenuProductRepository jpaMenuProductRepository,
            final JpaProductRepository jpaProductRepository
    ) {
        this.jpaMenuRepository = jpaMenuRepository;
        this.jpaMenuGroupRepository = jpaMenuGroupRepository;
        this.jpaMenuProductRepository = jpaMenuProductRepository;
        this.jpaProductRepository = jpaProductRepository;
    }

    @Transactional
    public Menu create(final Menu menu) {
        final BigDecimal price = menu.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        if (menu.getMenuGroup() == null) {
            throw new IllegalArgumentException();
        }

        if (!jpaMenuGroupRepository.existsById(menu.getMenuGroup().getId())) {
            throw new IllegalArgumentException();
        }

        final List<MenuProduct> menuProducts = menu.getMenuProducts();

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = jpaProductRepository.findById(menuProduct.getProduct().getId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        final Menu savedMenu = jpaMenuRepository.save(menu);

        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenu(savedMenu);
            savedMenuProducts.add(jpaMenuProductRepository.save(menuProduct));
        }
        savedMenu.setMenuProducts(savedMenuProducts);

        return savedMenu;
    }

    public List<Menu> list() {
        final List<Menu> menus = jpaMenuRepository.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(jpaMenuProductRepository.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
