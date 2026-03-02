# AREP Microframeworks WEB - Desarrollo de Framework Web para Servicios REST

- **Autor**: Carlos David Barrero Velasquez
- **Universidad**: Escuela Colombiana de Ingeniería Julio Garavito  
- **Asignatura**: Arquitecturas Empresariales (AREP)
- **Fecha**: Marzo 2026

## Introducción

Este proyecto desarrolla un **Microframework Web personalizado en Java** que habilita a los desarrolladores para construir aplicaciones web con servicios REST backend de manera simple y eficiente. El framework implementa características clave como:

- **Servicios REST con funciones lambda**: Define rutas usando expresiones lambda
- **Extracción de parámetros de consulta**: Accede a parámetros HTTP de forma automática
- **Servicio de archivos estáticos**: Configura la ubicación de recursos (HTML, CSS, JS, imágenes)
- **Arquitectura desacoplada**: Separación clara entre servidor, requests, responses y handlers

Este laboratorio profundiza en conceptos fundamentales de arquitectura web, comunicación HTTP, y patrones de diseño en Java.

---

## Estructura del Repositorio

```
AREP_Microframeworks_WEB/
│
├── lab/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/arep/lab/
│   │   │   │   ├── HttpServer.java           # Servidor principal (get, staticfiles)
│   │   │   │   ├── HttpRequest.java          # Manejo de requests y query parameters
│   │   │   │   ├── HttpResponse.java         # Constructores de responses
│   │   │   │   ├── WebMethod.java            # Interfaz funcional para handlers
│   │   │   │   └── appexamples/
│   │   │   │       └── MathService.java      # Aplicación de ejemplo
│   │   │   └── resources/webroot/public/
│   │   │       ├── index.html                # Página principal
│   │   │       ├── styles.css                # Estilos básicos
│   │   │       └── app.js                    # JavaScript para formulario AJAX
│   │   └── test/java/com/arep/lab/
│   │       ├── HttpRequestTest.java          # Tests para HttpRequest
│   │       ├── HttpResponseTest.java         # Tests para HttpResponse
│   │       └── EndpointTest.java             # Tests de integración de endpoints
│   ├── pom.xml                               # Configuración Maven
│   ├── Capturas                              # Evidencia 
|   └── README.md
│
├── .gitignore                                # Excluye target/ y archivos IDE
└── README.md                                 # Este archivo

```

---

## Cómo Ejecutar

### Requisitos Previos

- **Java 17** o superior
- **Maven 3.6** o superior
- Git

### Clonar y Construir el Proyecto

```bash
# 1. Clonar el repositorio
git clone https://github.com/tu-usuario/AREP_Microframeworks_WEB.git
cd AREP_Microframeworks_WEB/lab

# 2. Compilar el proyecto
./mvnw clean compile

# 3. Ejecutar los tests
./mvnw test

# 4. Ejecutar la aplicación
java -cp target/classes com.arep.lab.appexamples.MathService
```

El servidor iniciará en `http://localhost:8081`

### Comandos Windows

```cmd
# Compilar
mvnw.cmd clean compile

# Tests
mvnw.cmd test

# Ejecutar
java -cp target\classes com.arep.lab.appexamples.MathService
```

---

## Descripción de Componentes Principales

### 1. **HttpServer** - Servidor Principal

Componente central que:
- Acepta conexiones HTTP en un puerto configurable
- Realiza route matching de URLs a handlers
- Sirve archivos estáticos desde un directorio configurable

**Métodos principales:**
```java
public static void get(String path, WebMethod wm)        // Registra endpoint GET
public static void staticfiles(String location)          // Configura directorio de archivos
public static void setPort(int port)                     // Cambiar puerto (default 8080)
```

**Flujo de procesamiento:**
```
Accept cliente → Parse HTTP request → Extract path & query → 
Match route → Execute handler → Build HTTP response → Send client
```

### 2. **HttpRequest** - Representación de Solicitud

Encapsula información del request HTTP:
- **Path**: Ruta solicitada (ej: `/App/hello`)
- **Query String**: Parámetros de URL (ej: `name=Pedro&age=25`)
- **Query Parameters**: Mapa clave-valor de parámetros

**métodos:**
```java
String getValues(String paramName)     // Obtiene valor de parámetro
String getPath()                        // Retorna path del request
String getQueryString()                 // Retorna query string completo
```

**Ejemplo:**
```
Request: GET /App/hello?name=Pedro&city=Bogota
getValues("name")  → "Pedro"
getValues("city")  → "Bogota"
getValues("age")   → "" (no existe)
```

### 3. **HttpResponse** - Constructora de Respuesta

Permite personalizar respuestas HTTP:
```java
void setBody(String body)              // Contenido de la respuesta
void setContentType(String type)       // application/json, text/html, etc
void setStatusCode(int code)           // 200, 404, 500, etc
```

### 4. **WebMethod** - Interfaz Funcional

Define el contrato para handlers de endpoints:
```java
public interface WebMethod {
    public String execute(HttpRequest req, HttpResponse res);
}
```

Permite usar lambda expressions:
```java
get("/hello", (req, res) -> "Hello " + req.getValues("name"));
```

---

## Implementación de Características

### 1. **Método get() para Servicios REST con Lambda Functions**

**Requisito**: Implementar método que permita definir servicios REST usando lambdas

**Solución**:
```java
static Map<String, WebMethod> endPoints = new HashMap<>();

public static void get(String path, WebMethod wm) {
    endPoints.put(path, wm);
}
```

**Uso**:
```java
get("/App/hello", (req, resp) -> "Hello " + req.getValues("name"));
get("/App/pi", (req, resp) -> String.valueOf(Math.PI));
```

**Resultado**: Los endpoints se registran en un mapa y se invocan al recibir requests matching.

---

### 2. **Mecanismo de Extracción de Query Parameters**

**Requisito**: Extraer parámetros de consulta y hacerlos accesibles en servicios REST

**Implementación en HttpRequest**:
```java
private Map<String, String> queryParams;

private void parseQueryParams() {
    if (queryString != null && !queryString.isEmpty()) {
        String[] pairs = queryString.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                queryParams.put(keyValue[0], keyValue[1]);
            }
        }
    }
}

public String getValues(String paramName) {
    return queryParams.getOrDefault(paramName, "");
}
```

**Proceso**:
1. URI es parseada para extraer query string
2. Query string se divide por "&" para obtener pares clave=valor
3. Cada par se mapea en HashMap
4. `getValues()` accede al mapa

---

### 3. **Método staticfiles() para Ubicación de Archivos Estáticos**

**Requisito**: Permitir especificar directorio donde ubican archivos estáticos

**Implementación**:
```java
private static String staticFilesLocation = "/webroot/public";

public static void staticfiles(String location) {
    staticFilesLocation = location;
}
```

**Servicio de archivos**:
```java
private static String tryServeStaticFile(String path) {
    String resourcePath = staticFilesLocation + path;
    InputStream inputStream = HttpServer.class.getResourceAsStream(resourcePath);
    
    if (inputStream != null) {
        // Lee contenido del archivo
        // Determina Content-Type
        // Retorna respuesta HTTP con archivo
    }
    return null;
}
```

**Estructura de directorios**:
```
target/classes/webroot/public/
├── index.html
├── styles.css
└── app.js
```

---

## Pruebas Automatizadas

Se implementaron **19 tests automatizados** usando JUnit 5 que validan la funcionalidad core:

### HttpRequestTest (7 tests)

```java
✓ testGetValuesWithValidParameter
✓ testGetValuesWithMultipleParameters  
✓ testGetValuesWithNonexistentParameter
✓ testGetValuesWithEmptyQueryString
✓ testGetValuesWithNullQueryString
✓ testGetPath
✓ testGetQueryString
```

**Evidencia**:
```
[INFO] Running com.arep.lab.HttpRequestTest
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
```

### HttpResponseTest (6 tests)

```java
✓ testDefaultValues
✓ testSetBody
✓ testSetContentType
✓ testSetStatusCode
✓ testSetMultipleProperties
✓ testEmptyBody
```

**Evidencia**:
```
[INFO] Running com.arep.lab.HttpResponseTest
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

### EndpointTest (5 tests)

```java
✓ testHelloEndpointWithParameter
✓ testHelloEndpointWithDifferentName
✓ testPiEndpoint
✓ testHelloEndpointWithEmptyName
✓ testMultipleParameters
```

**Evidencia**:
```
[INFO] Running com.arep.lab.appexamples.EndpointTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
```

### Resultados Consolidados

```
[INFO] Results:
[INFO]
[INFO] Tests run: 19, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] BUILD SUCCESS
```

---

## Evidencia de Ejecución

### Página HTML Estática

Respuesta del servidor mostrando archivo estático:

<p align="center">
  <img src="lab\Capturas/index.png" alt="Prueba de index.html">
</p>
*Figura 1: Verificación de que index.html se sirve correctamente*

### Prueba del Endpoint /App/hello

Respuesta del endpoint REST con parámetro de consulta:

<p align="center">
  <img src="lab\Capturas/hello.png" alt="Endpoint /App/hello?name=Pedro">
</p>
*Figura 2: Endpoint GET /App/hello?name=Pedro retornando respuesta personalizada*

### Prueba del Endpoint /App/pi

Respuesta del endpoint REST que retorna el valor de PI:

<p align="center">
  <img src="lab\Capturas/pi.png" alt="Endpoint /App/pi">
</p>
*Figura 3: Endpoint GET /App/pi retornando el valor de Math.PI*

---

## Arquitectura del Sistema

### Diagrama de Flujo

```
1. Cliente hace request HTTP
   GET /App/hello?name=Pedro HTTP/1.1

2. HttpServer acepta conexión
   └─ Parse primer línea del request
   └─ Extrae path (/App/hello) y query (name=Pedro)

3. Route Matching
   └─ Busca en Map<String, WebMethod> 
   └─ Encuentra handler lambda registrado

4. Crear HttpRequest
   └─ Parsea query parameters
   └─ Almacena en HashMap

5. Crear HttpResponse
   └─ Configuración inicial (200, text/html)

6. Invocar Lambda Expression
   execute(request, response)
   └─ "Hello " + req.getValues("name")
   └─ Retorna: "Hello Pedro"

7. Construir respuesta HTTP
   HTTP/1.1 200 OK\r\n
   Content-Type: text/html\r\n
   \r\n
   Hello Pedro

8. Enviar al cliente
```

### URLs de Prueba

| URL | Respuesta | Descripción |
|-----|-----------|-------------|
| `GET /App/hello?name=Pedro` | `Hello Pedro` | Saludo con parámetro |
| `GET /App/pi` | `3.141592653589793` | Valor de PI |
| `GET /index.html` | [HTML] | Archivo estático |


---

## Conclusiones

1. **Simplicidad es poder**: Con menos de 200 líneas de código en HttpServer se implementa un framework funcional.

2. **Las lambdas transforman la API**: El uso de `WebMethod` interface permite una sintaxis limpia y expresiva.

3. **Separación de responsabilidades**: Dividir en HttpRequest, HttpResponse, WebMethod y HttpServer mejora mantenibilidad.

4. **Tests son fundamentales**: 19 tests automáticos dan confianza en cambios futures.

5. **HTTP es simple**: Entender el protocolo base abre puertas a frameworks complejos (Spring, Play, etc).

---

## Referencias

- **Java Documentation**: [Java Sockets](https://docs.oracle.com/javase/tutorial/networking/sockets/)
- **HTTP Protocol**: [RFC 7230 - HTTP/1.1 Message Syntax](https://tools.ietf.org/html/rfc7230)
- **HTTP Overview**: [MDN Web Docs - HTTP Guides Overview](https://developer.mozilla.org/en-US/docs/Web/HTTP/Guides/Overview)
- **Lambda Expressions**: [Oracle Java Tutorials](https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html)
- **Presentación del profe Diseño 00**: [Presentación - OO Design, Generics y Lambda Expressions](https://campusvirtual.escuelaing.edu.co/moodle/pluginfile.php/304858/mod_resource/content/0/06OODesignGenericsLambdaExpressions.pdf)
- **Maven**: [Apache Maven Documentation](https://maven.apache.org/guides/)
- **JUnit 5**: [JUnit Platform Documentation](https://junit.org/junit5/)
- **Network Architecture**: [Names, Network, Client, Service](https://campusvirtual.escuelaing.edu.co/moodle/pluginfile.php/222974/mod_resource/content/0/NamesNetworkClientService.pdf)
- **HTTP & REST Video**: [YouTube - HTTP Protocol Overview](https://www.youtube.com/watch?v=5EYcz9hBwiY)
- **Java Web Development Video**: [YouTube - Java Web Services](https://www.youtube.com/watch?v=RMsPU55q4UY)
