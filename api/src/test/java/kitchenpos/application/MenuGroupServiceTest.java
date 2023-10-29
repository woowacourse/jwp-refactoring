package kitchenpos.application;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.menu.MenuGroupRepository;
import kitchenpos.menu.application.dto.MenuGroupCreateRequest;
import kitchenpos.menu.application.dto.MenuGroupResponse;
import kitchenpos.menu.application.MenuGroupService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MenuGroupServiceTest {

    private MenuGroupService menuGroupService;
    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupRepository);
    }

    @Test
    void 메뉴_그룹_생성할_수_있다() {
        MenuGroupCreateRequest request = new MenuGroupCreateRequest("분식");
        MenuGroupResponse menuGroupResponse = menuGroupService.create(request);

        Assertions.assertThat(menuGroupRepository.findById(menuGroupResponse.getId())).isPresent();
    }

    @Test
    void 전체_메뉴_그룹_조회할_수_있다() {
        MenuGroupCreateRequest request = new MenuGroupCreateRequest("분식");
        MenuGroupResponse menuGroupResponse = menuGroupService.create(request);
        List<MenuGroupResponse> responses = menuGroupService.list();

        Assertions.assertThat(responses.stream()
                        .filter(response -> Objects.equals(response.getName(), menuGroupResponse.getName()))
                        .collect(Collectors.toList()))
                .hasSize(1);
    }
}
