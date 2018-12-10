package be.chencorp.shop.converter;

import be.chencorp.shop.exception.BadRequestException;
import be.chencorp.shop.model.Product;
import be.chencorp.shop.resource.ProductResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ProductConverter {

    public Product convert(ProductResource product){
        if (StringUtils.isEmpty(product.getName())){
            throw new BadRequestException("MISSING_NAME", "The name field is missing");
        }
        if (product.getPrice() == null){
            throw new BadRequestException("MISSING_PRICE", "The price field is missing");
        }
        return Product.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }
}
