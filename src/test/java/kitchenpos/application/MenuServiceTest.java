package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.MenuCreateRequest;
import kitchenpos.application.dto.request.MenuProductDto;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuService menuService;

    @Nested
    class create는 {

        private final BigDecimal price = BigDecimal.valueOf(13000);
        private final Long menuGroupId = 11L;
        private final MenuProduct menuProduct = new MenuProduct(1L, 1L, 10L);
        private final List<MenuProduct> menuProducts = Arrays.asList(menuProduct);

        @Test
        void 메뉴를_생성한다() {
            // given
            MenuCreateRequest request = 메뉴_생성_dto를_만든다(price, menuGroupId, menuProducts);
            when(menuRepository.save(any(Menu.class))).thenReturn(
                    new Menu(1L, "pasta", price, menuGroupId, menuProducts));
            when(menuGroupRepository.existsById(any(Long.class))).thenReturn(true);
            when(productRepository.findById(any(Long.class))).thenReturn(
                    Optional.of(new Product(1L, "pasta", BigDecimal.valueOf(13000))));

            // when
            MenuResponse response = menuService.create(request);

            // then
            Assertions.assertAll(
                    () -> assertThat(response.getId()).isNotNull(),
                    () -> assertThat(response.getPrice()).isEqualTo(price),
                    () -> assertThat(response.getMenuGroupId()).isEqualTo(menuGroupId)
            );
        }

        @Test
        void price가_null이면_예외를_반환한다() {
            BigDecimal nullPrice = null;
            MenuCreateRequest request = 메뉴_생성_dto를_만든다(nullPrice, menuGroupId, menuProducts);
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void price가_음수면_예외를_반환한다() {
            // given
            BigDecimal negativePrice = BigDecimal.valueOf(-1000);
            MenuCreateRequest request = 메뉴_생성_dto를_만든다(negativePrice, menuGroupId, menuProducts);

            // when & then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void menu_group이_존재하지_않으면_예외를_반환한다() {
            // given
            Long notExistMenuGroupId = 101L;
            MenuCreateRequest request = 메뉴_생성_dto를_만든다(price, notExistMenuGroupId, menuProducts);
            when(menuGroupRepository.existsById(notExistMenuGroupId)).thenReturn(false);

            // when & then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 가격_총합이_0이하이면_예외를_반환한다() {
            // given
            MenuCreateRequest request = 메뉴_생성_dto를_만든다(price, menuGroupId, menuProducts);
            when(menuGroupRepository.existsById(menuGroupId)).thenReturn(true);

            // when & then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 상품이_존재하지_않으면_예외를_반환한다() {
            // given
            Long notExistProductId = 1001L;
            MenuProduct menuProductWithNotExistProduct = new MenuProduct(1L, notExistProductId, 10L);
            List<MenuProduct> menuProductsWithNotExistProducts = Arrays.asList(menuProductWithNotExistProduct);

            MenuCreateRequest request = 메뉴_생성_dto를_만든다(price, menuGroupId, menuProductsWithNotExistProducts);
            when(menuGroupRepository.existsById(menuGroupId)).thenReturn(true);

            when(productRepository.findById(notExistProductId)).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }

    @Nested
    class list는 {

        private final BigDecimal price = BigDecimal.valueOf(13000);
        private final Long menuGroupId = 11L;
        private final MenuProduct menuProduct = new MenuProduct(1L, 1L, 10L);
        private final List<MenuProduct> menuProducts = Arrays.asList(menuProduct);

        @Test
        void 메뉴_목록을_조회한다() {
            // given
            when(menuRepository.findAllWithMenuProducts()).thenReturn(
                    Arrays.asList(new Menu(1L, "pasta", price, menuGroupId, menuProducts)));

            // when
            List<MenuResponse> responses = menuService.list();

            // then
            Assertions.assertAll(
                    () -> assertThat(responses).hasSize(1),
                    () -> assertThat(responses.get(0).getMenuProducts()).hasSize(1)
            );
        }
    }

    private MenuCreateRequest 메뉴_생성_dto를_만든다(final BigDecimal price, final Long menuGroupId,
                                             final List<MenuProduct> menuProducts) {
        return new MenuCreateRequest(
                "pasta",
                price,
                menuGroupId,
                menuProducts.stream()
                        .map(MenuProductDto::new)
                        .collect(Collectors.toList()));
    }
}
