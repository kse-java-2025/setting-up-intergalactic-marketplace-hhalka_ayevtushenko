package com.example.cosmocatsmarket.repository.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "cart")
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cart_id_seq")
    @SequenceGenerator(
            name = "cart_id_seq",
            sequenceName = "cart_id_seq",
            allocationSize = 1
    )

    private Long id;
    @Column(name = "num_product", nullable = false)
    private Integer numProduct;

    @Column(name = "tot_price", nullable = false)
    private Double totalPrice;
}
