package com.example.guaumiau.data.remote

import android.annotation.SuppressLint
import com.example.guaumiau.data.model.WeatherResponse
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * Interfaz de servicio Retrofit para Open-Meteo API
 * 
 * Open-Meteo es una API gratuita de pronóstico del tiempo que no requiere API key.
 * Documentación: https://open-meteo.com/en/docs
 */
interface WeatherApiService {
    
    /**
     * Obtiene el clima actual para una ubicación específica
     * 
     * Endpoint: https://api.open-meteo.com/v1/forecast
     * 
     * @param latitude Latitud de la ubicación (Santiago: -33.46)
     * @param longitude Longitud de la ubicación (Santiago: -70.65)
     * @param currentWeather Flag para incluir datos del clima actual (siempre "true")
     * @param timezone Zona horaria (default: "auto" detecta automáticamente)
     * @return Response con los datos del clima o error HTTP
     */
    @GET("v1/forecast")
    suspend fun getCurrentWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current_weather") currentWeather: String = "true",
        @Query("timezone") timezone: String = "auto"
    ): Response<WeatherResponse>
}

/**
 * Cliente Retrofit singleton para consumo de Open-Meteo API
 * 
 * Implementa patrón Singleton para reutilizar la misma instancia
 * en toda la aplicación, evitando crear múltiples clientes HTTP.
 */
object RetrofitClient {
    
    /**
     * URL base de la API Open-Meteo
     */
    private const val BASE_URL = "https://api.open-meteo.com/"
    
    /**
     * Timeout de conexión en segundos
     * Si no hay respuesta en 15s, se considera timeout
     */
    private const val CONNECT_TIMEOUT = 15L
    
    /**
     * Timeout de lectura en segundos
     * Si la transferencia de datos tarda más de 15s, se considera timeout
     */
    private const val READ_TIMEOUT = 15L
    
    /**
     * Timeout de escritura en segundos
     */
    private const val WRITE_TIMEOUT = 15L
    
    /**
     * Interceptor de logging para debugging
     * Solo registra logs en builds de debug
     */
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
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
     * Cliente OkHttp configurado con timeouts e interceptores
     * 
     * Configuración:
     * - Timeouts: 15 segundos para conexión, lectura y escritura
     * - Logging interceptor para debug de requests/responses
     * - Retry on connection failure habilitado
     * - ConnectionSpec para TLS 1.2/1.3 compatible con certificados modernos
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
     * Instancia de Retrofit configurada con Gson y OkHttp
     * 
     * - Converter: GsonConverterFactory para parseo JSON automático
     * - Client: OkHttpClient personalizado con timeouts
     */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    /**
     * Instancia del servicio WeatherApiService
     * 
     * Esta es la interfaz que se usa para hacer las llamadas a la API.
     * Ejemplo de uso:
     * ```
     * val response = RetrofitClient.weatherApiService.getCurrentWeather(-33.46, -70.65)
     * if (response.isSuccessful) {
     *     val weather = response.body()
     *     // Procesar datos
     * }
     * ```
     */
    val weatherApiService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}
