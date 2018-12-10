package be.chencorp.shop.converter;

import be.chencorp.shop.exception.BadRequestException;
import be.chencorp.shop.model.OrderUser;
import be.chencorp.shop.model.OrderItem;
import be.chencorp.shop.model.Product;
import be.chencorp.shop.resource.OrderItemResource;
import be.chencorp.shop.resource.OrderResource;
import be.chencorp.shop.resource.ProductResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderConverter {

    @Autowired
    ProductConverter productConverter;

    public OrderUser convert(OrderResource order){
        if (StringUtils.isEmpty(order.getMail())){
            throw new BadRequestException("MISSING_MAIL", "The mail field was not found");
        }
        OrderUser orderUser = OrderUser.builder()
                .id(order.getId())
                .mail(order.getMail())
                .build();
        List<OrderItem> orderItemModel = new ArrayList<>();
        for (OrderItemResource orderItem : order.getOrderItems()){
            if (orderItem.getNumber() == null){
                throw new BadRequestException("MISSING_NUMBER_ITEM", "The number of Item is missing for orderItem : "+orderItem);
            }
            ProductResource productResource = orderItem.getProduct();
            if (productResource == null || productResource.getId() == null){
                throw new BadRequestException("MISSING_PRODUCT_ID", "productId is missing for orderItem : "+orderItem);
            }
            if (orderItem.getNumber() == 0){
                continue;
            }
            orderItemModel.add(OrderItem.builder()
                    .number(orderItem.getNumber())
                    .product(productConverter.convert(orderItem.getProduct()))
                    .orderUser(orderUser)
                    .build());
        }
        if (orderItemModel.isEmpty()){
            throw new BadRequestException("MISSING_ORDER_ITEMS", "No valid OrderItem were found in this order"+order.getOrderItems());
        }
        orderUser.setOrderItems(orderItemModel);
        return orderUser;
    }
}
