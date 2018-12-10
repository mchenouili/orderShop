package be.chencorp.shop.assembler;

import be.chencorp.shop.model.OrderItem;
import be.chencorp.shop.model.OrderUser;
import be.chencorp.shop.model.Product;
import be.chencorp.shop.resource.OrderResource;
import be.chencorp.shop.resource.ProductResource;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class OrderResourceAssemblerTest {

    final private String ORDER_MAIL = "nono@popo.com";
    final private int ORDER_ID = 32;

    final private int PRODUCT_NUMBER_1 = 2;
    final private double PRODUCT_PRICE_1 = 7.15;
    final private int PRODUCT_NUMBER_2 = 10;
    final private double PRODUCT_PRICE_2 = 3.30;

    @InjectMocks
    OrderResourceAssembler orderResourceAssembler;

    @Mock
    ProductResourceAssembler productResourceAssembler;

    @Mock
    ProductResource productResource;

    @Mock
    OrderUser orderUser;

    @Mock
    Product product;

    public OrderUser createOrderUser(int id, String mail, OrderItem... orderItems ){
        List<OrderItem> orderItemList = Arrays.asList(orderItems);
        return OrderUser.builder()
                .id(id)
                .mail(mail)
                .orderItems(orderItemList)
                .build();
    }

    public OrderItem createOrderItem(int number, double price){
        Product product = Mockito.mock(Product.class);
        Mockito.when(product.getPrice()).thenReturn(price);
        return OrderItem.builder()
                .number(number)
                .product(product)
                .build();
    }

    @Test
    public void convert() {
        OrderUser orderUser = createOrderUser(ORDER_ID, ORDER_MAIL,
                createOrderItem(PRODUCT_NUMBER_1, PRODUCT_PRICE_1),
                createOrderItem(PRODUCT_NUMBER_2, PRODUCT_PRICE_2));
        Mockito.when(productResourceAssembler.convert(Mockito.any(Product.class))).thenReturn(productResource);

        OrderResource orderResource = orderResourceAssembler.convert(orderUser);

        Mockito.verify(productResourceAssembler, new Times(2))
                .convert(Mockito.any(Product.class));

        double totalPrice = PRODUCT_NUMBER_1 * PRODUCT_PRICE_1 + PRODUCT_NUMBER_2* PRODUCT_PRICE_2;
        Assertions.assertThat(orderResource.getId()).isEqualTo(ORDER_ID);
        Assertions.assertThat(orderResource.getMail()).isEqualTo(ORDER_MAIL);
        Assertions.assertThat(orderResource.getTotalPrice()).isEqualTo(totalPrice);
    }
}
