package kitchenpos.integration;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static kitchenpos.testutils.TestDomainBuilder.menuBuilder;
import static kitchenpos.testutils.TestDomainBuilder.menuProductBuilder;
import static org.assertj.core.api.Assertions.assertThat;

public class MenuIntegrationTest extends AbstractIntegrationTest {

    @DisplayName("POST /api/menus - (이름, 가격, 메뉴 그룹 아이디, 복수의 메뉴 상품)으로 메뉴를 추가한다.")
    @Test
    void create() {
        // given
        String name = "후라이드+후라이드";
        BigDecimal price = BigDecimal.valueOf(19000);
        Long friedChickenProductId = 1L;
        Long doubleChickenMenuGroupId = 1L;

        MenuProduct newMenuProduct = menuProductBuilder()
                .productId(friedChickenProductId)
                .quantity(2)
                .build();
        Menu newMenu = menuBuilder()
                .name(name)
                .price(price)
                .menuGroupId(doubleChickenMenuGroupId)
                .menuProducts(Collections.singletonList(newMenuProduct))
                .build();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        // when
        ResponseEntity<Menu> responseEntity = post(
                "/api/menus",
                httpHeaders,
                newMenu,
                new ParameterizedTypeReference<Menu>() {
                }
        );
        Menu createdMenu = responseEntity.getBody();

        // then
        assertThat(createdMenu).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(extractLocation(responseEntity)).isEqualTo("/api/menus/" + createdMenu.getId());
        assertThat(createdMenu.getId()).isNotNull();
        assertThat(createdMenu.getName()).isEqualTo(name);
        assertThat(createdMenu.getPrice()).isEqualByComparingTo(price);
        assertThat(createdMenu.getMenuGroupId()).isEqualByComparingTo(doubleChickenMenuGroupId);

        assertThat(createdMenu.getMenuProducts())
                .extracting(MenuProduct::getSeq)
                .hasSize(1);
        assertThat(createdMenu.getMenuProducts())
                .extracting(MenuProduct::getProductId)
                .containsExactly(friedChickenProductId);
        assertThat(createdMenu.getMenuProducts())
                .extracting(MenuProduct::getMenuId)
                .containsExactly(createdMenu.getId());
        assertThat(createdMenu.getMenuProducts())
                .extracting(MenuProduct::getQuantity)
                .containsExactly(2L);
    }

    @DisplayName("GET /api/menus - 전체 메뉴의 리스트를 가져온다.")
    @Test
    void list() {
        // when
        ResponseEntity<List<Menu>> responseEntity = get(
                "/api/menus",
                new ParameterizedTypeReference<List<Menu>>() {
                }
        );
        List<Menu> menus = responseEntity.getBody();

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(menus).hasSize(6);
    }
}
