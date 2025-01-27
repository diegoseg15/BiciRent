<div align="center">
    <img src="https://github.com/diegoseg15/rentaBicicletas/blob/master/frontend/public/assets/CapturaPantalla.png?raw=true" alt="TWCAM - Plataforma de Alquiler de Bicicletas" />
</div>

# TWCAM - Plataforma de Alquiler de Bicicletas 🚴‍♂️

El proyecto **TWCAM** es una plataforma diseñada para facilitar el alquiler de bicicletas, con funcionalidades esenciales para usuarios y administradores. Los usuarios pueden registrarse, iniciar sesión, alquilar bicicletas, realizar devoluciones y consultar información actualizada sobre bicicletas y estaciones disponibles.

---

## ✨ Autores

- **Daniela Perdomo**  
- **Cynthia Endara**  
- **Diego Segovia**  

---

## 🚀 Tecnologías Utilizadas

| **Tecnología**         | **Propósito**                           |
|------------------------|-----------------------------------------|
| **Angular**            | Desarrollo de la interfaz de usuario.  |
| **Jakarta EE**         | Implementación del backend.            |
| **WildFly**            | Servidor de aplicaciones para el backend. |
| **Maven**              | Gestión de dependencias y construcción del backend. |

---

## 📂 Estructura del Proyecto

- **Frontend (Angular):** Contiene el código de la interfaz web interactiva.  
- **Backend (Jakarta EE):** Implementación de la lógica de negocio y los endpoints REST.  
- **Documentación:** Archivos relacionados con la planificación y detalles del proyecto.

---

## ⚙️ Cómo Instalar y Ejecutar el Proyecto

### Requisitos Previos

- **Node.js** y **npm** instalados.  
- **Java JDK 11 o superior.**  
- **Maven 3.x o superior.**  
- **WildFly** configurado y en ejecución.  

---

### 🔧 Pasos para Instalar el Frontend

```bash
# Clonar el Repositorio
git clone https://github.com/usuario/twcam.git
cd twcam

# Navegar al Directorio del Frontend
cd frontend

# Instalar Dependencias
npm install

# Iniciar la Aplicación
npm start
```

La aplicación estará disponible en [http://localhost:4200](http://localhost:4200).

---

### 🔧 Pasos para Construir el Backend

```bash
# Navegar al Directorio del Backend
cd backend

# Construir el Archivo .war
mvn clean package
```

Al finalizar, encontrarás el archivo `.war` en la carpeta `target`.

---

### 🛠️ Configuración de Archivos JSON para el Backend

1. Los datos necesarios para el funcionamiento del backend están contenidos en la carpeta `data` del proyecto.  
2. Copia todos los archivos JSON al directorio `bin/backend` dentro de la instalación de WildFly:
   ```bash
   cp -r data/*.json /ruta/a/wildfly/bin/backend/
   ```

3. Si la carpeta `backend` no existe, créala manualmente:
   ```bash
   mkdir /ruta/a/wildfly/bin/backend
   ```

---

## 📜 Licencia

Este proyecto está licenciado bajo la **MIT License**. Consulta el archivo `LICENSE` para más detalles.
