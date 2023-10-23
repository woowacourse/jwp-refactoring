package kitchenpos.domain.menu;

import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.fixture.MenuGroupFixture.menuGroup;
import static kitchenpos.fixture.MenuProductFixture.menuProduct;
import static kitchenpos.fixture.ProductFixture.product;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class MenuValidatorTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MenuPriceValidator menuValidator;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    void 메뉴의_가격이_메뉴_상품들의_합과_다르면_예외가_발생한다() {
        // given
        MenuGroup menuGroup = menuGroupRepository.save(menuGroup("menugroup"));
        Product product = productRepository.save(product("product", 1000L));
        MenuProduct menuProduct = menuProduct(product.getId(), 1L);
        Menu menu = menuRepository.save(new Menu("product", BigDecimal.valueOf(1001), menuGroup.getId(), List.of(menuProduct)));

        // expect
        assertThatThrownBy(() -> menuValidator.validate(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격의 합이 맞지 않습니다");
    }

    @Test
    void 메뉴_그룹이_존재하지_않으면_예외가_발생한다() {
        // given
        Menu menu = menuRepository.save(new Menu("product", BigDecimal.valueOf(1001), Long.MAX_VALUE, List.of()));

        // expect
        assertThatThrownBy(() -> menuValidator.validate(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 그룹이 존재 해야합니다");
    }
}
