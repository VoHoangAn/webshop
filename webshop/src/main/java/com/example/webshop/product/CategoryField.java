/*
package com.example.webshop.category;

import com.example.webshop.util.StringToListConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;

//@Data
@Entity
@Getter
@Setter
//@Builder
@NoArgsConstructor
//@AllArgsConstructor
public class CategoryField {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String name;
//    @ElementCollection
    @Convert(converter= StringToListConverter.class)
    private List<String> value;
    private String measure;
    private boolean isFilterable = false;
    @JsonIgnore
    private boolean isAvailable = true;
    @CreationTimestamp
    @JsonIgnore
    private Timestamp timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
//    @ToString.Exclude
    @JsonIgnore//Properties({"name", "image", "categoryFields" })
//    @JoinColumn(name = "category_id")
    private Category category;

    @Override
    public String toString() {
        return "{id:"+id+",name:"+name+",value:"+value.toString()+",measure:"+measure+",filterable:"+isFilterable+"}";
    }
}
*/
