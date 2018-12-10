package be.chencorp.shop.converter;

import be.chencorp.shop.ExceptionMatcher;
import be.chencorp.shop.exception.BadRequestException;
import be.chencorp.shop.exception.ResourceNotFoundException;
import be.chencorp.shop.model.Product;
import be.chencorp.shop.resource.ProductResource;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class ProductConverterTest {

    final private String PRODUCT_NAME = "product1";
    final private int PRODUCT_ID = 25;
    final private double PRODUCT_PRICE = 10.52;

    @Spy
    ProductConverter productConverter;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private ProductResource fixtureTest(int id, String name, Double price){
        return ProductResource.builder()
                .id(id)
                .name(name)
                .price(price)
                .build();
    }

    @Test
    public void convertProductTest(){
        ProductResource productResource = fixtureTest(PRODUCT_ID, PRODUCT_NAME, PRODUCT_PRICE);
        Product product = productConverter.convert(productResource);
        Assertions.assertThat(product.getId()).isEqualTo(PRODUCT_ID);
        Assertions.assertThat(product.getName()).isEqualTo(PRODUCT_NAME);
        Assertions.assertThat(product.getPrice()).isEqualTo(PRODUCT_PRICE);
    }

    public void expectation(String expectedErrorCode, ProductResource productResource){
        exception.expect(BadRequestException.class);
        exception.expect(ExceptionMatcher.hasCode(expectedErrorCode));
        productConverter.convert(productResource);
    }

    @Test
    public void noNameFilled(){
        ProductResource productResource = fixtureTest(PRODUCT_ID, null, PRODUCT_PRICE);
        expectation("MISSING_NAME", productResource);
    }

    @Test
    public void noPriceFilled(){
        ProductResource productResource = fixtureTest(PRODUCT_ID, PRODUCT_NAME, null);
        expectation("MISSING_PRICE", productResource);
    }
}
