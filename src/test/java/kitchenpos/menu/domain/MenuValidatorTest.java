package kitchenpos.menu.domain;

import static kitchenpos.common.fixtures.MenuProductFixtures.MENU_PRODUCT1_QUANTITY;
import static kitchenpos.common.fixtures.MenuProductFixtures.MENU_PRODUCT2_QUANTITY;
import static kitchenpos.common.fixtures.ProductFixtures.PRODUCT1;
import static kitchenpos.common.fixtures.ProductFixtures.PRODUCT2;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MenuValidatorTest {

    @Autowired
    private MenuValidator menuValidator;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("생성 시 메뉴 가격이 메뉴 상품 가격 합보다 크면 예외가 발생한다.")
    void throws_menuPriceLargeThanSum() {
        // given
        Product product1 = PRODUCT1();
        Product product2 = PRODUCT2();
        Product savedProduct1 = productRepository.save(product1);
        Product savedProduct2 = productRepository.save(product2);

        MenuProduct menuProduct1 = new MenuProduct(savedProduct1.getId(), MENU_PRODUCT1_QUANTITY);
        MenuProduct menuProduct2 = new MenuProduct(savedProduct2.getId(), MENU_PRODUCT2_QUANTITY);
        List<MenuProduct> menuProducts = List.of(menuProduct1, menuProduct2);

        final BigDecimal price1 = product1.calculateTotalPrice(menuProduct1.getQuantity());
        final BigDecimal price2 = product1.calculateTotalPrice(menuProduct2.getQuantity());
        final BigDecimal overedTotalPrice = price1.add(price2).add(BigDecimal.ONE);

        // when & then
        assertThatThrownBy(() -> menuValidator.validateCreate(overedTotalPrice, menuProducts))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 메뉴의 가격이 메뉴 상품 가격의 합보다 클 수 없습니다.");
    }

}
