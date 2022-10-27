package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Comparator;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class MenuAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("메뉴를 등록한다.")
    void createMenu() {
        // given
        final MenuGroup menuGroup = MenuGroupFixture.requestCreate(localServerPort);
        final Product product1 = ProductFixture.requestCreate(localServerPort);
        final Product product2 = ProductFixture.requestCreate(localServerPort);
        final Product product3 = ProductFixture.requestCreate(localServerPort);
        final Menu menu = MenuFixture.createDefaultWithoutId(menuGroup, product1, product2, product3);

        // when
        final ResponseEntity<Menu> response = testRestTemplate.postForEntity(
                "http://localhost:" + localServerPort + "/api/menus", menu, Menu.class);

        // then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(response.getBody().getMenuProducts())
                        .usingFieldByFieldElementComparator()
                        .extracting("menuId", "seq")
                        .containsExactly(tuple(1L, 1L), tuple(1L, 2L), tuple(1L, 3L)),
                () -> assertThat(response.getBody()).usingRecursiveComparison()
                        .ignoringFields("id", "menuProducts.menuId", "menuProducts.seq")
                        .withComparatorForFields((Comparator<BigDecimal>) BigDecimal::compareTo, "price")
                        .isEqualTo(menu)
        );
    }
}
