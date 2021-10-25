package kitchenpos.acceptance;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 관련 기능")
class MenuAcceptanceTest extends AcceptanceTest {

    MenuGroup 한마리메뉴 = new MenuGroup();
    MenuGroup 두마리메뉴 = new MenuGroup();

    Menu 한마리메뉴_후라이드치킨 = new Menu();
    Menu 두마리메뉴_양념_간장치킨 = new Menu();

    Product 후라이드치킨 = new Product();
    Product 양념치킨 = new Product();
    Product 간장치킨 = new Product();

    @BeforeEach
    void setUp() {
        한마리메뉴.setName("한마리메뉴");
        한마리메뉴 = menuGroupDao.save(한마리메뉴);

        두마리메뉴.setName("두마리메뉴");
        두마리메뉴 = menuGroupDao.save(두마리메뉴);

        후라이드치킨.setName("후라이드치킨");
        후라이드치킨.setPrice(BigDecimal.valueOf(15000));
        후라이드치킨 = productDao.save(후라이드치킨);

        양념치킨.setName("양념치킨");
        양념치킨.setPrice(BigDecimal.valueOf(16000));
        양념치킨 = productDao.save(양념치킨);

        간장치킨.setName("간장치킨");
        간장치킨.setPrice(BigDecimal.valueOf(16000));
        간장치킨 = productDao.save(간장치킨);

        한마리메뉴_후라이드치킨.setName("후라이드치킨");
        한마리메뉴_후라이드치킨.setPrice(BigDecimal.valueOf(15000));
        한마리메뉴_후라이드치킨.setMenuGroupId(한마리메뉴.getId());
        한마리메뉴_후라이드치킨 = menuDao.save(한마리메뉴_후라이드치킨);

        두마리메뉴_양념_간장치킨.setName("양념+간장치킨");
        두마리메뉴_양념_간장치킨.setPrice(BigDecimal.valueOf(32000));
        두마리메뉴_양념_간장치킨.setMenuGroupId(두마리메뉴.getId());
        두마리메뉴_양념_간장치킨 = menuDao.save(두마리메뉴_양념_간장치킨);
    }

    @DisplayName("등록된 전체 메뉴에 대한 정보를 반환한다.")
    @Test
    void getMenus() {
        // when
        ResponseEntity<Menu[]> responseEntity = testRestTemplate.getForEntity("/api/menus", Menu[].class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).hasSize(2);
    }

    @DisplayName("메뉴 그룹 카테고리를 생성한다")
    @Test
    void createMenu() {
        // given
        MenuProduct 두마리메뉴_후라이드_양념치킨_중_후라이드 = new MenuProduct();
        두마리메뉴_후라이드_양념치킨_중_후라이드.setProductId(후라이드치킨.getId());
        두마리메뉴_후라이드_양념치킨_중_후라이드.setQuantity(1);

        MenuProduct 두마리메뉴_후라이드_양념치킨_중_양념 = new MenuProduct();
        두마리메뉴_후라이드_양념치킨_중_양념.setProductId(양념치킨.getId());
        두마리메뉴_후라이드_양념치킨_중_양념.setQuantity(1);

        Menu 두마리메뉴_후라이드_양념치킨 = new Menu();
        두마리메뉴_후라이드_양념치킨.setName("두마리메뉴_후라이드_양념치킨");
        두마리메뉴_후라이드_양념치킨.setPrice(후라이드치킨.getPrice().add(양념치킨.getPrice()));
        두마리메뉴_후라이드_양념치킨.setMenuGroupId(두마리메뉴.getId());
        두마리메뉴_후라이드_양념치킨.setMenuProducts(Arrays.asList(두마리메뉴_후라이드_양념치킨_중_후라이드, 두마리메뉴_후라이드_양념치킨_중_양념));

        // when
        ResponseEntity<Menu> response = testRestTemplate.postForEntity("/api/menus", 두마리메뉴_후라이드_양념치킨, Menu.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Menu 응답된_메뉴 = response.getBody();
        assertThat(응답된_메뉴.getName()).isEqualTo("두마리메뉴_후라이드_양념치킨");
        assertThat(응답된_메뉴.getMenuProducts()).hasSize(2);
    }
}
