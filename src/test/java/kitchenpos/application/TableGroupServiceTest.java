package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.dto.tablegroup.TableGroupRequest;
import kitchenpos.application.dto.tablegroup.TableGroupResponse;
import kitchenpos.application.dto.tablegroup.TableOfGroupDto;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.support.DataDependentIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends DataDependentIntegrationTest {

    private static final long NOT_EXIST_ID = Long.MAX_VALUE;

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

    @Autowired
    private TableGroupService tableGroupService;

    @DisplayName("테이블 그룹을 생성, 저장한다.")
    @Test
    void create_success() {
        // given
        final OrderTable orderTable1 = new OrderTable(3, true);
        final OrderTable orderTable2 = new OrderTable(3, true);
        orderTableRepository.saveAll(List.of(orderTable1, orderTable2));

        final TableGroupRequest tableGroupRequest = new TableGroupRequest(List.of(TableOfGroupDto.from(orderTable1), TableOfGroupDto.from(orderTable2)));

        // when
        final TableGroupResponse savedTableGroup = tableGroupService.create(tableGroupRequest);

        // then
        assertThat(savedTableGroup.getId()).isNotNull();
        assertThat(savedTableGroup.getOrderTables()).usingRecursiveComparison()
            .isEqualTo(List.of(TableOfGroupDto.from(orderTable1), TableOfGroupDto.from(orderTable2)));
    }

    @DisplayName("테이블 그룹을 생성할 때, 묶을 테이블이 없으면 예외가 발생한다.")
    @Test
    void create_empty_fail() {
        // given
        final TableGroupRequest request = new TableGroupRequest(Collections.emptyList());

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 생성할 때, 테이블이 2개 보다 적다면 예외가 발생한다.")
    @Test
    void create_wrongSize_fail() {
        // given
        final OrderTable orderTable1 = new OrderTable(3, true);
        orderTableRepository.save(orderTable1);

        final TableGroupRequest request = new TableGroupRequest(List.of(TableOfGroupDto.from(orderTable1)));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 생성할 때, 존재하지 않는 테이블이 있다면 예외가 발생한다.")
    @Test
    void create_notExistOrderTable_fail() {
        // given
        final OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(3, true));
        final TableOfGroupDto notSavedOrderTableRequest = new TableOfGroupDto(NOT_EXIST_ID);
        final TableGroupRequest request = new TableGroupRequest(List.of(TableOfGroupDto.from(savedOrderTable), notSavedOrderTableRequest));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 생성할 때, 비어있지 않은 테이블이 있다면 예외가 발생한다.")
    @Test
    void create_emptyTable_fail() {
        // given
        final OrderTable orderTable1 = orderTableRepository.save(new OrderTable(3, true));
        final OrderTable orderTable2 = orderTableRepository.save(new OrderTable(3, false));

        final TableGroupRequest request = new TableGroupRequest(List.of(TableOfGroupDto.from(orderTable1), TableOfGroupDto.from(orderTable2)));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 생성할 때, 이미 다른 그룹에 속한 테이블이 있다면 예외가 발생한다.")
    @Test
    void create_alreadyInOtherGroup_fail() {
        // given
        final OrderTable orderTable1 = orderTableRepository.save(new OrderTable(3, true));
        final OrderTable orderTable2 = orderTableRepository.save(new OrderTable(3, true));
        final TableGroupRequest firstRequest = new TableGroupRequest(List.of(TableOfGroupDto.from(orderTable1), TableOfGroupDto.from(orderTable2)));
        tableGroupService.create(firstRequest);

        // when, then
        final OrderTable orderTable3 = orderTableRepository.save(new OrderTable(3, true));
        final TableGroupRequest request = new TableGroupRequest(List.of(TableOfGroupDto.from(orderTable1), TableOfGroupDto.from(orderTable3)));

        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹을 해제한다.")
    @Test
    void ungroup_success() {
        // given
        final OrderTable orderTable1 = orderTableRepository.save(new OrderTable(3, true));
        final OrderTable orderTable2 = orderTableRepository.save(new OrderTable(4, true));
        final TableGroupRequest firstRequest = new TableGroupRequest(List.of(TableOfGroupDto.from(orderTable1), TableOfGroupDto.from(orderTable2)));
        final TableGroupResponse tableGroupResponse = tableGroupService.create(firstRequest);

        // when, then
        assertDoesNotThrow(() -> tableGroupService.ungroup(tableGroupResponse.getId()));
    }

    @DisplayName("그룹에 속한 테이블 중, 주문 상태가 COMPLETION 이 아닌 테이블이 있다면 그룹 해제 시 예외가 발생한다.")
    @Test
    void ungroup_wrongStatus_fail() {
        // given
        final OrderTable orderTable1 = orderTableRepository.save(new OrderTable(3, true));
        final OrderTable orderTable2 = orderTableRepository.save(new OrderTable(2, true));
        final TableGroupRequest request = new TableGroupRequest(List.of(TableOfGroupDto.from(orderTable1), TableOfGroupDto.from(orderTable2)));
        final TableGroupResponse tableGroupResponse = tableGroupService.create(request);
        final Long tableGroupId = tableGroupResponse.getId();

        orderRepository.save(new Order(orderTable1.getId(), OrderStatus.COOKING, LocalDateTime.now(), List.of(new OrderLineItem(createMenuAndGetId(), 1))));

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private long createMenuAndGetId() {
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("menuGroup"));
        final Product product = productRepository.save(new Product("product", Price.from(BigDecimal.valueOf(1000L))));
        final List<MenuProduct> menuProducts = List.of(new MenuProduct(product.getId(), 1));
        final Menu menu = Menu.of("menu", Price.from(BigDecimal.valueOf(1000L)), menuGroup.getId(), menuProducts);
        menuRepository.save(menu);

        return menu.getId();
    }
}
