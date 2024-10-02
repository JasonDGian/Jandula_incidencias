# 📌 Módulo de gestión de incidencias.
Repositorio del proyecto para módulo de gestión de incidencias.

## 🔹 Casos de uso.
En el microservicio están previstas las siguientes acciones.
1. ✅ Crear una nueva incidencia.
2. ✅ Cambiar el estado de una incidencia a "CANCELADA".
3. ✅ Cambiar el estsado de una incidencia a "RESUELTA".
4. ✅ Listar todas las incidencias.
5. ✅ Listar todas las incidencias "EN PROGRESO".
6. ✅ Listar todas las incidencias "RESUELTA".
7. ✅ Listar todas las incidencias "CANCELADA".
8. Buscar Incidencia por ID. -> POR IMPLEMENTAR.

<p align="center">
   <img src="https://github.com/user-attachments/assets/60f3bc4e-72f3-4d39-8089-b5002f4995b7">
</p>


## 🔹 Requisitos de ejecución.
El servicio requiere la existencia de un esquema denominado "**incidencias**" en una base de datos **MySQL**, la cual debe estar en escucha en el puerto **3306**. En el archivo de configuración del proyecto, `application.yaml`, se establecen el **nombre** y las **credenciales de acceso** a dicha base de datos y esquema.
   
![image](https://github.com/user-attachments/assets/f9ee69fb-669e-4008-922e-e3458b6340af)
   
## 🔹 Enpoints configurados.
   
- **/incidencias** (GET):   
Devuelve una lista de todas las incidencias registradas en el sistema.
**Modo de empleo:** Enviar una petición GET al endpoint sin cuerpo ni parametros necesarios.
   
- **/nueva** (POST):   
Permite crear una nueva incidencia. El estado inicial de la incidencia se establece como `EN_PROGRESO`.   
**Modo de empleo:** Enviar una petición POST al endpoint con un cuerpo JSON.
Ejemplo formato:
```json
{
"numeroAula":"0.7"
"nombreProfesor":"Lorem Ipsum"
"fechaIncidencia":"2025-09-30T10:30:00.000+00:00"
"dedscripcionIncidencia":"Pantalla de alumno ha dejado de feuncionar, necesita respuesto ASAP."
}
```
   
>[!NOTE]
>La introducción de la fecha se produce de manera controlada, no como string, y deberá ser formateada dentro de la aplicación.  
   
- **/resuelta** (POST):   
Cambia el estado de una incidencia a `RESUELTA`, identificada por su ID.
   
- **/cancelar** (POST):   
Cambia el estado de una incidencia a `CANCELADA`, identificada por su ID.
   
- **/incidencias_resueltas** (GET):   
Devuelve una lista de todas las incidencias que están marcadas como `RESUELTA`.
    
- **/incidencias_canceladas** (GET):   
Devuelve una lista de todas las incidencias que están marcadas como `CANCELADA`.
   
- **/incidencias_en_progreso** (GET):   
Devuelve una lista de todas las incidencias que están en estado `EN_PROGRESO`.
