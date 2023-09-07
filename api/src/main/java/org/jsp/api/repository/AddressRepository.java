package org.jsp.api.repository;

import org.jsp.api.dto.Address;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AddressRepository  extends JpaRepository<Address, Integer>{

}
