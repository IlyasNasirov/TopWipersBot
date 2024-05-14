package com.example.topwipersbot.repository;

import com.example.topwipersbot.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CityRepository extends JpaRepository<City,Integer> {
    @Query( "SELECT name FROM City" )
    public List<String> findAllName();

}
