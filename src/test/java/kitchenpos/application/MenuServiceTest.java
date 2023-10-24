package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.Fixture;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.dto.response.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends ServiceIntegrationTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MenuGroupService menuGroupService;

    private Long menuGroupId;
    private MenuProductRequest menuProductRequest;

    @BeforeEach
    void setUp() {
        menuGroupId = menuGroupService.create(Fixture.MENU_GROUP).getId();
        final ProductResponse productResponse = productService.create(Fixture.PRODUCT);
        menuProductRequest = new MenuProductRequest(productResponse.getId(), 2);
    }

    @Test
    void create() {
        // given
        final MenuCreateRequest menu = new MenuCreateRequest(
                "후라이드+후라이드",
                BigDecimal.valueOf(19000),
                menuGroupId,
                List.of(menuProductRequest));

        // when
        final MenuResponse result = menuService.create(menu);

        // then
        assertThat(result.getId()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 99999})
    void create_priceException(final int price) {
        // given
        final MenuCreateRequest menu = new MenuCreateRequest("후라이드+후라이드", BigDecimal.valueOf(price), menuGroupId,
                List.of(menuProductRequest));

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_menuGroupException() {
        // given
        final MenuCreateRequest menu = new MenuCreateRequest("후라이드+후라이드", BigDecimal.valueOf(1000), Fixture.INVALID_ID,
                List.of(menuProductRequest));

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() {
        // given
        final MenuResponse menu1 = menuService.create(
                new MenuCreateRequest("Menu1", BigDecimal.valueOf(1000), menuGroupId, List.of(menuProductRequest)));
        final MenuResponse menu2 = menuService.create(
                new MenuCreateRequest("Menu2", BigDecimal.valueOf(2000), menuGroupId, List.of(menuProductRequest)));

        // when
        final List<MenuResponse> result = menuService.list();

        // then
        assertSoftly(softly -> {
            softly.assertThat(result).hasSize(2);
            softly.assertThat(result.get(0))
                    .usingRecursiveComparison()
                    .ignoringFields("price")
                    .isEqualTo(menu1);
            softly.assertThat(result.get(0).getPrice()).isEqualByComparingTo(menu1.getPrice());
            softly.assertThat(result.get(1))
                    .usingRecursiveComparison()
                    .ignoringFields("price")
                    .isEqualTo(menu2);
            softly.assertThat(result.get(1).getPrice()).isEqualByComparingTo(menu2.getPrice());
        });
    }
}
