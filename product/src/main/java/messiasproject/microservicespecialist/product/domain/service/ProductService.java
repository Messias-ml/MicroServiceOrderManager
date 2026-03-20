package messiasproject.microservicespecialist.product.domain.service;

import lombok.RequiredArgsConstructor;
import messiasproject.microservicespecialist.product.application.model.CodeProduct;
import messiasproject.microservicespecialist.product.application.model.ProductRepresentation;
import messiasproject.microservicespecialist.product.infra.CreateProduct;
import messiasproject.microservicespecialist.product.infra.FindAllProduct;
import messiasproject.microservicespecialist.product.infra.FindProductByUuid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final CreateProduct createProduct;
    private final FindAllProduct findAllProduct;
    private final FindProductByUuid findProductByUuid;

    @CacheEvict(value = "product", allEntries = true)
    public CodeProduct createProduct(ProductRepresentation product) {
        return createProduct.create(product);
    }

    @Cacheable(value = "product", key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort.toString()")
    public Page<ProductRepresentation> findAllProduct(Pageable pageable) {
        return findAllProduct.find(pageable);
    }

    @Cacheable(value = "productByUuid", key = "#uuid")
    public ProductRepresentation findByUuid(String uuid){
        System.out.println("não cacheou uuid");
        return findProductByUuid.find(uuid);
    }
}
