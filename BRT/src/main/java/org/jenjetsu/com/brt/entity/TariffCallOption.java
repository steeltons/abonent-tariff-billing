// package org.jenjetsu.com.brt.entity;

// import java.util.UUID;

// import org.hibernate.annotations.Check;

// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.FetchType;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.ManyToOne;
// import jakarta.persistence.Table;
// import jakarta.persistence.UniqueConstraint;
// import lombok.AllArgsConstructor;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;
// import lombok.ToString;

// import static jakarta.persistence.CascadeType.*;
// import jakarta.persistence.FetchType;

// /*
//  * Many to many table that linke Tarfiff and CallOption
//  * Unusual fields description:
//  * - callPriority - priority of CallOption in tariff.
//  *   Tariff can have two equal priorit
//  */
// @Getter @Setter @ToString
// @AllArgsConstructor
// @NoArgsConstructor
// @Entity(name = "tariff_call_option")
// @Table(uniqueConstraints = {
//                 @UniqueConstraint(columnNames = {"tariff_id", "call_option_id"})
//             }
//       )
// public class TariffCallOption {

//     public final static Byte HIGHEST_CALL_PRIORITY = (byte) 0;
//     public final static Byte LOWEST_CALL_PRIORITY = (byte) 10;
    
//     @Id
//     @GeneratedValue(strategy = GenerationType.UUID)
//     @Column(name = "tariff_call_option_id")
//     private UUID id;
//     @Column(name = "call_priority", nullable = false, columnDefinition = "INT2 DEFAULT 0")
//     private Byte callPriority;
//     @Column(name = "are_call_types_linked", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
//     private Boolean areCallTypesLinked;
//     @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {MERGE, DETACH})
//     @JoinColumn(name = "call_option_id", nullable = false)
//     private CallOption callOption;
//     @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {MERGE, DETACH})
//     @JoinColumn(name = "tariff_id", nullable = false)
//     private Tariff tariff;

// }
