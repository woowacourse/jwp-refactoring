package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.메뉴_상품_생성;
import static kitchenpos.fixture.MenuFixture.메뉴_저장;
import static kitchenpos.fixture.ProductFixture.상품_저장;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.IntegrationTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends IntegrationTest {

    @Autowired
    private MenuService menuService;
    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("메뉴 등록 시 전달받은 정보를 새 id로 저장한다.")
    void 메뉴_등록_성공_저장() {
        // given

        // when
        final Product product = 상품_저장(productService::create, BigDecimal.valueOf(10000));
        final Menu menu = 메뉴_저장(menuService::create, 1L, BigDecimal.valueOf(5000), product);

        // then
        assertThat(menuService.list())
                .map(Menu::getId)
                .filteredOn(id -> Objects.equals(id, menu.getId()))
                .hasSize(1);
    }

    @Test
    @DisplayName("메뉴는 1개의 메뉴 그룹에 속한다.")
    void 메뉴_등록_실패_메뉴_그룹_없음() {
        // given
        final Product product = 상품_저장(productService::create, new BigDecimal("10000"));

        // when
        final long menuGroupId = -1L;
        final Menu menu = new Menu("할인치킨", BigDecimal.valueOf(10000), menuGroupId, List.of(메뉴_상품_생성(product)));

        // then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 등록 시 가격은 상품 총액보다 클 수 없다.")
    void 메뉴_등록_실패_상품_총액보다_큰_가격() {
        // given
        final BigDecimal menuPrice = BigDecimal.valueOf(Long.MAX_VALUE);
        final BigDecimal productPrice = menuPrice.min(BigDecimal.ONE);
        final Product product = 상품_저장(productService::create, productPrice);

        // when
        final Menu menu = new Menu("할인치킨", menuPrice, 1L, List.of(메뉴_상품_생성(product)));

        // then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
