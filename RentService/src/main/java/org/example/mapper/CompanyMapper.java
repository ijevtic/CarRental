package org.example.mapper;

import org.example.domain.Company;
import org.example.dto.CreateCompanyDto;
import org.example.dto.EditCompanyDto;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {
    public CompanyMapper() {
    }

    public EditCompanyDto companyToEditCompanyDto(Company company) {
        EditCompanyDto editCompanyDto = new EditCompanyDto();
        editCompanyDto.setId(company.getId());
        editCompanyDto.setCompanyName(company.getCompanyName());
        editCompanyDto.setDescription(company.getDescription());
        return editCompanyDto;
    }

    public Company editCompanyDtoToCompany(EditCompanyDto oldCompany, EditCompanyDto newCompany) {
        Company company = new Company();
        company.setId(oldCompany.getId());
        if(newCompany.getCompanyName() != null) {
            company.setCompanyName(newCompany.getCompanyName());
        } else {
            company.setCompanyName(oldCompany.getCompanyName());
        }
        if(newCompany.getDescription() != null) {
            company.setDescription(newCompany.getDescription());
        } else {
            company.setDescription(oldCompany.getDescription());
        }
        return company;
    }

    public Company createCompanyDtoToCompany(CreateCompanyDto createCompanyDto) {
        Company company = new Company();
        company.setCompanyName(createCompanyDto.getCompanyName());
        company.setDescription(createCompanyDto.getDescription());
        return company;
    }
}
