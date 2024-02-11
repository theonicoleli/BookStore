package com.bookstore.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.demo.exceptions.CommentException;
import com.bookstore.demo.model.Comment;
import com.bookstore.demo.model.CommentRequest;
import com.bookstore.demo.model.User;
import com.bookstore.demo.repositories.CommentRepository;

import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
	
    private final CommentRepository commentRepository;
    private final UserService userService;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
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
    
    public Long getCommentLikes(Long commentId) {
    	return commentRepository.countLikesByCommentId(commentId);
    }
    
    public List<User> getUserCommentLikes(Long commentId) {
    	return commentRepository.findUsersWhoLikedComment(commentId);
    }
    
    public List<Long> getUserIdsWhoLikedComment(Long commentId) {
    	return commentRepository.findUserIdsWhoLikedComment(commentId);
    }
    
    public boolean hasUserLikedComment(Long userId, Long commentId) {
        return commentRepository.existsUserLikeByCommentId(commentId, userId);
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
    
    public Optional<Comment> addCommentToComment(Long parentId, CommentRequest newComment) {
        Optional<Comment> parentComment = commentRepository.findById(parentId);

        if (parentComment.isPresent()) {
            Comment newCommentToComment = new Comment();
            newCommentToComment.setParentComment(parentComment.get());
            newCommentToComment.setBook(parentComment.get().getBook());
            newCommentToComment.setText(newComment.getText());
            newCommentToComment.setUser(userService.getUserById(newComment.getUserId()));

            if (parentComment.get().getReplies() == null) {
                parentComment.get().setReplies(new ArrayList<>());
            }

            parentComment.get().getReplies().add(newCommentToComment);

            commentRepository.save(parentComment.get());
            Comment savedComment = commentRepository.save(newCommentToComment);
            return Optional.of(savedComment);
        } else {
            throw new CommentException("Parent comment not found");
        }
    }
    
    public Optional<Comment> userCommentLike(Long userId, Long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            List<User> likedByUsers = comment.getLikedByUsers();
            User user = userService.getUserById(userId);
            
            if (likedByUsers == null) {
                likedByUsers = new ArrayList<>();
            }

            boolean alreadyLiked = likedByUsers.stream().anyMatch(userLiked -> userLiked.getId().equals(userId));
            if (!alreadyLiked) {
                likedByUsers.add(user);
                comment.setLikedByUsers(likedByUsers);
                Comment savedComment = commentRepository.save(comment);
                
                return Optional.of(savedComment);
            } else {
                likedByUsers.remove(user);
                comment.setLikedByUsers(likedByUsers);
                Comment savedComment = commentRepository.save(comment);
                
                return Optional.of(savedComment);
            }
        } else {
            throw new CommentException("Comment not found");
        }
    }

    public Optional<User> getUserByCommentId(Long commentId) {
        return commentRepository.findUserByCommentId(commentId);
    }
    
    public void deleteCommentsByUserId(Long userId) {
    	for (Comment comment: commentRepository.findAllUserComments(userId)) {
    		deleteComment(comment.getId());
    	}
    }

}
