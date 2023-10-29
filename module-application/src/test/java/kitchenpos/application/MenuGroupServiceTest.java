package kitchenpos.application;

import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.dto.MenuGroupCreateRequest;
import kitchenpos.menu.application.dto.MenuGroupResponse;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kitchenpos.fixture.MenuGroupFixtures.TEST_GROUP;

@Transactional
@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    @DisplayName("메뉴그룹 등록시 등록된 메뉴 그룹 정보가 반환된다.")
    void createMenuGroup() {
        // given
        final String testGroupName = TEST_GROUP().getName();
        final MenuGroupCreateRequest request = new MenuGroupCreateRequest(testGroupName);

        // when
        final MenuGroupResponse response = menuGroupService.create(request);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.getId()).isNotNull();
            softly.assertThat(response.getName()).isEqualTo(testGroupName);
        });
    }

    @Test
    @DisplayName("등록된 메뉴그룹들의 리스트를 조회한다.")
    void getListOfMenuGroup() {
        // given
        final MenuGroup savedGroup = menuGroupRepository.save(TEST_GROUP());
        final MenuGroupResponse expectedLastGroup = MenuGroupResponse.from(savedGroup);

        // when
        final List<MenuGroupResponse> response = menuGroupService.list();

        // then
        final MenuGroupResponse actualLastGroup = response.get(response.size() - 1);
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response).isNotEmpty();
            softly.assertThat(actualLastGroup).usingRecursiveComparison()
                    .isEqualTo(expectedLastGroup);
        });
    }
}
