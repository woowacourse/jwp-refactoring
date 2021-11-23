package kitchenpos.integration.menu;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.application.dto.request.MenuGroupRequestDto;
import kitchenpos.application.dto.response.MenuGroupResponseDto;
import kitchenpos.integration.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupServiceIntegrationTest extends IntegrationTest {

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void create_Valid_Success() {
        // given
        MenuGroupRequestDto requestDto = new MenuGroupRequestDto("시즌 메뉴");

        // when
        MenuGroupResponseDto responseDto = menuGroupService.create(requestDto);

        // then
        assertThat(responseDto.getId()).isNotNull();
    }

    @DisplayName("메뉴 그룹의 목록을 조회할 수 있다.")
    @Test
    void list_Valid_Success() {
        // given
        MenuGroupRequestDto requestDto = new MenuGroupRequestDto("시즌 메뉴");

        menuGroupService.create(requestDto);

        // when
        List<MenuGroupResponseDto> responsesDto = menuGroupService.list();

        // then
        assertThat(responsesDto).hasSize(1);
    }
}
