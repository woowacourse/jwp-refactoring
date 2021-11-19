package kitchenpos.integration;

import kitchenpos.application.menu.MenuProductService;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.repository.MenuGroupRepository;
import kitchenpos.domain.menu.repository.MenuRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.exception.NonExistentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Collections;

import static kitchenpos.application.ServiceTestFixture.DomainFactory.CREATE_MENU;
import static kitchenpos.application.ServiceTestFixture.DomainFactory.CREATE_MENU_GROUP;
import static kitchenpos.application.ServiceTestFixture.DomainFactory.CREATE_PRODUCT;
import static kitchenpos.application.ServiceTestFixture.RequestFactory.CREATE_MENU_PRODUCT_REQUEST;
import static kitchenpos.application.ServiceTestFixture.RequestFactory.CREATE_MENU_REQUEST;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IntegrationTest
@DisplayName("MenuProduct 서비스 통합 테스트")
public class MenuProductServiceIntegrationTest {

    @Autowired
    private MenuProductService menuProductService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    private MenuGroup menuGroup;
    private Product product;

    @BeforeEach
    void setUp() {
        product = CREATE_PRODUCT(1L, "kitchenpos.ui.product", BigDecimal.TEN);
        productRepository.save(product);

        menuGroup = CREATE_MENU_GROUP(1L, "group");
        menuGroupRepository.save(menuGroup);
    }

    @DisplayName("MenuProduct를 생성한다. - 성공, 검증에 통과하여 정상적으로 반환된다.")
    @Test
    void addMenuToMenuProduct() {
        // given
        MenuRequest menuRequest = CREATE_MENU_REQUEST("kitchenpos.ui.menu", BigDecimal.TEN, 1L,
                Collections.singletonList(CREATE_MENU_PRODUCT_REQUEST(product.getId(), 1L))
        );
        Menu menu = CREATE_MENU(1L, menuRequest.getName(), menuRequest.getPrice(), menuGroup);
        menuRepository.save(menu);

        // when - then
        assertThatCode(() -> menuProductService.addMenuToMenuProduct(menuRequest, menu))
                .doesNotThrowAnyException();
    }

    @DisplayName("MenuProduct를 생성한다. - 실패, 상품이 존재하지 않음.")
    @Test
    void addMenuToMenuProductFailedWhenProductNotExist() {
        // given
        long wrongProductId = -100L;
        MenuRequest menuRequest = CREATE_MENU_REQUEST("kitchenpos.ui.menu", BigDecimal.TEN, 1L,
                Collections.singletonList(CREATE_MENU_PRODUCT_REQUEST(wrongProductId, 1L))
        );
        Menu menu = CREATE_MENU(1L, menuRequest.getName(), menuRequest.getPrice(), menuGroup);
        menuRepository.save(menu);

        // when - then
        assertThatThrownBy(() -> menuProductService.addMenuToMenuProduct(menuRequest, menu))
                .isInstanceOf(NonExistentException.class)
                .hasMessageContaining("상품을 찾을 수 없습니다.");
    }

    @DisplayName("MenuProduct를 생성한다. - 실패, 합산 가격이 검증에 통과하지 못함.")
    @Test
    void addMenuToMenuProductFailedWhenTotalPriceNotValid() {
        // given
        MenuRequest menuRequest = CREATE_MENU_REQUEST("kitchenpos.ui.menu", BigDecimal.valueOf(100), 1L,
                Collections.singletonList(CREATE_MENU_PRODUCT_REQUEST(product.getId(), 1L))
        );
        Menu menu = CREATE_MENU(1L, menuRequest.getName(), menuRequest.getPrice(), menuGroup);
        menuRepository.save(menu);

        // when - then
        assertThatThrownBy(() -> menuProductService.addMenuToMenuProduct(menuRequest, menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("상품 가격에 비해 메뉴의 가격이 큽니다.");
    }
}
