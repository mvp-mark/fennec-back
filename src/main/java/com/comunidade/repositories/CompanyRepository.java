package com.comunidade.repositories;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import com.comunidade.domain.Company;

public interface CompanyRepository  extends JpaRepository<Company, Integer>  {
    


    // public void deleteById(Integer id) {
    // }

    // public Company save(Company obj) {
    //     return null;
    // }

    // public Optional<Company> findById(Integer id) {
    //     return null;
    // }
}
