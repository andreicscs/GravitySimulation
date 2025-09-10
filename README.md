# Particle Gravity Simulation with Polarity Dynamics

A Java + JavaFX simulation of **gravitational interactions** between positive, negative, and neutral particles in real-time 2D space.

![gravityDemo](https://github.com/user-attachments/assets/273db425-f6d4-42eb-b70e-fd78a6288ed2)

---

## 🔧 Key Features

### ⚛️ Polarity-based Dynamics

| Polarity             | Behavior                                     |
|----------------------|----------------------------------------------|
| 🔴 **Positive (Red)** | Attracts negatives, repels positives         |
| 🔵 **Negative (Blue)** | Attracts positives, repels negatives         |
| ⚪ **Neutral (Gray)** | Attracted by both, repels none               |

---

### 🎛️ Real-time Controls

| Feature             | Description                                   |
|---------------------|-----------------------------------------------|
| Gravity Slider      | Adjust gravitational constant                 |
| Mass Slider         | Set particle mass                             |
| Trail Toggle        | Toggle particle motion trails                 |
| Collisions Toggle   | Enable/Disable collision physics              |
| Clear Button        | Reset simulation state                        |

---

### 🖱️ Interactive Creation

- **Left-click**: Set particle origin
- **Drag**: Define initial velocity
- **Release**: Spawn particle with vector
- Live preview of particle trajectory during drag

---

### 🚀 Performance Optimizations

- 🧵 **Dual-threaded architecture**  
  - 60 FPS rendering (JavaFX)  
  - Physics calculations in parallel thread  
- 🔐 **Semaphore-synced** shared resources  
- ⏱️ Adaptive frame timing for smooth simulation

---

## 🧰 Installation & Setup

### 🔨 Requirements

- Java **17+** JDK  
- JavaFX **19+** SDK  
- IDE with JavaFX support (Eclipse, IntelliJ, VS Code)

---

### ⚙️ Eclipse Setup

#### 🧭 Import Project

**Option 1: Clone Repository**
```bash
git clone https://github.com/andreicscs/GravitySimulation.git
```
Then in Eclipse:

File → Import → Existing Projects into Workspace → Select root directory → Browse to the cloned folder

---

**Option 2: Download Project Folder**

File → Import → Existing Projects into Workspace → Select root directory → Browse to the downloaded folder

### Add JavaFX Libraries:
Right-click project → Build Path → Configure Build Path → Add External JARs (from JavaFX lib folder)

#### VM Arguments (Run Configurations):
--module-path "path/to/javafx-sdk-19/lib" --add-modules javafx.controls,javafx.fxml
> Replace `"path/to/javafx-sdk-19/lib"` with your actual JavaFX SDK path.

---

## 🎮 Usage Guide

### 🧪 Particle Creation
- **Left-click** to place particle center
- **Drag** to set initial velocity vector
- **Release** to spawn particle

### 📋 Control Panel (Right Sidebar)

| Component        | Functionality                               |
|------------------|----------------------------------------------|
| ≡ / ✕ Button     | Toggle control panel                         |
| Gravity Slider   | Adjust interaction strength                  | 
| Mass Slider      | Set particle density                         |
| Movable Toggle   | Lock/Unlock particle mobility                |
| Trail Toggle     | Enable/Disable trajectory visualization      |
| Collisions Toggle| Enable/Disable collision physics             |
| Clear Button     | Reset simulation state                       |

<img width="320" height="661" alt="gravityPanel" src="https://github.com/user-attachments/assets/28ac603d-f35c-4245-a191-cd0e56244f85" />

---

## 🧱 Technical Architecture

### 🧵 Thread Management
- **Physics Thread:** Fixed time-step calculations
- **Render Thread:** JavaFX thread used for drawing
- **Semaphore-controlled** Alternates between rendering and calculating for an accurate simulation

## 🖼️ UI Framework
JavaFX Canvas for rendering

FXML-based control interface

CSS-styled GUI components






