# Administra-Horarios IC-CTLSC

Sistema de generación automática de horarios para la carrera de Ingeniería en Computación del Instituto Tecnológico de Costa Rica, Campus San Carlos. Desarrollado como primer proyecto programado del curso **IC-2101 Programación Orientada a Objetos**.

---

## Tabla de contenidos

- [Descripción general](#descripción-general)
- [Funcionalidades](#funcionalidades)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Descripción de clases](#descripción-de-clases)
- [Cómo funciona](#cómo-funciona)
- [Cómo compilar y ejecutar](#cómo-compilar-y-ejecutar)
- [Guía de uso para el profesor](#guía-de-uso-para-el-profesor)
- [Escenarios de prueba](#escenarios-de-prueba)
- [Conceptos de POO aplicados](#conceptos-de-poo-aplicados)
- [Autores](#autores)

---

## Descripción general

El sistema permite a un coordinador generar automáticamente horarios válidos para un semestre a partir de:

- Las asignaturas registradas para ese semestre.
- La disponibilidad horaria y los cursos asignados a cada profesor.
- Las aulas disponibles y su tipo (teoría o laboratorio).

Un horario válido es aquel que no presenta choques de aula, choques de profesor ni choques de cursos dentro de un mismo semestre. Si la generación automática no puede resolver todos los conflictos, el coordinador puede ajustar manualmente las asignaciones afectadas.

Los estudiantes y profesores pueden consultar el horario generado para cualquier semestre disponible.

---

## Funcionalidades

- Generación automática de horarios usando la disponibilidad de los profesores y la compatibilidad de aulas.
- Detección de conflictos: reporta choques de aula y de profesor.
- Ajuste manual del horario por parte del coordinador cuando se detectan conflictos, con registro de la razón del cambio.
- Revalidación automática de choques después de cada ajuste manual.
- Login por rol: identifica si el usuario es estudiante, profesor o coordinador y muestra el menú correspondiente.
- Consulta de horario según el rol:
    - **Coordinador** — horario completo con detalles del aula.
    - **Profesor** — solo sus propias asignaciones.
    - **Estudiante** — todos los cursos del semestre con detalles completos.
- Tres escenarios de prueba con datos quemados que cubren generación limpia, conflicto con resolución manual y múltiples profesores.
- Opción para ver todos los usuarios y contraseñas disponibles directamente desde el menú principal.

---

## Estructura del proyecto

Las clases están organizadas en paquetes que separan las entidades del dominio, la lógica de negocio y la interfaz de texto:

```
src/
├── Main.java                                      # Punto de entrada, menús y datos quemados
│
├── modelo/
│   ├── academico/
│   │   ├── Asignatura.java                        # Clase abstracta base para asignaturas
│   │   ├── AsignaturaTeorica.java                 # Asignatura teórica (tiene sitio de apuntes)
│   │   ├── AsignaturaPractica.java                # Asignatura práctica (materiales y SO)
│   │   └── Departamento.java                      # Departamento académico del profesor
│   │
│   ├── infraestructura/
│   │   ├── Aula.java                              # Clase abstracta base para aulas
│   │   ├── AulaTeoria.java                        # Aula de teoría (AC, multimedia)
│   │   └── AulaLab.java                           # Laboratorio (equipo técnico)
│   │
│   └── usuarios/
│       ├── Usuario.java                           # Clase abstracta base para usuarios
│       ├── Coordinador.java                       # Genera y ajusta horarios
│       ├── Profesor.java                          # Registra disponibilidad, consulta sus cursos
│       └── Estudiante.java                        # Consulta el horario del semestre
│
└── logica/
    ├── horario/
    │   ├── GeneradorHorario.java                  # Motor de generación automática
    │   ├── Semestre.java                          # Semestre con su lista de asignaciones
    │   └── Asignacion.java                        # Unidad del horario: asignatura + profesor + aula + bloque
    │
    └── tiempo/
        └── BloqueTiempo.java                      # Bloque de tiempo: día + hora inicio + hora fin
```

**Justificación de los paquetes:** `modelo` agrupa las entidades del dominio, subdividido en `academico` (qué se enseña), `infraestructura` (dónde se enseña) y `usuarios` (quién usa el sistema). `logica` contiene las reglas de negocio: el generador, los semestres y las asignaciones. Esta separación permite que la lógica evolucione sin afectar el modelo, y que el modelo sea reutilizable independientemente de la interfaz.

---

## Descripción de clases

### `modelo.usuarios.Usuario` _(abstracta)_
Clase base para todos los usuarios. Contiene los atributos comunes (`nombre`, `cedula`, `login`, `password`) y define los métodos abstractos `consultarHorario()` y `obtenerRol()`. El método `iniciarSesion()` identifica el rol con `instanceof` e imprime el mensaje de bienvenida.

### `modelo.usuarios.Coordinador`
Extiende `Usuario`. Implementa `consultarHorario()` mostrando el horario completo con detalles del aula. Tiene `generarHorario()`, que delega en `GeneradorHorario`, y `ajustarHorario()`, que reemplaza una asignación específica por una corregida.

### `modelo.usuarios.Profesor`
Extiende `Usuario`. Implementa `consultarHorario()` filtrando solo sus propias asignaciones. Tiene `registrarDisponibilidad()` para agregar bloques de tiempo disponibles y `setCursoImpartido()` para vincular asignaturas.

### `modelo.usuarios.Estudiante`
Extiende `Usuario`. Implementa `consultarHorario()` mostrando todas las asignaciones del semestre con asignatura, tipo, aula, profesor y bloque.

### `modelo.academico.Asignatura` _(abstracta)_
Define los atributos comunes de toda asignatura: `nombre`, `creditos`, `esExclusivaCarrera`. Declara los métodos abstractos `obtenerDetalles()` y `obtenerTipo()`, e implementa `getHorasSemanales()` (créditos × 3).

- **`AsignaturaTeorica`** — agrega `sitioWebApuntes`.
- **`AsignaturaPractica`** — agrega `materialApoyo` (lista) y `sistemaOperativo`.

### `modelo.infraestructura.Aula` _(abstracta)_
Define los atributos comunes de toda aula: `nombre`, `numAula`, `ubicacion`, `capacidad`. Declara los métodos abstractos `obtenerDetalles()`, `obtenerTipo()` y `esApropiada(Asignatura)`.

- **`AulaTeoria`** — compatible con `AsignaturaTeorica`. Registra si tiene aire acondicionado y equipo multimedia.
- **`AulaLab`** — compatible con `AsignaturaPractica`. Registra tipo y cantidad de equipo técnico.

### `modelo.academico.Departamento`
Clase de datos simple que representa el departamento académico al que pertenece un profesor.

### `logica.tiempo.BloqueTiempo`
Representa un slot horario con `diaSemana`, `horarioInicio` y `horaFin`. Lo usan los profesores para expresar disponibilidad y el generador para fijar cuándo ocurre cada asignación.

### `logica.horario.Asignacion`
Une una asignatura, un profesor, un aula y un bloque de tiempo. Es la unidad central del horario. El aula puede ser `null` al momento de crearla; el `GeneradorHorario` la completa después.

### `logica.horario.Semestre`
Contiene un `numero` y una `LinkedList<Asignacion>`. Las asignaciones las pobla el coordinador a través del `GeneradorHorario`.

### `logica.horario.GeneradorHorario`
Motor de scheduling. Recibe la lista de asignaciones del semestre, las aulas disponibles, los profesores y las asignaturas. Expone tres métodos:

| Método | Responsabilidad |
|--------|----------------|
| `generadorAutomatico()` | Itera las asignaturas, encuentra el profesor que la imparte, toma su primer bloque disponible y crea la asignación |
| `asignarAula()` | Para cada asignación sin aula, busca la primera aula compatible y desocupada en ese bloque |
| `validarChoques()` | Detecta y reporta choques de aula y de profesor; confirma si no hay ninguno |

---

## Cómo funciona

1. **Carga de datos** — Al iniciar, `Main` construye tres escenarios independientes con departamentos, aulas, asignaturas y profesores. Cada profesor registra su disponibilidad y sus cursos.

2. **Login** — El usuario ingresa su login y contraseña. El sistema identifica el rol y muestra el menú correspondiente.

3. **Generación de horario** — El coordinador selecciona `1. Generar horario` y elige un semestre. `GeneradorHorario` ejecuta `generadorAutomatico()`:
    - Encuentra el profesor de cada asignatura.
    - Toma el primer bloque disponible del profesor y crea una `Asignacion` sin aula.
    - Llama a `asignarAula()` para asignar un aula compatible y libre.
    - Llama a `validarChoques()` para reportar conflictos restantes.

4. **Ajuste manual** — Si hay conflictos, el coordinador selecciona `3. Ajustar horario manualmente`, elige la asignación por número, ingresa un nuevo día y hora de inicio (la hora fin se calcula sumando 50 minutos automáticamente, o puede ingresarse manualmente si es mayor al mínimo), y selecciona un aula de la lista. Ingresa la razón del ajuste. El sistema reemplaza la asignación y revalida choques de inmediato.

5. **Consulta de horario** — Cualquier usuario selecciona la opción de consulta, elige el semestre y ve el horario según su rol. Si el horario no fue generado aún, el sistema muestra `Horario no disponible`.

---

## Cómo compilar y ejecutar

**Requisito:** Java 11 o superior.

```bash
# Desde la raíz del repositorio (carpeta que contiene src/)

# Crear carpeta de salida
mkdir -p out

# Compilar (Linux / macOS)
javac -encoding UTF-8 -sourcepath src src/Main.java -d out

# Compilar (Windows CMD)
javac -encoding UTF-8 -sourcepath src src\Main.java -d out

# Ejecutar
java -cp out Main
```

> En IDEs como IntelliJ IDEA o Eclipse: marcar `src/` como *Sources Root* y ejecutar `Main` directamente sin pasos adicionales.

---

## Guía de uso para el profesor

Al iniciar el sistema se despliega el menú principal. Seleccione `2. Ver usuarios disponibles` para ver todos los logins y contraseñas en pantalla, o consúltelos en la tabla siguiente.

### Usuarios disponibles

| Login | Contraseña | Rol | Escenario |
|-------|-----------|-----|-----------|
| `coord1` | `coord1` | Coordinador (Ana Vargas) | Semestre I |
| `estud1` | `estud1` | Estudiante (Carlos Mora) | Semestre I |
| `prof1` | `prof1` | Profesor (Juan Perez) | Semestre I |
| `prof2` | `prof2` | Profesor (Maria Solis) | Semestre I |
| `coord2` | `coord2` | Coordinador (Pedro Jimenez) | Semestre III |
| `estud2` | `estud2` | Estudiante (Laura Rojas) | Semestre III |
| `prof3` | `prof3` | Profesor (Luis Quesada) | Semestre III |
| `prof4` | `prof4` | Profesor (Sofia Castro) | Semestre III |
| `coord3` | `coord3` | Coordinador (Diana Brenes) | Semestre V |
| `estud3` | `estud3` | Estudiante (Miguel Torres) | Semestre V |
| `prof5` | `prof5` | Profesor (Andres Mora) | Semestre V |
| `prof6` | `prof6` | Profesor (Valeria Nunez) | Semestre V |
| `prof7` | `prof7` | Profesor (Ricardo Ulate) | Semestre V |

### Flujo recomendado por escenario

---

#### Escenario 1 — Semestre I: generación sin conflictos

**Objetivo:** mostrar que la generación automática produce un horario válido cuando no hay choques.

1. Login: `coord1` / `coord1`
2. `1. Generar horario` → semestre `1`
    - El sistema asigna Programacion I (Juan Perez, Lunes 07:00, Aula T-101) y Lab. Programacion I (Maria Solis, Martes 08:00, Lab L-201).
    - Salida esperada: `No se encontraron choques en el horario.`
3. `2. Consultar horario` → semestre `1` — ver horario completo con detalles de aula.
4. `0. Cerrar sesion`
5. Login: `estud1` / `estud1`
6. `1. Consultar horario` → semestre `1` — mismo horario desde la vista del estudiante.
7. `0. Cerrar sesion`
8. Login: `prof1` / `prof1`
9. `1. Ver mi disponibilidad y cursos` — ver bloques registrados (Lunes y Miercoles 07:00) y cursos asignados.
10. `2. Consultar mi horario` → semestre `1` — solo aparece Programacion I.

---

#### Escenario 2 — Semestre III: conflicto y ajuste manual

**Objetivo:** mostrar detección de choque de profesor y resolución manual con revalidación inmediata.

1. Login: `coord2` / `coord2`
2. `1. Generar horario` → semestre `3`
    - Luis Quesada imparte Estructuras de Datos y Matematica Discreta con un único bloque disponible (Lunes 09:00). El generador los asigna a ambos en ese bloque.
    - Salida esperada: `CHOQUE de profesor: Luis Quesada asignado a Estructuras de Datos y Matematica Discreta`
3. `2. Consultar horario` → semestre `3` — verificar que uno de los cursos aparece `Sin aula (choque)`.
4. `3. Ajustar horario manualmente` → semestre `3`
    - Se muestra la tabla numerada. Elegir la asignación en conflicto (por ejemplo `2`).
    - Día: `Martes`
    - Hora inicio: `10:00`
    - Hora fin: presionar Enter → se calcula automáticamente `10:50`
    - Seleccionar aula (por ejemplo `2` para Aula T-306)
    - Razón: `Choque de profesor, se mueve a Martes 10:00`
    - El sistema imprime: `No se encontraron choques en el horario.`
5. `2. Consultar horario` → semestre `3` — confirmar horario sin conflictos.

---

#### Escenario 3 — Semestre V: tres profesores, sin conflictos

**Objetivo:** mostrar que el sistema escala correctamente con múltiples profesores de distintos departamentos.

1. Login: `coord3` / `coord3`
2. `1. Generar horario` → semestre `5`
    - Andres Mora (Lunes 12:30) → Redes de Computadoras → Aula T-501
    - Valeria Nunez (Martes 14:00) → Sistemas Operativos → Aula T-502
    - Ricardo Ulate (Viernes 07:00) → Lab. Redes → Lab L-601
    - Salida esperada: `No se encontraron choques en el horario.`
3. `2. Consultar horario` → semestre `5` — ver los tres cursos asignados.
4. `0. Cerrar sesion`
5. Login: `prof5` / `prof5` (Andres Mora)
6. `2. Consultar mi horario` → semestre `5` — solo aparece Redes de Computadoras.
7. `0. Cerrar sesion`
8. Login: `prof6` / `prof6` (Valeria Nunez)
9. `2. Consultar mi horario` → semestre `5` — solo aparece Sistemas Operativos.

---

## Escenarios de prueba

### Escenario 1 — Semestre I: generación limpia
Dos asignaturas (una teórica y una práctica), dos profesores con bloques no superpuestos. Demuestra la generación automática exitosa sin conflictos.

| | Detalle |
|-|---------|
| Asignaturas | Programacion I (teórica, 4 créditos) · Lab. Programacion I (práctica, 1 crédito) |
| Aulas | Aula T-101 (Edificio A, AC + multimedia, cap. 30) · Lab L-201 (Edificio B, 20 PCs HP) |
| Profesores | Juan Perez — Lunes y Miercoles 07:00 (Ciencias de la Computacion) · Maria Solis — Martes 08:00 (Matematica) |

### Escenario 2 — Semestre III: resolución manual de conflicto
Luis Quesada imparte dos asignaturas con un único bloque disponible, generando un choque de profesor intencionado. El coordinador lo resuelve con ajuste manual.

| | Detalle |
|-|---------|
| Asignaturas | Estructuras de Datos (teórica, 3 créditos) · Matematica Discreta (teórica, 3 créditos, servicio) · Lab. Estructuras (práctica, 1 crédito) |
| Aulas | Aula T-305 (multimedia, sin AC) · Aula T-306 (AC, sin multimedia) · Lab L-401 (25 Raspberry Pi) |
| Profesores | Luis Quesada — Lunes 09:00, imparte 2 cursos → choque · Sofia Castro — Jueves 10:00 |

### Escenario 3 — Semestre V: múltiples profesores
Tres asignaturas, tres profesores de dos departamentos distintos, bloques completamente separados. Demuestra escalabilidad sin conflictos.

| | Detalle |
|-|---------|
| Asignaturas | Redes de Computadoras (teórica, 4 créditos) · Sistemas Operativos (teórica, 4 créditos) · Lab. Redes (práctica, 1 crédito) |
| Aulas | Aula T-501 · Aula T-502 (ambas Edificio E, AC + multimedia, cap. 40) · Lab L-601 (15 Servidores Dell) |
| Profesores | Andres Mora — Lunes 12:30 · Valeria Nunez — Martes 14:00 · Ricardo Ulate — Viernes 07:00 |

---

## Conceptos de POO aplicados

| Concepto | Dónde se aplica |
|----------|----------------|
| **Abstracción** | `Usuario`, `Asignatura` y `Aula` son clases abstractas que definen un contrato que todas sus subclases deben cumplir |
| **Herencia** | `Estudiante`, `Profesor` y `Coordinador` extienden `Usuario`; `AsignaturaTeorica` y `AsignaturaPractica` extienden `Asignatura`; `AulaTeoria` y `AulaLab` extienden `Aula` |
| **Polimorfismo** | `consultarHorario()` y `obtenerDetalles()` se comportan de forma diferente en cada subclase; `esApropiada()` y `obtenerTipo()` se adaptan al tipo concreto de aula o asignatura |
| **Encapsulamiento** | Todos los atributos son `private` con sus getters y setters en cada clase |

Se eligieron clases abstractas en lugar de interfaces porque las jerarquías comparten atributos comunes y métodos con implementación parcial que no pueden definirse en una interfaz: por ejemplo, `iniciarSesion()` en `Usuario` (que usa `instanceof` para identificar el rol) y `getHorasSemanales()` en `Asignatura` (que calcula créditos × 3 para cualquier subclase). Una interfaz solo puede declarar estos comportamientos, no implementarlos con los datos de instancia del objeto.

---

## Autores

- Jason Rene Martinez Gutierrez (j.martinez.4@estudiantec.cr)
- Mainor Olivier Martinez Sanchez (m.martinez.3@estudiantec.cr)

**Curso:** IC-2101 Programación Orientada a Objetos  
**Profesor:** Oscar Víquez Acuña  
**Institución:** Instituto Tecnológico de Costa Rica, Campus San Carlos