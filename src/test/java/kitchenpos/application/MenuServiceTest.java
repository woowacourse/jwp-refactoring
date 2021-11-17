package kitchenpos.application;

import kitchenpos.ProductFixture;
import kitchenpos.domain.Price;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.menuproduct.MenuProductRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.ui.dto.MenuProductRequest;
import kitchenpos.ui.dto.MenuRequest;
import kitchenpos.ui.dto.MenuResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kitchenpos.MenuFixture.*;
import static kitchenpos.ProductFixture.createProduct1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MenuProductRepository menuProductRepository;

    @InjectMocks
    private MenuService menuService;

    @DisplayName("메뉴 목록을 조회할 수 있다.")
    @Test
    void list() {
        Menu menu1 = createMenu1();
        Menu menu2 = createMenu1();
        when(menuRepository.findAll()).thenReturn(Arrays.asList(menu1, menu2));

        List<MenuResponse> actual = menuService.list();

        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual).containsExactly(MenuResponse.of(menu1), MenuResponse.of(menu2))
        );
    }

    @DisplayName("메뉴 생성은")
    @Nested
    class Create {

        private MenuRequest request;
        private Product product;
        private MenuProductRequest menuProductRequest;

        @BeforeEach
        void beforeAll() {
            product = ProductFixture.createProduct1();
            menuProductRequest = new MenuProductRequest(PRODUCT_ID, QUANTITY);
        }

        @DisplayName("가격이 0원 미만일 경우 생성할 수 없다.")
        @Test
        void createExceptionIfPriceZero() {
            request = new MenuRequest(MENU_NAME1, BigDecimal.valueOf(-1000), MENU_GROUP_ID, Collections.singletonList(menuProductRequest));

            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 메뉴 그룹에 속한 경우 생성할 수 없다.")
        @Test
        void createExceptionIfNotExistGroup() {
            request = new MenuRequest(MENU_NAME1, MENU_PRICE, 0L, Collections.singletonList(menuProductRequest));

            when(menuGroupRepository.findById(any())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 상품을 포함한 경우 생성할 수 없다.")
        @Test
        void createExceptionIfNotExistProduct() {
            request = new MenuRequest(MENU_NAME1, MENU_PRICE, MENU_GROUP_ID, Collections.singletonList(menuProductRequest));
            when(menuGroupRepository.findById(any())).thenReturn(Optional.of(createMenuGroup1(MENU_GROUP_ID)));
            when(productRepository.findById(any())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴 가격이 메뉴 상품 가격의 총합보다 클 수 없다.")
        @Test
        void createExceptionIfExceedPrice() {
            product.setId(1L);
            product.setPrice(new Price(BigDecimal.ONE));
            request = new MenuRequest(MENU_NAME1, BigDecimal.TEN, MENU_GROUP_ID, Arrays.asList(new MenuProductRequest(1L, 1), new MenuProductRequest(1L, 2)));

            when(menuGroupRepository.findById(any())).thenReturn(Optional.of(createMenuGroup1(MENU_GROUP_ID)));
            when(productRepository.findById(any())).thenReturn(Optional.of(product));

            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("조건을 만족하는 경우 생성할 수 있다.")
        @Test
        void create() {
            product.setId(1L);
            product.setPrice(new Price(MENU_PRICE));
            request = new MenuRequest(MENU_NAME1, MENU_PRICE, MENU_GROUP_ID, Arrays.asList(new MenuProductRequest(1L, 1), new MenuProductRequest(1L, 2)));

            when(menuGroupRepository.findById(any())).thenReturn(Optional.of(createMenuGroup1(MENU_GROUP_ID)));
            when(productRepository.findById(any())).thenReturn(Optional.of(product));
            when(menuRepository.save(any())).thenReturn(createMenu1(1L));

            assertDoesNotThrow(() -> menuService.create(request));
        }
    }
}
