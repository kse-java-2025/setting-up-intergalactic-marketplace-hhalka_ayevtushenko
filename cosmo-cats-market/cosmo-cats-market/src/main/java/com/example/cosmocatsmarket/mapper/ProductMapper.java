package com.example.cosmocatsmarket.mapper;
//
//import com.example.cosmocatsmarket.domain.Product;
//import com.example.cosmocatsmarket.dto.ProductDTO;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//
//import java.util.List;
//import java.util.UUID;
//
//@Mapper(componentModel = "spring")
//public interface ProductMapper {
//
//    ProductDTO toDto(Product product);
//    List<ProductDTO> toDtoList(List<Product> products);
//
////    @Mapping(target = "categories", ignore = true)
//    Product fromDTO(ProductDTO dto);
//    List<Product> toEntityList(List<ProductDTO> dtoList);
//
//    default String toStrFromUuid(UUID id) {
//        return id != null ? id.toString() : null;
//    }
//
//    default UUID toUuidFromStr(String s) {
//        return s != null ? UUID.fromString(s) : null;
//    }
//}




import com.example.cosmocatsmarket.domain.Product;
import com.example.cosmocatsmarket.dto.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "productId", target = "productId")
    ProductDTO toDto(Product product);

    @Mapping(target = "productId", ignore = true)
    Product fromDTO(ProductDTO dto);
}
