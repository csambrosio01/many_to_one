package com.example.jpa.controller;

import com.example.jpa.exception.ResourceNotFoundException;
import com.example.jpa.model.Comment;
import com.example.jpa.repository.CommentRespository;
import com.example.jpa.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;

@RestController
public class CommentController {

    @Autowired
    private CommentRespository commentRespository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/posts/{postId}/comments")
    public Page<Comment> getAllCommentsByPostId(@PathVariable (value = "postId") Long postId, Pageable pageable) {
        return commentRespository.findByPostId(postId, pageable);
    }

    @PostMapping("/posts/{postId}/comments")
    public Comment creatComment(@PathVariable (value = "postId") Long postId, @Valid@RequestBody Comment comment){
        return postRepository.findById(postId).map(post -> {comment.setPost(post); return commentRespository.save(comment);}).orElseThrow(() -> new ResourceNotFoundException("PostId "+postId+ "not found"));

    }

    @PutMapping("/posts/{postId}/comments/{commentId}")
    public Comment updateComment(@PathVariable (value = "postId") Long postId, @PathVariable (value = "commentId") Long commentId, @Valid @RequestBody Comment commentRequest) {
        if(!postRepository.existsById(postId)){
            throw new ResourceNotFoundException("PostId "+ postId+ " not found");
        }
        return commentRespository.findById(commentId).map(comment -> {comment.setText(commentRequest.getText());return commentRespository.save(comment);}).orElseThrow(() -> new ResourceNotFoundException("CommentId "+ commentId+ " not found"));

    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable (value = "postId") Long postId, @PathVariable (value = "commentId") Long commentId){
        if(!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("PostId "+ postId +" not found");
        }

        return commentRespository.findById(commentId).map(comment -> {commentRespository.delete(comment);return ResponseEntity.ok().build();}).orElseThrow(() -> new ResourceNotFoundException("CommentId " + commentId+" not found"));

    }
}
