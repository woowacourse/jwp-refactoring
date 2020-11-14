package kitchenpos.application.event.listener;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.event.ValidateMenuPriceEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.MenuFixture.createMenu;
import static kitchenpos.fixture.MenuFixture.createMenuProduct;
import static kitchenpos.fixture.ProductFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class MenuEventListenerTest {
    @Autowired
    private ProductDao productDao;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    private Long productId1;
    private Long productId2;

    @BeforeEach
    void setUp() {
        productId1 = productDao.save(createProduct(null, "후라이드", BigDecimal.valueOf(10000))).getId();
        productId2 = productDao.save(createProduct(null, "양념치킨", BigDecimal.valueOf(15000))).getId();
    }

    @Test
    @DisplayName("메뉴 가격이 각 (상품 가격 * 수량)의 합보다 작거나 같을 경우, 이벤트 발생 시 예외가 발생하지 않는다")
    void doesNotThrowExceptionIfValid() {
        List<MenuProduct> menuProducts = Arrays.asList(
                createMenuProduct(1L, productId1, 2, 1L),
                createMenuProduct(2L, productId2, 1, 1L)
        );
        Menu menu = createMenu(1L, "후라이드2+양념치킨1", BigDecimal.valueOf(35000), 5L, menuProducts);

        applicationEventPublisher.publishEvent(new ValidateMenuPriceEvent(menu));
    }

    @Test
    @DisplayName("메뉴 가격이 각 (상품 가격 * 수량)의 합보다 클 경우, 이벤트 발생 시 예외가 발생한다")
    void throwExceptionIfInvalid() {
        List<MenuProduct> menuProducts = Arrays.asList(
                createMenuProduct(1L, productId1, 2, 1L),
                createMenuProduct(2L, productId2, 1, 1L)
        );
        Menu menu = createMenu(1L, "후라이드2+양념치킨1", BigDecimal.valueOf(36000), 5L, menuProducts);

        assertThatThrownBy(() -> applicationEventPublisher.publishEvent(new ValidateMenuPriceEvent(menu)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}