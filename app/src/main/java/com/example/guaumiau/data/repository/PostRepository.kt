package com.example.guaumiau.data.repository

import com.example.guaumiau.data.local.PostDao
import com.example.guaumiau.data.model.PostEntity

/**
 * Repositorio para manejar las operaciones de publicaciones
 */
class PostRepository(private val postDao: PostDao) {
    
    /**
     * Obtiene todas las publicaciones ordenadas por fecha (más recientes primero)
     */
    suspend fun getAllPosts(): List<PostEntity> {
        return postDao.getAllPosts()
    }
    
    /**
     * Obtiene publicaciones de un usuario específico
     */
    suspend fun getPostsByUser(userEmail: String): List<PostEntity> {
        return postDao.getPostsByUser(userEmail)
    }
    
    /**
     * Obtiene una publicación por ID
     */
    suspend fun getPostById(postId: Int): PostEntity? {
        return postDao.getPostById(postId)
    }
    
    /**
     * Inserta una nueva publicación
     */
    suspend fun insertPost(post: PostEntity): Long {
        return postDao.insertPost(post)
    }
    
    /**
     * Elimina una publicación
     */
    suspend fun deletePost(post: PostEntity) {
        postDao.deletePost(post)
    }
    
    /**
     * Elimina una publicación por ID
     */
    suspend fun deletePostById(postId: Int) {
        postDao.deletePostById(postId)
    }
    
    /**
     * Actualiza los likes de una publicación
     */
    suspend fun updateLikes(postId: Int, likes: Int) {
        postDao.updateLikes(postId, likes)
    }
}
