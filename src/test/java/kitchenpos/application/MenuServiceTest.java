package kitchenpos.application;

import kitchenpos.TestObjectFactory;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Sql("/delete_all.sql")
class MenuServiceTest {
    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @DisplayName("메뉴 생성 기능 테스트")
    @Test
    void create() {
        Menu menuFixture = createMenuToSave("추천메뉴", "양념", 12000);

        Menu savedMenu = menuService.create(menuFixture);

        List<MenuProduct> savedMenuProducts = savedMenu.getMenuProducts();
        assertAll(
                () -> assertThat(savedMenu.getId()).isNotNull(),
                () -> assertThat(savedMenuProducts.get(0).getMenuId()).isEqualTo(savedMenu.getId())
        );
    }

    @DisplayName("메뉴 생성 - price가 null일 때 예외처리")
    @Test
    void createWhenNullPrice() {
        Menu menuFixture = createMenuToSave("추천메뉴", "양념", 12000);
        menuFixture.setPrice(null);

        assertThatThrownBy(() -> menuService.create(menuFixture))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 - price가 0 미만일 경우 예외처리")
    @Test
    void createWhenPriceLessZero() {
        Menu menuFixture = createMenuToSave("추천메뉴", "양념", 12000);
        menuFixture.setPrice(BigDecimal.valueOf(-1));

        assertThatThrownBy(() -> menuService.create(menuFixture))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 - price 구성품 가격의 합보다 큰 경우 예외처리")
    @Test
    void createWhenPriceGraterSum() {
        Menu menuFixture = createMenuToSave("추천메뉴", "양념", 12000);
        menuFixture.setPrice(BigDecimal.valueOf(90000));

        assertThatThrownBy(() -> menuService.create(menuFixture))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void name() {
        menuService.create(createMenuToSave("추천메뉴", "양념", 13000));
        menuService.create(createMenuToSave("추천메뉴", "후라이드", 12000));

        assertThat(menuService.list()).hasSize(2);

    }

    private Menu createMenuToSave(String menuGroupName, String productName, int productPrice) {
        MenuGroup savedMenuGroup = menuGroupDao.save(TestObjectFactory.createMenuGroupDto(menuGroupName));
        Product savedProduct = productDao.save(TestObjectFactory.createProductDto(productName, productPrice));

        List<MenuProduct> menuProducts = Arrays.asList(TestObjectFactory.createMenuProduct(savedProduct.getId(), 2));
        return TestObjectFactory.createMenuDto(productName + "+" + productName, productPrice * 2, savedMenuGroup.getId(), menuProducts);
    }
}
