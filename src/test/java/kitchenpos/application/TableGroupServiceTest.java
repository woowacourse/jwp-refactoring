package kitchenpos.application;

import kitchenpos.Product.repository.ProductRepository;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.presentation.dto.TableGroupCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static kitchenpos.TestFixtureFactory.새로운_주문_테이블;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    @Test
    void 테이블들을_단체로_지정한다() {
        OrderTable 주문_테이블1 = orderTableRepository.save(새로운_주문_테이블(null, 3, true));
        OrderTable 주문_테이블2 = orderTableRepository.save(새로운_주문_테이블(null, 2, true));

        TableGroupCreateRequest 단체_지정_생성_요청 = new TableGroupCreateRequest(List.of(주문_테이블1.getId(), 주문_테이블2.getId()));

        TableGroup tableGroup = tableGroupService.create(단체_지정_생성_요청);

        assertSoftly(softly -> {
            softly.assertThat(tableGroup.getId()).isNotNull();

        });
    }

    @Test
    void 이미_지정된_테이블을_단체_지정할_수_없다() {
        OrderTable 주문_테이블1 = orderTableRepository.save(새로운_주문_테이블(null, 3, true));
        OrderTable 주문_테이블2 = orderTableRepository.save(새로운_주문_테이블(null, 2, true));

        TableGroupCreateRequest 단체_지정_생성_요청 = new TableGroupCreateRequest(List.of(주문_테이블1.getId(), 주문_테이블2.getId()));

        TableGroup tableGroup = tableGroupService.create(단체_지정_생성_요청);

        TableGroupCreateRequest 또다른_단체_지정_생성_요청 = new TableGroupCreateRequest(List.of(주문_테이블1.getId(), 주문_테이블2.getId()));
        assertThatThrownBy(() -> tableGroupService.create(또다른_단체_지정_생성_요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정을_삭제한다() {
        OrderTable 주문_테이블1 = orderTableRepository.save(새로운_주문_테이블(null, 3, true));
        OrderTable 주문_테이블2 = orderTableRepository.save(새로운_주문_테이블(null, 2, true));

        TableGroupCreateRequest 단체_지정_생성_요청 = new TableGroupCreateRequest(List.of(주문_테이블1.getId(), 주문_테이블2.getId()));

        TableGroup tableGroup = tableGroupService.create(단체_지정_생성_요청);

        tableGroupService.ungroup(tableGroup.getId());

        assertSoftly(softly -> {
            softly.assertThat(주문_테이블1.getTableGroup()).isNull();
            softly.assertThat(주문_테이블2.getTableGroup()).isNull();
        });
    }
}
