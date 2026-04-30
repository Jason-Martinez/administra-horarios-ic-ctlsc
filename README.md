# Administra-Horarios IC-CTLSC

Automatic course scheduling system for the Computer Engineering program at Instituto Tecnológico de Costa Rica, Campus San Carlos. Built as the first programming project for the **IC-2101 Object-Oriented Programming** course.

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Project Structure](#project-structure)
- [Class Descriptions](#class-descriptions)
- [How It Works](#how-it-works)
- [Test Scenarios](#test-scenarios)
- [How to Run](#how-to-run)
- [OOP Concepts Applied](#oop-concepts-applied)
- [Authors](#authors)

---

## Overview

The system allows a coordinator to automatically generate valid semester schedules based on:

- The subjects assigned to each semester.
- The availability and assigned courses of each professor.
- The available classrooms and their types.

A valid schedule is one with no room conflicts, no professor conflicts, and no course conflicts within the same semester. If the automatic generator cannot resolve all conflicts, the coordinator can manually adjust individual assignments.

Students and professors can consult the generated schedule for any semester.

---

## Features

- Automatic schedule generation using professor availability and room compatibility.
- Conflict detection: reports room and professor clashes.
- Manual schedule adjustment by the coordinator when conflicts cannot be auto-resolved.
- Role-based login identifying whether the user is a Student, Professor, or Coordinator.
- Schedule consultation per role:
  - **Coordinator** — full schedule with complete room details.
  - **Professor** — only their own assigned courses.
  - **Student** — all courses in the semester with full details.
- Three hardcoded test scenarios covering different cases.
- All data is pre-loaded (no runtime input required).

---

## Project Structure

```
src/
├── Usuario.java              # Abstract base class for all users
├── Estudiante.java           # Student: can consult schedules
├── Profesor.java             # Professor: registers availability, consults own schedule
├── Coordinador.java          # Coordinator: generates and adjusts schedules
├── Asignatura.java           # Abstract base class for subjects
├── AsignaturaTeorica.java    # Theoretical subject (has notes website)
├── AsignaturaPractica.java   # Practical subject (has materials and OS)
├── Aula.java                 # Abstract base class for classrooms
├── AulaTeoria.java           # Theory classroom (AC, multimedia)
├── AulaLab.java              # Laboratory classroom (technical equipment)
├── BloqueTiempo.java         # Time block: day + start/end time
├── Asignacion.java           # Assignment: subject + professor + room + time block
├── Semestre.java             # Semester: holds a list of assignments
├── Departamento.java         # Academic department a professor belongs to
├── GeneradorHorario.java     # Core scheduling engine
└── Main.java                 # Entry point with 3 hardcoded test scenarios
```

---

## Class Descriptions

### `Usuario` _(abstract)_
Base class for all users in the system. Holds credentials (`nombre`, `cedula`, `login`, `password`) and defines the abstract methods `consultarHorario()` and `obtenerRol()`. The `iniciarSesion()` method identifies the user's role using `instanceof`.

### `Estudiante`, `Profesor`, `Coordinador`
Concrete subclasses of `Usuario`. Each implements `consultarHorario()` differently:
- `Estudiante` shows all assignments in a semester.
- `Profesor` filters only their own assignments.
- `Coordinador` shows all assignments with full room details.

`Profesor` additionally has `registrarDisponibilidad()` to register available time blocks, and `setCursoImpartido()` to link subjects they teach.

`Coordinador` has `generarHorario()` which delegates to `GeneradorHorario`, and `ajustarHorario()` for manual conflict resolution.

### `Asignatura` _(abstract)_
Defines common subject attributes: `nombre`, `creditos`, `esExclusivaCarrera`. Declares abstract methods `obtenerDetalles()` and `obtenerTipo()`.

- `AsignaturaTeorica` — adds `sitioWebApuntes`.
- `AsignaturaPractica` — adds `materialApoyo` (list) and `sistemaOperativo`.

### `Aula` _(abstract)_
Defines common classroom attributes: `nombre`, `numAula`, `ubicacion`, `capacidad`. Declares abstract methods `obtenerDetalles()`, `obtenerTipo()`, and `esApropiada(Asignatura)`.

- `AulaTeoria` — compatible with `AsignaturaTeorica`.
- `AulaLab` — compatible with `AsignaturaPractica`.

### `BloqueTiempo`
Represents a time slot on a specific weekday: `diaSemana`, `horarioInicio`, `horaFin`. Used both to express professor availability and to define when an assignment takes place.

### `Asignacion`
Links together a subject, professor, room, and time block. This is the core unit of a generated schedule. The room may be `null` initially and is filled in by the `GeneradorHorario`.

### `Semestre`
Holds a `numero` and a `LinkedList<Asignacion>`. Assignments are populated by the coordinator via `GeneradorHorario`.

### `Departamento`
Simple data class representing the academic department a professor belongs to.

### `GeneradorHorario`
The scheduling engine. Receives the semester's assignment list, available rooms, professors, and subjects. Contains three core methods:

| Method | Responsibility |
|--------|---------------|
| `generadorAutomatico()` | Iterates subjects, finds an available professor and time block, creates each assignment |
| `asignarAula()` | Finds a compatible, unoccupied room for each assignment |
| `validarChoques()` | Detects and reports room and professor conflicts |

---

## How It Works

1. **Data setup** — Departments, classrooms, subjects, and professors are created in `Main` with hardcoded values. Professors register their available time blocks and link their courses.

2. **Login** — `iniciarSesion()` is called on a user object. Since data is hardcoded, no input is needed; the method identifies the role and prints a welcome message.

3. **Schedule generation** — The coordinator calls `generarHorario()`, which instantiates a `GeneradorHorario` and runs `generadorAutomatico()`. This method:
   - Finds a professor who teaches each subject.
   - Takes the professor's first available time block.
   - Creates an `Asignacion` with a `null` room.
   - Calls `asignarAula()` to assign a compatible, free room.
   - Calls `validarChoques()` to report any remaining conflicts.

4. **Manual adjustment** — If a conflict is detected, the coordinator calls `ajustarHorario()` with a corrected `Asignacion` object. The old assignment for that subject is removed and replaced.

5. **Schedule consultation** — Any user calls `consultarHorario(semestre)` to view the schedule according to their role.

---

## Test Scenarios

### Scenario 1 — Semester I: clean generation
Two subjects (one theoretical, one practical), two professors with non-overlapping time blocks. Demonstrates successful automatic generation with no conflicts. Login and schedule consultation shown for all three roles.

### Scenario 2 — Semester III: manual conflict resolution
One professor is assigned to two subjects in the same time block, intentionally causing a professor conflict. The system detects and reports the clash. The coordinator then calls `ajustarHorario()` to move one subject to a different day, resolving the conflict manually.

### Scenario 3 — Semester V: multiple professors
Three subjects across two types (theoretical and practical), three different professors with completely separate time blocks. Demonstrates that the system scales correctly and generates a conflict-free schedule. Individual schedule consultation shown for two professors.

---

## How to Run

**Requirements:** Java 11 or higher.

```bash
# Compile all files from the src directory
javac src/*.java -d out/

# Run the main class
java -cp out/ Main
```

All output is printed to the console. No user input is required.

---

## OOP Concepts Applied

| Concept | Where applied |
|---------|--------------|
| **Abstraction** | `Usuario`, `Asignatura`, `Aula` are abstract classes with abstract methods |
| **Inheritance** | `Estudiante`, `Profesor`, `Coordinador` extend `Usuario`; `AsignaturaTeorica`, `AsignaturaPractica` extend `Asignatura`; `AulaTeoria`, `AulaLab` extend `Aula` |
| **Polymorphism** | `consultarHorario()` and `obtenerDetalles()` behave differently per subclass; `esApropiada()` uses `instanceof` for type checking |
| **Encapsulation** | All attributes are `private` with getters and setters in every class |

Abstract classes were chosen over interfaces because the hierarchies share common attributes and partial implementations (e.g., `iniciarSesion()` in `Usuario`, `getHorasSemanales()` in `Asignatura`) that would be impossible to provide through an interface alone.

---

## Authors

- Jason Rene Martinez Gutierrez (j.martinez.4@estudiantec.cr)
- Mainor Olivier Martinez Sanchez (m.martinez.3@estudiantec.cr)

**Course:** IC-2101 Programación Orientada a Objetos  
**Professor:** Oscar Víquez Acuña  
**Institution:** Instituto Tecnológico de Costa Rica, Campus San Carlos
