package be.chencorp.shop.assembler;

import be.chencorp.shop.model.OrderUser;
import be.chencorp.shop.model.OrderItem;
import be.chencorp.shop.resource.OrderItemResource;
import be.chencorp.shop.resource.OrderResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderResourceAssembler {

    @Autowired
    ProductResourceAssembler productResourceAssembler;

    public OrderResource convert(OrderUser orderUser) {
        List<OrderItemResource> orderItemResources = new ArrayList<>();
        double totalPrice = 0;
        for (OrderItem orderItem : orderUser.getOrderItems()){
            orderItemResources.add(OrderItemResource.builder()
                    .number(orderItem.getNumber())
                    .product(productResourceAssembler.convert(orderItem.getProduct()))
                    .build());
            totalPrice += orderItem.getProduct().getPrice()*orderItem.getNumber();
        }
        return OrderResource.builder()
                .id(orderUser.getId())
                .mail(orderUser.getMail())
                .totalPrice(totalPrice)
                .creation(orderUser.getCreation())
                .orderItems(orderItemResources)
                .build();
    }
}
