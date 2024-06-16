package edu.ap.softwareproject.api.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsProductDTO {
    private String id;
    private String hsCode;
    private String name;
    private int quantity;
    private int weight;
    private String containerNumber;
    private String containerSize;
    private String containerType;
}
