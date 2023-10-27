package kitchenpos.order.application;

import kitchenpos.dto.OrderHistoryResponse;
import kitchenpos.dto.OrderResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.menu.domain.vo.Name;
import kitchenpos.menu.domain.vo.Price;
import kitchenpos.menu.domain.vo.Quantity;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest
class OrderQueryServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;


    @DisplayName("[SUCCESS] 전체 주문 목록을 조회한다.")
    @Test
    void success_findAll() {
        // given
        final Menu savedMenu = createMenu();
        final OrderTable savedOrderTable = orderTableRepository.saveAndFlush(OrderTable.withoutTableGroup(5, true));
        final OrderSheet requestOrderSheet = new OrderSheet(
                savedOrderTable.getId(),
                List.of(
                        new OrderSheet.OrderSheetItem(savedMenu.getId(), 10L)
                )
        );

        // when
        final OrderResponse expected = orderService.create(requestOrderSheet);

        // when
        final List<OrderHistoryResponse> actual = orderService.list();

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual).hasSize(1);
            final OrderHistoryResponse actualOrder = actual.get(0);

            softly.assertThat(actualOrder.getId()).isEqualTo(expected.getId());
            softly.assertThat(actualOrder.getOrderTable())
                    .usingRecursiveComparison()
                    .isEqualTo(expected.getOrderTable());
            softly.assertThat(actualOrder.getOrderStatus()).isEqualTo(expected.getOrderStatus());
            softly.assertThat(actualOrder.getOrderedTime()).isEqualTo(expected.getOrderedTime());
        });
    }

    private Menu createMenu() {
        final Product savedProduct = productRepository.save(new Product(new Name("테스트용 상품명"), Price.from("1000")));
        final MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup(new Name("테스트용 메뉴 그룹명")));
        final Menu menu = Menu.withEmptyMenuProducts(
                new Name("테스트용 메뉴명"),
                Price.from("1000"),
                savedMenuGroup
        );
        menu.addMenuProducts(List.of(
                MenuProduct.withoutMenu(savedProduct, new Quantity(10))
        ));

        return menuRepository.saveAndFlush(menu);
    }
}
