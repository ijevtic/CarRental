package org.example.domain;

import javax.persistence.Id;
import javax.persistence.ManyToOne;

public class Review {
    @Id
    private Long id;
    @ManyToOne(optional = false)
    private Company company;
    private String comment;
    private String city;
    //Ocena od 1 do 5
    private int mark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }
}
