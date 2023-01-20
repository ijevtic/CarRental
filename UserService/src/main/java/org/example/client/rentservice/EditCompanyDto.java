package org.example.client.rentservice;

import javax.validation.constraints.NotBlank;

public class EditCompanyDto {
    @NotBlank
    private Long id;
    private String companyName;
    private String description;

    public EditCompanyDto() {
    }

    public EditCompanyDto(Long id, String companyName, String description) {
        this.id = id;
        this.companyName = companyName;
        this.description = description;
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
