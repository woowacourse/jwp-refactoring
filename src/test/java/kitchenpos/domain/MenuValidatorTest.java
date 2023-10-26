package kitchenpos.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.ServiceTest;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.exception.InvalidMenuException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
public class MenuValidatorTest {

    @Autowired
    private MenuValidator menuValidator;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    private List<MenuProduct> menuProducts;

    @BeforeEach
    void setUp() {
        final Product product = productRepository.save(new Product("치킨", BigDecimal.valueOf(10000)));
        menuProducts = List.of(new MenuProduct(1L, product.getId(), 1));
    }

    @Test
    void 메뉴_그룹이_존재하지_않으면_예외가_발생한다() {
        // given
        final long nonExistMenuGroupId = 99L;
        final Menu menu = new Menu("치킨세트", BigDecimal.valueOf(10000), nonExistMenuGroupId, menuProducts);

        // when & then
        assertThatThrownBy(() -> menuValidator.validate(menu))
                .isInstanceOf(InvalidMenuException.class);
    }

    @Test
    void 메뉴_가격이_메뉴_상품들의_가격_총합보다_크면_예외가_발생한다() {
        // given
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("치킨세트그룹"));
        final Menu menu = new Menu("치킨세트", BigDecimal.valueOf(90000), menuGroup.getId(), menuProducts);

        // when & then
        assertThatThrownBy(() -> menuValidator.validate(menu))
                .isInstanceOf(InvalidMenuException.class);
    }
}
