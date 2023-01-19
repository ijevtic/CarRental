package org.example.domain;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(indexes = {@Index(columnList = "typeName", unique = true)})
public class CarType {

    public static final Map<ECarType, Long> typeMap = new HashMap<ECarType,Long>() {{
        put(ECarType.SUV, 1L);
        put(ECarType.SEDAN, 2L);
        put(ECarType.HATCHBACK, 3L);
    }};

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String  typeName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
