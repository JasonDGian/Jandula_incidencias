# 📌 Módulo de gestión de incidencias.
Repositorio del proyecto para módulo de gestión de incidencias.

## 🔹 Casos de uso.
En el microservicio están previstas las siguientes acciones.
1. ✅ Crear una nueva incidencia.
2. ✅ Cambiar el estado de una incidencia a "CANCELADA".
3. ✅ Cambiar el estado de una incidencia a "RESUELTA".
4. ✅ Listar todas las incidencias.
5. ✅ Listar todas las incidencias con filtro aplicado.
6. ✅ Buscar Incidencia por ID.

<!-- <p align="center">
   <img src="https://github.com/user-attachments/assets/60f3bc4e-72f3-4d39-8089-b5002f4995b7">
</p> -->


## 🔹 Requisitos de ejecución.
El servicio requiere la existencia de un esquema denominado "**incidencias**" en una base de datos **MySQL**, la cual debe estar en escucha en el puerto **3306**. En el archivo de configuración del proyecto, `application.yaml`, se establecen el **nombre** y las **credenciales de acceso** a dicha base de datos y esquema.
<p align="center">
   <img src="https://github.com/user-attachments/assets/ab96e2e9-29fd-4182-b6dd-dfd06b9f966b">
   <img src="https://github.com/user-attachments/assets/f9ee69fb-669e-4008-922e-e3458b6340af">
</p>


--- 

# 📌 Endpoints expuestos.
A continuación el listado de endpoints expuestos actualmente y los parametros necesarios con una descripcion de su comportamiento.

<table border="1">
  <thead>
    <tr>
      <th>Método</th>
      <th>Ruta</th>
      <th>Descripción</th>
      <th>Parámetros Requeridos</th>
      <th>Acción</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>GET</td>
      <td>/incidencias</td>
      <td>Devuelve un listado de todas las incidencias almacenadas.</td>
      <td>N/A</td>
      <td>Obtiene todas las incidencias.</td>
    </tr>
    <tr>
      <td>POST</td>
      <td>/incidencias</td>
      <td>Devuelve la incidencia con el ID proporcionado.</td>
      <td><strong>Query:</strong> "id" - Número entero</td>
      <td>Obtiene una incidencia específica.</td>
    </tr>
    <tr>
      <td>POST</td>
      <td>/incidencias/nueva</td>
      <td>Genera una nueva incidencia y la almacena en la BBDD.</td>
      <td>
        <strong>Header:</strong> "correoDocente" - Dirección de correo<br>
        <strong>Body:</strong> Objeto JSON con "numeroAula" y "descripcionIncidencia"
      </td>
      <td>Crea una nueva incidencia.</td>
    </tr>
    <tr>
      <td>POST</td>
      <td>/incidencias/resuelve</td>
      <td>Marca la incidencia con el ID proporcionado como RESUELTA.</td>
      <td><strong>Query:</strong> "id" - Identificador de la incidencia</td>
      <td>Cambia el estado a resuelta y avisa al docente.</td>
    </tr>
    <tr>
      <td>POST</td>
      <td>/incidencias/cancela</td>
      <td>Marca la incidencia con el ID proporcionado como CANCELADA.</td>
      <td><strong>Query:</strong> "id" - Identificador de la incidencia</td>
      <td>Cambia el estado a cancelada y avisa al docente.</td>
    </tr>
    <tr>
      <td>POST</td>
      <td>/incidencias/filtro</td>
      <td>Devuelve un listado de incidencias filtradas por estado.</td>
      <td><strong>Query:</strong> "estado" - Cadena que describe el estado</td>
      <td>Filtra incidencias por estado.</td>
    </tr>
  </tbody>
</table>

<!-- 
`GET` - `/incidencias`    
Al responder a una peticion GET devuelve un listado de TODAS las incidencias almacenadas.    
    
`POST` - `/incidencias`      
**Parametro requerido (query)** : "id" - numero entero      
Devuelve la incidencia con identificador equivalente al recibido como parametro, no haber incidencias no devolverá nada.    
    
`POST` - `/incidencias/nueva`     
**Parametro requerido (header)** : "correoDocente" - direccion de correo del docente que señala.    
**Parametro requerido (body)** : Objeto Json.     
```Json
{
    "numeroAula":"0.5",
    "descripcionIncidencia":"EL equipo numero 12 no enciente."
}
```
Genera una nueva incidencia y la almacena en la BBDD.       
El campo **fecha de creación**, **estado**, y el **id** se asignan automaticamente.   
Con la lógica actual, si estos detalles fueran proporcionados, serán sobre escritos por el controlador a la escucha.
    
`POST` - `/incidencias/resuelve`     
**Parametro requerido (query)** : "id" - Identificador de la incidencia a marcar como resuelta.      
Cambiar a RESUELTA la incidencia con el ID proporcionado. En caso de que la incidencia YA estuviara resuelta
antes de haber recibido esta petición el mensaje informará mediante un aviso. En caso de que no exista una incidencia con dicho ID también aparecerá otro aviso.    
*TODO: Cuando el proceso finalice con éxito se llama al metodo de aviso a docente de actualización.*    
      
`POST` - `/incidencias/cancela`      
**Parametro requerido (query)** : "id" - Identificador de la incidencia a marcar como cancelada.       
Cambiar a CANCELADA la incidencia con el ID proporcionado. En caso de que la incidencia YA estuviara cancelada 
antes de haber recibido esta petición el mensaje informará mediante un aviso. En caso de que no exista una incidencia con dicho ID también aparecerá otro aviso.    
*TODO: Cuando el proceso finalice con éxito se llama al metodo de aviso a docente de actualización.*    
       
`POST` - `/incidencias/filtro`       
**Parametro requerido (query)** : "estado" - Cadena que describe el estado en el que se encuentra la incidencia.    
Segun el parametro especificado, devolvera un listado de incidencias en ese estado de manera ordenada por la fecha de creación.    
-->
    
      

## 🔹 Ejemplo de creación de una incidencia nueva.
Atacando al endpoint `/nueva`, enviando el header y cuerpo **requeridos**. 

*En el header, el correo del docente.*
<p align="center">
<img src="https://github.com/user-attachments/assets/05216b81-1b7f-4c47-a117-ed4570fd178d">
</p>

*En el body, el numero de aula y la descripción del problema.*
<p align="center">
<img src="https://github.com/user-attachments/assets/3aafb830-1ca0-4630-a4fe-caad991fea79">
</p>

*Respuesta 200 OK del servidor.*
<p align="center"> 
<img src="https://github.com/user-attachments/assets/058324ed-37d9-43bd-b684-81b39cd18271">
</p>

*Comprobación de resultados.*
<p align="center"> 
<img src="https://github.com/user-attachments/assets/4161d03f-754c-42b0-9521-6cda6c8fafb5">
</p>

 
