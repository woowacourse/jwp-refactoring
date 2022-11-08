package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;
import kitchenpos.tablegroup.application.dto.TableGroupRequest;
import kitchenpos.tablegroup.application.dto.TableIdRequest;
import kitchenpos.application.support.IntegrationTest;
import kitchenpos.menugroup.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import kitchenpos.product.domain.repository.ProductRepository;
import kitchenpos.tablegroup.application.TableGroupService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class TableGroupServiceTest {

    @Autowired
    private TableGroupService sut;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ProductRepository productRepository;

    @Nested
    @DisplayName("단체 지정")
    class TableGroupTest {

        @DisplayName("단체로 지정할 테이블이 비어있으면 단체로 지정할 수 없다.")
        @Test
        void groupTableWithOrderTable() {
            final TableGroupRequest request = new TableGroupRequest(Collections.emptyList());

            assertThatThrownBy(() -> sut.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("두 개 미만의 테이블은 단체로 지정할 수 없다.")
        @Test
        void groupTableLessThanTwo() {
            final TableIdRequest tableIdRequest = new TableIdRequest(1L);
            final TableGroupRequest tableGroup = new TableGroupRequest((List.of(tableIdRequest)));

            assertThatThrownBy(() -> sut.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("단체 지정 해제")
    class clearGroupTableTest {

//        @DisplayName("계산 완료되지 않은 테이블이 존재하는 경우 단체 지정을 해제할 수 없다.")
//        @Test
//        void clearGroupTableWithNotCompletionTable() {
//            final OrderTable emptyTable1 = orderTableRepository.save(테이블_생성(빈_테이블1.getNumberOfGuests(), 빈_테이블1.isEmpty()));
//            final OrderTable emptyTable2 = orderTableRepository.save(테이블_생성(빈_테이블2.getNumberOfGuests(), 빈_테이블2.isEmpty()));
//            final TableIdRequest tableIdRequest1 = new TableIdRequest(emptyTable1.getId());
//            final TableIdRequest tableIdRequest2 = new TableIdRequest(emptyTable2.getId());
//            final TableGroup tableGroup = sut.create(new TableGroupRequest(List.of(tableIdRequest1, tableIdRequest2)));
//
//            final Product product = productRepository.getOne(후라이드_상품.getId());
//            final MenuProduct menuProduct = new MenuProduct(product, 5L);
//            final MenuGroup menuGroup = menuGroupRepository.getOne(한마리메뉴_그룹.getId());
//            final Menu menu = menuRepository.save(new Menu("한마리메뉴", BigDecimal.TEN, menuGroup, List.of(menuProduct)));
//            final OrderLineItem orderLineItem = 주문_항목_생성(menu.getId(), 1);
//            orderRepository.save(주문_생성(emptyTable1.getId(), OrderStatus.COOKING.name(), List.of(orderLineItem)));
//
//            assertThatThrownBy(() -> sut.ungroup(tableGroup.getId()))
//                    .isInstanceOf(IllegalArgumentException.class);
//        }
    }
}
