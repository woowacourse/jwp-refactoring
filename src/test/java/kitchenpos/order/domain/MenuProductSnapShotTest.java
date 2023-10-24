package kitchenpos.order.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.math.BigDecimal;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("메뉴상품 스냅샷(MenuProductSnapShot) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class MenuProductSnapShotTest {

    @Test
    void 메뉴상품을_통해_생성되며_해당_시점의_메뉴상품의_상태를_저장한다() {
        // given
        Product product = new Product("말랑1", BigDecimal.valueOf(1000));
        MenuProduct menuProduct = new MenuProduct(product, 2L);

        // when
        MenuProductSnapShot snapShot = new MenuProductSnapShot(
                product.getName(),
                product.getPrice(),
                menuProduct.getQuantity()
        );

        // then
        ReflectionTestUtils.setField(product, "name", "변경된 상품1 이름");
        assertThat(snapShot.getName()).isEqualTo("말랑1");
    }
}
