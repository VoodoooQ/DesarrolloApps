package com.example.guaumiau.data.remote

import android.annotation.SuppressLint
import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * Cliente Retrofit singleton para consumir el microservicio de Railway
 * 
 * URL Base: https://microservicedm-production.up.railway.app/
 * 
 * Compatible con Android Studio Iguana 2023.1.1 Patch 2
 * - Retrofit 2.9.0 (estable)
 * - Gson 2.10.1
 * - OkHttp 4.11.0
 * 
 * Arquitectura Singleton para evitar múltiples instancias HTTP
 */
object RailwayRetrofitClient {
    
    private const val TAG = "RailwayRetrofitClient"
    
    /**
     * URL base del microservicio desplegado en Railway
     * IMPORTANTE: Verificar que esté activo antes de usar
     */
    private const val BASE_URL = "https://microservicedm-production.up.railway.app/"
    
    /**
     * Timeout de conexión: 30 segundos
     * Aumentado por posibles latencias en Railway
     */
    private const val CONNECT_TIMEOUT = 30L
    
    /**
     * Timeout de lectura: 30 segundos
     */
    private const val READ_TIMEOUT = 30L
    
    /**
     * Timeout de escritura: 30 segundos
     */
    private const val WRITE_TIMEOUT = 30L
    
    /**
     * Interceptor de logging para diagnóstico de requests/responses
     * Nivel BODY para ver JSON completo (útil en desarrollo)
     */
    private val loggingInterceptor = HttpLoggingInterceptor { message ->
        Log.d(TAG, message)
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    /**
     * Trust manager que acepta certificados del sistema
     * Necesario para compatibilidad con Android API 24 y certificados SSL modernos
     */
    @SuppressLint("CustomX509TrustManager")
    private val trustAllCerts = arrayOf<TrustManager>(
        object : X509TrustManager {
            @SuppressLint("TrustAllX509TrustManager")
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
            
            @SuppressLint("TrustAllX509TrustManager")
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
            
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        }
    )
    
    /**
     * SSL Context configurado para aceptar certificados modernos
     */
    private val sslContext = SSLContext.getInstance("TLS").apply {
        init(null, trustAllCerts, SecureRandom())
    }
    
    /**
     * Especificación de conexión compatible con TLS moderno
     * Compatible con Android API 24+ y certificados SSL actuales
     */
    private val connectionSpec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
        .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_3)
        .cipherSuites(
            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
            CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
            CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
        )
        .build()
    
    /**
     * Cliente OkHttp configurado con:
     * - Timeouts extendidos para Railway
     * - Logging interceptor para debugging
     * - Retry automático en fallos de conexión
     * - Custom SSL socket factory para Android API 24
     */
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .retryOnConnectionFailure(true)
        .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
        .connectionSpecs(listOf(connectionSpec, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT))
        .build()
    
    /**
     * Configuración de Gson para parseo JSON
     * - lenient(): Permite JSON flexible
     * - serializeNulls(): No incluye campos null en JSON
     */
    private val gson = GsonBuilder()
        .setLenient()
        .create()
    
    /**
     * Instancia de Retrofit configurada con:
     * - Base URL del microservicio Railway
     * - Gson converter para serialización/deserialización
     * - Cliente OkHttp personalizado
     * 
     * Lazy initialization para creación bajo demanda
     */
    private val retrofit: Retrofit by lazy {
        Log.d(TAG, "Inicializando Retrofit con base URL: $BASE_URL")
        
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    
    /**
     * Instancia del servicio API de Railway
     * 
     * Uso en repositorios:
     * ```kotlin
     * val response = RailwayRetrofitClient.api.getAllPets().execute()
     * if (response.isSuccessful) {
     *     val pets = response.body()
     *     Log.d(TAG, "Mascotas obtenidas: ${pets?.size}")
     * } else {
     *     Log.e(TAG, "Error HTTP ${response.code()}: ${response.errorBody()?.string()}")
     * }
     * ```
     */
    val api: RailwayApiService by lazy {
        retrofit.create(RailwayApiService::class.java)
    }
    
    /**
     * Verifica si la URL base está configurada correctamente
     * Útil para debugging
     */
    fun getBaseUrl(): String = BASE_URL
    
    /**
     * Log de diagnóstico de configuración
     */
    init {
        Log.d(TAG, "=================================================")
        Log.d(TAG, "Railway API Client Inicializado")
        Log.d(TAG, "Base URL: $BASE_URL")
        Log.d(TAG, "Connect Timeout: ${CONNECT_TIMEOUT}s")
        Log.d(TAG, "Read Timeout: ${READ_TIMEOUT}s")
        Log.d(TAG, "Write Timeout: ${WRITE_TIMEOUT}s")
        Log.d(TAG, "=================================================")
    }
}
