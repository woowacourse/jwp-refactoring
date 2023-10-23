package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.fixtures.Fixtures;
import kitchenpos.ui.dto.request.MenuProductRequest;
import kitchenpos.ui.dto.request.MenuRequest;
import kitchenpos.ui.dto.response.MenuResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MenuServiceTest extends ServiceTest {

    @Autowired
    MenuService menuService;

    @Autowired
    Fixtures fixtures;

    @Nested
    class 메뉴_등록 {

        @Test
        void 메뉴를_등록한다() {
            // given
            MenuGroup menuGroup = fixtures.메뉴_그룹_저장("치킨메뉴");
            Product product = fixtures.상품_저장("치킨", 18_000L);

            MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 1L);
            MenuRequest request = new MenuRequest(
                    "한마리 메뉴",
                    BigDecimal.valueOf(18_000),
                    menuGroup.getId(),
                    List.of(menuProductRequest)
            );

            // when
            MenuResponse result = menuService.create(request);

            // then
            assertThat(result.getId()).isNotNull();
            assertThat(result.getName()).isEqualTo("한마리 메뉴");
            assertThat(result.getPrice()).isEqualTo(BigDecimal.valueOf(18_000));
            assertThat(result.getMenuGroupId()).isEqualTo(menuGroup.getId());
        }

        @Test
        void 메뉴그룹이_존재하지_않으면_예외가_발생한다() {
            // given
            Product product = fixtures.상품_저장("치킨", 18_000L);

            MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 1L);
            MenuRequest request = new MenuRequest(
                    "한마리 메뉴",
                    BigDecimal.valueOf(18_000),
                    -1L,
                    List.of(menuProductRequest)
            );

            // when, then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴그룹이_null_인_경우_예외가_발생한다() {
            // given
            Product product = fixtures.상품_저장("치킨", 18_000L);

            MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 1L);
            MenuRequest request = new MenuRequest(
                    "한마리 메뉴",
                    BigDecimal.valueOf(18_000),
                    null,
                    List.of(menuProductRequest)
            );

            // when, then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 모든_메뉴_목록을_불러온다() {
        // given
        MenuGroup menuGroup = fixtures.메뉴_그룹_저장("치킨메뉴");
        Product product = fixtures.상품_저장("치킨", 18_000L);
        Menu menu = fixtures.메뉴_저장(menuGroup, "치킨 + 콜라", 18_000L);
        fixtures.메뉴_상품_저장(menu, product, 1L);

        // when
        List<Menu> menus = menuService.list();

        // then
        assertThat(menus.get(0)).isEqualTo(menu);
    }
}

