package com.example.cosmocatsmarket.dto;

import com.example.cosmocatsmarket.validation.CosmoCatAnnotations;
// import jakarta.validation.constraints.NotNull;
// import jakarta.validation.constraints.Min;
// import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {
    private String categoryName;
    private String categoryId;
}