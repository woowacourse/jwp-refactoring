package kitchenpos.application;

import kitchenpos.application.common.TestFixtureFactory;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuProductRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.menu.MenuResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql("/delete_all.sql")
class MenuServiceTest extends TestFixtureFactory {
    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    @DisplayName("메뉴 생성 기능 테스트")
    @Test
    void create() {
        String menuName = "양념2마리";
        int menuPrice = 20000;
        String menuGroupName = "추천메뉴";
        String productName = "양념";
        int productPrice = 13000;
        long productQuantity = 2L;
        MenuRequest menuRequest = makeMenuCreateRequest(
                menuName,
                menuPrice,
                menuGroupName,
                productName,
                productPrice,
                productQuantity
        );

        MenuResponse menuResponse = menuService.create(menuRequest);

        List<MenuProduct> menuProductsByMenuId = menuProductRepository.findAllByMenuId(menuResponse.getId());
        assertAll(
                () -> assertThat(menuResponse.getId()).isNotNull(),
                () -> assertThat(menuResponse.getName()).isEqualTo(menuName),
                () -> assertThat(menuResponse.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(menuPrice)),
                () -> assertThat(menuProductsByMenuId.get(0).getQuantity()).isEqualTo(productQuantity),
                () -> assertThat(menuProductsByMenuId.get(0).getId()).isEqualTo(menuResponse.getId())
        );
    }

    @DisplayName("메뉴 생성 - price가 null일 때 예외처리")
    @Test
    void createWhenNullPrice() {
        MenuRequest menuRequest = new MenuRequest("추천메뉴", null, 1L, new ArrayList<>());

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 - price가 0 미만일 경우 예외처리")
    @Test
    void createWhenPriceLessZero() {
        MenuRequest menuRequest = new MenuRequest("추천메뉴", BigDecimal.valueOf(-1), 1L, new ArrayList<>());

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 - price 구성품 가격의 합보다 큰 경우 예외처리")
    @Test
    void createWhenPriceGraterSum() {
        MenuRequest menuRequest = new MenuRequest("추천메뉴", BigDecimal.valueOf(90000), 1L, new ArrayList<>());

        assertThatThrownBy(() -> menuService.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록 조회 기능 테스트")
    @Test
    void list() {
        MenuRequest menuRequest1 = makeMenuCreateRequest(
                "양념2마리",
                20000,
                "추천메뉴1",
                "양념",
                11000,
                2
        );
        MenuRequest menuRequest2 = makeMenuCreateRequest(
                "후라이드2마리",
                20000,
                "추천메뉴2",
                "양념",
                11000,
                2
        );

        menuService.create(menuRequest1);
        menuService.create(menuRequest2);

        assertAll(
                () -> assertThat(menuService.list()).hasSize(2)
        );
    }

    @AfterEach
    void tearDown() {
        menuProductRepository.deleteAll();
        menuRepository.deleteAll();
        productRepository.deleteAll();
        menuGroupRepository.deleteAll();
    }
}
