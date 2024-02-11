package com.bookstore.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bookstore.demo.model.Book;
import com.bookstore.demo.model.Comment;
import com.bookstore.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.book.id = :bookId")
    List<Comment> findAllBookComments(@Param("bookId") Long bookId);

    @Query("SELECT c FROM Comment c WHERE c.user.id = :userId")
    List<Comment> findAllUserComments(@Param("userId") Long userId);

    @Query("SELECT c FROM Comment c WHERE c.user.id = :userId AND c.book.id = :bookId")
    List<Comment> findAllUserCommentsBook(@Param("userId") Long userId, @Param("bookId") Long bookId);

    @Query("SELECT c FROM Comment c WHERE c.parentComment = :parentComment")
    List<Comment> findAllRepliesToParent(Comment parentComment);

    @Query("SELECT c FROM Comment c WHERE c.id = :commentId")
    Optional<Comment> findById(@Param("commentId") Long commentId);

    @Query("SELECT c.user FROM Comment c WHERE c.id = :commentId")
    Optional<User> findUserByCommentId(@Param("commentId") Long commentId);
    
    @Query("SELECT c.book FROM Comment c WHERE c.id = :commentId")
    Optional<Book> findBookByComment(@Param("commentId") Long commentId);
    
    @Query("SELECT COUNT(u) FROM Comment c JOIN c.likedByUsers u WHERE c.id = :commentId")
    Long countLikesByCommentId(@Param("commentId") Long commentId);

    @Query("SELECT u FROM Comment c JOIN c.likedByUsers u WHERE c.id = :commentId")
    List<User> findUsersWhoLikedComment(@Param("commentId") Long commentId);
    
    @Query("SELECT u.id FROM Comment c JOIN c.likedByUsers u WHERE c.id = :commentId")
    List<Long> findUserIdsWhoLikedComment(@Param("commentId") Long commentId);
    
    @Query("SELECT COUNT(u) > 0 FROM Comment c JOIN c.likedByUsers u WHERE c.id = :commentId AND u.id = :userId")
    boolean existsUserLikeByCommentId(@Param("commentId") Long commentId, @Param("userId") Long userId);
    
    List<User> findAllByIdIn(List<Long> userIds);
}
