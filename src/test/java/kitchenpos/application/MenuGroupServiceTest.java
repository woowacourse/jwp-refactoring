package kitchenpos.application;

import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.dto.MenuGroupCreateRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그룹을 생성할 수 있다")
    void create() {
        //given
        final MenuGroupCreateRequest menuGroup = new MenuGroupCreateRequest("떡볶이");

        //when
        final MenuGroupResponse saved = menuGroupService.create(menuGroup);

        //then
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    @DisplayName("메뉴 그룹을 생성할 때 이름이 없으면 예외가 발생한다")
    void create_fail() {
        //given
        final MenuGroupCreateRequest menuGroup = new MenuGroupCreateRequest(null);

        //when, then
        assertThatThrownBy(() -> menuGroupService.create(menuGroup))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("메뉴 그룹 전체 조회할 수 있다")
    void list() {
        assertDoesNotThrow(() -> menuGroupService.list());
    }

    @Test
    @DisplayName("메뉴 그룹이 존재하지 않으면 예외가 발생한다")
    void validateExistence() {
        assertThatThrownBy(() -> menuGroupService.validateExistenceById(0L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 메뉴 그룹입니다.");
    }
}
