package kitchenpos.application;

import static kitchenpos.domain.fixture.MenuGroupFixture.치킨_세트;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.dto.request.MenuGroupRequest;
import kitchenpos.application.dto.response.MenuGroupResponse;
import kitchenpos.repository.MenuGroupRepository;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
@DisplayName("MenuGroup 서비스 테스트")
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @DisplayName("메뉴 그룹을 등록한다")
    @Test
    void create() {
        final MenuGroupRequest request = new MenuGroupRequest("치킨 세트");

        final MenuGroupResponse response = menuGroupService.create(request);

        assertThat(response.getId()).isNotNull();
    }

    @DisplayName("메뉴 그룹의 목록을 조회한다")
    @Test
    void list() {
        final int numberOfMenuGroup = 5;
        for (int i = 0; i < numberOfMenuGroup; i++) {
            menuGroupRepository.save(치킨_세트());
        }

        final List<MenuGroupResponse> responses = menuGroupService.list();

        assertThat(responses).hasSize(numberOfMenuGroup);
    }
}
