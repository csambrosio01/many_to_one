package com.example.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.jpa.model.Post;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {



}
