package kitchenpos.menu.application;

import static kitchenpos.menu.application.MenuServiceTest.MenuRequestFixture.메뉴_생성_요청;
import static kitchenpos.menu.domain.MenuFixture.메뉴;
import static kitchenpos.menugroup.domain.MenuGroupFixture.메뉴_그룹;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductFixture;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductCreateRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.vo.ProductSpecification;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductFixture;
import kitchenpos.product.domain.ProductRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Sql(value = "classpath:test_truncate_table.sql", executionPhase = BEFORE_TEST_METHOD)
@SpringBootTest
class MenuServiceTest {

    private static final BigDecimal PRICE = BigDecimal.valueOf(1).setScale(2, RoundingMode.HALF_UP);

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    private Long menuGroupId;
    private MenuProduct menuProduct;

    @BeforeEach
    void setUp() {
        menuGroupId = menuGroupRepository.save(메뉴_그룹()).getId();
        Product product = productRepository.save(ProductFixture.상품(PRICE));
        ProductSpecification productSpec = ProductSpecification.from(product.getName(), product.getPriceValue());
        menuProduct = MenuProductFixture.메뉴_상품(product.getId(), 1L, productSpec);
    }

    @Test
    void 메뉴를_생성한다() {
        // given
        MenuCreateRequest menuCreateRequest = 메뉴_생성_요청(PRICE, menuGroupId, List.of(menuProduct));

        // when
        MenuResponse createdMenu = menuService.create(menuCreateRequest);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(createdMenu.getId()).isNotNull();
            softly.assertThat(createdMenu.getMenuProducts().get(0).getSeq()).isNotNull();
            softly.assertThat(createdMenu).usingRecursiveComparison()
                    .ignoringFields("id", "menuProducts.seq", "menuProducts.menuId")
                    .isEqualTo(MenuResponse.from(메뉴(menuGroupId, List.of(menuProduct))));
        });
    }

    @Test
    void 메뉴를_생성할_때_메뉴_가격이_0_미만이면_예외를_던진다() {
        // given
        MenuCreateRequest invalidMenu = 메뉴_생성_요청(BigDecimal.valueOf(-1L), menuGroupId, List.of(menuProduct));

        // expect
        Assertions.assertThatThrownBy(() -> menuService.create(invalidMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴를_생성할_때_메뉴_가격이_null이면_예외를_던진다() {
        // given
        MenuCreateRequest invalidMenu = 메뉴_생성_요청(null, menuGroupId, List.of(menuProduct));

        // expect
        Assertions.assertThatThrownBy(() -> menuService.create(invalidMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴를_생성할_때_메뉴_그룹이_존재하지_않으면_예외를_던진다() {
        // given
        MenuCreateRequest invalidMenu = 메뉴_생성_요청(PRICE, Long.MIN_VALUE, List.of(menuProduct));

        // expect
        Assertions.assertThatThrownBy(() -> menuService.create(invalidMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴를_생성할_때_메뉴_상품이_존재하지_않는_상품이면_예외를_던진다() {
        // given
        MenuCreateRequest invalidMenu = 메뉴_생성_요청(PRICE, menuGroupId, List.of());

        // expect
        Assertions.assertThatThrownBy(() -> menuService.create(invalidMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴를_생성할_때_메뉴_가격이_메뉴_상품의_총가격을_초과하면_예외를_던진다() {
        // given
        MenuCreateRequest invalidMenu = 메뉴_생성_요청(PRICE.add(BigDecimal.ONE), menuGroupId, List.of());

        // expect
        Assertions.assertThatThrownBy(() -> menuService.create(invalidMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 전체_메뉴를_조회한다() {
        // given
        Long menuId = menuService.create(메뉴_생성_요청(PRICE, menuGroupId, List.of(menuProduct))).getId();

        // when
        List<MenuResponse> menus = menuService.list();

        // then
        Assertions.assertThat(menus).usingRecursiveComparison()
                .ignoringFields("menuProducts.seq", "menuProducts.menuId")
                .isEqualTo(List.of(MenuResponse.from(메뉴(menuId, menuGroupId, PRICE, List.of(menuProduct)))));
    }

    static class MenuRequestFixture {

        public static MenuCreateRequest 메뉴_생성_요청(BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
            return new MenuCreateRequest("menuName", price, menuGroupId, 메뉴_상품_생성_요청(menuProducts));
        }

        public static List<MenuProductCreateRequest> 메뉴_상품_생성_요청(List<MenuProduct> menuProducts) {
            return menuProducts.stream()
                    .map(menuProduct -> new MenuProductCreateRequest(
                            menuProduct.getProductId(),
                            menuProduct.getQuantityValue()
                    ))
                    .collect(Collectors.toList());
        }
    }
}
