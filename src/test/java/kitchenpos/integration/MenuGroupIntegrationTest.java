package kitchenpos.integration;

import static kitchenpos.integration.fixture.MenuGroupAPIFixture.DEFAULT_MENU_GROUP_NAME;
import static kitchenpos.integration.fixture.MenuGroupAPIFixture.createDefaultMenuGroup;
import static kitchenpos.integration.fixture.MenuGroupAPIFixture.listMenuGroups;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.integration.helper.InitIntegrationTest;
import kitchenpos.menu.application.dto.response.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;

public class MenuGroupIntegrationTest extends InitIntegrationTest {

    @Test
    @DisplayName("메뉴 그룹을 성공적으로 생성한다.")
    void testCreateMenuGroup() {
        //given
        //when
        final MenuGroupResponse response = createDefaultMenuGroup();

        //then
        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getName()).isEqualTo(DEFAULT_MENU_GROUP_NAME)
        );
    }

    @Test
    @DisplayName("메뉴 그룹 전체를 성공적으로 조회한다.")
    void testListMenuGroupsSuccess() {
        //given
        createDefaultMenuGroup();

        //when
        final List<MenuGroupResponse> responses = listMenuGroups();

        //then
        assertAll(
                () -> assertThat(responses.size()).isEqualTo(1),
                () -> assertThat(responses.get(0).getId()).isNotNull(),
                () -> assertThat(responses.get(0).getName()).isEqualTo(DEFAULT_MENU_GROUP_NAME)
        );
    }
}
