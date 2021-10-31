package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.MenuProductRepository;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.exception.NonExistentException;
import kitchenpos.ui.dto.MenuRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@DisplayName("MenuProduct 서비스 테스트")
class MenuProductServiceTest extends ServiceTest {
    @InjectMocks
    private MenuProductService menuProductService;

    @Mock
    private MenuProductRepository menuProductRepository;

    @Mock
    private ProductRepository productRepository;

    @DisplayName("MenuProduct를 생성한다. - 실패, Product를 찾을 수 없음.")
    @Test
    void createFailedProductNotFound() {
        // given
        MenuRequest menuRequest = CREATE_MENU_REQUEST("menu", BigDecimal.TEN, 1L,
                Arrays.asList(CREATE_MENU_PRODUCT_REQUEST(-1L, 1L))
        );

        Menu menu = new Menu(1L, menuRequest.getName(), menuRequest.getPrice(), new MenuGroup("group"));

        given(productRepository.findById(anyLong())).willThrow(NonExistentException.class);

        // when - then
        assertThatThrownBy(() -> menuProductService.create(menuRequest, menu)).isInstanceOf(NonExistentException.class);
        then(productRepository).should(times(1))
                .findById(anyLong());
        then(menuProductRepository).should(never())
                .save(any());
    }

    @DisplayName("MenuProduct를 생성한다. - 실패, 메뉴 금액이 단품 합산 금액보다 큼")
    @Test
    void createFailedSumNotValid() {
        // given
        MenuRequest menuRequest = CREATE_MENU_REQUEST("menu", BigDecimal.valueOf(20000), 1L,
                Arrays.asList(CREATE_MENU_PRODUCT_REQUEST(1L, 1L))
        );
        Product product = new Product("product", BigDecimal.TEN);
        Menu menu = new Menu(1L, menuRequest.getName(), menuRequest.getPrice(), new MenuGroup("group"));

        given(productRepository.findById(anyLong())).willReturn(Optional.of(product));

        // when - then
        assertThatThrownBy(() -> menuProductService.create(menuRequest, menu)).isInstanceOf(IllegalArgumentException.class);
        then(productRepository).should(times(1))
                .findById(anyLong());
        then(menuProductRepository).should(never())
                .save(any());
    }
}
