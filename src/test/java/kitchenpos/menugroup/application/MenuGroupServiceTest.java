package kitchenpos.menugroup.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.menugroup.exception.InvalidNameException;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.menugroup.ui.request.MenuGroupRequest;
import kitchenpos.menugroup.ui.response.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("MenuGroup Service 테스트")
@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @DisplayName("MenuGroup을 저장할 때")
    @Nested
    class Save {

        @DisplayName("정상적인 MenuGroup은 저장에 성공한다.")
        @Test
        void success() {
            // given
            MenuGroupRequest request = MenuGroupRequest를_생성한다("인기 메뉴");

            // when
            MenuGroupResponse response = menuGroupService.create(request);

            // then
            assertThat(menuGroupRepository.findById(response.getId())).isPresent();
            assertThat(response).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(response);
        }

        @DisplayName("name이 Null인 경우 예외가 발생한다.")
        @Test
        void nameNullException() {
            // given
            MenuGroupRequest request = MenuGroupRequest를_생성한다(null);

            // when, then
            assertThatThrownBy(() -> menuGroupService.create(request))
                .isExactlyInstanceOf(InvalidNameException.class);
        }
    }

    @DisplayName("모든 MenuGroup을 조회한다.")
    @Test
    void findAll() {
        // given
        List<MenuGroupResponse> beforeSavedMenuGroups = menuGroupService.list();

        beforeSavedMenuGroups.add(menuGroupService.create(MenuGroupRequest를_생성한다("인기 메뉴")));
        beforeSavedMenuGroups.add(menuGroupService.create(MenuGroupRequest를_생성한다("추천 메뉴")));
        beforeSavedMenuGroups.add(menuGroupService.create(MenuGroupRequest를_생성한다("주는대로 먹어")));

        // when
        List<MenuGroupResponse> afterSavedMenuGroups = menuGroupService.list();

        // then
        assertThat(afterSavedMenuGroups).hasSize(beforeSavedMenuGroups.size());
        assertThat(afterSavedMenuGroups).usingRecursiveComparison()
            .isEqualTo(beforeSavedMenuGroups);
    }

    private MenuGroupRequest MenuGroupRequest를_생성한다(String name) {
        return new MenuGroupRequest(name);
    }
}
