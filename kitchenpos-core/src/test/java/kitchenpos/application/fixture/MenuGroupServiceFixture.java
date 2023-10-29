package kitchenpos.application.fixture;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.persistence.JpaMenuGroupRepository;
import kitchenpos.common.dto.request.CreateMenuGroupRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class MenuGroupServiceFixture {

    @Autowired
    private JpaMenuGroupRepository menuGroupRepository;

    protected CreateMenuGroupRequest 생성할_메뉴_그룹;
    protected List<MenuGroup> 저장된_메뉴_그룹;
    protected MenuGroup 저장한_메뉴_그룹_1;
    protected MenuGroup 저장한_메뉴_그룹_2;

    protected void 메뉴_그룹을_생성한다_픽스처_생성() {
        생성할_메뉴_그룹 = new CreateMenuGroupRequest("추천 메뉴");
    }

    protected void 메뉴_그룹을_조회한다_픽스처_생성() {
        저장한_메뉴_그룹_1 = new MenuGroup("저장할_메뉴_그룹_1");
        저장한_메뉴_그룹_2 = new MenuGroup("저장할_메뉴_그룹_2");
        저장된_메뉴_그룹 = menuGroupRepository.saveAll(List.of(저장한_메뉴_그룹_1, 저장한_메뉴_그룹_2));
    }
}
