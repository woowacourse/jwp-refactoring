package kitchenpos.application;

import static kitchenpos.fixture.domain.MenuGroupFixture.메뉴그룹A;
import static kitchenpos.fixture.domain.MenuGroupFixture.메뉴그룹B;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.dto.request.MenuGroupRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuGroupServiceTest extends ServiceTest {

    @Test
    @DisplayName("메뉴 그룹을 생성한다.")
    void create() {
        // given
        final MenuGroupRequest request = new MenuGroupRequest("메뉴 그룹");

        // when
        final MenuGroupResponse actual = menuGroupService.create(request);

        // then
        final Long actualId = actual.getId();
        assertAll(
                () -> assertThat(actualId).isNotNull(),
                () -> assertThat(menuGroupDao.findById(actualId)).isPresent()
        );
    }

    @Test
    @DisplayName("메뉴그룹 목록을 조회한다.")
    void list() {
        // given
        메뉴그룹등록(메뉴그룹A);
        메뉴그룹등록(메뉴그룹B);

        // when
        final List<MenuGroupResponse> actual = menuGroupService.list();

        // then
        assertThat(actual).hasSize(2);
    }
}
