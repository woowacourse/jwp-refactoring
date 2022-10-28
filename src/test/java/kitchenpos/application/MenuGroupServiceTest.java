package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dto.request.MenuGroupRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import kitchenpos.fixture.MenuGroupFixture;

@SuppressWarnings("NonAsciiCharacters")
@Transactional
@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성하면 ID가 할당된 MenuGroup객체가 반환된다")
    @Test
    void create() {
        MenuGroupRequest request = MenuGroupFixture.두마리메뉴.toRequest();

        MenuGroupResponse actual = menuGroupService.create(request);
        assertThat(actual).isNotNull();
    }

    @DisplayName("존재하는 모든 메뉴 그룹 목록을 조회한다")
    @Test
    void list() {
        int numOfMenuGroups = 6;
        for (int i = 0; i < numOfMenuGroups; i++) {
            menuGroupService.create(MenuGroupFixture.두마리메뉴.toRequest());
        }

        List<MenuGroupResponse> actual = menuGroupService.list();
        assertThat(actual).hasSize(numOfMenuGroups);
    }
}
