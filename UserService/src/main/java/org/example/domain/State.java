package org.example.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.HashMap;
import java.util.Map;

@Entity
public class State {

    public static final Map<EState,Long> stateMap = new HashMap<EState,Long>() {{
        put(EState.OK, 1L);
        put(EState.NOT_VERIFIED, 2L);
        put(EState.BAN, 3L);
        put(EState.BAN_AND_NOT_VERIFIED, 4L);
    }};
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public EState getNameEnum() {
        return EState.valueOf(name);
    }

    public void setName(String name) {
        this.name = name;
    }
}
