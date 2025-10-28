package com.example.guaumiau.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.guaumiau.data.model.PostEntity

/**
 * DAO para operaciones de base de datos con publicaciones
 */
@Dao
interface PostDao {
    
    @Query("SELECT * FROM posts ORDER BY timestamp DESC")
    suspend fun getAllPosts(): List<PostEntity>
    
    @Query("SELECT * FROM posts WHERE userEmail = :userEmail ORDER BY timestamp DESC")
    suspend fun getPostsByUser(userEmail: String): List<PostEntity>
    
    @Query("SELECT * FROM posts WHERE id = :postId")
    suspend fun getPostById(postId: Int): PostEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostEntity): Long
    
    @Delete
    suspend fun deletePost(post: PostEntity)
    
    @Query("DELETE FROM posts WHERE id = :postId")
    suspend fun deletePostById(postId: Int)
    
    @Query("UPDATE posts SET likes = :likes WHERE id = :postId")
    suspend fun updateLikes(postId: Int, likes: Int)
}
