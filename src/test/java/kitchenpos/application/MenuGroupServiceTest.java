package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.application.dto.MenuGroupCreationDto;
import kitchenpos.application.dto.MenuGroupDto;
import kitchenpos.common.annotation.SpringTestWithData;
import kitchenpos.ui.dto.request.MenuGroupCreationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("MenuGroupService 는 ")
@SpringTestWithData
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("MenuGroup 을 생성한다.")
    @Test
    void createMenuGroup() {
        final MenuGroupCreationRequest menuGroupCreationRequest = new MenuGroupCreationRequest("menuGroupName");
        final MenuGroupDto menuGroupDto = menuGroupService.create(MenuGroupCreationDto.from(menuGroupCreationRequest));

        assertAll(
                () -> assertThat(menuGroupDto.getId()).isGreaterThanOrEqualTo(1L),
                () -> assertThat(menuGroupDto.getName()).isEqualTo("menuGroupName")
        );
    }
}
