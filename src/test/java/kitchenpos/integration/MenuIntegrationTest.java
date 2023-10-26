package kitchenpos.integration;

import static kitchenpos.integration.fixture.MenuAPIFixture.DEFAULT_MENU_NAME;
import static kitchenpos.integration.fixture.MenuAPIFixture.DEFAULT_MENU_QUANTITY;
import static kitchenpos.integration.fixture.MenuAPIFixture.createDefaultMenu;
import static kitchenpos.integration.fixture.MenuAPIFixture.listMenus;
import static kitchenpos.integration.fixture.MenuGroupAPIFixture.createDefaultMenuGroup;
import static kitchenpos.integration.fixture.ProductAPIFixture.createDefaultProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.application.dto.response.MenuGroupResponse;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.integration.helper.InitIntegrationTest;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;

class MenuIntegrationTest extends InitIntegrationTest {

    @Test
    void testCreateSuccess() {
        //given
        final ProductResponse productResponse = createDefaultProduct();
        final MenuGroupResponse menuGroupResponse = createDefaultMenuGroup();

        //when
        final MenuResponse response = createDefaultMenu(productResponse.getId(), productResponse.getPrice(), menuGroupResponse.getId());

        //then
        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getName()).isEqualTo(DEFAULT_MENU_NAME),
                () -> assertThat(response.getPrice()).isEqualByComparingTo(productResponse.getPrice().multiply(BigDecimal.valueOf(DEFAULT_MENU_QUANTITY))),
                () -> assertThat(response.getMenuGroupId()).isEqualTo(menuGroupResponse.getId()),
                () -> assertThat(response.getMenuProducts()).hasSize(1),
                () -> assertThat(response.getMenuProducts().get(0).getProductId()).isEqualTo(productResponse.getId()),
                () -> assertThat(response.getMenuProducts().get(0).getQuantity()).isEqualTo(DEFAULT_MENU_QUANTITY)
        );
    }

    @Test
    void testListSuccess() {
        //given
        final ProductResponse productResponse = createDefaultProduct();
        final MenuGroupResponse menuGroupResponse = createDefaultMenuGroup();
        createDefaultMenu(productResponse.getId(), productResponse.getPrice(), menuGroupResponse.getId());

        //when
        final List<MenuResponse> responses = listMenus();

        //then
        assertThat(responses).hasSize(1);
        final MenuResponse response = responses.get(0);
        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getName()).isEqualTo(DEFAULT_MENU_NAME),
                () -> assertThat(response.getPrice()).isEqualByComparingTo(productResponse.getPrice().multiply(BigDecimal.valueOf(DEFAULT_MENU_QUANTITY))),
                () -> assertThat(response.getMenuGroupId()).isEqualTo(menuGroupResponse.getId()),
                () -> assertThat(response.getMenuProducts()).hasSize(1),
                () -> assertThat(response.getMenuProducts().get(0).getProductId()).isEqualTo(productResponse.getId()),
                () -> assertThat(response.getMenuProducts().get(0).getQuantity()).isEqualTo(DEFAULT_MENU_QUANTITY)
        );
    }
}
