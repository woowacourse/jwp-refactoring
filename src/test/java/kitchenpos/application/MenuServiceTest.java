package kitchenpos.application;

import kitchenpos.menu.application.dto.request.MenuCreateRequest;
import kitchenpos.menu.application.dto.MenuProductDto;
import kitchenpos.menu.application.dto.response.MenuProductResponse;
import kitchenpos.menu.application.dto.response.MenuResponse;
import kitchenpos.common.Price;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.persistence.MenuGroupRepository;
import kitchenpos.menu.persistence.MenuRepository;
import kitchenpos.product.persistence.ProductRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

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
    class 메뉴_생성 {
        @Test
        void 메뉴를_생성한다() {
            // given
            final MenuGroup savedMenuGroup = new MenuGroup(1L, "한식");
            final Product savedProduct = new Product(1L, "상품", new Price(BigDecimal.valueOf(1000)));
            final MenuProduct savedMenuProduct = new MenuProduct(1L, savedProduct, 1);
            final Menu savedMenu = new Menu(1L, "신메뉴", new Price(BigDecimal.valueOf(1000)), new MenuGroup(1L, "한식"), new MenuProducts(List.of(savedMenuProduct)));

            when(menuGroupRepository.findById(anyLong()))
                    .thenReturn(Optional.of(savedMenuGroup));
            when(menuRepository.save(any(Menu.class)))
                    .thenReturn(savedMenu);
            when(productRepository.findById(anyLong()))
                    .thenReturn(Optional.of(savedProduct));

            // when
            final MenuCreateRequest request = new MenuCreateRequest("신메뉴", BigDecimal.valueOf(1000), 1L,
                    List.of(new MenuProductDto(1L, 1L)));

            final MenuResponse result = menuService.create(request);
            final MenuResponse expect = new MenuResponse(1L, "신메뉴", BigDecimal.valueOf(1000), 1L,
                    List.of(new MenuProductResponse(1L, 1L, 1L, 1)));

            // then
            assertThat(result)
                    .usingRecursiveComparison()
                    .isEqualTo(expect);
        }

        @Test
        void 메뉴를_생성할_때_가격이_null이면_실패한다() {
            // given
            final MenuCreateRequest request = new MenuCreateRequest("신메뉴", null, 1L, Collections.emptyList());

            // when, then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴를_생성할_때_가격이_0보다_작으면_실패한다() {
            // given
            final MenuCreateRequest request = new MenuCreateRequest("신메뉴", BigDecimal.valueOf(-1000), 1L,
                    List.of(new MenuProductDto(1L, 1L)));

            // when, then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴를_생성할_때_존재하지_않는_메뉴_그룹_아이디를_전달하면_실패한다() {
            // given
            final MenuCreateRequest request = new MenuCreateRequest("신메뉴", BigDecimal.valueOf(1000), 1L,
                    List.of(new MenuProductDto(1L, 1L)));

            when(menuGroupRepository.findById(anyLong()))
                    .thenReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴를_생성할_때_존재하지_않는_상품_아이디를_전달하면_실패한다() {
            // given
            final MenuGroup savedMenuGroup = new MenuGroup(1L, "한식");

            when(menuGroupRepository.findById(anyLong()))
                    .thenReturn(Optional.of(savedMenuGroup));
            when(productRepository.findById(anyLong()))
                    .thenReturn(Optional.empty());

            // when
            final MenuCreateRequest request = new MenuCreateRequest("신메뉴", BigDecimal.valueOf(1000), 1L,
                    List.of(new MenuProductDto(1L, 1L)));

            // then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴를_생성할_때_전달한_가격과보다_메뉴_상품들의_가격의_작지_않으면_실패한다() {
            // given
            final MenuGroup savedMenuGroup = new MenuGroup(1L, "한식");
            final Product savedProduct = new Product("상품", new Price(BigDecimal.valueOf(500)));

            when(menuGroupRepository.findById(anyLong()))
                    .thenReturn(Optional.of(savedMenuGroup));
            when(productRepository.findById(anyLong()))
                    .thenReturn(Optional.of(savedProduct));

            // when
            final MenuCreateRequest request = new MenuCreateRequest("신메뉴", BigDecimal.valueOf(1000), 1L,
                    List.of(new MenuProductDto(1L, 1L)));

            // then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 전체_메뉴_목록을_반환한다() {
        // given
        when(menuRepository.findAll())
                .thenReturn(Collections.emptyList());

        // when
        final List<MenuResponse> result = menuService.list();

        // then
        assertThat(result).isEmpty();
    }
}