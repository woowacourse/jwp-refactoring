package kitchenpos.application;

import kitchenpos.domain.Product;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.ui.dto.MenuRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@DisplayName("메뉴 서비스 테스트")
class MenuServiceTest extends ServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MenuRepository menuRepository;

    @DisplayName("메뉴를 생성한다. - 실패, 메뉴의 가격이 음수인 경우")
    @Test
    void createFailedWhenPriceIsNegative() {
        // given
        MenuRequest menuRequest = CREATE_MENU_REQUEST(
                "후라이드+후라이드",
                BigDecimal.valueOf(-1),
                1L,
                singletonList(CREATE_MENU_PRODUCT_REQUEST(1L, 2L))
        );

        // when - then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
        then(menuGroupRepository).should(never())
                .existsById(anyLong());
        then(productRepository).should(never())
                .findById(anyLong());
        then(menuRepository).should(never())
                .save(menuRequest.toMenu());
    }

    @DisplayName("메뉴를 생성한다. - 실패, 메뉴 그룹 Id가 존재하지 않는 경우")
    @Test
    void createFailedWhenMenuGroupIdNotFound() {
        // given
        MenuRequest menuRequest = CREATE_MENU_REQUEST(
                "후라이드+후라이드",
                BigDecimal.valueOf(19000),
                1L,
                singletonList(CREATE_MENU_PRODUCT_REQUEST(1L, 2L))
        );


        given(menuGroupRepository.existsById(1L)).willReturn(false);

        // when - then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
        then(menuGroupRepository).should(times(1))
                .existsById(anyLong());
        then(productRepository).should(never())
                .findById(anyLong());
        then(menuRepository).should(never())
                .save(menuRequest.toMenu());
    }

    @DisplayName("메뉴를 생성한다. - 실패, 메뉴 상품이 존재하지 않는 경우")
    @Test
    void createFailedWhenMenuProductNotFound() {
        // given
        MenuRequest menuRequest = CREATE_MENU_REQUEST(
                "후라이드+후라이드",
                BigDecimal.valueOf(19000),
                1L,
                singletonList(CREATE_MENU_PRODUCT_REQUEST(-1L, 2L))
        );

        given(menuGroupRepository.existsById(1L)).willReturn(true);
        given(productRepository.findById(-1L)).willThrow(IllegalArgumentException.class);

        // when - then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
        then(menuGroupRepository).should(times(1))
                .existsById(1L);
        then(productRepository).should(times(1))
                .findById(-1L);
        then(menuRepository).should(never())
                .save(menuRequest.toMenu());
    }

    @DisplayName("메뉴를 생성한다. - 실패, 메뉴 상품과 수량을 곱한 값보다 메뉴에 등록할 가격이 더 비싼 경우")
    @Test
    void createFailedWhenPriceIsBigger() {
        // given
        MenuRequest menuRequest = CREATE_MENU_REQUEST(
                "후라이드+후라이드",
                BigDecimal.valueOf(19000),
                1L,
                singletonList(CREATE_MENU_PRODUCT_REQUEST(1L, 2L))
        );

        given(menuGroupRepository.existsById(1L)).willReturn(true);

        Product product = new Product(1L, "후라이드", BigDecimal.valueOf(1000));
        given(productRepository.findById(1L)).willReturn(Optional.of(product));


        // product.getPrice() => 1000
        // menuProduct.getQuantity() => 2
        // sum => 2000
        // price => 19000
        // 후라이드+후라이드는 19000원에 팔면서, 사실 후라이드 2개 사면 2000원인 경우
        // when - then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
        then(menuGroupRepository).should(times(1))
                .existsById(1L);
        then(productRepository).should(times(1))
                .findById(1L);
        then(menuRepository).should(never())
                .save(menuRequest.toMenu());
    }
}
