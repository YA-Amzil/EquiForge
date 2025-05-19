# PXL-Digital Research Project

## Beschrijving Case
Het project richt zich op het ontwikkelen van een mobiele applicatie voor het beheren van een paardenfokkerij. De applicatie bevat zowel een backend als een frontend. Gebruikers kunnen paarden en hun dekkingen beheren, het geboortedagboek bijhouden en gebruikersbeheer met verschillende rechten wordt aangeboden.

## Projectstructuur
Het project bestaat uit twee hoofdmappen:

### Backend
Bevat Java Spring Boot code voor de logica.

### Frontend
Bevat Vue.js code voor de gebruikersinterface.

## Architectuur
De architectuur volgt een Domein-Gedreven Ontwerpbenadering met Cross-Origin Resource Sharing (CORS) ingeschakeld.

## Mappenstructuur

### Backend

- **Api**: Bevat aanvraag- en responsedata-overdrachtsobjecten (DTO's).
- **Config**: Initialiseert seedgegevens en configureert CORS om te communiceren met de frontend.
- **Controller**: Behandelt HTTP-verzoeken en -antwoorden.
- **Domain**: Bevat domeinlogica en modellen.
- **Exception**: Foutafhandelingen van de applicatie.
- **Repository**: Bevat JPA-repositories voor gegevenstoegang.
- **Security**: Beheert gebruikersaccounts, beveiligingsconfiguraties en authenticatie met behulp van beartokens en basisverificatie.
- **Service**: Definieert service-interfaces en hun implementaties.
- **Resources**: Slaat applicatie-eigenschappen op.

### Frontend

- **Public**: Deze map bevat openbare statische assets die direct beschikbaar zijn voor de frontend.
- **Assets**: Toegewezen statische assets zoals afbeeldingen of CSS-bestanden worden hier bewaard.
- **Components**: Vue-componenten, die de bouwstenen vormen van de gebruikersinterface, worden hier gedefinieerd.
- **Router**: Hier wordt de Vue Router-configuratie gedefinieerd voor het routeren van pagina's binnen de frontend applicatie.
- **Store**: De Pinia Store wordt hier geïmplementeerd voor het beheren van de applicatiestatus en gegevens.
- **Views**: Vue views of pagina's worden hier geplaatst, die bestaan uit een of meer Vue-componenten en worden weergegeven aan de gebruiker.

## Database
Er wordt gebruik gemaakt van MariaDB.
Zorg ervoor dat MariaDB is geïnstalleerd en ingesteld volgens de configuratie in `application.properties`.

## Technologiestack

### Backend
- Java
- Spring Boot
- REST API
- Maven

### Frontend
- Vue.js
- Vuetify
- Pinia
- JavaScript
- NPM

## Codekwaliteit
SonarQube wordt gebruikt voor codeanalyse en rapportages.

## Unit Testing
Unit tests worden uitgevoerd met Mockito.

## Aan de slag
1. Clone deze repository.
2. Navigeer naar de respectievelijke backend- en frontendmappen.
3. Volg de installatie-instructies die worden verstrekt in de README-bestanden van elke map.

## Installatie

### Backend
1. Ga naar de backend directory:
   ```
   cd ../backend
   ```

2. Installeer de afhankelijkheden en bouw het project:
   ```
   mvn clean install
   ```

3. Start de applicatie:
   ```
   mvn spring-boot:run
   ```

#### Frontend
1. Ga naar de frontend directory:
   ```
   cd ../frontend
   ```

2. Installeer de afhankelijkheden en bouw het project:
   ```
   npm install
   ```

3. Start de applicatie:
   ```
   npm run dev
   ```

## Gebruik
- Backend API is beschikbaar op http://localhost:8082.
- Frontend applicatie is toegankelijk op http://localhost:3000.








