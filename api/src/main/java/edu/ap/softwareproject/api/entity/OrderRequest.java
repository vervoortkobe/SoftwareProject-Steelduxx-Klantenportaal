package edu.ap.softwareproject.api.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A temporary order request that an Admin can approve/deny/edit and is then sent to the API.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class OrderRequest {

  // ---- GETTERS AND SETTERS ----
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;
  private String customerCode;
  @Column
  private String transportType;
  @Column(unique = true)
  private String customerReferenceNumber;
  @Column
  private String portCode;
  @Column
  private String cargoType;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  private List<OrderRequestProduct> products;

  public OrderRequest(String customerCode, String transportType, String customerReferenceNumber, String portCode, String cargoType, List<OrderRequestProduct> products) {
    this.customerCode = customerCode;
    this.transportType = transportType;
    this.customerReferenceNumber = customerReferenceNumber;
    this.portCode = portCode;
    this.cargoType = cargoType;
    this.products = products;
  }
}