package kitchenpos.acceptance;

import kitchenpos.menu.ui.request.MenuProductRequest;
import kitchenpos.menu.ui.request.MenuRequest;
import kitchenpos.menu.ui.response.MenuResponse;
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

    @BeforeEach
    void setUp() {
        super.setUp();
    }

    @DisplayName("등록된 전체 메뉴에 대한 정보를 반환한다.")
    @Test
    void getMenus() {
        // when
        ResponseEntity<MenuResponse[]> responseEntity = testRestTemplate.getForEntity("/api/menus", MenuResponse[].class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).hasSize(2);
    }

    @DisplayName("메뉴 그룹 카테고리를 생성한다")
    @Test
    void createMenu() {
        // given
        MenuProductRequest 두마리메뉴_후라이드_양념치킨_중_후라이드 = new MenuProductRequest();
        두마리메뉴_후라이드_양념치킨_중_후라이드.setProductId(후라이드치킨.getId());
        두마리메뉴_후라이드_양념치킨_중_후라이드.setQuantity(1L);

        MenuProductRequest 두마리메뉴_후라이드_양념치킨_중_양념 = new MenuProductRequest();
        두마리메뉴_후라이드_양념치킨_중_양념.setProductId(양념치킨.getId());
        두마리메뉴_후라이드_양념치킨_중_양념.setQuantity(1L);

        MenuRequest 두마리메뉴_후라이드_양념치킨 = new MenuRequest();
        두마리메뉴_후라이드_양념치킨.setName("두마리메뉴_후라이드_양념치킨");
        두마리메뉴_후라이드_양념치킨.setPrice(후라이드치킨.getPrice().add(양념치킨.getPrice()));
        두마리메뉴_후라이드_양념치킨.setMenuGroupId(두마리메뉴.getId());
        두마리메뉴_후라이드_양념치킨.setMenuProducts(Arrays.asList(두마리메뉴_후라이드_양념치킨_중_후라이드, 두마리메뉴_후라이드_양념치킨_중_양념));

        // when
        ResponseEntity<MenuResponse> response = testRestTemplate.postForEntity("/api/menus", 두마리메뉴_후라이드_양념치킨, MenuResponse.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        MenuResponse 응답된_메뉴 = response.getBody();
        assertThat(응답된_메뉴.getName()).isEqualTo("두마리메뉴_후라이드_양념치킨");
        assertThat(응답된_메뉴.getMenuProducts()).hasSize(2);
        assertThat(응답된_메뉴.getMenuProducts().get(0).getProductId()).isEqualTo(후라이드치킨.getId());
        assertThat(응답된_메뉴.getMenuProducts().get(1).getProductId()).isEqualTo(양념치킨.getId());
    }

    @DisplayName("메뉴 그룹 카테고리를 생성 시, 메뉴 그룹의 가격 총합이 개별 메뉴 가격의 총합보다 크다면 예외가 발생한다.")
    @Test
    void cannotCreateMenuWhenMenuProductIsMoreExpensive() {
        // given
        MenuProductRequest 두마리메뉴_후라이드_양념치킨_중_후라이드 = new MenuProductRequest();
        두마리메뉴_후라이드_양념치킨_중_후라이드.setProductId(후라이드치킨.getId());
        두마리메뉴_후라이드_양념치킨_중_후라이드.setQuantity(1L);

        MenuProductRequest 두마리메뉴_후라이드_양념치킨_중_양념 = new MenuProductRequest();
        두마리메뉴_후라이드_양념치킨_중_양념.setProductId(양념치킨.getId());
        두마리메뉴_후라이드_양념치킨_중_양념.setQuantity(1L);

        MenuRequest 두마리메뉴_후라이드_양념치킨 = new MenuRequest();
        두마리메뉴_후라이드_양념치킨.setName("두마리메뉴_후라이드_양념치킨");
        두마리메뉴_후라이드_양념치킨.setPrice(BigDecimal.valueOf(9999999L));
        두마리메뉴_후라이드_양념치킨.setMenuGroupId(두마리메뉴.getId());
        두마리메뉴_후라이드_양념치킨.setMenuProducts(Arrays.asList(두마리메뉴_후라이드_양념치킨_중_후라이드, 두마리메뉴_후라이드_양념치킨_중_양념));

        // when
        ResponseEntity<MenuResponse> response = testRestTemplate.postForEntity("/api/menus", 두마리메뉴_후라이드_양념치킨, MenuResponse.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
