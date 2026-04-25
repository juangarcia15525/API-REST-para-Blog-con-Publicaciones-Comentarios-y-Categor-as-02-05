# API REST para Blog

API REST para la gestión de un blog, desarrollada con el objetivo de administrar publicaciones, comentarios y categorías de forma organizada y segura.

El proyecto incluye validaciones de datos, manejo de errores adecuado y uso correcto de códigos de estado HTTP y encabezados de respuesta.

## Descripción

Esta API permite gestionar los principales recursos de un blog:

- Publicaciones
- Comentarios
- Categorías

Cada recurso cuenta con endpoints para realizar operaciones CRUD: crear, leer, actualizar y eliminar.

Además, la API implementa validaciones para asegurar que los datos enviados sean correctos antes de ser almacenados o procesados.

## Funcionalidades principales

### Publicaciones

La API permite gestionar publicaciones del blog.

Operaciones disponibles:

- Crear una publicación
- Obtener todas las publicaciones
- Obtener una publicación por ID
- Actualizar una publicación
- Eliminar una publicación

Campos principales:

- `title`
- `content`
- `categoryId`

Validaciones:

- El título es obligatorio
- El contenido es obligatorio
- El contenido debe cumplir una longitud mínima y/o máxima
- El título debe ser único

## Comentarios

La API permite gestionar comentarios asociados a publicaciones.

Operaciones disponibles:

- Crear un comentario
- Obtener comentarios
- Obtener comentarios por publicación
- Actualizar un comentario
- Eliminar un comentario

Campos principales:

- `author`
- `content`
- `postId`

Validaciones:

- El autor es obligatorio
- El contenido es obligatorio
- El contenido debe cumplir las reglas de longitud establecidas
- El comentario debe estar asociado a una publicación existente

## Categorías

La API permite organizar las publicaciones mediante categorías.

Operaciones disponibles:

- Crear una categoría
- Obtener todas las categorías
- Obtener una categoría por ID
- Actualizar una categoría
- Eliminar una categoría

Campos principales:

- `name`
- `description`

Validaciones:

- El nombre de la categoría es obligatorio
- El nombre de la categoría debe ser único
- La descripción debe respetar la longitud permitida

## Validaciones implementadas

La API incluye validaciones para garantizar la integridad de los datos:

### Campos obligatorios

Se valida que los campos necesarios estén presentes antes de crear o actualizar un recurso.

Ejemplos:

- Una publicación no puede crearse sin título o contenido
- Un comentario no puede crearse sin contenido
- Una categoría no puede crearse sin nombre

### Longitud del contenido

Se valida que los textos cumplan con los límites definidos por la aplicación.

Ejemplos:

- El contenido de una publicación debe tener una longitud válida
- El contenido de un comentario no debe estar vacío ni superar el límite permitido
- La descripción de una categoría debe respetar la longitud máxima

### Restricciones únicas

Se controla que ciertos campos no se repitan.

Ejemplos:

- No puede existir más de una publicación con el mismo título
- No puede existir más de una categoría con el mismo nombre

## Manejo de errores

La API devuelve respuestas claras cuando ocurre un error.

Ejemplos de errores manejados:

- Recurso no encontrado
- Datos inválidos
- Campos obligatorios faltantes
- Violación de restricciones únicas
- Errores internos del servidor

Ejemplo de respuesta de error:

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "El campo title es obligatorio"
}
