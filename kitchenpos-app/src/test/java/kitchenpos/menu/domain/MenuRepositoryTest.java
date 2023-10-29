package kitchenpos.menu.domain;

import static kitchenpos.common.fixtures.MenuFixtures.MENU1_NAME;
import static kitchenpos.common.fixtures.MenuFixtures.MENU1_PRICE;
import static kitchenpos.common.fixtures.MenuGroupFixtures.MENU_GROUP1;
import static kitchenpos.common.fixtures.MenuProductFixtures.MENU_PRODUCT1_QUANTITY;
import static kitchenpos.common.fixtures.MenuProductFixtures.MENU_PRODUCT2_QUANTITY;
import static kitchenpos.common.fixtures.ProductFixtures.PRODUCT1;
import static kitchenpos.common.fixtures.ProductFixtures.PRODUCT2;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MenuRepositoryTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("ID를 받아서 ID에 해당하는 행의 개수를 반환한다.")
    void countByIdIn() {
        // given
        final MenuGroup savedMenuGroup = menuGroupRepository.save(MENU_GROUP1());
        final Product savedProduct1 = productRepository.save(PRODUCT1());
        final Product savedProduct2 = productRepository.save(PRODUCT2());
        final MenuProduct menuProduct1 = new MenuProduct(savedProduct1.getId(), MENU_PRODUCT1_QUANTITY);
        final MenuProduct menuProduct2 = new MenuProduct(savedProduct2.getId(), MENU_PRODUCT2_QUANTITY);
        final Menu savedMenu1 = menuRepository.save(
                new Menu(MENU1_NAME, MENU1_PRICE, savedMenuGroup, List.of(menuProduct1, menuProduct2)));
        final Menu savedMenu2 = menuRepository.save(
                new Menu(MENU1_NAME, MENU1_PRICE, savedMenuGroup, List.of(menuProduct1, menuProduct2)));
        final List<Long> ids = List.of(savedMenu1.getId(), savedMenu2.getId());

        // when
        int result = menuRepository.countByIdIn(ids);

        // then
        assertThat(result).isEqualTo(2);
    }
}
