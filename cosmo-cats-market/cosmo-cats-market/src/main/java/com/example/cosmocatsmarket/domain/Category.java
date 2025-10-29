package com.example.cosmocatsmarket.domain;

import lombok.*;
import java.util.UUID;

@Data
public class Category {
    private String categoryName;
    private UUID categoryId;
}
