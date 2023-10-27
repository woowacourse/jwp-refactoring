package kitchenpos;

import kitchenpos.application.MenuService;
import kitchenpos.application.request.MenuProductRequest;
import kitchenpos.application.request.MenuRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @InjectMocks
    MenuService menuService;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private MenuGroupValidator menuGroupValidator;
    @Mock
    private MenuProductValidator menuProductValidator;

    @Nested
    class CreateTest {
        private List<MenuProductRequest> menuProductRequests;
        private Product product1, product2, product3;

        @BeforeEach
        void setUp() {
            menuProductRequests = List.of(
                    new MenuProductRequest(1L, 1),
                    new MenuProductRequest(2L, 1),
                    new MenuProductRequest(3L, 1)
            );
            product1 = new Product(new ProductName("product1"), new ProductPrice(BigDecimal.valueOf(100)));
            product2 = new Product(new ProductName("product2"), new ProductPrice(BigDecimal.valueOf(200)));
            product3 = new Product(new ProductName("product3"), new ProductPrice(BigDecimal.valueOf(300)));
        }

        @Test
        @DisplayName("가격이 null 혹은 음수값이면 예외가 발생한다.")
        void priceNullOrNegative() {
            // given
            final MenuRequest request1 = new MenuRequest("productName", null, 1L, Collections.emptyList());
            final MenuRequest request2 = new MenuRequest("productName", new BigDecimal("-1"), 1L, Collections.emptyList());

            // when, then
            assertSoftly(softly -> {
                assertThatThrownBy(() -> menuService.create(request1)).isInstanceOf(IllegalArgumentException.class);
                assertThatThrownBy(() -> menuService.create(request2)).isInstanceOf(IllegalArgumentException.class);
            });
        }

        @Test
        @DisplayName("menuGroup이 존재하지 않으면 예외가 발생한다.")
        void menuGroupNotExist() {
            // given
            final MenuRequest request = mock(MenuRequest.class);
            doThrow(IllegalArgumentException.class).when(menuGroupValidator).validate(any());

            given(request.getMenuGroupId()).willReturn(1L);

            // when, then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("사용자가 요청한 가격이 서버에서 측정한 상품의 총 합계 가격보다 크다면 예외가 발생한다.")
        void comparePriceAndSum() {
            // given
            final MenuRequest request = mock(MenuRequest.class);
            doThrow(IllegalArgumentException.class).when(menuProductValidator).validate(any(), any());

            // when, then
            assertThatThrownBy(() -> menuService.create(request)).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("메뉴를 생성한다.")
        void createMenu() {
            // given
            final MenuRequest request = new MenuRequest("productName", BigDecimal.valueOf(600), 1L, menuProductRequests);

            final Menu menu = new Menu(new MenuName(request.getName()), new MenuPrice(request.getPrice()), request.getMenuGroupId(), Collections.emptyList());

            given(menuRepository.save(any())).willReturn(menu);

            // when
            final Menu savedMenu = menuService.create(request);

            // then
            assertSoftly(softly -> {
                verify(menuRepository, times(1)).save(any());
                assertThat(savedMenu).usingRecursiveComparison().isEqualTo(menu);
            });
        }
    }

    @Test
    @DisplayName("전체 메뉴를 조회한다.")
    void list() {
        // given
        final Menu menu = new Menu(new MenuName("productName"), new MenuPrice(BigDecimal.valueOf(35_000)), 1L, Collections.emptyList());
        given(menuRepository.findAll()).willReturn(List.of(menu));

        // when
        final List<Menu> menus = menuService.list();

        // then
        assertSoftly(softly -> {
            assertThat(menus).hasSize(1);
            assertThat(menus.get(0)).usingRecursiveComparison().isEqualTo(menu);
        });
    }
}
