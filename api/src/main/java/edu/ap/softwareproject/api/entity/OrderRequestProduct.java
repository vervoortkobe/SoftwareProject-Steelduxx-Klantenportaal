package edu.ap.softwareproject.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * An individual product inside of an order request.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orderproducts")
public class OrderRequestProduct {

    // ---- GETTERS AND SETTERS ----
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;
  @Column
  private String hsCode;
  @Column
  private String name;
  @Column
  private int quantity;
  @Column
  private int weight;
  @Column
  private String containerNumber;
  @Column
  private String containerSize;
  @Column
  private String containerType;

  public OrderRequestProduct(String hsCode, String name, int quantity, int weight, String containerNumber, String containerSize, String containerType) {
    this.hsCode = hsCode;
    this.name = name;
    this.quantity = quantity;
    this.weight = weight;
    this.containerNumber = containerNumber;
    this.containerSize = containerSize;
    this.containerType = containerType;
  }
}