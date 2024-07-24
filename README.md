# Product Web App

Simple application for product management.
## Table of Contents

- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)

## Getting Started

### Prerequisites

To run the application, ensure you have the following installed:

- Java 
- Maven
- Docker

### Installation

1. Clone the repository:

   ```bash
   git clone git@github.com:btaras1/product-app.git

2. Navigate to the cloned directory:

   ```bash
   cd product-app

3. Install packages:
   ```bash
    ./mvnw clean install

4. Navigate to the docker directory:
   ```bash
   cd docker
    ```

5. Start the necessary Docker containers, including the PostgreSQL database:

   ```bash
   docker-compose up
