# 📌 Módulo de gestión de incidencias.
Repositorio del proyecto para el módulo de gestión de incidencias.
Este servicio maneja las solicitudes HTTP relacionadas con la creación, actualización, consulta y eliminación de incidencias en una base de datos de motor MySQL Server.   

<table>
   <tr>
      <th>METODO</th>
      <th>URL</th>
      <th>DESCRIPCION</th>
   </tr>
   <tr>
      <td>🟢 GET</td>
      <td>/incidencias</td>
      <td>Obtiene una lista de incidencias basada en los parámetros del cuerpo Json de la solicitud.</td>
   </tr>
   <tr>
      <td>🟡 POST</td>
      <td>/incidencias</td>
      <td>Crea una nueva incidencia en el sistema.</td>
   </tr>
   <tr>
      <td>🔵 PUT</td>
      <td>/incidencias</td>
      <td>Actualiza una incidencia existente según los datos del cuerpo de la solicitud.</td>
   </tr>
   <tr>
      <td>🔴 DEL</td>
      <td>/incidencias</td>
      <td>Elimina una incidencia específica de la base de datos.</td>
   </tr>
</table>

## 🔹 Requisitos de ejecución.
El servicio necesita un esquema llamado "**incidencias**" en una base de datos **MySQL**, que debe estar escuchando en el puerto **3306**. En el archivo de configuración del proyecto, `application.yaml`, se definen el nombre del esquema y las credenciales de acceso a la base de datos.

<p align="center">
   <img src="https://github.com/user-attachments/assets/ab96e2e9-29fd-4182-b6dd-dfd06b9f966b">
</p>

**Para crear un contenedor de forma rápida y sencilla que proporcione este servicio, utiliza el siguiente comando:**
```docker
docker run -d -p 3306:3306 --name mi_mysql -e MYSQL_ROOT_PASSWORD=1234 -e MYSQL_DATABASE=incidencias mysql
```

<p align="center">
   <img src="https://github.com/user-attachments/assets/f9ee69fb-669e-4008-922e-e3458b6340af">
</p>

<br/>
<br/>

# 📌 Endpoints expuestos.
A continuación el listado de endpoints expuestos actualmente y los parámetros necesarios con una descripcion de su comportamiento.
   
---
   
### 🟡 POST - Crear nueva incidencia.
Este endpoint permite registrar nuevas incidencias en el sistema con los datos proporcionados por el docente.
```
localhost:8888/incidencias
```

<!--El endpoint para añadir una nueva incidencia al sistema requiere lo siguiente:

1. **Cabecera (header):** Debe incluir el correo del docente que realiza la señalización, bajo el campo `correo-docente`.   
2. **Cuerpo de la petición (body):** Debe incluir los siguientes datos:
      - `numeroAula`: El número del aula donde se ha detectado la incidencia.
      - `descripcionIncidencia`: La descripción de la incidencia detectada. -->


   
**Requiere cabecera:**
```json
"correoDocente":"<correo_del_docente>"
```
**Requiere cuerpo:**
```json
{   
"numeroAula" : "<valor_numero_aula>" ,
"descripcionIncidencia" : "<valor_descripcion_incidencia>"
}
```
      
---    
    
### 🟢 GET - Filtra incidencias.
```
localhost:8888/incidencias
```
Endpoint que permite recuperar una **lista** de incidencias basandose en una serie de parametros recibidos por el cuerpo de la peticion. Los parametros pueden ser nulos. En caso de que los parametros sean todos nulos, devolvera todas las incidencias registradas.  
   
**Requiere cuerpo:**
```json
{
  "numeroAula": "<valor_numero_aula>",
  "correoDocente": "<valor_correo_docente>",
  "fechaInicio": "<valor_fecha_inicio>",
  "fechaFin": "<valor_fecha_fin>",
  "descripcionIncidencia": "<valor_descripcion_incidencia>",
  "estadoIncidencia": "<valor_estado_incidencia>",
  "comentario": "<valor_comentario>"
}

```
      
---    
    
### 🔵 PUT - Actualiza incidencia existente.
```
localhost:8888/incidencias
```
Este endpoint permite modificar una incidencia existente, sobreescribiendo los datos previos con los nuevos detalles proporcionados en el cuerpo de la solicitud. No permite cambios en los campos identificativos de la incidencia.
   
**Requiere cuerpo:**
```json
{
  "numeroAula": "<valor_numero_aula>",
  "correoDocente": "<valor_correo_docente>",
  "fechaInicio": "<valor_fecha_inicio>",
  "fechaFin": "<valor_fecha_fin>",
  "descripcionIncidencia": "<valor_descripcion_incidencia>",
  "estadoIncidencia": "<valor_estado_incidencia>",
  "comentario": "<valor_comentario>"
}
```
         
---    
     
    
### 🔴 DELETE - Borra incidencia.
```
localhost:8888/incidencias
```
Este endpoint permite borrar una incidencia específica de manera precisa, asegurando que se elimine la incidencia correcta.
   
**Requiere cuerpo:**
```json
{
  "numeroAula": "<valor_numero_aula>",
  "correoDocente": "<valor_correo_docente>",
  "fechaInicio": "<valor_fecha_inicio>",
  "fechaFin": "<valor_fecha_fin>",
  "descripcionIncidencia": "<valor_descripcion_incidencia>",
  "estadoIncidencia": "<valor_estado_incidencia>",
  "comentario": "<valor_comentario>"
}

```
    
---    
    



