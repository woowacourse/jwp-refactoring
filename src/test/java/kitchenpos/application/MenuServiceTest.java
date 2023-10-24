package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import kitchenpos.ServiceTest;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.dto.menu.MenuCreateRequest;
import kitchenpos.dto.menu.MenuProductCreateRequest;
import kitchenpos.dto.menu.MenuResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    private Product product;

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        Product newProduct = Product.of("치킨", BigDecimal.valueOf(18_000L));

        MenuGroup newMenuGroup = new MenuGroup("튀김류");

        product = productRepository.save(newProduct);
        menuGroup = menuGroupRepository.save(newMenuGroup);
    }

    @Nested
    class 메뉴_생성 {

        @Test
        void 정상_요청() {
            // given
            MenuProductCreateRequest menuProductRequest = new MenuProductCreateRequest(product.getId(), 1L);
            MenuCreateRequest request = createMenuRequest("치킨 단품", BigDecimal.valueOf(18_000L), menuGroup.getId(),
                    menuProductRequest);

            // when
            MenuResponse menuResponse = menuService.create(request);

            // then
            assertSoftly(
                    softly -> {
                        softly.assertThat(menuResponse.getMenuGroupId()).isEqualTo(request.getMenuGroupId());
                        softly.assertThat(menuResponse.getName()).isEqualTo(request.getName());
                        softly.assertThat(menuResponse.getPrice()).isEqualTo(request.getPrice());
                        softly.assertThat(menuResponse.getMenuProducts().get(0).getSeq()).isPositive();
                        softly.assertThat(menuResponse.getMenuProducts().get(0).getProductId())
                                .isEqualTo(menuProductRequest.getProductId());
                        softly.assertThat(menuResponse.getMenuProducts().get(0).getQuantity())
                                .isEqualTo(menuProductRequest.getQuantity());
                    }
            );
        }

        @ParameterizedTest
        @ValueSource(longs = {-10_000, -18_000})
        void 요청_가격이_0미만이면_예외_발생(final Long price) {
            // given
            MenuProductCreateRequest menuProductRequest = new MenuProductCreateRequest(product.getId(), 1L);
            MenuCreateRequest request = createMenuRequest("치킨 단품", BigDecimal.valueOf(price), menuGroup.getId(),
                    menuProductRequest);

            // when, then
            assertThatThrownBy(
                    () -> menuService.create(request)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @NullSource
        void 요청_가격이_null이면_예외_발생(final BigDecimal price) {
            // given
            MenuProductCreateRequest menuProductRequest = new MenuProductCreateRequest(product.getId(), 1L);
            MenuCreateRequest request = createMenuRequest("치킨 단품", price, menuGroup.getId(), menuProductRequest);

            // when, then
            assertThatThrownBy(
                    () -> menuService.create(request)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청_상품이_등록된_상품이_아니면_예외_발생() {
            // given
            long invalidProductId = -1L;
            MenuProductCreateRequest menuProductRequest = new MenuProductCreateRequest(invalidProductId, 1L);
            MenuCreateRequest request = createMenuRequest("치킨 단품", BigDecimal.valueOf(18_000L), menuGroup.getId(),
                    menuProductRequest);

            // when, then
            assertThatThrownBy(
                    () -> menuService.create(request)
            ).isInstanceOf(NoSuchElementException.class);
        }

        @Test
        void 요청_가격이_메뉴_상품들_가격의_합보다_크면_예외_발생() {
            // given
            BigDecimal wrongPrice = BigDecimal.valueOf(100_000_000L);
            MenuProductCreateRequest menuProductRequest = new MenuProductCreateRequest(product.getId(), 1L);
            MenuCreateRequest request = createMenuRequest("치킨 단품", wrongPrice, menuGroup.getId(), menuProductRequest);

            // when, then
            assertThatThrownBy(
                    () -> menuService.create(request)
            ).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 메뉴_전체_조회 {

        @Test
        void 정상_요청() {
            // given
            MenuProductCreateRequest menuProductRequest = new MenuProductCreateRequest(product.getId(), 1L);
            MenuCreateRequest request = createMenuRequest("치킨 단품", BigDecimal.valueOf(18_000L), menuGroup.getId(),
                    menuProductRequest);
            MenuResponse menuResponse = menuService.create(request);

            // when
            List<MenuResponse> menuResponses = menuService.readAll();

            // then
            assertThat(menuResponses)
                    .extracting(MenuResponse::getId)
                    .contains(menuResponse.getId());
        }
    }

    private MenuCreateRequest createMenuRequest(final String name,
                                                final BigDecimal price,
                                                final Long menuGroupId,
                                                final MenuProductCreateRequest... menuProducts) {
        return new MenuCreateRequest(name, price, menuGroupId, Arrays.asList(menuProducts));
    }
}
