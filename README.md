# 📌 Módulo de gestión de incidencias.
Repositorio del proyecto para el módulo de gestión de incidencias.
Este servicio maneja las solicitudes HTTP relacionadas con la creación, actualización, búsqueda, eliminación y listado de incidencias.   

<!--
**Proporciona endpoints para realizar operaciones como:**
- Crear una nueva incidencia a partir de los datos enviados en el cuerpo de la solicitud.
- Listar todas las incidencias almacenadas en la base de datos.
- Buscar una incidencia específica por su ID.
- Actualizar el estado de una incidencia y añadir un comentario sobre su solución.
- Cargar un lote de incidencias para facilitar pruebas de uso.
- Eliminar una incidencia utilizando su ID (eliminar de la bbdd). -->


<table>
   <tr>
      <th>METODO</th>
      <th>URL</th>
      <th>DESCRIPCION</th>
   </tr>
   <tr>
      <td>🟢 GET</td>
      <td>/incidencias</td>
      <td>Recupera un listado de todas las incidencias registradas en la base de datos.</td>
   </tr>
   <tr>
      <td>🟡 POST</td>
      <td>/incidencias</td>
      <td>Endpoint que permite recuperar una incidencia específica almacenada en el sistema mediante su ID.</td>
   </tr>
   <tr>
      <td>🟡 POST</td>
      <td>/incidencias/carga-lote</td>
      <td>Permite cargar un lote de incidencias para realizar pruebas con ellos en lugar de ir añadiendo incidencias una a una.
</td>
   </tr>
   <tr>
      <td>🟡 POST</td>
      <td>/incidencias/nueva</td>
      <td>Endpoint que permite añadir incidencias al sistema.</td>
   </tr>
   <tr>
      <td>🟡 POST</td>
      <td>/incidencias/actualiza</td>
      <td>Endpoint que actualiza una incidencia con ID específico basándose en el cuerpo JSON que recibe.</td>
   </tr>
   <tr>
      <td>🔴 DEL</td>
      <td>/incidencias/borrar</td>
      <td>Endpoint que permite borrar de la base de datos una incidencia específica a partir de su ID.</td>
   </tr>
</table>

## 🔹 Requisitos de ejecución.
El servicio requiere la existencia de un esquema denominado "**incidencias**" en una base de datos **MySQL**, la cual debe estar en escucha en el puerto **3306**. En el archivo de configuración del proyecto, `application.yaml`, se establecen el **nombre** y las **credenciales de acceso** a dicha base de datos y esquema.

<p align="center">
   <img src="https://github.com/user-attachments/assets/ab96e2e9-29fd-4182-b6dd-dfd06b9f966b">
</p>

**Para crear un contenedor de manera fácil y rápida que pueda proporcionar este servicio emplear el siguiente comando**
```docker
docker run --name myServer -p 3306:3306 -e MYSQL_ROOT_PASSWORD=1234 -d mysql
```

<p align="center">
   <img src="https://github.com/user-attachments/assets/f9ee69fb-669e-4008-922e-e3458b6340af">
</p>


--- 

# 📌 Endpoints expuestos.
A continuación el listado de endpoints expuestos actualmente y los parámetros necesarios con una descripcion de su comportamiento.
   
### 🟢 GET - Listar incidencias.
```
localhost:8888/incidencias
```
Recupera un listado de todas las incidencias registradas en la base de datos.
   
---
   
### 🟡 POST - Cargar lote incidencias
```
localhost:8888/incidencias/carga-lote
```
Permite cargar un lote de incidencias.   
Espera una **lista** de objetos Json que especifiquen ciertos atributos.     
   
**Requiere cuerpo**:   
```json
[
   {   
   "numeroAula" : "String" ,
   "correoDocente" : "String" ,
   "descripcionIncidencia" : "String"
   },
   {   
   "numeroAula" : "String" ,
   "correoDocente" : "String" ,
   "descripcionIncidencia" : "String"
   }
   ...
]
```
   
---
   
### 🟡 POST - Crear nueva incidencia.
```
localhost:8888/incidencias/nueva
```
Endpoint que permite añadir incidencias al sistema, de una en una.
   
**Requiere cabecera:**
```
correoDocente
```
**Requiere cuerpo:**
```json
{   
"numeroAula" : "String" ,
"descripcionIncidencia" : "String"
}
```
      
---    
    
### 🟡 POST - Busca incidencia con ID
```
localhost:8888/incidencias
```
Endpoint que permite recuperar una incidencia específica almacenada en el sistema mediante su ID.   
   
**Requiere parámetro:**
```
long id
```
      
---    
    
### 🟡 POST - Resuelve incidencia con comentario.
```
localhost:8888/incidencias/actualiza
```
Endpoint que actualiza una incidencia con ID específico basándose en el cuerpo JSON que recibe.   
   
**Requiere parámetro:**
```
long id
```
   
**Requiere cuerpo:**
```json
{
    "estado":"String",
    "comentario":"String"
}
```
      
---    
    
### 🔴 DELETE - Borra incidencia por ID.
```
localhost:8888/incidencias/borrar
```
Endpoint que permite borrar de la base de datos una incidencia específica a partir de su ID. 
   
**Requiere parámetro:**
```
long id
```
         
---    
    



