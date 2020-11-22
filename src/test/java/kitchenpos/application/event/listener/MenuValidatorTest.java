package kitchenpos.application.event.listener;

import kitchenpos.dao.DaoTest;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.service.MenuValidator;
import kitchenpos.ui.dto.MenuCreateRequest;
import kitchenpos.ui.dto.MenuProductCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.MenuFixture.*;
import static kitchenpos.fixture.ProductFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DaoTest
class MenuValidatorTest {
    @Autowired
    private ProductDao productDao;

    private MenuValidator menuValidator;
    private Long productId1;
    private Long productId2;

    @BeforeEach
    void setUp() {
        menuValidator = new MenuValidator(productDao);
        productId1 = productDao.save(createProduct(null, "후라이드", BigDecimal.valueOf(10000))).getId();
        productId2 = productDao.save(createProduct(null, "양념치킨", BigDecimal.valueOf(15000))).getId();
    }

    @Test
    @DisplayName("메뉴 가격이 각 (상품 가격 * 수량)의 합보다 작거나 같을 경우 아이디가 null인 메뉴가 생성된다")
    void doesNotThrowExceptionIfValid() {
        List<MenuProduct> menuProducts = Arrays.asList(
                createMenuProduct(null, productId1, 2, null),
                createMenuProduct(null, productId2, 1, null)
        );
        Menu menu = createMenu(null, "후라이드2+양념치킨1", BigDecimal.valueOf(35000), 5L, menuProducts);
        List<MenuProductCreateRequest> menuProductCreateRequests =
                Arrays.asList(createMenuProductRequest(productId1, 2), createMenuProductRequest(productId2, 1));
        MenuCreateRequest menuRequest = createMenuRequest("후라이드2+양념치킨1", BigDecimal.valueOf(35000), 5L, menuProductCreateRequests);

        assertThat(menuValidator.getValidMenu(menuRequest)).usingRecursiveComparison().isEqualTo(menu);
    }

    @Test
    @DisplayName("메뉴 가격이 각 (상품 가격 * 수량)의 합보다 클 경우 예외가 발생한다")
    void throwExceptionIfInvalid() {
        List<MenuProductCreateRequest> menuProductRequests = Arrays.asList(
                createMenuProductRequest(productId1, 2),
                createMenuProductRequest(productId2, 1)
        );
        MenuCreateRequest menuRequest = createMenuRequest("후라이드2+양념치킨1", BigDecimal.valueOf(36000), 5L, menuProductRequests);

        assertThatThrownBy(() -> menuValidator.getValidMenu(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
}