package kitchenpos.application;

import static kitchenpos.support.DataFixture.createMenuGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.request.MenuGroupRequest;
import kitchenpos.menu.dto.response.MenuGroupResponse;
import kitchenpos.support.DatabaseCleanUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void setUp() {
        databaseCleanUp.clear();
    }

    @DisplayName("메뉴 그룹을 저장한다.")
    @Test
    void save() {
        // given
        final MenuGroupRequest menuGroupRequest = createMenuGroupRequest("추천메뉴");

        // when
        final MenuGroupResponse response = menuGroupService.create(menuGroupRequest);

        // then
        assertThat(response.getName()).isEqualTo(menuGroupRequest.getName());
    }

    @DisplayName("메뉴 그룸의 이름이 null인 경우 예외를 반환한다.")
    @Test
    void save_throwException_ifNameIsNull() {
        // given
        final MenuGroupRequest menuGroupRequest = createMenuGroupRequest(null);

        // when, then
        assertThatThrownBy(() -> menuGroupService.create(menuGroupRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름은 null일 수 없습니다.");
    }

    @DisplayName("메뉴 그룹을 전체 조회한다.")
    @Test
    void findAll() {
        // given
        menuGroupRepository.save(createMenuGroup("추천메뉴"));

        // when, then
        assertThat(menuGroupService.findAll()).hasSize(1);
    }

    private MenuGroupRequest createMenuGroupRequest(final String name) {
        return new MenuGroupRequest(name);
    }
}
