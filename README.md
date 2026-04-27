# 🎬 Sistema de Gestión de Videoclub

## Descripción del proyecto

Sistema de gestión integral para un videoclub desarrollado en Java. Permite administrar el catálogo de películas, el registro de socios, la gestión de ejemplares físicos y el control de alquileres desde una interfaz de consola interactiva. La aplicación sigue una arquitectura por capas (modelo, DAO y aplicación) y utiliza SQLite como base de datos embebida.

## Requisitos previos

Lista de software necesario con las versiones recomendadas:

- Java JDK 17 o superior (desarrollado con JDK 25)
- SQLite JDBC 3.53.0.0 (se descarga automáticamente con Maven, no requiere instalación)
- Maven 3.8 o superior
- IntelliJ IDEA 2024.x (recomendado, aunque cualquier IDE compatible con Maven es válido)

## Configuración de la base de datos

1. **Crear la base de datos.** No es necesario ningún paso manual. Al ejecutar el programa por primera vez, la clase `ConexionBD` genera automáticamente el fichero `videoclub.db` en el directorio raíz del proyecto.

2. **Ejecutar el script SQL de creación de tablas.** Las tablas se crean automáticamente mediante el método `inicializarBaseDatos()` de la clase `ConexionBD`. Las tablas generadas son: `Director`, `Actor`, `Pelicula`, `reparto`, `Ejemplares`, `Socio` y `Alquiler`.

3. **Cargar los datos iniciales de prueba.** No se incluyen datos de prueba predefinidos. Una vez arrancada la aplicación, se pueden introducir datos desde el menú interactivo de consola.

4. **Configurar la cadena de conexión en el proyecto.** La cadena de conexión está definida en `src/main/java/conexionBD/ConexionBD.java`:
    ```java
    private static final String DB_URL = "jdbc:sqlite:videoclub.db";
    ```
    No es necesario modificarla para un uso estándar. Si se desea cambiar la ubicación del fichero, basta con editar esa línea.

## Clonar el repositorio

```bash
git clone https://github.com/Antonioprzz/videoclub.git
cd videoclub
```

## Compilación y ejecución

Pasos para compilar el proyecto y ejecutarlo desde línea de comandos o IDE.

**Desde línea de comandos (Maven):**

```bash
# Compilar el proyecto
mvn compile

# Ejecutar directamente
mvn exec:java -Dexec.mainClass="app.Main"

# O compilar y empaquetar en un JAR ejecutable
mvn package
java --enable-native-access=ALL-UNNAMED -jar target/videoclub-1.0-SNAPSHOT.jar
```

**Desde IntelliJ IDEA:**

1. Abrir el proyecto con `File → Open` y seleccionar la carpeta `videoclub`.
2. Esperar a que Maven descargue las dependencias automáticamente.
3. Abrir `src/main/java/app/Main.java` y pulsar el botón ▶ junto al método `main`.

> **Nota:** Al ejecutar con Java 17+, el driver SQLite puede mostrar avisos sobre acceso nativo en la consola. Son inofensivos. Para suprimirlos, añade `--enable-native-access=ALL-UNNAMED` en `Run → Edit Configurations → VM options`.

## Estructura del proyecto

Árbol de directorios con una breve descripción de cada carpeta o archivo relevante.

```
videoclub/
├── pom.xml                          # Configuración Maven y dependencias
├── videoclub.db                     # Base de datos SQLite (se genera al ejecutar)
└── src/
    └── main/
        └── java/
            ├── app/
            │   └── Main.java        # Punto de entrada; menús e interfaz de consola
            ├── conexionBD/
            │   └── ConexionBD.java  # Gestión de la conexión JDBC y creación del esquema
            ├── dao/
            │   ├── ActorDAO.java / ActorDAOImpl.java         # CRUD de actores
            │   ├── DirectorDAO.java / DirectorDAOImpl.java   # CRUD de directores
            │   ├── PeliculaDAO.java / PeliculaDAOImpl.java   # CRUD de películas
            │   ├── EjemplarDAO.java / EjemplarDAOImpl.java   # CRUD de ejemplares
            │   ├── SocioDAO.java / SocioDAOImpl.java         # CRUD de socios
            │   ├── AlquilerDAO.java / AlquilerDAOImpl.java   # CRUD de alquileres
            │   └── *DAOException.java                        # Excepciones personalizadas
            └── modelo/
                ├── Actor.java               # Entidad actor
                ├── Director.java            # Entidad director
                ├── Pelicula.java            # Entidad película
                ├── Ejemplar.java            # Entidad ejemplar físico
                ├── Socio.java               # Entidad socio
                ├── Alquiler.java            # Entidad alquiler
                └── EstadoConservacion.java  # Enum con los estados de un ejemplar
```

## Equipo

Listado de integrantes con su rol en el proyecto.

| Nombre | Rol |
|---|---|
| Sergio Ojeda | Jefe de Proyecto |
| Antonio Béltrán | Desarrollador Backend |
| Daniel Del Toro | Desarrollador de Interfaz |
| Juan María Alanis | Diseñador de Base de Datos |
| Antonio Pérez | Responsable de Calidad y Documentación |
