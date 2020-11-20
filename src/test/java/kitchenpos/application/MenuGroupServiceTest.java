package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import kitchenpos.dto.MenuGroupCreateRequest;
import kitchenpos.dto.MenuGroupCreateResponse;
import kitchenpos.dto.MenuGroupFindAllResponse;
import kitchenpos.dto.MenuGroupFindAllResponses;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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

        MenuGroupFindAllResponses actual = menuGroupService.list();
        MenuGroupFindAllResponse actualItem = actual.getMenuGroupFindAllResponses().get(0);

        assertAll(
                () -> assertThat(actual.getMenuGroupFindAllResponses()).hasSize(1),
                () -> assertThat(actualItem.getId()).isNotNull(),
                () -> assertThat(actualItem.getName()).isEqualTo(menuGroup.getName())
        );
    }
}