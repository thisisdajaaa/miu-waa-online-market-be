package com.example.minionlinemarket.Repository;

import com.example.minionlinemarket.model.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<MyUser, Integer> {
}
