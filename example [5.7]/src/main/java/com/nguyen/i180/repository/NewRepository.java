package com.nguyen.i180.repository;

import com.nguyen.i180.entity.New;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewRepository extends JpaRepository<New, Integer> {
}
