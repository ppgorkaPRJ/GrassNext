# GRASS-NEXT

GRASS-NEXT website was created for the purpose of visualising data gathered through detectors placed on the roads. It consists of four modules, each of them written in a different programming language:
- frontend application for data visualization (TypeScript with Angular framework),
- backend server for data processing (Java Spring Boot),
- computational library for complex matrix calculations (C++),
- database for storing data (PostgreSQL).

The website collects data from files of the format specified in the [topographical data file](#topographical-data-file) section, processes them, and stores the results in a PostgreSQL database. The processed data is then passed to an advanced multithreading C++ computational engine, which performs complex dispersion modeling using the Gaussian plume algorithm. This ensures accurate and efficient visualization of air pollution levels, seamlessly integrating high-performance computing with a user-friendly web interface.

The online version of the application is available through the [green-tronic.org](http://www.green-tronic.eu) website. It allows users to freely perform simulations based on the topographical data provided by VITRONIC, gathered through motion sensors placed on roads in various locations.

# Features
- providing a GUI for users to interact with and input necessary simulation parameters
- gathering topographical data through files sent from detectors
- calculating air pollution dispersion with complex matrix calculations
- presenting calculation results on a map in the form of contours on a Leaflet map
- providing information about the project and its partners via the "About" page

# Requirements

## Minimal version requirements to run the application:
- Frontend
  - **nginx**: 1.25.2
- Backend
  - **Java**: 17

## Minimal version requirements to run the projects:
- Frontend
  - **Angular**: 17.0.0
  - **TypeScript**: 5.2.2
  - **Node.js**: 22.15.0
  - **npm**: latest version compatible with the installed Node.js version
- Backend
  - **Java**: 17
  - **Spring Boot**: 3.1.5
- Library
  - **C++**: 20

# How to run the application

All files necessary to launch the application are in the <a href="./grass-next-app" target="_blank">grass-next-app</a> folder.

## Frontend

Launching the website requires the installation of an HTTP server of the user's choice. For the purpose of this project, nginx (version 1.25.2 or higher) was used. To run the frontend application, <a href="./grass-next-app/frontend" target="_blank">the frontend distribution files</a> need to be placed in the appropriate web server folder.

## Backend

The application server is run through the .jar file in the <a href="./grass-next-app/backend" target="_blank">backend data folder</a>. It contains all the necessary files, including the compiled C++ library performing matrix calculations. To run the application, the user needs to install Java (version 17 or higher) and use the following command:

`java -jar GrassNextServer-0.0.1-SNAPSHOT.jar`

# Projects

The source code of all parts of the application can be found in the <a href="./grass-next-app" target="_blank">folder</a>. It consists of 3 parts:
- <a href="./grass-next-prj/GrassNext_project/GrassNextFrontend" target="_blank">frontend application project</a>
- <a href="./grass-next-prj/GrassNext_project/GrassNextServer" target="_blank">backend server project</a>
- <a href="./grass-next-prj/MathTest" target="_blank">external library project</a>

# Topographical data file

An example of the topographical data file can be found <a href="./topo/topo-example.txt" target="_blank">here</a>. All files added through the local version of the application need to follow the same format. The website provided at [green-tronic.org](http://www.green-tronic.eu) doesn't allow users to upload custom files; all further actions must be consulted with VITRONIC.

# Authors

Patryk Górka  
Patryk.Gorka@zut.edu.pl

Krzysztof Małecki  
krzysztof.Malecki@zut.edu.pl

Stanisław Iwan  
s.iwan@pm.szczecin.pl

Kinga Kijewska  
k.kijewska@pm.szczecin.pl

Michał Żuchora  
Michal.Zuchora@vitronic.com

# Licenses

ABMTrafSimCA was created using Apache License, Version 2.0 (see <a href="./LICENSE" target="_blank">LICENSE.TXT</a> or <a href="https://www.apache.org/licenses/LICENSE-2.0">online license</a>).
