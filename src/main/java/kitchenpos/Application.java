package kitchenpos;

import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.application.ProductService;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Application.class);
        application.setAdditionalProfiles("dev");
        ConfigurableApplicationContext ctx = application.run(args);
        TestData testData = (TestData) ctx.getBean("testData");

        System.out.println("====== menu그룹과 product 리스트를 생성한다. ======");
        testData.initialSetting();

        System.out.println("====== menu를 생성한다. ======");
        testData.createMenu();

        System.out.println("====== menu 삭제 시 하위 menu_product를 함께 삭제한다 ======");
        testData.removeMenu();
    }
}

@Component
class TestData {
    private final MenuGroupService menuGroupService;
    private final MenuService menuService;
    private final ProductService productService;
    private final MenuRepository menuRepository;

    private MenuGroupResponse menu_group;
    private ProductResponse product1;
    private ProductResponse product2;
    private ProductResponse product3;
    private MenuResponse testMenu;

    public TestData(MenuGroupService menuGroupService, MenuService menuService, ProductService productService, MenuRepository menuRepository) {
        this.menuGroupService = menuGroupService;
        this.menuService = menuService;
        this.productService = productService;
        this.menuRepository = menuRepository;
    }

    public void initialSetting() {
        menu_group = menuGroupService.create(new MenuGroupRequest("MENU_GROUP"));
        product1 = productService.create(new ProductRequest("PRODUCT1", BigDecimal.ONE));
        product2 = productService.create(new ProductRequest("PRODUCT2", BigDecimal.ONE));
        product3 = productService.create(new ProductRequest("PRODUCT2", BigDecimal.ONE));
    }

    public void createMenu() {
        testMenu = menuService.create(new MenuRequest("MENU1", BigDecimal.ONE, menu_group.getId(), Arrays.asList(
                new MenuProductRequest(1L, product1.getId(), 1L),
                new MenuProductRequest(2L, product2.getId(), 1L),
                new MenuProductRequest(3L, product3.getId(), 1L)
        )));
    }

    public void removeMenu() {
        menuRepository.deleteById(testMenu.getId());
    }
}

/*
====== menu를 생성한다. ======
Hibernate: select product0_.id as id1_7_0_, product0_.name as name2_7_0_, product0_.price as price3_7_0_ from product product0_ where product0_.id=?
Hibernate: select product0_.id as id1_7_0_, product0_.name as name2_7_0_, product0_.price as price3_7_0_ from product product0_ where product0_.id=?
Hibernate: select product0_.id as id1_7_0_, product0_.name as name2_7_0_, product0_.price as price3_7_0_ from product product0_ where product0_.id=?
Hibernate: select menugroup0_.id as id1_1_0_, menugroup0_.name as name2_1_0_ from menu_group menugroup0_ where menugroup0_.id=?
Hibernate: insert into menu_product (product_id, quantity, seq, id) values (?, ?, ?, ?)
Hibernate: insert into menu_product (product_id, quantity, seq, id) values (?, ?, ?, ?)
Hibernate: insert into menu_product (product_id, quantity, seq, id) values (?, ?, ?, ?)
Hibernate: insert into menu (menu_group_id, name, price, id) values (?, ?, ?, ?)
Hibernate: update menu_product set menu_id=? where id=?
Hibernate: update menu_product set menu_id=? where id=?
Hibernate: update menu_product set menu_id=? where id=?
====== menu 삭제 시 하위 menu_product를 함께 삭제한다 ======
Hibernate: select menu0_.id as id1_0_0_, menu0_.menu_group_id as menu_gro4_0_0_, menu0_.name as name2_0_0_, menu0_.price as price3_0_0_, menugroup1_.id as id1_1_1_, menugroup1_.name as name2_1_1_ from menu menu0_ left outer join menu_group menugroup1_ on menu0_.menu_group_id=menugroup1_.id where menu0_.id=?
Hibernate: select menuproduc0_.menu_id as menu_id5_2_0_, menuproduc0_.id as id1_2_0_, menuproduc0_.id as id1_2_1_, menuproduc0_.product_id as product_4_2_1_, menuproduc0_.quantity as quantity2_2_1_, menuproduc0_.seq as seq3_2_1_, product1_.id as id1_7_2_, product1_.name as name2_7_2_, product1_.price as price3_7_2_ from menu_product menuproduc0_ left outer join product product1_ on menuproduc0_.product_id=product1_.id where menuproduc0_.menu_id=?
Hibernate: update menu_product set menu_id=null where menu_id=?
Hibernate: delete from menu_product where id=?
Hibernate: delete from menu_product where id=?
Hibernate: delete from menu_product where id=?
Hibernate: delete from menu where id=?
 */
