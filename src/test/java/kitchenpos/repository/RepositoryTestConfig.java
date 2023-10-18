package kitchenpos.repository;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;

import static kitchenpos.application.fixture.MenuFixture.menu;
import static kitchenpos.application.fixture.MenuGroupFixture.menuGroup;
import static kitchenpos.application.fixture.MenuProductFixture.menuProduct;
import static kitchenpos.application.fixture.ProductFixture.product;

@DataJpaTest
public abstract class RepositoryTestConfig {

    @Autowired
    protected EntityManager em;

    protected MenuGroup createMenuGroup(final String name) {
        final MenuGroup menuGroup = menuGroup(name);
        em.persist(menuGroup);

        return menuGroup;
    }

    protected Menu createMenu(final String name, final BigDecimal price, final MenuGroup menuGroup) {
        final Menu menu = menu(name, price, menuGroup, new ArrayList<>());
        em.persist(menu);
        return menu;
    }

    protected Product createProduct(final String name, final BigDecimal price) {
        final Product product = product(name, price);
        em.persist(product);
        return product;
    }

    protected MenuProduct createMenuProduct(final Menu menu, final Product product, final long price) {
        final MenuProduct menuProduct = menuProduct(menu, product, price);
        em.persist(menuProduct);
        return menuProduct;
    }
}
