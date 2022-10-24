package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends ServiceTest{

    @Autowired
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        final Product product = 후라이드();
        product.setId(1L);
        final Menu menu = 후라이드후라이드(1L);
        menu.setId(1L);

        given(menuGroupDao.existsById(1L)).willReturn(true);
        given(menuGroupDao.existsById(2L)).willReturn(false);
        given(menuDao.save(any())).willReturn(menu);
        given(productDao.findById(1L)).willReturn(Optional.of(product));
    }

    @ParameterizedTest(name = "{1} 경우 메뉴를 생성하면 예외가 발생한다.")
    @MethodSource("invalidParams")
    void createWithInvalid(final Menu menu, final String testName) {
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> invalidParams() {
        return Stream.of(
                Arguments.of(new Menu("후라이드+후라이드", BigDecimal.valueOf(-1000), 1L, Arrays.asList()),
                        "가격이 음수일"),
                Arguments.of(new Menu("후라이드+후라이드", BigDecimal.valueOf(1000), 2L, Arrays.asList()),
                        "존재하지 않는 메뉴그룹일"),
                Arguments.of(new Menu("후라이드+후라이드", BigDecimal.valueOf(21000), 1L, Arrays.asList(new MenuProduct(1L, 2))),
                        "메뉴의 가격이 메뉴안의 상품들의 가격의 합을 넘을")
        );
    }
}
