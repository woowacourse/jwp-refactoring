package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import kitchenpos.menu.exception.InvalidMenuPriceException;
import kitchenpos.menu.exception.MenuGroupNotFoundException;
import kitchenpos.menu.exception.ProductNotFoundException;
import kitchenpos.menu.ui.request.MenuProductRequest;
import kitchenpos.menu.ui.request.MenuRequest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("MenuValidator 통합 테스트")
@SpringBootTest
class MenuValidatorTest {

    @Autowired
    private MenuValidator menuValidator;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("생성 요청을 검증할 때")
    @Nested
    class ValidateCreate {

        @DisplayName("menuGroupId 에 해당하는 메뉴 그룹이 없는 경우 예외가 발생한다.")
        @Test
        void menuGroupNotFoundException() {
            // given
            Product product = productRepository.save(new Product("햄버거", BigDecimal.valueOf(3_000)));

            MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 2L);
            MenuRequest request = new MenuRequest(
                "햄버거 단품",
                BigDecimal.valueOf(3_000),
                Long.MAX_VALUE,
                Collections.singletonList(menuProductRequest)
            );

            // when, then
            assertThatThrownBy(() -> menuValidator.validateMenu(request))
                .isExactlyInstanceOf(MenuGroupNotFoundException.class);
        }

        @DisplayName("menuGroupId 에 해당하는 메뉴 그룹이 없는 경우 예외가 발생한다.")
        @Test
        void productNotFoundException() {
            // given
            MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("단품 메뉴"));

            MenuProductRequest menuProductRequest = new MenuProductRequest(Long.MAX_VALUE, 2L);
            MenuRequest request = new MenuRequest(
                "햄버거 단품",
                BigDecimal.valueOf(3_000),
                menuGroup.getId(),
                Collections.singletonList(menuProductRequest)
            );

            // when, then
            assertThatThrownBy(() -> menuValidator.validateMenu(request))
                .isExactlyInstanceOf(ProductNotFoundException.class);
        }

        @DisplayName("상품 가격의 합보다 메뉴 가격이 높을 경우 예외가 발생한다.")
        @Test
        void overPriceMenuException() {
            // given
            Product product = productRepository.save(new Product("햄버거", BigDecimal.valueOf(3_000)));
            MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("단품 메뉴"));

            MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 2L);
            MenuRequest request = new MenuRequest(
                "햄버거 단품",
                BigDecimal.valueOf(6_001),
                menuGroup.getId(),
                Collections.singletonList(menuProductRequest)
            );

            // when, then
            assertThatThrownBy(() -> menuValidator.validateMenu(request))
                .isExactlyInstanceOf(InvalidMenuPriceException.class);
        }
    }
}
