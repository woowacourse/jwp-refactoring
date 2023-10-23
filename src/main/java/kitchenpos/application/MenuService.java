package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import kitchenpos.dao.JpaMenuGroupRepository;
import kitchenpos.dao.JpaMenuProductRepository;
import kitchenpos.dao.JpaMenuRepository;
import kitchenpos.dao.JpaProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;
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
        final Price price = menu.getPrice();

        if (Objects.isNull(price)) {
            throw new IllegalArgumentException();
        }

        if (menu.getMenuGroup() == null) {
            throw new IllegalArgumentException();
        }

        if (menu.getPrice() == null) {
            throw new IllegalArgumentException();
        }

        if (!jpaMenuGroupRepository.existsById(menu.getMenuGroup().getId())) {
            throw new IllegalArgumentException();
        }

        final List<MenuProduct> menuProducts = menu.getMenuProducts();

        Price sum = new Price(0);
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = jpaProductRepository.findById(menuProduct.getProduct().getId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(menuProduct.getQuantity()));
        }

        if (price.isGreaterThan(sum)) {
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
