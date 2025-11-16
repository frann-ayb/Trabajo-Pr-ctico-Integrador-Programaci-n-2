# Programación 2
**Tecnicatura Universitaria en Programación**  

## TP Integrador – Gestión de Pedidos y Envíos
 *Universidad Tecnológica Nacional*  

## Estudiantes  
- **Nombres:** Martin Gomez (Comision 08) - Matias Ezequiel Gonzalez(Comision 15)- Franco Esteban Herrera Gonzalez
(Comision 15)- Agustin Mario Nicolas Lepka(Comision 13)


## Descripción 

Este proyecto simula un sistema de **gestión logística de pedidos y envíos**, desarrollado como parte de un **Trabajo Práctico Integrador académico**. El sistema permite registrar pedidos, asociar envíos, realizar búsquedas, actualizaciones y aplicar baja lógica, todo desde una interfaz de consola.

El dominio modela una empresa ficticia que administra pedidos de clientes y coordina sus envíos con distintas empresas logísticas.

---

## ️ Requisitos técnicos

- **Java:** versión 24
- **Base de datos:** MySQL
- **Driver JDBC:** `mysql-connector-java-8.4.xx.jar` (debe estar incluido en el classpath)
- **Usuario MySQL:** `root`
- **Contraseña:** *(vacía)*

---

##️ Creación de la base de datos

1. Abrí tu gestor SQL (phpMyAdmin, MySQL Workbench, etc.)
2. Ejecutá el script `TPIntegrador.sql` provisto para crear las tablas necesarias:
   - `Pedido`
   - `Envio`
3. Verificá que la base se haya creado correctamente:
   ```sql
   SHOW TABLES FROM TPIntegrador;


 Compilación y ejecución
1. Compilar
- En NetBeans: Run → Clean and Build Project
- Asegurate de que el .jar del conector JDBC esté agregado en Properties → Libraries → Compile
2. Ejecutar
- Desde Main.java, se accede al menú principal:

===== MENÚ PRINCIPAL – LOGÍSTICA ENVÍOS =====
1. Crear Pedido sin Envio
2. Listar Pedidos
3. Buscar Pedido por ID
4. Buscar Pedido por Numero
5. Actualizar Pedido
6. Eliminar Pedido (baja lógica)
7. Asociar Envío a Pedido
8. Listar Envíos
9. Buscar Envío por ID
10. Buscar Envio por Tracking
11. Testeo automático (CRUD completo)
12. Crear un pedido con Envio
0. Salir

3. Credenciales de prueba
- Usuario: root
- Contraseña: (vacía)
- Base de datos: TPIntegrador


## Enlace al video 
https://www.youtube.com/watch?v=s6L0bNb288s 
