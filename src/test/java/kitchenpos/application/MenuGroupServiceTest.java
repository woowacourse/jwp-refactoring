package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.MenuGroupFixture.createMenuGroup;
import static kitchenpos.fixture.MenuGroupFixture.createMenuGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql("classpath:db/test/truncate.sql")
@ActiveProfiles("test")
@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void create() {
        MenuGroupRequest menuGroupRequest = createMenuGroupRequest();
        MenuGroupResponse savedMenuGroup = menuGroupService.create(menuGroupRequest);
        assertAll(
                () -> assertThat(savedMenuGroup).isNotNull(),
                () -> assertThat(savedMenuGroup.getId()).isNotNull(),
                () -> assertThat(savedMenuGroup.getName()).isEqualTo(menuGroupRequest.getName())
        );
    }

    @DisplayName("메뉴 그룹 목록을 반환한다.")
    @Test
    void list() {
        List<MenuGroup> savedMenuGroups = Arrays.asList(createMenuGroup(), createMenuGroup());
        menuGroupRepository.saveAll(savedMenuGroups);
        List<MenuGroupResponse> list = menuGroupService.list();
        assertAll(
                () -> assertThat(list).hasSize(savedMenuGroups.size()),
                () -> assertThat(MenuGroupResponse.listOf(savedMenuGroups)).usingRecursiveComparison().isEqualTo(list)
        );
    }
}
