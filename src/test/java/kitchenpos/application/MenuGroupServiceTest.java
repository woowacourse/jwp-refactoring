package kitchenpos.application;

import static kitchenpos.support.fixtures.DomainFixtures.MENU_GROUP_NAME1;
import static kitchenpos.support.fixtures.DomainFixtures.MENU_GROUP_NAME2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.application.dto.request.MenuGroupCommand;
import kitchenpos.application.dto.response.MenuGroupResponse;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.domain.MenuGroup;
import kitchenpos.support.cleaner.ApplicationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    @DisplayName("MenuGroup을 생성한다.")
    void create() {
        MenuGroupCommand menuGroupCommand = new MenuGroupCommand(MENU_GROUP_NAME1);
        MenuGroupResponse menuGroupResponse = menuGroupService.create(menuGroupCommand);

        assertAll(
                () -> assertThat(menuGroupResponse.id()).isNotNull(),
                () -> assertThat(menuGroupResponse.name()).isEqualTo(MENU_GROUP_NAME1)
        );
    }

    @Test
    @DisplayName("MenuGroup을 모두 조회한다.")
    void list() {
        menuGroupRepository.save(new MenuGroup(MENU_GROUP_NAME1));
        menuGroupRepository.save(new MenuGroup(MENU_GROUP_NAME2));

        assertThat(menuGroupService.list()).hasSize(2);
    }
}
