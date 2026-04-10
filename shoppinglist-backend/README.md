# ShoppingList Backend

Quarkus-basiertes Backend für die ShoppingList-Anwendung.

## Voraussetzungen

- Java 17+
- Maven 3.9+
- Docker & Docker Compose
- PostgreSQL (bereits als Container vorhanden)

## Konfiguration

Die Konfiguration erfolgt über Umgebungsvariablen:

| Variable | Beschreibung | Standardwert |
|----------|-------------|--------------|
| `DB_USERNAME` | PostgreSQL Benutzername | `shoppinglist` |
| `DB_PASSWORD` | PostgreSQL Passwort | `shoppinglist123` |
| `DB_URL` | PostgreSQL JDBC URL | `jdbc:postgresql://localhost:5432/shoppinglist` |
| `SHOPPINGLIST_API_KEY` | API-Key für Authentifizierung | `dev-api-key-12345` |

## Entwicklung

### Lokaler Start

```bash
# Backend im Dev-Modus starten
mvn quarkus:dev
```

### Mit Docker

```bash
# Docker Image bauen
docker build -t shoppinglist-backend .

# Container starten (mit bestehendem PostgreSQL)
docker-compose up -d
```

## API-Endpunkte

### Einkaufslisten

- `GET /api/lists` - Alle Listen abrufen
- `GET /api/lists/{id}` - Liste nach ID abrufen
- `POST /api/lists` - Neue Liste erstellen
- `PUT /api/lists/{id}` - Liste aktualisieren
- `DELETE /api/lists/{id}` - Liste löschen

### Artikel

- `GET /api/lists/{listId}/items` - Alle Artikel einer Liste
- `POST /api/lists/{listId}/items` - Neuen Artikel hinzufügen
- `PUT /api/lists/{listId}/items/{itemId}` - Artikel aktualisieren
- `DELETE /api/lists/{listId}/items/{itemId}` - Artikel löschen

### Geräte

- `GET /api/devices` - Alle Geräte abrufen
- `POST /api/devices` - Neues Gerät registrieren
- `PUT /api/devices/{id}` - Gerät aktualisieren
- `DELETE /api/devices/{id}` - Gerät löschen
- `POST /api/devices/{id}/heartbeat` - Heartbeat senden

## Sicherheit

Alle Endpunkte erfordern einen API-Key im Header:

```
X-API-Key: <dein-api-key>
```

## Cloudflare Tunnel

Für den Zugriff von außen kann ein Cloudflare Tunnel verwendet werden:

```bash
cloudflared tunnel --url http://localhost:8080
```

## Datenbank-Schema

Das Schema wird automatisch von Hibernate erstellt (`generation: update`).

Tabellen:
- `shopping_lists` - Einkaufslisten
- `shopping_items` - Artikel in den Listen
- `devices` - Registrierte Android-Geräte
