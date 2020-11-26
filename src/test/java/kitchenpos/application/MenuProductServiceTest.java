package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.Product;
import kitchenpos.domain.menu.repository.MenuProductRepository;
import kitchenpos.domain.menu.repository.ProductRepository;
import kitchenpos.dto.menu.ProductQuantityRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MenuProductServiceTest {
    private static final String 메뉴_그룹_이름_후라이드_세트 = "후라이드 세트";
    private static final Long 메뉴_그룹_ID_1 = 1L;
    private static final Long 메뉴_ID_1 = 1L;
    private static final String 메뉴_이름_후라이드_치킨 = "후라이드 치킨";
    private static final BigDecimal 메뉴_가격_16000원 = new BigDecimal("16000.0");
    private static final String 상품_후라이드_치킨 = "후라이드 치킨";
    private static final String 상품_코카콜라 = "코카콜라";
    private static final Long 상품_1개 = 1L;
    private static final Long 상품_2개 = 2L;
    private static final Long 상품_ID_1 = 1L;
    private static final Long 상품_ID_2 = 2L;
    private static final BigDecimal 상품_가격_15000원 = new BigDecimal("15000.0");
    private static final BigDecimal 상품_가격_1000원 = new BigDecimal("1000.0");
    private static final Long 메뉴_상품_SEQ_1 = 1L;
    private static final Long 메뉴_상품_SEQ_2 = 2L;

    @Mock
    private MenuProductRepository menuProductRepository;
    @Mock
    private ProductRepository productRepository;

    private MenuProductService menuProductService;
    private List<ProductQuantityRequest> productQuantityRequests;
    private Product product;
    private Menu menu;

    @BeforeEach
    void setUp() {
        menuProductService = new MenuProductService(menuProductRepository, productRepository);
        List<ProductQuantityRequest> productQuantityRequestList = Arrays.asList(
                new ProductQuantityRequest(상품_ID_1, 상품_1개)
        );
        productQuantityRequests = productQuantityRequestList;
        product = new Product(상품_ID_1, 상품_후라이드_치킨, 상품_가격_15000원);
        MenuGroup menuGroup = new MenuGroup(메뉴_그룹_ID_1, 메뉴_그룹_이름_후라이드_세트);
        menu = new Menu(메뉴_ID_1, 메뉴_이름_후라이드_치킨, 메뉴_가격_16000원, menuGroup);
    }

    @DisplayName("MenuProduct 생성이 올바르게 수행한다.")
    @Test
    void createTest() {
        when(productRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(product));

        menuProductService.createMenuProducts(menu, productQuantityRequests);

        verify(menuProductRepository).saveAll(anyList());
    }

    @DisplayName("예외 테스트: MenuProduct 생성 중 Menu가 null이면, 예외가 발생한다.")
    @Test
    void createWithNullMenuExceptionTest() {
        assertThatThrownBy(() -> menuProductService.createMenuProducts(null, productQuantityRequests))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 메뉴가 입력되었습니다.");
    }

    @DisplayName("예외 테스트: MenuProduct 생성 중 Product를 찾을 수 없으면, 예외가 발생한다.")
    @Test
    void createWithNotFoundProductExceptionTest() {
        when(productRepository.findById(anyLong())).thenThrow(new IllegalArgumentException("상품을 찾을 수 없습니다."));

        assertThatThrownBy(() -> menuProductService.createMenuProducts(menu, productQuantityRequests))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품을 찾을 수 없습니다.");
    }

    @DisplayName("예외 테스트: MenuProduct 생성 중 ProductQuantityRequests가 null이면, 예외가 발생한다.")
    @Test
    void createWithNullProductQuantityRequestsExceptionTest() {
        assertThatThrownBy(() -> menuProductService.createMenuProducts(menu, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 상품이 입력되었습니다.");
    }

    @DisplayName("예외 테스트: MenuProduct 생성 중 ProductQuantityRequests가 비어있으면, 예외가 발생한다.")
    @Test
    void createWithEmptyProductQuantityRequestsExceptionTest() {
        assertThatThrownBy(() -> menuProductService.createMenuProducts(menu, Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 상품이 입력되었습니다.");
    }
}
