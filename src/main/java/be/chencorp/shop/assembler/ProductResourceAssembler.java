package be.chencorp.shop.assembler;

import be.chencorp.shop.model.Product;
import be.chencorp.shop.resource.ProductResource;
import org.springframework.stereotype.Component;

@Component
public class ProductResourceAssembler {

    public ProductResource convert(Product product){
        return ProductResource.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }
}
