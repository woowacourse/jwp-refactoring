package kitchenpos;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import kitchenpos.domain.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static kitchenpos.MenuProductFixture.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MenuValidatorTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuValidator menuValidator;

    @Test
    @DisplayName("메뉴의 가격이 메뉴를 구성하는 실제 제품들을 단품으로 주문하였을 때의 가격 합보다 크면 메뉴를 생성할 수 없다.")
    void addMenuProductsExpensivePrice() {
        // given
        Product 후라이드치킨 = new Product(1L, "후라이드치킨", 17000);
        Product 양념치킨 = new Product(2L, "양념치킨", 17000);
        Menu menu = new Menu(
                "양념 반 + 후라이드 반",
                BigDecimal.valueOf(34001),
                1L);
        List<MenuProduct> menuProducts = Arrays.asList(후라이드치킨_한마리_메뉴상품, 양념치킨_한마리_메뉴상품);
        given(productRepository.findById(후라이드치킨_한마리_메뉴상품.getProductId())).willReturn(Optional.of(후라이드치킨));
        given(productRepository.findById(양념치킨_한마리_메뉴상품.getProductId())).willReturn(Optional.of(양념치킨));

        // when & then
        assertThatThrownBy(() -> menu.addMenuProducts(menuProducts, menuValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격은 제품 단품의 합보다 클 수 없습니다.");
    }
}
