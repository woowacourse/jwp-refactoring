package kitchenpos.application;

import static kitchenpos.common.fixtures.MenuFixtures.야채곱창_메뉴_가격;
import static kitchenpos.common.fixtures.MenuFixtures.야채곱창_수량;
import static kitchenpos.common.fixtures.MenuFixtures.잘못된_상품_아이디;
import static kitchenpos.common.fixtures.MenuGroupFixtures.루나세트_이름;
import static kitchenpos.common.fixtures.ProductFixtures.야채곱창_가격;
import static kitchenpos.common.fixtures.ProductFixtures.야채곱창_이름;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.common.builder.MenuBuilder;
import kitchenpos.common.builder.MenuGroupBuilder;
import kitchenpos.common.builder.MenuProductBuilder;
import kitchenpos.common.builder.ProductBuilder;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

public class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    private MenuGroup 루나세트;
    private MenuProduct 루나_야채곱창;

    @BeforeEach
    void setUp() {
        Product 야채곱창 = new ProductBuilder()
                .name(야채곱창_이름)
                .price(BigDecimal.valueOf(야채곱창_가격))
                .build();
        야채곱창 = productDao.save(야채곱창);

        루나_야채곱창 = new MenuProductBuilder()
                .productId(야채곱창.getId())
                .quantity(1)
                .build();

        루나세트 = new MenuGroupBuilder()
                .name(루나세트_이름)
                .build();
        루나세트 = menuGroupDao.save(루나세트);
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void 메뉴를_등록한다() {
        // given
        Menu 야채곱창_메뉴 = 메뉴_생성(야채곱창_이름, 야채곱창_메뉴_가격, 루나세트.getId(), List.of(루나_야채곱창));

        // when
        Menu actual = menuService.create(야채곱창_메뉴);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(야채곱창_이름)
        );
    }

    @DisplayName("메뉴를 등록할 때, 가격이 0원 보다 작으면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -100})
    void 메뉴를_등록할_때_가격이_0원_보다_작으면_예외가_발생한다(int 잘못된_가격) {
        // given
        Menu 야채곱창_메뉴 = 메뉴_생성(야채곱창_이름, 잘못된_가격, 루나세트.getId(), List.of(루나_야채곱창));

        // when & then
        assertThatThrownBy(() -> menuService.create(야채곱창_메뉴))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 등록할 때, 가격이 null 이면 예외가 발생한다.")
    @Test
    void 메뉴를_등록할_때_가격이_null_이면_예외가_발생한다() {
        // given
        Menu 야채곱창_메뉴 = new MenuBuilder()
                .name(야채곱창_이름)
                .price(null)
                .menuGroupId(루나세트.getId())
                .menuProducts(List.of(루나_야채곱창))
                .build();

        // when & then
        assertThatThrownBy(() -> menuService.create(야채곱창_메뉴))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 등록할 때, 메뉴 그룹이 존재하지 않으면 예외가 발생한다.")
    @Test
    void 메뉴를_등록할_때_메뉴_그룹이_존재하지_않으면_예외가_발생한다() {
        // given
        Long 잘못된_메뉴그룹_아이디 = -1L;
        Menu 야채곱창_메뉴 = 메뉴_생성(야채곱창_이름, 야채곱창_메뉴_가격, 잘못된_메뉴그룹_아이디, List.of(루나_야채곱창));

        // when & then
        assertThatThrownBy(() -> menuService.create(야채곱창_메뉴))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 등록할 때, 메뉴 상품이 상품에 등록되어 있지 않으면 예외가 발생한다.")
    @Test
    void 메뉴를_등록할_때_메뉴_상품이_상품에_등록되어_있지_않으면_예외가_발생한다() {
        // given
        MenuProduct 등록되지_않은_메뉴상품 = 메뉴상품_생성(잘못된_상품_아이디, 야채곱창_수량);
        Menu 야채곱창_메뉴 = 메뉴_생성(야채곱창_이름, 야채곱창_메뉴_가격, 루나세트.getId(), List.of(등록되지_않은_메뉴상품));

        // when & then
        assertThatThrownBy(() -> menuService.create(야채곱창_메뉴))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 등록할 때, 메뉴 가격이 메뉴상품 가격 합보다 크면 예외가 발생한다.")
    @Test
    void 메뉴를_등록할_때_메뉴_가격이_메뉴상품_가격_합보다_크면_예외가_발생한다() {
        // given
        int 흑자_가격 = 20000;
        Menu 야채곱창_메뉴 = 메뉴_생성(야채곱창_이름, 흑자_가격, 루나세트.getId(), List.of(루나_야채곱창));

        // when & then
        assertThatThrownBy(() -> menuService.create(야채곱창_메뉴))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void 메뉴_목록을_조회한다() {
        // given
        Menu 야채곱창_메뉴 = 메뉴_생성(야채곱창_이름, 야채곱창_가격, 루나세트.getId(), List.of(루나_야채곱창));
        menuDao.save(야채곱창_메뉴);

        // when
        List<Menu> 메뉴들 = menuService.list();

        // then
        assertThat(메뉴들).hasSize(1);
    }

    private Menu 메뉴_생성(final String name, final int price, final Long menuGroupId,
                       final List<MenuProduct> products) {
        return new MenuBuilder()
                .name(name)
                .price(BigDecimal.valueOf(price))
                .menuGroupId(menuGroupId)
                .menuProducts(products)
                .build();
    }

    private MenuProduct 메뉴상품_생성(final Long productId, final long quantity) {
        return new MenuProductBuilder()
                .productId(productId)
                .quantity(quantity)
                .build();
    }
}
