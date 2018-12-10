package be.chencorp.shop.controller;

import be.chencorp.shop.resource.OrderItemResource;
import be.chencorp.shop.resource.OrderResource;
import be.chencorp.shop.resource.ProductResource;
import be.chencorp.shop.service.OrderService;
import be.chencorp.shop.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.preprocess.Preprocessors;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerIntegrationTest extends AbstractControllerIT{

    final private String ORDER_MAIL = "nono@popo.com";
    final private int ORDER_ID = 32;

    final private int PRODUCT_NUMBER_1 = 20;
    final private int PRODUCT_NUMBER_2 = 15;

    final private String PRODUCT_NAME = "product1";
    final private int PRODUCT_ID = 1;
    final private double PRODUCT_PRICE = 10.52;

    final private String PRODUCT_NAME_2 = "product2";
    final private int PRODUCT_ID_2 = 2;
    final private double PRODUCT_PRICE_2 = 25.15;

    @Autowired
    OrderService orderService;

    @Autowired
    ProductService productService;

    @Test
    public void createOrder() throws Exception {
        ProductResource product_1 = createProduct(null, PRODUCT_NAME, PRODUCT_PRICE);
        ProductResource product_2 = createProduct(null, PRODUCT_NAME_2, PRODUCT_PRICE_2);
        product_1 = productService.create(product_1);
        product_2 = productService.create(product_2);

        OrderItemResource orderItemResource_1 = createOrderItem(PRODUCT_NUMBER_1, product_1);
        OrderItemResource orderItemResource_2 = createOrderItem(PRODUCT_NUMBER_2, product_2);
        OrderResource orderResource = createOrder(null, ORDER_MAIL);
        orderResource.setOrderItems(Lists.newArrayList(orderItemResource_1, orderItemResource_2));
        this.mockMvc.perform(
                post("/order_user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(orderResource)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.any(Integer.class)))
                .andExpect(jsonPath("$.mail", is(ORDER_MAIL)))
                .andExpect(jsonPath("$.orderItems").exists())
                .andExpect(jsonPath("$.orderItems[0].product").exists())
                .andExpect(jsonPath("$.orderItems[0].number", is(PRODUCT_NUMBER_1)))
                .andExpect(jsonPath("$.orderItems[1].product").exists())
                .andExpect(jsonPath("$.orderItems[1].number", is(PRODUCT_NUMBER_2)))
                .andExpect(jsonPath("$.orderItems").exists())
                .andDo(document("create_order", Preprocessors.preprocessResponse(Preprocessors.prettyPrint())));
    }

    @Test
    public void listProduct() throws Exception {
        ProductResource product_1 = createProduct(PRODUCT_ID, PRODUCT_NAME, PRODUCT_PRICE);
        ProductResource product_2 = createProduct(PRODUCT_ID_2, PRODUCT_NAME_2, PRODUCT_PRICE_2);
        product_1 = productService.create(product_1);
        product_2 = productService.create(product_2);
        OrderItemResource orderItemResource_1 = createOrderItem(PRODUCT_NUMBER_1, product_1);
        OrderItemResource orderItemResource_2 = createOrderItem(PRODUCT_NUMBER_2, product_2);
        OrderResource orderResource = createOrder(null, ORDER_MAIL);
        orderResource.setOrderItems(Lists.newArrayList(orderItemResource_1, orderItemResource_2));
        orderService.create(orderResource);
        this.mockMvc.perform(
                get("/order_user/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Matchers.any(Integer.class)))
                .andExpect(jsonPath("$[0].mail", is(ORDER_MAIL)))
                .andExpect(jsonPath("$[0].orderItems").exists())
                .andExpect(jsonPath("$[0].orderItems[0].product").exists())
                .andExpect(jsonPath("$[0].orderItems[0].number", is(PRODUCT_NUMBER_1)))
                .andExpect(jsonPath("$[0].orderItems[1].product").exists())
                .andExpect(jsonPath("$[0].orderItems[1].number", is(PRODUCT_NUMBER_2)))
                .andExpect(jsonPath("$[0].orderItems").exists())
                .andDo(document("list_order", Preprocessors.preprocessResponse(Preprocessors.prettyPrint())));
    }



    @Test
    public void listProductByDate() throws Exception {
        ProductResource product_1 = createProduct(PRODUCT_ID, PRODUCT_NAME, PRODUCT_PRICE);
        ProductResource product_2 = createProduct(PRODUCT_ID_2, PRODUCT_NAME_2, PRODUCT_PRICE_2);
        product_1 = productService.create(product_1);
        product_2 = productService.create(product_2);
        OrderItemResource orderItemResource_1 = createOrderItem(PRODUCT_NUMBER_1, product_1);
        OrderItemResource orderItemResource_2 = createOrderItem(PRODUCT_NUMBER_2, product_2);
        OrderResource orderResource = createOrder(null, ORDER_MAIL);
        orderResource.setOrderItems(Lists.newArrayList(orderItemResource_1, orderItemResource_2));
        orderService.create(orderResource);

        Date yesterday = addDays(new Date(), -1);
        Date tomorrow = addDays(new Date(), 1);

        this.mockMvc.perform(
                get("/order_user/from/"+getFormat(yesterday)+"/to/"+getFormat(tomorrow))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Matchers.any(Integer.class)))
                .andExpect(jsonPath("$[0].mail", is(ORDER_MAIL)))
                .andExpect(jsonPath("$[0].orderItems").exists())
                .andExpect(jsonPath("$[0].orderItems[0].product").exists())
                .andExpect(jsonPath("$[0].orderItems[0].number", is(PRODUCT_NUMBER_1)))
                .andExpect(jsonPath("$[0].orderItems[1].product").exists())
                .andExpect(jsonPath("$[0].orderItems[1].number", is(PRODUCT_NUMBER_2)))
                .andExpect(jsonPath("$[0].orderItems").exists())
                .andDo(document("list_order_between", Preprocessors.preprocessResponse(Preprocessors.prettyPrint())));
    }


    private OrderItemResource createOrderItem(Integer number, ProductResource productResource){
        return OrderItemResource.builder()
                .number(number)
                .product(productResource)
                .build();
    }

    private OrderResource createOrder(Integer id, String mail){
        return OrderResource.builder()
                .id(id)
                .mail(mail)
                .build();
    }

    public static Date addDays(Date date, Integer days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }

    public String getFormat(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
}
