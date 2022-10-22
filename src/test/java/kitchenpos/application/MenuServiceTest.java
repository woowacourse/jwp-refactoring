package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class MenuServiceTest {

    @MockBean(name = "menuGroupDao")
    private MenuGroupDao menuGroupDao;

    @MockBean(name = "productDao")
    private ProductDao productDao;

    @Autowired
    private MenuService menuService;

    private Menu menu;

    @BeforeEach
    void setUp() {
        final Product product = new Product("후라이드", new BigDecimal(10000));
        product.setId(1L);
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2);

        menu = new Menu();
        menu.setName("후라이드+후라이드");
        menu.setPrice(new BigDecimal(19000));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Arrays.asList(menuProduct));

        given(menuGroupDao.existsById(1L)).willReturn(true);
        given(menuGroupDao.existsById(2L)).willReturn(false);
        given(productDao.findById(1L)).willReturn(Optional.of(product));
    }

    @Test
    @DisplayName("메뉴를 생성한다.")
    void create() {
        final Menu savedMenu = menuService.create(this.menu);
        assertThat(savedMenu.getId()).isNotNull();
    }

    @ParameterizedTest(name = "{1} 메뉴를 생성하면 예외가 발생한다.")
    @MethodSource("invalidParams")
    void createWithInvalid(final Menu menu, final String testName) {
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> invalidParams() {
        return Stream.of(
                Arguments.of(new Menu("후라이드+후라이드", new BigDecimal(-1000), 1L, Arrays.asList()),
                        "가격이 음수일 경우"),
                Arguments.of(new Menu("후라이드+후라이드", new BigDecimal(1000), 2L, Arrays.asList()),
                        "존재하지 않는 메뉴그룹일 경우"),
                Arguments.of(new Menu("후라이드+후라이드", new BigDecimal(21000), 1L,
                                Arrays.asList(new MenuProduct(1L, 2))),
                        "메뉴안의 상품들의 가격의 합이 메뉴의 가격을 넘을 경우")
        );
    }
}
