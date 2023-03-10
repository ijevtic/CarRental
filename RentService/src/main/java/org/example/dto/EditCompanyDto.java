package org.example.dto;

import javax.validation.constraints.NotBlank;

public class EditCompanyDto {
    @NotBlank
    private Long id;
    private String companyName;
    private String description;

    public EditCompanyDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
