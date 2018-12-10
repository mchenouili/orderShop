package be.chencorp.shop.assembler;

import be.chencorp.shop.model.Product;
import be.chencorp.shop.resource.OrderItemResource;
import be.chencorp.shop.resource.ProductResource;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.stereotype.Component;

@RunWith(MockitoJUnitRunner.class)
public class ProductResourceAssemblerTest {

    final private String PRODUCT_NAME = "product1";
    final private int PRODUCT_ID = 25;
    final private double PRODUCT_PRICE = 10.52;

    @Spy
    ProductResourceAssembler productResourceAssembler;

    private Product createProduct(int id, String name, double price){
        return Product.builder()
                .id(id)
                .name(name)
                .price(price)
                .build();
    }
    @Test
    public void convertTest(){
        Product product = createProduct(PRODUCT_ID, PRODUCT_NAME, PRODUCT_PRICE);
        ProductResource productResource = productResourceAssembler.convert(product);

        Assertions.assertThat(productResource.getId()).isEqualTo(PRODUCT_ID);
        Assertions.assertThat(productResource.getName()).isEqualTo(PRODUCT_NAME);
        Assertions.assertThat(productResource.getPrice()).isEqualTo(PRODUCT_PRICE);
    }

}
