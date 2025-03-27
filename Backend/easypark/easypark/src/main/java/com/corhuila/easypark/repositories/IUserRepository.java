package com.corhuila.easypark.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.corhuila.easypark.models.User;

@Repository
public interface IUserRepository extends JpaRepository<User,Integer>{

}
