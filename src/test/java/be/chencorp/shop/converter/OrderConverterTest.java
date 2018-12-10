package be.chencorp.shop.converter;


import be.chencorp.shop.ExceptionMatcher;
import be.chencorp.shop.exception.BadRequestException;
import be.chencorp.shop.model.OrderItem;
import be.chencorp.shop.model.OrderUser;
import be.chencorp.shop.model.Product;
import be.chencorp.shop.resource.OrderItemResource;
import be.chencorp.shop.resource.OrderResource;
import be.chencorp.shop.resource.ProductResource;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderConverterTest {

    final private String ORDER_MAIL = "nono@popo.com";
    final private int ORDER_ID = 32;

    final private int PRODUCT_NUMBER_1 = 20;
    final private int PRODUCT_NUMBER_2 = 15;

    @Spy
    @InjectMocks
    OrderConverter orderConverter;

    @Mock
    ProductConverter productConverter;

    @Mock
    ProductResource product_1;

    @Mock
    ProductResource product_2;

    @Mock
    Product product;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void init(){
        when(productConverter.convert(Mockito.any(ProductResource.class)))
                .thenReturn(product);
    }

    @Test
    public void convertOrderTest(){
        OrderItemResource orderItemResource_1 = createOrderItem(PRODUCT_NUMBER_1, product_1);
        OrderItemResource orderItemResource_2 = createOrderItem(PRODUCT_NUMBER_2, product_2);
        OrderResource orderResource = createOrder(ORDER_ID, ORDER_MAIL);
        orderResource.setOrderItems(Lists.newArrayList(orderItemResource_1, orderItemResource_2));

        OrderUser orderUser = orderConverter.convert(orderResource);
        Assertions.assertThat(orderUser.getId()).isEqualTo(ORDER_ID);
        Assertions.assertThat(orderUser.getMail()).isEqualTo(ORDER_MAIL);
        Assertions.assertThat(orderUser.getOrderItems().get(0).getNumber()).isEqualTo(PRODUCT_NUMBER_1);
        Assertions.assertThat(orderUser.getOrderItems().get(1).getNumber()).isEqualTo(PRODUCT_NUMBER_2);
        Mockito.verify(productConverter).convert(product_1);
        Mockito.verify(productConverter).convert(product_2);
    }

    private OrderItemResource createOrderItem(Integer number, ProductResource productResource){
        return OrderItemResource.builder()
                .number(number)
                .product(productResource)
                .build();
    }

    private OrderResource createOrder(int id, String mail){
        return OrderResource.builder()
                .id(id)
                .mail(mail)
                .build();
    }


    public void expectation(String expectedErrorCode, OrderResource orderResource){
        exception.expect(BadRequestException.class);
        exception.expect(ExceptionMatcher.hasCode(expectedErrorCode));
        orderConverter.convert(orderResource);
    }

    @Test
    public void noMailFilled(){
        OrderResource orderResource = createOrder(ORDER_ID, "");
        expectation("MISSING_MAIL", orderResource);
    }

    @Test
    public void noOrderItem(){
        OrderResource orderResource = createOrder(ORDER_ID, ORDER_MAIL);
        expectation("MISSING_ORDER_ITEMS", orderResource);
    }
    @Test
    public void noNumberOfItemFilled(){
        OrderResource orderResource = createOrder(ORDER_ID, ORDER_MAIL);
        OrderItemResource orderItemResource_1 = createOrderItem(null, product_1);
        OrderItemResource orderItemResource_2 = createOrderItem(PRODUCT_NUMBER_2, product_2);
        orderResource.setOrderItems(Lists.newArrayList(orderItemResource_1, orderItemResource_2));
        expectation("MISSING_NUMBER_ITEM", orderResource);
    }
    @Test
    public void noProductIdFilled(){
        OrderResource orderResource = createOrder(ORDER_ID, ORDER_MAIL);
        when(product_1.getId()).thenReturn(null);
        OrderItemResource orderItemResource_1 = createOrderItem(PRODUCT_NUMBER_1, product_1);
        OrderItemResource orderItemResource_2 = createOrderItem(PRODUCT_NUMBER_2, product_2);
        orderResource.setOrderItems(Lists.newArrayList(orderItemResource_1, orderItemResource_2));
        expectation("MISSING_PRODUCT_ID", orderResource);
    }
}
