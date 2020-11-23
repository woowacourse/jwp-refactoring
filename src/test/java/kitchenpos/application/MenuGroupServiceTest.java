package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dto.menuGroup.MenuGroupCreateRequest;
import kitchenpos.dto.menuGroup.MenuGroupCreateResponse;
import kitchenpos.dto.menuGroup.MenuGroupFindAllResponse;
import kitchenpos.dto.menuGroup.MenuGroupFindAllResponses;

class MenuGroupServiceTest extends ServiceTest {
    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void create() {
        MenuGroupCreateRequest menuGroupCreateRequest = new MenuGroupCreateRequest(null, "메뉴그룹");

        MenuGroupCreateResponse actual = menuGroupService.create(menuGroupCreateRequest);

        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo(menuGroupCreateRequest.getName());
    }

    @Test
    void list() {
        MenuGroupCreateRequest menuGroup = new MenuGroupCreateRequest(null, "메뉴그룹");

        menuGroupService.create(menuGroup);

        MenuGroupFindAllResponses actual = menuGroupService.findAll();
        MenuGroupFindAllResponse actualItem = actual.getMenuGroupFindAllResponses().get(0);

        assertAll(
                () -> assertThat(actual.getMenuGroupFindAllResponses()).hasSize(1),
                () -> assertThat(actualItem.getId()).isNotNull(),
                () -> assertThat(actualItem.getName()).isEqualTo(menuGroup.getName())
        );
    }
}