package com.formindev.meetroom.repository;

import com.formindev.meetroom.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

}
