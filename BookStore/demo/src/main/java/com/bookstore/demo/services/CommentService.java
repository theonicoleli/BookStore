package com.bookstore.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.demo.model.Comment;
import com.bookstore.demo.model.User;
import com.bookstore.demo.repositories.CommentRepository;

import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
	
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getAllBookComments(Long bookId) {
        return commentRepository.findAllBookComments(bookId);
    }

    public List<Comment> getAllUserComments(Long userId) {
        return commentRepository.findAllUserComments(userId);
    }

    public List<Comment> getAllUserCommentsForBook(Long userId, Long bookId) {
        return commentRepository.findAllUserCommentsBook(userId, bookId);
    }

    public List<Comment> getAllRepliesToParent(Comment parentComment) {
        return commentRepository.findAllRepliesToParent(parentComment);
    }
    
    public Optional<Comment> getCommentById(Long commentId) {
    	return commentRepository.findById(commentId);
    }
    
    public Comment addComment(Comment comment) {
    	return commentRepository.save(comment);
    }
    
    public void deleteComment(Long commentId) {
    	commentRepository.deleteById(commentId);
    }
    
    public Comment updateComment(Long commentId, Comment commentRequest) {
    	Optional<Comment> optionalComment = commentRepository.findById(commentId);
    	
    	if (optionalComment.isPresent()) {
    		Comment existingComment = optionalComment.get();
    		
    		existingComment.setText(commentRequest.getText());
    		existingComment.setUser(commentRequest.getUser());
    		existingComment.setBook(commentRequest.getBook());
    		
    		if (!commentRequest.getParentComment().equals(null)) {
    		    existingComment.setParentComment(commentRequest.getParentComment());
    		}

    		if (!commentRequest.getReplies().equals(null)) {
    		    existingComment.setReplies(commentRequest.getReplies());
    		} else {
    		    existingComment.setReplies(new ArrayList<>());
    		}

    		return commentRepository.save(existingComment);
    		
    	} else {
            throw new EntityNotFoundException("Comentário com ID " + commentId + " não encontrado.");
        }
    }

    public Optional<User> getUserByCommentId(Long commentId) {
        return commentRepository.findUserByCommentId(commentId);
    }

}
